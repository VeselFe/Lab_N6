package ru.itmo.lab.serverInterfaces;

import ru.itmo.lab.interfaces.InvokerActions;

public interface ServerInvokerActions extends InvokerActions
{
    void executeServerCommand( String name );
}
