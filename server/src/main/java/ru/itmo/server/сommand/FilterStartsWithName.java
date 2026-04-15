package ru.itmo.server.сommand;

import ru.itmo.lab.common.interfaces.CommandWithArgs;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.lab.common.myExceptions.CommandException;
import ru.itmo.lab.common.interfaces.IO_Handler;

/**
 * Команда для вывода элементов коллекции, отсортировав по началу названия элементов
 */
public class FilterStartsWithName implements CommandWithArgs
{
    private final CollectionManager collection;
    private String tempName;

    public FilterStartsWithName( CollectionManager newCollection )
    {
        collection = newCollection;
    }

    @Override
    public void getArgs( String Args )
    {
        try
        {
            tempName = Args;
        }
        catch (IllegalArgumentException e)
        {
            throw new CommandException("Неверно введен параметр: по данному ключу ничего не найдено");
        }
    }

    @Override
    public void execute( IO_Handler consol )
    {
        if( tempName == null || tempName.isEmpty() )
        {
            consol.printError("Введеная подстрока пустая!");
            return;
        }

        var filteredGroups = collection.getStudyGroups().entrySet().stream()
                .filter(entry -> entry.getValue().getName().startsWith(tempName))
                .toList();
        filteredGroups.forEach(group -> {
            consol.printInfo(group.getKey() + ": " + group.getValue().getName());
            consol.printInfo("Подробная информация о " + group.getKey() + "-ом элеменете коллеции:\n" + group.getValue().getInformation());
        });
        if( filteredGroups.isEmpty() )
        {
            consol.printInfo("Не найдено ни одного элемента с именем, начинающимся со строки '" + tempName + "'!");
        }
    }
    @Override
    public String getName()
    {
        return "filter_starts_with_name { name }";
    }
    @Override
    public String getDescription()
    {
        return "вывести элементы, значение поля name которых начинается с заданной подстроки";
    }
}
