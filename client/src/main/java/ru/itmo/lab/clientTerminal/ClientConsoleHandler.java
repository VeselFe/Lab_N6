package ru.itmo.lab.clientTerminal;

import ru.itmo.lab.myEnums.Commands;
import ru.itmo.lab.myExceptions.CommandException;
import ru.itmo.lab.network.NetworkManager;
import ru.itmo.lab.commonNet.Request;
import ru.itmo.lab.terminal.GenericConsoleHandler;
import ru.itmo.lab.terminal.*;
import ru.itmo.lab.interfaces.IO_Handler;
import ru.itmo.lab.model.Person;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myExceptions.CreationException;
import ru.itmo.lab.manager.serverLogic.Invoker;
import ru.itmo.lab.myRecords.*;

import java.util.Scanner;
/**
 * Консольный обработчик ввода-вывода для лабораторной работы №5.
 *
 * <p>Реализует интерфейс {@link IO_Handler} и расширяет {@link GenericConsoleHandler}
 * для обработки команд интерактивного режима работы программы.</p>
 *
 * <p><b>Основное назначение:</b></p>
 * <ul>
 *   <li>Чтение пользовательских команд и их выполнение через {@link Invoker}</li>
 *   <li>Интерактивный ввод сложных объектов: {@link Person}, {@link StudyGroup}</li>
 *   <li>Форматированный вывод информационных сообщений, ошибок и запросов</li>
 *   <li>Управление жизненным циклом консольного приложения</li>
 * </ul>
 */
public class ClientConsoleHandler extends GenericConsoleHandler<NetworkManager>
    implements IO_Handler
{
    private final Scanner scanner = new Scanner(System.in);
    private boolean stop = false;
    private String input;

    public ClientConsoleHandler()
    {
    }

    @Override
    public void welcomMessage()
    {
        print("Клиентское приложение запущено. Введите 'help' для просмотра возможных команд.");
    }

    @Override
    public boolean executing()
    {
        print("\n         Введите команду");
        print("=====================================");
        input = readline();
        if( input.trim().toLowerCase().equals("exit") )
        {
            stop();
            return stop;
        }
        Request request = buildRequest(input);
        try
        {
            provider.network(request);

        }
        catch( Exception e )
        {
            printError("Ошибка при попытке отпраки сообщения");
        }

        return stop;
    }

    public void printInfo( String message )
    {
        print( "<i> " + message );
    }
    public void printError( String message )
    {
        print("<Ошибка>\n" + message);
    }
    public void printRequest( String request )
    {
        printCurLine( request );
    }
    public String readline()
    {
        return scanner.nextLine();
    }
    public void stop() { stop = true; }

    public Person readPerson() throws CreationException
    {
        return new PersonReader(this).readPerson();
    }
    public StudyGroup readNewStudyGroup()
    {
        return new StudyGroupReader(this).readStudygroup();
    }

    private Request buildRequest( String input )
    {
        if (input.trim().isEmpty())
        {
            printError("Пустая команда");
            return null;
        }

        String[] args = input.trim().split("\\s+");
        String name = args[0];

        try
        {
            switch (name)
            {
                case "insert_element" -> {
                    if( args.length != 2 ) throw new CommandException("Ошибка получения аргументов: <команда> <аргумент");
                    Long id = Long.parseLong(args[1]);
                    StudyGroup newGroup = readNewStudyGroup();
                    return new Request.Builder()
                            .setCommandType(name)
                            .setID(id)
                            .setGroup(newGroup)
                            .buildRequest();
                }
                case "update_id" -> {
                    if( args.length != 2 ) throw new CommandException("Ошибка получения аргументов: <команда> <аргумент");
                    Long id = Long.parseLong(args[1]);
                    printInfo("Какой параметр Вы хотите обновить?\n" +
                            "===========================================");
                    for (FieldDescriptor element : Lab5FieldDescriptor.UPDATED_FIELDS)
                    {
                        printInfo(element.name());
                    }
                    print("===========================================");
                    printRequest("Введите название параметра: ");
                    String updated_name = readline().trim();

                    if( updated_name.trim().isEmpty() )
                    {
                        throw new CommandException("Поле ввода пусто!");
                    }
                    updated_name = updated_name.toLowerCase();
                    for(FieldDescriptor element : Lab5FieldDescriptor.UPDATED_FIELDS)
                    {
                        if( updated_name.toLowerCase().equals(element.name().toLowerCase()) )
                        {
                            if( updated_name.equals("group admin") )
                            {
                                printInfo(element.request());
                                // Создание нового админа
                                Person newAdmin = readPerson();
                                return new Request.Builder()
                                        .setCommandType(name)
                                        .setID(id)
                                        .setPerson(newAdmin)
                                        .setUpdatedField(updated_name)
                                        .buildRequest();
                            }
                            printRequest(element.request());
                            String value = readline().trim();

                            return new Request.Builder()
                                    .setCommandType(name)
                                    .setArgument(value)
                                    .setUpdatedField(updated_name)
                                    .setID(id)
                                    .buildRequest();
                        }
                    }
                }
                default -> {
                    if( Commands.find(name) == null ) throw new IllegalArgumentException("Неизвестная команда!");
                    Request.Builder clientRequestBuilder = new Request.Builder();
                    clientRequestBuilder.setCommandType(name);
                    if( args.length == 2 )
                    {
                        try
                        {
                            Long id = Long.parseLong( args[1] );
                            clientRequestBuilder.setID(id);
                        }
                        catch( NumberFormatException e )
                        {
                            clientRequestBuilder.setArgument(args[1]);
                        }
                    }

                    return clientRequestBuilder.buildRequest();
                }
            }
            return null;
        }
        catch( IllegalArgumentException e )
        {
            printError("Некорректный формат для ключа!");
            return null;
        }
        catch ( Exception e )
        {
            printError("Ошибка при выполнении команды: " + e.getMessage());
            return null;
        }
    }
}
