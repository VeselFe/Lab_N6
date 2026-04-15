package ru.itmo.lab.сommand;

import ru.itmo.lab.interfaces.CommandWithArgs;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myEnums.Semester;
import ru.itmo.lab.myExceptions.CommandException;
import ru.itmo.lab.interfaces.IO_Handler;

import java.util.List;
import java.util.stream.Collectors;


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
