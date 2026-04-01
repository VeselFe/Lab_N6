package ru.itmo.lab.network;

import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myExceptions.CommandException;

import java.io.Serializable;

public class Request implements Serializable
{
    private final String commandType;
    private final String argument;
    private final Long idArg;
    private final StudyGroup group;

    private Request( Builder builder )
    {
        this.commandType = builder.commandType;
        this.argument = builder.argument;
        this.idArg = builder.idArg;
        this.group = builder.group;
    }

    public static class Builder
    {
        private String commandType = null;
        private String argument = null;
        private Long idArg = null;
        private StudyGroup group = null;

        public Request buildRequest()
        {
            return new Request( this );
        }

        public Builder setCommandType( String newCommandType )
        {
            if( newCommandType.isEmpty() )
            {
                throw new CommandException("Некорректный тип команды!");
            }

            commandType = newCommandType;
            return this;
        }
        public Builder setArgument( String newArgument )
        {
            if( newArgument.isEmpty() )
            {
                throw new CommandException("Некорректный аргумент команды!");
            }

            argument = newArgument;
            return this;
        }
        public Builder setID( Long newID )
        {
            if( newID == null )
            {
                throw new CommandException("Некорректный аргумент команды!");
            }

            idArg = newID;
            return this;
        }
        public Builder setGroup( StudyGroup newGroup )
        {
            if( newGroup == null )
            {
                throw new CommandException("Некорректный аргумент команды!");
            }

            group = newGroup;
            return this;
        }
    }
}
