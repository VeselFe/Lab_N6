package ru.itmo.server.manager.serverLogic;

import ru.itmo.lab.common.interfaces.Command;
import ru.itmo.lab.common.interfaces.IO_Handler;

/**
 * Команда для выхода из программы
 */
public class ExitCommand implements Command
{
    public ExitCommand()
    {
    }
    @Override
    public void execute( IO_Handler console )
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
