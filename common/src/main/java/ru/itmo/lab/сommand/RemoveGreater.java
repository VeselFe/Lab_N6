package ru.itmo.lab.сommand;

import ru.itmo.lab.interfaces.CommandWithKey;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.manager.collection.StudyGroupByStudentsComparator;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myExceptions.CommandException;
import ru.itmo.lab.interfaces.IO_Handler;

import java.util.Map;

/**
 * Команда для удаления элементов коллекции с большим количеством студентов
 */
public class RemoveGreater implements CommandWithKey
{
    private final CollectionManager collection;
    private Long Key;
    private StudyGroup choosenGroup;

    public RemoveGreater( CollectionManager newCollection )
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
        long count = 0;
        int sizeBefore = collection.getStudyGroups().size();

        collection.getStudyGroups().entrySet().removeIf(
                entry -> comparator.compare(entry.getValue(), choosenGroup) > 0
        );
        count = sizeBefore - collection.getStudyGroups().size();
        if( count == 0 )
        {
            consol.printInfo("Совпадений не найдено!");
        }
        else
        {
            consol.printInfo("Было найдено и удалено " + count + " превышающих элементов.");
        }
    }
    @Override
    public String getName()
    {
        return "remove_greater  { Key }";
    }
    @Override
    public String getDescription()
    {
        return "удалить из коллекции все элементы, которых количество студентов больше, чем в заданном";
    }
}
