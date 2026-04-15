package ru.itmo.lab.common.interfaces;

import ru.itmo.lab.common.model.Person;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.lab.common.myExceptions.CommandException;
import ru.itmo.lab.common.myRecords.UpdatedFieldDescriptor;

import java.util.Map;

/**
 * Интерфейс, описывающий действия manager.Invoker
 */
public interface InvokerActions
{
    public void initIOput( OutputHandler oHandler );
    public void addCommand( String name, Command newCommand );
    public void execute(  String name,
                          Long id,
                          String arg,
                          UpdatedFieldDescriptor updatedField,
                          StudyGroup newGroup,
                          Person newAdmin ) throws CommandException;
    public Map<String, Command> getCommands();
}
