package ru.itmo.lab.serverNetManager;

import ru.itmo.lab.commonNet.Request;
import ru.itmo.lab.interfaces.OutputHandler;

import java.io.IOException;
import java.io.ObjectInputStream;

public class RequestReader
{
    public static Request read(ObjectInputStream inputStream ) throws IOException, ClassNotFoundException
    {
        Object clientObject = inputStream.readObject();
        if( clientObject == null )
        {
            throw new IOException("Получен пустой пакет!");
        }
        return (Request) clientObject;
    }
}
