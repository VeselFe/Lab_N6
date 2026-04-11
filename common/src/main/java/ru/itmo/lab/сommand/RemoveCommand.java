package ru.itmo.lab.сommand;

import ru.itmo.lab.interfaces.CommandWithKey;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.myExceptions.CommandException;
import ru.itmo.lab.interfaces.IO_Handler;

/**
 * Команда для удаления элемента коллекции по ключу
 */
public class RemoveCommand implements CommandWithKey
{
    private final CollectionManager collection;
    private Long Key;

    public RemoveCommand( CollectionManager newCollection )
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
    public void execute( IO_Handler consol )
    {
        collection.removeElement(Key);
    }
    @Override
    public String getName()
    {
        return "remove_key";
    }
    @Override
    public String getDescription()
    {
        return "удалить элемент коллекции по его ключу";
    }
}
