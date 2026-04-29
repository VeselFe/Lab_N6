package ru.itmo.client.clientTerminal;

import ru.itmo.lab.common.commonNet.Request;
import ru.itmo.lab.common.terminal.AbstractConsoleHandler;
import ru.itmo.lab.common.terminal.PersonReader;
import ru.itmo.lab.common.terminal.StudyGroupReader;
import ru.itmo.lab.common.interfaces.IO_Handler;
import ru.itmo.lab.common.model.Person;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.lab.common.myExceptions.CreationException;

import java.util.Scanner;

public class ClientConsoleHandler extends AbstractConsoleHandler
    implements IO_Handler
{
    private final Scanner scanner = new Scanner(System.in);
    private String input;
    private RequestCreator requestCreator;

    public ClientConsoleHandler()
    {
    }

    public void welcomMessage()
    {
        print("Введите 'help' для просмотра возможных команд.");
    }
    public void initRequestCreator( IO_Handler ioHandler )
    {
        this.requestCreator = new RequestCreator( ioHandler );
    }

    public Request createRequest()
    {
        if( requestCreator == null ) throw new RuntimeException("Сборщик запроса не инициирован.");
        print("\n         Введите команду");
        print("=====================================");
        input = readline();

        return requestCreator.buildRequest(input);
    }

    public void printInfo( String message )
    {
        print( "<i> " + message );
    }
    public void printError( String message )
    {
        print("<Ошибка>\n" + message);
    }
    public void printRequest( String request )
    {
        printCurLine( request );
    }
    public String readline()
    {
        return scanner.nextLine();
    }
    public Person readPerson() throws CreationException
    {
        return new PersonReader(this).readPerson();
    }
    public StudyGroup readNewStudyGroup()
    {
        return new StudyGroupReader(this).readStudygroup();
    }
}
