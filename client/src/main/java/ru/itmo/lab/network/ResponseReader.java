package ru.itmo.lab.network;

import ru.itmo.lab.commonNet.Response;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ResponseReader
{
    public static Response read( ObjectInputStream inputStream ) throws IOException, ClassNotFoundException
    {
        Object serverObject = inputStream.readObject();
        if( serverObject == null )
        {
            throw new IOException("Получен пустой пакет!");
        }
        return (Response) serverObject;
    }
}
