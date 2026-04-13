package ru.itmo.lab.manager.serverLogic;

import ru.itmo.lab.commonNet.Request;
import ru.itmo.lab.commonNet.Response;
import ru.itmo.lab.ioHandlers.ResponseIOHandler;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.serverInterfaces.InvokerActions;

import java.util.Collection;

public class CommandProccessor
{
    public static Response ProcessRequest(Request clientRequest, InvokerActions invoker )
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
}
