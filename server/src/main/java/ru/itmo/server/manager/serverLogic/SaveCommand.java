package ru.itmo.server.manager.serverLogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.lab.common.interfaces.OutputHandler;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.server.manager.collection.fileManagement.GroupsFileManager;
import ru.itmo.lab.common.myExceptions.FileManagerException;
import ru.itmo.lab.common.interfaces.IO_Handler;
import ru.itmo.server.serverInterfaces.ServerCommand;


/**
 * Команда для сохранении коллекции в файл
 */
public class SaveCommand implements ServerCommand
{
    public static final Logger logger = LoggerFactory.getLogger(SaveCommand.class);
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
    public void execute( OutputHandler consol )
    {
        try
        {
            fileManager.save( collection.getStudyGroups() );
            logger.info("Коллекция успешно соранена в файл.");
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
