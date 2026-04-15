package ru.itmo.server.сommand;

import ru.itmo.lab.common.interfaces.CommandWithKey;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.lab.common.myExceptions.CreationException;
import ru.itmo.lab.common.interfaces.IO_Handler;

/**
 * Команда для добавления нового элемента в коллекции
 */
public class InsertElCommand implements CommandWithKey
{
    private final CollectionManager collection;
    private Long Key;

    public InsertElCommand( CollectionManager newCollection )
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
            throw new CreationException("Некорректные данные для ключа");
        }
    }

    @Override
    public void execute( IO_Handler consol )
    {
        try
        {
            if(collection.getStudyGroups().get(Key) != null)
            {
                throw new CreationException("Элемент с таким ключем уже существует");
            }
            StudyGroup newGroup = consol.readNewStudyGroup();
            collection.addElement(Key, newGroup);
            consol.printInfo("Колекция: Добавлен новый элемент!");
        }
        catch (Exception e)
        {
            consol.printError("*Ошибка при создании группы*\n" + e.getMessage());
        }
    }

    @Override
    public String getName()
    {
        return "insert_element";
    }
    @Override
    public String getDescription()
    {
        return "добавить новый элемент с заданным ключом";
    }
}
