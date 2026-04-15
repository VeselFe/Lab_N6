package ru.itmo.server.manager.serverLogic;

import ru.itmo.lab.common.commonNet.Request;
import ru.itmo.lab.common.commonNet.Response;
import ru.itmo.server.ioHandlers.ResponseIOHandler;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.server.serverInterfaces.ServerInvokerActions;

import java.util.List;

public class CommandProccessor
{
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
            invoker.initIOput(responseHandler);
            invoker.execute(clientRequest.getCommandType(),
                                   clientRequest.getIdArg(),
                                   clientRequest.getArgument(),
                                   clientRequest.getUpdatedField(),
                                   clientRequest.getGroup(),
                                   clientRequest.getAdmin() );
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
