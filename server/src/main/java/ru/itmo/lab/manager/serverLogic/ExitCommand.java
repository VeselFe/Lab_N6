package ru.itmo.lab.manager.serverLogic;

import ru.itmo.lab.interfaces.Command;
import ru.itmo.lab.interfaces.IO_Handler;

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
