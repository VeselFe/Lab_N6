package ru.itmo.lab.ioHandlers;

import ru.itmo.lab.commonNet.Response;
import ru.itmo.lab.interfaces.IO_Handler;
import ru.itmo.lab.manager.serverLogic.CommandProccessor;
import ru.itmo.lab.model.Person;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myExceptions.CommandException;

public class ResponseIOHandler implements IO_Handler
{
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
        return Response.builder()
                .setSuccess(!hasError)
                .setMessage(output.toString())
                .setSortedCollection(CommandProccessor.getSortedGroups())
                .buildResponse();
    }
}
