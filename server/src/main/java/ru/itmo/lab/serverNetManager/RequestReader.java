package ru.itmo.lab.serverNetManager;

import ru.itmo.lab.commonNet.Request;

import java.io.IOException;
import java.io.ObjectInputStream;

public class RequestReader
{
    public static Request read( ObjectInputStream objectInputStream ) throws IOException, ClassNotFoundException
    {
        return (Request) objectInputStream.readObject();
    }
}
