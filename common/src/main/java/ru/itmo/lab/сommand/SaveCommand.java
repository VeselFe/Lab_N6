package ru.itmo.lab.сommand;

import ru.itmo.lab.interfaces.Command;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.manager.collection.fileManagement.GroupsFileManager;
import ru.itmo.lab.myExceptions.FileManagerException;
import ru.itmo.lab.interfaces.IO_Handler;


/**
 * Команда для сохранении коллекции в файл
 */
public class SaveCommand implements Command
{
    private final CollectionManager collection;
    private final String fileName;
    private final GroupsFileManager fileManager;

    public SaveCommand( CollectionManager newCollection, String newFileName )
    {
        collection = newCollection;
        fileName = newFileName;
        fileManager = new GroupsFileManager( fileName );
    }

    @Override
    public void execute( IO_Handler consol )
    {
        try
        {
            fileManager.save( collection.getStudyGroups() );
        }
        catch ( Exception e )
        {
            throw new FileManagerException("ошибка сохранения коллекции в файл '" + fileName + "'");
        }
        finally
        {
            consol.printInfo("Коллекция успешно сохранена!");
        }
    }
    @Override
    public String getName()
    {
        return "save";
    }
    @Override
    public String getDescription()
    {
        return "сохранить коллекцию в файл";
    }
}
