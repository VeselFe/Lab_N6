package ru.itmo.client.clientTerminal;

import ru.itmo.lab.myExceptions.ConnectionException;
import ru.itmo.lab.myExceptions.ResponseException;
import ru.itmo.client.network.NetworkManager;
import ru.itmo.lab.commonNet.Request;
import ru.itmo.lab.terminal.GenericConsoleHandler;
import ru.itmo.lab.terminal.*;
import ru.itmo.lab.interfaces.IO_Handler;
import ru.itmo.lab.model.Person;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myExceptions.CreationException;

import java.util.Scanner;
/**
 * Консольный обработчик ввода-вывода для лабораторной работы №5.
 *
 * <p>Реализует интерфейс {@link IO_Handler} и расширяет {@link GenericConsoleHandler}
 * для обработки команд интерактивного режима работы программы.</p>
 *
 * <p><b>Основное назначение:</b></p>
 * <ul>
 *   <li>Интерактивный ввод сложных объектов: {@link Person}, {@link StudyGroup}</li>
 *   <li>Форматированный вывод информационных сообщений, ошибок и запросов</li>
 *   <li>Управление жизненным циклом консольного приложения</li>
 * </ul>
 */
public class ClientConsoleHandler extends GenericConsoleHandler<NetworkManager>
    implements IO_Handler
{
    private boolean printCollection = false;
    private final Scanner scanner = new Scanner(System.in);
    private boolean stop = false;
    private String input;
    private RequestCreator requestCreator;

    public ClientConsoleHandler()
    {
    }

    public void turnPrinterCollection( boolean state ) { printCollection = state; }

    @Override
    public void welcomMessage()
    {
        print("Клиентское приложение запущено. Введите 'help' для просмотра возможных команд.");
    }
    public void initRequestCreator( IO_Handler ioHandler )
    {
        this.requestCreator = new RequestCreator( ioHandler );
    }

    @Override
    public boolean executing()
    {
        String serverResponse;

        if( requestCreator == null ) throw new RuntimeException("Сборщик запроса не инициирован.");
        print("\n         Введите команду");
        print("=====================================");
        input = readline();
        Request request = requestCreator.buildRequest(input);
        try
        {
            if( input.trim().toLowerCase().equals("exit") )
            {
                stop();
            }
            if( request != null )
            {
                provider.network(request);
                serverResponse = provider.getServerResponse(printCollection);
                if( !stop )
                {
                    printInfo(serverResponse);
                }
                else
                {
                    printInfo("Соединение успешно завершено!");
                }
            }
            else
            {
                printError("Запрос не отправлен!");
            }
        }
        catch( ConnectionException e )
        {
            throw new ConnectionException(e.getMessage());
        }
        catch( ResponseException e )
        {
            printError(e.getMessage());
            return true;
        }
        catch( Exception e )
        {
            printError("ошибка при попытке отправки запроса на сервер. " + e.getMessage());
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
}
