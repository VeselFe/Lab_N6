package ru.itmo.server.сommand;

import ru.itmo.lab.common.interfaces.CommandWithArgs;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.lab.common.myEnums.Semester;
import ru.itmo.lab.common.myExceptions.CommandException;
import ru.itmo.lab.common.interfaces.IO_Handler;

import java.util.List;


/**
 * Команда для вывода элементов коллекции, отсортировав по семестру обучения
 */
public class FilterBySemesterEnum implements CommandWithArgs
{
    private final CollectionManager collection;
    private Semester sem;

    public FilterBySemesterEnum( CollectionManager newCollection )
    {
        collection = newCollection;
    }

    @Override
    public void getArgs( String Args )
    {
        try
        {
            sem = Semester.valueOf( Args.toUpperCase() );
        }
        catch (IllegalArgumentException e)
        {
            throw new CommandException("Неверно введен семестр. Варианты: FIRST, SECOND, THIRD, FIFTH, EIGHTH");
        }
    }

    @Override
    public void execute( IO_Handler consol )
    {
        if( sem == null )
            throw new CommandException("Не определен семестр для сравнения.");
        List<StudyGroup> filteredGroups = collection.getStudyGroups().values().stream()
                .filter(group -> group.getSemester().equals(sem))
                .toList();

        filteredGroups.forEach(group -> consol.printInfo(group.getInformation()));
        if( filteredGroups.isEmpty() )
        {
            consol.printInfo("Совпадений не найдено!");
        }
        else
        {
            consol.printInfo("Было найдено " + filteredGroups.size() + " совпадений.");
        }
    }
    @Override
    public String getName()
    {
        return "filter_by_semester_enum { semesterEnum }";
    }
    @Override
    public String getDescription()
    {
        return "вывести элементы, значение поля semesterEnum которых равно заданному";
    }
}
