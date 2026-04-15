package ru.itmo.server.manager.collection.fileManagement;

import ru.itmo.lab.common.interfaces.OutputHandler;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.lab.common.model.StudyGroup;

import java.util.Hashtable;

/**
 * Класс, реализующий чтение и обработку коллекции из xml-файла
 */
public class LoadCollection
{
    private final CollectionManager collection;
    private final String fileName;
    private final GroupsFileManager fileManager;
    private final OutputHandler printer;

    public LoadCollection(CollectionManager newCollection, String newFileName, OutputHandler newConsole )
    {
        collection = newCollection;
        fileName = newFileName;
        fileManager = new GroupsFileManager( fileName );
        printer = newConsole;
    }

    public void loadNewCollection()
    {
        try
        {
            Hashtable<Long, StudyGroup> loadedCollection = fileManager.load();

            collection.getStudyGroups().clear();
            for (Long key : loadedCollection.keySet())
            {
                collection.getStudyGroups().put(key, loadedCollection.get( key ));
            }
            printer.printInfo("Загружено " + collection.getStudyGroups().size() + " групп(-ы).\n");
        }
        catch( Exception e )
        {
            printer.printError(e.getMessage());
        }
    }
}
