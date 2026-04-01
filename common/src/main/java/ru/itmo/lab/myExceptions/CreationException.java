package ru.itmo.lab.myExceptions;

public class CreationException extends RuntimeException
{
    public CreationException(String message)
    {
        super("Ошибка создания сущности: " + message);
    }
}
