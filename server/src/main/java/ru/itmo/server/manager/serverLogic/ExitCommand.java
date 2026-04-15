package ru.itmo.server.manager.serverLogic;

import ru.itmo.lab.common.interfaces.Command;
import ru.itmo.lab.common.interfaces.IO_Handler;
import ru.itmo.lab.common.interfaces.OutputHandler;
import ru.itmo.server.serverInterfaces.ServerCommand;

/**
 * Команда для выхода из программы
 */
public class ExitCommand implements ServerCommand
{
    public ExitCommand()
    {
    }
    @Override
    public void execute( OutputHandler console )
    {
        console.printInfo("Подключение прекращено. Сервер Отключен.");
        CommandProccessor.stopServerProgramm();
    }

    @Override
    public String getName()
    {
        return "exit";
    }
    @Override
    public String getDescription()
    {
        return "завершить программу";
    }
}
