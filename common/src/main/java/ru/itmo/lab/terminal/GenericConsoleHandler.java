package ru.itmo.lab.terminal;

import ru.itmo.lab.interfaces.IO_Handler;import ru.itmo.lab.interfaces.IO_Handler;import java.util.Scanner;

/**
 * Абстрактный базовый класс для обработки консольного ввода команд.
 *
 * Предоставляет общую логику интерактивного консольного интерфейса для
 * различных типов инвокаторов команд. Наследники реализуют специфическую
 * логику выполнения команд через абстрактный метод {@link #executing()}.
 */
abstract public class GenericConsoleHandler<T>
{
    protected T provider;
    private boolean exit = false;

    public GenericConsoleHandler()
    {    }

    public void setProvider( T provider )
    {
        this.provider = provider;
    }

    protected String getInput( Scanner scanner )
    {
        return scanner.nextLine();
    }
    final protected void print( String message ) { System.out.println(message); }
    final protected void printCurLine( String message ) { System.out.print(message); }
    protected void error( String messege ) { print("Ошибка: " + messege); }
    protected void welcomMessage() { print("Go!"); }

    protected boolean executing() { return false; };
    protected boolean executing( String input ) { return false; }
}
