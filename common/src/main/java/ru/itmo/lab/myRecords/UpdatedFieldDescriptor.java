package ru.itmo.lab.myRecords;

import java.io.Serializable;

public record UpdatedFieldDescriptor(
    String name,
    String request,
    String methodName,
    Class<?> type
) implements Serializable
{
    private static final long serialVersionUID = 666L;
}
