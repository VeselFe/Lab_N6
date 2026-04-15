package ru.itmo.server.сommand;

import ru.itmo.lab.common.interfaces.Command;
import ru.itmo.lab.common.interfaces.IO_Handler;
import ru.itmo.lab.common.interfaces.InvokerActions;

import java.util.Comparator;

/**
 * Команда для вывода в консоль списка всех пользовательских команд
 */
public class HelpCommand implements Command
{
    private final InvokerActions invoker;

    public HelpCommand( InvokerActions newInvokerInterface )
    {
        invoker = newInvokerInterface;
    }

    @Override
    public void execute( IO_Handler consol )
    {
        consol.printInfo("Список всех возможных команд:\n" +
                "--------------------------------------------------------------");
        invoker.getCommands().values().stream()
                .sorted(Comparator.comparing(Command::getName))
                .forEach(cmd -> consol.printInfo(cmd.getName() + ": " + cmd.getDescription()));
    }

    @Override
    public String getName()
    {
        return "help";
    }
    @Override
    public String getDescription()
    {
        return "вывести справку по доступным командам";
    }
}
