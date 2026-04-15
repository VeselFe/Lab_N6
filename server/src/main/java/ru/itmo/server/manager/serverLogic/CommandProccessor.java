package ru.itmo.server.manager.serverLogic;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import ru.itmo.lab.common.commonNet.Request;
import ru.itmo.lab.common.commonNet.Response;
import ru.itmo.lab.common.model.Person;
import ru.itmo.lab.common.myRecords.UpdatedFieldDescriptor;
import ru.itmo.server.ioHandlers.ResponseIOHandler;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.server.serverInterfaces.ServerInvokerActions;

import java.util.List;

public class CommandProccessor
{
    public static final Logger logger = LoggerFactory.getLogger(CommandProccessor.class);
    private static boolean exit = false;
    private ServerInvokerActions invoker;
    private static List<StudyGroup> sortedByNameGroup = null;
    private CollectionManager serverCollectionManager;

    public CommandProccessor( ServerInvokerActions invoker, CollectionManager serverCollectionManager )
    {
        this.invoker = invoker;
        this.serverCollectionManager = serverCollectionManager;
    }

    public Response ProcessRequest( Request clientRequest )
    {
        ResponseIOHandler responseHandler = new ResponseIOHandler();
        responseHandler.setStudyGroupFromRequest(clientRequest.getGroup());
        responseHandler.setAdmin(clientRequest.getAdmin());
        try
        {
            String name = clientRequest.getCommandType();
            Long id = clientRequest.getIdArg();
            String arg = clientRequest.getArgument();
            UpdatedFieldDescriptor updatedField = clientRequest.getUpdatedField();
            StudyGroup newGroup = clientRequest.getGroup();
            Person newAdmin = clientRequest.getAdmin();

            logger.debug("Получен запрос");
            logger.debug("cmd type: " + (name != null ? name : "null"));
            logger.debug("arg: " + (arg != null ? arg : "null"));
            logger.debug("updated field name: " + (updatedField != null ? updatedField.name() : "null"));
            logger.debug("group: " + (newGroup != null ? newGroup.getInformation() : "null"));
            logger.debug("admin: " + (newAdmin != null ? newAdmin.getName() : "null"));
            invoker.initIOput(responseHandler);
            invoker.execute(name,
                           id,
                           arg,
                           updatedField,
                           newGroup,
                           newAdmin );
            setSortedByNameGroup();
            return responseHandler.createResponse();
        }
        catch( Exception e )
        {
            return Response.builder()
                    .setSuccess(false)
                    .setMessage("<ServerError> " + e.getMessage() + "\n")
                    .buildResponse();
        }
    }
    public static void stopServerProgramm()
    {
        exit = true;
    }
    public static void restartServerProgramm() { exit = false; }
    public static List<StudyGroup> getSortedGroups() { return sortedByNameGroup; }
    public void setSortedByNameGroup() { sortedByNameGroup = serverCollectionManager.getSortedByNameCollection(); }
    public boolean isProgrammFinished()
    {
        return exit;
    }
    public void saveCollection()
    {
        invoker.executeServerCommand("save");
    }
}
