package ru.itmo.lab.myExceptions;

public class RequestException extends RuntimeException
{
    public RequestException(String message)
    {
        super("Ошибка при обработке запроса: " + message);
    }
}
