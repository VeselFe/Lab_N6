package ru.itmo.lab.сommand;

import ru.itmo.lab.interfaces.CommandWithKey;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.model.Person;
import ru.itmo.lab.myExceptions.CommandException;
import ru.itmo.lab.myRecords.FieldDescriptor;
import ru.itmo.lab.myRecords.Lab5FieldDescriptor;
import ru.itmo.lab.interfaces.IO_Handler;

/**
 * Команда для обновления указанного пользователем поля элеменат коллекции
 */
public class UpdateIdCommand implements CommandWithKey
{
    private final CollectionManager collection;
    private Long Key;

    public UpdateIdCommand(CollectionManager newCollection )
    {
        collection = newCollection;
    }

    @Override
    public void getArgs( Long Args )
    {
        try
        {
            Key = Long.valueOf( Args );
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Некорректные данные для ключа");
        }
    }

    @Override
    public void execute( IO_Handler console )
    {
        boolean exit = false;
        while(!exit)
        {
            try
            {
                console.printInfo("Какой параметр Вы хотите обновить?\n" +
                        "===========================================");
                for (FieldDescriptor element : Lab5FieldDescriptor.UPDATED_FIELDS)
                {
                    console.printInfo(element.name());
                }
                console.printInfo("===========================================");
                console.printRequest("Введите название параметра: ");
                String name = console.readline().trim();

                if( name.trim().isEmpty() )
                {
                    throw new CommandException("Поле ввода пусто!");
                }
                for(FieldDescriptor element : Lab5FieldDescriptor.UPDATED_FIELDS)
                {
                    if( name.toLowerCase().equals(element.name().toLowerCase()) )
                    {
                        console.printRequest(element.request());
                        String input = console.readline().trim();

                        if( name.equals("Group Admin") )
                        {
                            console.printInfo(element.request());
                            // Создание нового админа
                            Person newAdmin = console.readPerson();
                            exit = collection.updateElement(Key, "admin", "", newAdmin);
                        }

                        exit = collection.updateElement(Key, element.name(), input, null);
                    }
                }
            }
            catch (Exception e)
            {
                console.printError("Ошибка при обновлении группы: \n" + e.getMessage());
            }
        }
        console.printInfo("Элемент коллекции успешно обновлен!");
    }

    @Override
    public String getName()
    {
        return "update_id";
    }
    @Override
    public String getDescription()
    {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}
