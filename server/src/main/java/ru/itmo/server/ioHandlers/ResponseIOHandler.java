package ru.itmo.server.ioHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.lab.common.commonNet.Response;
import ru.itmo.lab.common.interfaces.IO_Handler;
import ru.itmo.server.Server;
import ru.itmo.server.manager.serverLogic.CommandProccessor;
import ru.itmo.lab.common.model.Person;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.lab.common.myExceptions.CommandException;

public class ResponseIOHandler implements IO_Handler
{
    public static final Logger logger = LoggerFactory.getLogger(ResponseIOHandler.class);
    private StringBuilder output = new StringBuilder();
    private StudyGroup studyGroupFromRequest;
    private Person adminFromRequest;
    private boolean hasError = false;

    public void setStudyGroupFromRequest( StudyGroup groupFromRequest )
    {
        studyGroupFromRequest = groupFromRequest;
    }
    public void setAdmin( Person adminFromRequest )
    {
        this.adminFromRequest = adminFromRequest;
    }

    public StudyGroup readNewStudyGroup()
    {
        if( studyGroupFromRequest == null )
        {
            throw new CommandException("Данные группы не были переданы");
        }
        studyGroupFromRequest.generateGroupID();

        return studyGroupFromRequest;
    }
    public Person readPerson()
    {
        if( adminFromRequest == null )
        {
            throw new CommandException("Данные админа не были переданы");
        }
        return adminFromRequest;
    }

    public void printError( String messege )
    {
        hasError = true;
        output.append("<Error> " + messege + "\n");
    }
    public void printInfo( String messege ) { output.append(messege + "\n"); }
    public void printRequest( String request ) {}
    public String readline() { return ""; }

    public Response createResponse()
    {
        String respone = output.toString();
        logger.debug("Ответ сформирован.");
        logger.debug(!hasError ? "запрос обработан успешно." : "при обработке запроса возникли ошибки");
        logger.debug("Сформированный ответ сервера: " + respone);
        return Response.builder()
                .setSuccess(!hasError)
                .setMessage(respone)
                .setSortedCollection(CommandProccessor.getSortedGroups())
                .buildResponse();
    }
}
