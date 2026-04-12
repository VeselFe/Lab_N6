package ru.itmo.lab.myExceptions;

public class ConnectionException extends RuntimeException
{
    public ConnectionException( String mes )
    {
        super( mes );
    }
}
