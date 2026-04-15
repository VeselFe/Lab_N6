package ru.itmo.lab.сommand;

import ru.itmo.lab.interfaces.Command;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.interfaces.IO_Handler;
import ru.itmo.lab.myExceptions.CommandException;

/**
 * Команда для вывода информации о коллекции
 */
public class InfoCommand implements Command
{
    private CollectionManager CollInfo;
    public InfoCommand( CollectionManager collection )
    {
        CollInfo = collection;
    }

    @Override
    public void execute( IO_Handler consol )
    {
        if( CollInfo == null )
            throw new CommandException("Не выбран элемент!");
        consol.printInfo(CollInfo.getInfo());
    }
    @Override
    public String getName()
    {
        return "info";
    }
    @Override
    public String getDescription()
    {
        return "вывести информацию о коллекции";
    }
}
