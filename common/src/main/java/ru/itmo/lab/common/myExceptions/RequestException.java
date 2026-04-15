package ru.itmo.lab.common.myExceptions;

public class RequestException extends RuntimeException
{
    public RequestException(String message)
    {
        super("Ошибка при обработке запроса: " + message);
    }
}
