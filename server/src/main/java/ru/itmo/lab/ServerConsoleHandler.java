package ru.itmo.lab;

import ru.itmo.lab.terminal.GenericConsoleHandler;
import ru.itmo.lab.terminal.*;
import ru.itmo.lab.interfaces.IO_Handler;
import ru.itmo.lab.model.Person;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myExceptions.CreationException;
import ru.itmo.lab.manager.Invoker;

import java.util.Scanner;
/**
 * Консольный обработчик ввода-вывода для лабораторной работы №5.
 *
 * <p>Реализует интерфейс {@link IO_Handler} и расширяет {@link GenericConsoleHandler}
 * для обработки команд интерактивного режима работы программы.</p>
 *
 * <p><b>Основное назначение:</b></p>
 * <ul>
 *   <li>Чтение пользовательских команд и их выполнение через {@link Invoker}</li>
 *   <li>Интерактивный ввод сложных объектов: {@link Person}, {@link StudyGroup}</li>
 *   <li>Форматированный вывод информационных сообщений, ошибок и запросов</li>
 *   <li>Управление жизненным циклом консольного приложения</li>
 * </ul>
 */
public class ServerConsoleHandler extends GenericConsoleHandler<Invoker>
        implements IO_Handler
{
    private final Scanner scanner = new Scanner(System.in);
    private boolean stop = false;

    public ServerConsoleHandler()
    {
    }

    @Override
    public void start()
    {
        /// Логика работы
    }

    protected String getInput(  )
    {
        return "";
    }

    @Override
    protected boolean executing( String input )
    {
        if(invoker == null)
        {
            printError("Не инициализирован исполнитель команд!");
            return stop;
        }
        invoker.executeCommand(input);
        return stop;
    }

    public void printInfo( String message )
    {
        print( "<i> " + message );
    }
    public void printError( String messege )
    {
        print("<Ошибка>\n" + messege);
    }
    public void printRequest( String request )
    {
        printCurLine( request );
    }
    public void techPrint( String techMessege ) { printCurLine( techMessege ); }
    public String readline()
    {
        return scanner.nextLine();
    }
    public void stop() { stop = true; }

    public Person readPerson() throws CreationException
    {
        return new PersonReader(this).readPerson();
    }
    public StudyGroup readNewStudyGroup()
    {
        return new StudyGroupReader(this).readStudygroup();
    }
}
