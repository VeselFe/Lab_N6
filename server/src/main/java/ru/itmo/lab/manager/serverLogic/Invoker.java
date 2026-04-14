package ru.itmo.lab.manager.serverLogic;

import ru.itmo.lab.interfaces.*;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.model.Person;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myExceptions.CommandException;
import ru.itmo.lab.serverInterfaces.ServerInvokerActions;
import ru.itmo.lab.сommand.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для регистрации, обработки выполнения, передачи аргументов командам
 *
 */
public class Invoker implements ServerInvokerActions
{
    /** хранит все используемые пользовательские команды    */
    private final Map<String, Command> clientCommands;
    private final Map<String, Command> serverCommands;
    /** ссылка на обрабатываемую коллекцию    */
    private final CollectionManager ManagersCollection;
    /** обарботчик консоли    */
    private IO_Handler ioHandler;

    public Invoker( CollectionManager newManagersCollection )
    {
        ManagersCollection = newManagersCollection;
        clientCommands = new HashMap<>();
        serverCommands = new HashMap<>();
        registerCommands();
    }

    @Override
    public void initIOput( IO_Handler newIO_Handler )
    {
        ioHandler = newIO_Handler;
    }

    /**
     * Инициализация всех пользовательских команд
     */
    private void registerCommands()
    {
        /// регистрируем все команды
        addCommand("help", new HelpCommand(this));
        addCommand("info", new InfoCommand(ManagersCollection));
        addCommand("show", new ShowCommand(ManagersCollection));
        addCommand("insert_element", new InsertElCommand(ManagersCollection));
        addCommand("update_id", new UpdateIdCommand(ManagersCollection));
        addCommand("remove_key", new RemoveCommand(ManagersCollection));
        addCommand("clear", new ClearCommand(ManagersCollection));
        addCommand("exit", new ExitCommand());
        addCommand("remove_greater", new RemoveGreater(ManagersCollection));
        addCommand("remove_lower", new RemoveLower(ManagersCollection));
        addCommand("remove_lower_key", new RomoveLowerKey(ManagersCollection));
        addCommand("filter_by_semester_enum", new FilterBySemesterEnum(ManagersCollection));
        addCommand("filter_starts_with_name", new FilterStartsWithName(ManagersCollection));
        addCommand("print_ascending", new PrintAscending(ManagersCollection));

        //addCommand("execute_script", new ExecuteScriptCommand(ManagersCollection));
        addServerCommand("save", new SaveCommand(ManagersCollection, "SavedCollection.txt"));
        addServerCommand("show", new ShowCommand(ManagersCollection));
    }

    /**
     * Добавление команды в таблицу всех пользовательских команд
     *
     * @param name
     * @param newCommand
     */
    @Override
    public void addCommand( String name, Command newCommand )
    {
        if ( clientCommands.containsKey(name) )
        {
            throw new CommandException("Команда '" + name + "' уже существует");
        }
        clientCommands.put(name, newCommand);
    }
    protected void addServerCommand( String name, Command newCommand )
    {
        if ( serverCommands.containsKey(name) )
        {
            throw new CommandException("Команда '" + name + "' уже существует");
        }
        serverCommands.put(name, newCommand);
    }

    @Override
    public void execute(String name,
                        Long id,
                        String arg,
                        String updatedField,
                        StudyGroup newGroup,
                        Person newAdmin ) throws CommandException
    {
        if (name.trim().isEmpty())
        {
            throw new CommandException("Пустая команда");
        }
        Command cmd = clientCommands.get(name);
        if ( cmd == null )
        {
            throw new CommandException("Неизвестная команда: '" + name + "'");
        }
        try
        {
            if( cmd instanceof CommandWithKey )
            {
                if ( id == null )
                {
                    throw new CommandException("ID группы не определен");
                }
                ((CommandWithKey) cmd).getArgs( id );
                                                                     ioHandler.printInfo("Ключ определен: " + id.toString());
                cmd.execute(ioHandler);
            }
            else if( cmd instanceof CommandWithArgs )
            {
                String[] args = arg.trim().split("\\s+");
                if( args.length == 1 )
                {
                    CommandWithArgs cmdArgs = (CommandWithArgs) cmd;
                    cmdArgs.getArgs(args[0]);
                    cmdArgs.execute(ioHandler);
                }
                else
                {
                    throw new IllegalArgumentException("Некорректный ввод аргументов команды (Ожидается ввод 1 аргумента)");
                }
            }
            else
            {
                cmd.execute(ioHandler);
            }
        }
        catch ( IllegalArgumentException e )
        {
            throw new IllegalArgumentException("Неверные аргументы: " + e.getMessage());
        }
        catch ( Exception e )
        {
            throw new CommandException("ошибка исполнения команды '" + name + "': " + e.getMessage());
        }
    }
    @Override
    public void executeServerCommand( String name )
    {
        if (name.trim().isEmpty())
        {
            throw new CommandException("Пустая команда");
        }
        Command serverCommand = serverCommands.get(name);
        if( serverCommand == null )
        {
            throw new CommandException("Неизвестная команда: '" + name + "'");
        }
        if( ioHandler == null )
        {
            throw new CommandException("Не инициализирован канал для вывода технических сообщений!");
        }
        try
        {
            serverCommand.execute( ioHandler );
        }
        catch( Exception e )
        {
            ioHandler.printError("Ошибка при попытке завершить работу сервера: " + e.getMessage());
        }
    }

    /**
     * Возвращает таблицу всех команд
     *
     * @return Map
     */
    @Override
    public Map<String, Command> getCommands()
    {
        return clientCommands;
    }
}
