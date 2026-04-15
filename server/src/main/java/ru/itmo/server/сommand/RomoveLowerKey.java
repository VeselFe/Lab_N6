package ru.itmo.server.сommand;

import ru.itmo.lab.common.interfaces.CommandWithKey;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.lab.common.myExceptions.CreationException;
import ru.itmo.lab.common.interfaces.IO_Handler;

import java.util.List;
import java.util.Map;


/**
 * Команда для удаления элементов коллекции с меньшим ключом
 */
public class RomoveLowerKey implements CommandWithKey
{
    private final CollectionManager collection;
    private Long Key;

    public RomoveLowerKey( CollectionManager newCollection )
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
        long count = 0;
        try
        {
            List< Map.Entry<Long, StudyGroup> > removingList = collection.getStudyGroups().entrySet().stream()
                    .filter(element -> element.getKey() < Key)
                    .toList();

            if( removingList.isEmpty() )
                consol.printInfo("Совпадений не найдено!");
            else
            {
                StringBuilder deletedNames = new StringBuilder();
                for( var element : removingList )
                {
                    count++;
                    deletedNames.append("\n" + count + ") " + element.getValue().getName());
                    deletedNames.append("   'id' = " + element.getKey());
                }
                removingList.forEach(element -> collection.getStudyGroups().remove(element.getKey()));
                consol.printInfo("Было обнаружено и удалено " + removingList.size() + " элемента(-ов): " + deletedNames);
            }
        }
        catch (Exception e)
        {
            consol.printError("ошибка при удалении элементов с меньшим id " + e.getMessage());
        }
    }

    @Override
    public String getName()
    {
        return "remove_lower_key { key }";
    }
    @Override
    public String getDescription()
    {
        return "удалить из коллекции все элементы, ключ которых меньше, чем заданный";
    }
}
