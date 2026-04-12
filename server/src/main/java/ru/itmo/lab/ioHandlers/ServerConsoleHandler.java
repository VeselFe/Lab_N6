package ru.itmo.lab.ioHandlers;

import ru.itmo.lab.interfaces.OutputHandler;
import ru.itmo.lab.terminal.*;
import ru.itmo.lab.interfaces.IO_Handler;
import ru.itmo.lab.manager.serverLogic.Invoker;

/**
 * Консольный обработчик вывода для лабораторной работы №6.
 *
 * <p>Реализует интерфейс {@link IO_Handler} и расширяет {@link GenericConsoleHandler}
 * для обработки команд интерактивного режима работы программы.</p>
 *
 * <p><b>Основное назначение:</b></p>
 * <ul>
 *   <li>Чтение пользовательских команд и их выполнение через {@link Invoker}</li>
 *   <li>Форматированный вывод информационных сообщений, ошибок и запросов</li>
 * </ul>
 */
public class ServerConsoleHandler extends GenericConsoleHandler<Invoker>
        implements OutputHandler
{
    public ServerConsoleHandler()
    {
    }

    public void printInfo( String message )
    {
        print( "СЕРВЕР: " + message );
    }
    public void printError( String messege )
    {
        print("СЕРВЕР: <Ошибка> " + messege + "\n");
    }
    public void printRequest( String request ) {}
    public void techPrint( String techMessege ) { print( techMessege ); }
}
