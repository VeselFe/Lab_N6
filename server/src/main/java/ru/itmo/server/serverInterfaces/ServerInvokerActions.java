package ru.itmo.server.serverInterfaces;

import ru.itmo.lab.common.interfaces.InvokerActions;

public interface ServerInvokerActions extends InvokerActions
{
    void executeServerCommand( String name );
}
