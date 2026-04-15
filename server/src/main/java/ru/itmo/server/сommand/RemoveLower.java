package ru.itmo.server.сommand;

import ru.itmo.lab.common.interfaces.CommandWithKey;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.server.manager.collection.StudyGroupByStudentsComparator;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.lab.common.myExceptions.CommandException;
import ru.itmo.lab.common.interfaces.IO_Handler;

import java.util.List;
import java.util.Map;

/**
 * Команда для удаления элементов коллекции с меньшим количеством студентов
 */
public class RemoveLower implements CommandWithKey
{
    private final CollectionManager collection;
    private Long Key;
    private StudyGroup choosenGroup;

    public RemoveLower( CollectionManager newCollection )
    {
        collection = newCollection;
    }

    @Override
    public void getArgs( Long Args )
    {
        try
        {
            Key = Long.valueOf( Args );
            choosenGroup = collection.getStudyGroups().get(Key);
        }
        catch (IllegalArgumentException e)
        {
            throw new CommandException("Неверно введен параметр: по данному ключу ничего не найдено");
        }
    }

    @Override
    public void execute( IO_Handler consol )
    {
        if( choosenGroup == null )
        {
            consol.printError("По данному ключу не найдено элементов!");
            return;
        }

        StudyGroupByStudentsComparator comparator = new StudyGroupByStudentsComparator();

        List<Long> removingList = collection.getStudyGroups().entrySet().stream()
                .filter(element -> comparator.compare(element.getValue(), choosenGroup) < 0)
                .map(Map.Entry::getKey)
                .toList();
        long count = removingList.size();
        removingList.forEach(key -> collection.getStudyGroups().remove(key));

        if( count == 0 )
        {
            consol.printInfo("Совпадений не найдено!");
        }
        else
        {
            consol.printInfo("Было найдено и удалено " + count + " элементов с меньшим количеством.");
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
        return "удалить из коллекции все элементы, у которых количество студентов меньше, чем в заданном";
    }
}
