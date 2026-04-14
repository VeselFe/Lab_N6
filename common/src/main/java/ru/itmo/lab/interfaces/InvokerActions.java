package ru.itmo.lab.interfaces;

import ru.itmo.lab.interfaces.Command;
import ru.itmo.lab.interfaces.IO_Handler;
import ru.itmo.lab.model.Person;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myExceptions.CommandException;

import java.util.Map;

/**
 * Интерфейс, описывающий действия manager.Invoker
 */
public interface InvokerActions
{
    public void initIOput( IO_Handler ioHandler );
    public void addCommand( String name, Command newCommand );
    public void execute(  String name,
                          Long id,
                          String arg,
                          String updatedField,
                          StudyGroup newGroup,
                          Person newAdmin ) throws CommandException;
    public Map<String, Command> getCommands();
}
