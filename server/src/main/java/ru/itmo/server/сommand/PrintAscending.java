package ru.itmo.server.сommand;

import ru.itmo.lab.common.interfaces.Command;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.lab.common.interfaces.IO_Handler;

import java.util.List;

/**
 * Команда для отсортированного по названию вывода всех элементов коллекции
 */
public class PrintAscending implements Command
{
    private CollectionManager collectionManager;
    public PrintAscending( CollectionManager newCollectionManager )
    {
        collectionManager = newCollectionManager;
    }

    @Override
    public void execute( IO_Handler consol )
    {
        String flag = "";
        consol.printInfo("Элементы коллекции в отсортированном порядке: ");
        consol.printInfo("***************************************");
        List<StudyGroup> sortedGroups = collectionManager.getSortedCollection().stream()
                .toList();
        sortedGroups.forEach(group -> consol.printInfo(group.getInformation()));

        if( sortedGroups.isEmpty() )
        {
            consol.printInfo("В колекции отсутствуют элементы!");
        }
        consol.printInfo("***************************************\n");
    }
    @Override
    public String getName()
    {
        return "print_ascending";
    }
    @Override
    public String getDescription()
    {
        return "вывести элементы коллекции в порядке возрастания (по id)";
    }

}
