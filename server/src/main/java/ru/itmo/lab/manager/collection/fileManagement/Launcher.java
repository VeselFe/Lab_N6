package ru.itmo.lab.manager.collection.fileManagement;

import ru.itmo.lab.interfaces.OutputHandler;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.myExceptions.FileManagerException;

/**
 * Класс для загрузки коллекции из файла по названию Пользователя
 */
final public class Launcher
{
    private final CollectionManager collection;
    private final OutputHandler console;

    public Launcher( CollectionManager newCollection, OutputHandler ioHandler )
    {
        collection = newCollection;
        console = ioHandler;
    }

    public void launchCollection()
    {
        try
        {
            String collectionFileName = "SavedCollection.txt";
            new LoadCollection(collection, collectionFileName, console).loadNewCollection();
        }
        catch( Exception e )
        {
            throw new FileManagerException("Ошибка загрузки файла!" + e);
        }
    }
}
