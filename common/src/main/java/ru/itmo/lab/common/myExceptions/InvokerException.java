package ru.itmo.lab.common.myExceptions;

public class InvokerException extends RuntimeException
{
    public InvokerException( String mes )
    {
        super("Ошибка при работе исполнителя команд:\n" + mes);
    }
}
