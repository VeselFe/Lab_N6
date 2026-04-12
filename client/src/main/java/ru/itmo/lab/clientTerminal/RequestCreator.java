package ru.itmo.lab.clientTerminal;

import ru.itmo.lab.commonNet.Request;
import ru.itmo.lab.interfaces.IO_Handler;
import ru.itmo.lab.model.Person;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myEnums.Commands;
import ru.itmo.lab.myExceptions.CommandException;
import ru.itmo.lab.myRecords.FieldDescriptor;
import ru.itmo.lab.myRecords.Lab5FieldDescriptor;

public class RequestCreator 
{
    private IO_Handler console;
    public RequestCreator( IO_Handler newIOHandler )
    {
        console = newIOHandler;
    }
    public Request buildRequest( String input )
    {
        if (input.trim().isEmpty())
        {
            console.printError("Пустая команда");
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
                    StudyGroup newGroup = console.readNewStudyGroup();
                    return new Request.Builder()
                            .setCommandType(name)
                            .setID(id)
                            .setGroup(newGroup)
                            .buildRequest();
                }
                case "update_id" -> {
                    if( args.length != 2 ) throw new CommandException("Ошибка получения аргументов: <команда> <аргумент");
                    Long id = Long.parseLong(args[1]);
                    console.printInfo("Какой параметр Вы хотите обновить?\n" +
                            "===========================================");
                    for (FieldDescriptor element : Lab5FieldDescriptor.UPDATED_FIELDS)
                    {
                        console.printInfo(element.name());
                    }
                    console.printInfo("===========================================");
                    console.printRequest("Введите название параметра: ");
                    String updated_name = console.readline().trim();

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
                                console.printInfo(element.request());
                                // Создание нового админа
                                Person newAdmin = console.readPerson();
                                return new Request.Builder()
                                        .setCommandType(name)
                                        .setID(id)
                                        .setPerson(newAdmin)
                                        .setUpdatedField(updated_name)
                                        .buildRequest();
                            }
                            console.printRequest(element.request());
                            String value = console.readline().trim();

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
                    clientRequestBuilder.setCommandType(name);

                    return clientRequestBuilder.buildRequest();
                }
            }
            return null;
        }
        catch( IllegalArgumentException e )
        {
            console.printError("Некорректный формат для ключа!");
            return null;
        }
        catch ( Exception e )
        {
            console.printError("Ошибка при выполнении команды: " + e.getMessage());
            return null;
        }
    }
}
