package ru.itmo.lab.manager.serverLogic;

import ru.itmo.lab.commonNet.Request;
import ru.itmo.lab.commonNet.Response;
import ru.itmo.lab.ioHandlers.ResponseIOHandler;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.serverInterfaces.ServerInvokerActions;

import java.util.Collection;
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
