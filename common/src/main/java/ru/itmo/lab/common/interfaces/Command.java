package ru.itmo.lab.common.interfaces;

/**
 * Интерфейс для класса пользовательской команды
 */
public interface Command
{
    void execute( IO_Handler ioHandler );
    String getName();
    String getDescription();
}
