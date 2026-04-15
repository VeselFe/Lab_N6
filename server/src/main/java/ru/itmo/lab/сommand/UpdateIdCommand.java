package ru.itmo.lab.сommand;

import ru.itmo.lab.interfaces.CommandWithKey;
import ru.itmo.lab.interfaces.Updatable;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.model.Person;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myExceptions.CommandException;
import ru.itmo.lab.myRecords.FieldDescriptor;
import ru.itmo.lab.myRecords.Lab5FieldDescriptor;
import ru.itmo.lab.interfaces.IO_Handler;
import ru.itmo.lab.myRecords.UpdatedFieldDescriptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Команда для обновления указанного пользователем поля элеменат коллекции
 */
public class UpdateIdCommand implements CommandWithKey, Updatable
{
    private final CollectionManager collection;
    private Long Key;
    private String argument;
    private UpdatedFieldDescriptor updatedField;
    private Person newAdmin;

    public UpdateIdCommand(CollectionManager newCollection )
    {
        collection = newCollection;
    }

    @Override
    public void getArgs( Long Args )
    {
        try
        {
            Key = Long.valueOf( Args );
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Некорректные данные для ключа");
        }
    }

    @Override
    public void setUpdatedFieldName( UpdatedFieldDescriptor upName ) { updatedField = upName; }
    @Override
    public void setUpdatedPerson(Person newPerson ) { newAdmin = newPerson; }
    @Override
    public void setArgument( String value ) { argument = value; }
    @Override
    public void setUpID( Long id ) { Key = id; }

    @Override
    public void execute( IO_Handler console )
    {
        try
        {
            if( Key == null )
                throw new CommandException("Не установлен ключ обновляемого элемента!");
            StudyGroup group = collection.getStudyGroups().get(Key);
            if( group == null )
                throw new CommandException("Элемент с ключом " + Key + " не найден в коллекции!");
            if( updatedField == null )
                throw new CommandException("Не установлено название обновляемого поля!");

            if (updatedField.name().equals("admin"))
            {
                if(newAdmin == null)
                    throw new CommandException("Не определен новый админ!");
                group.updateAdmin(newAdmin);
            }
            else
            {
                if(argument == null)
                    throw new CommandException("Новое значение поля '" + updatedField.name() + "' не определено!");
                Method method = group.getClass().getMethod(updatedField.methodName(), updatedField.type());
                method.invoke(group, argument);
            }
        }
        catch( NoSuchMethodException e )
        {
            throw new CommandException("Ошибка при обновлении поля группы: метод " + updatedField.methodName() + " не найден в классе StudyGroup.");
        }
        catch( IllegalAccessException e )
        {
            throw new CommandException("Ошибка доступа: серверу запрещено вызывать метод " + updatedField.methodName());
        }
        catch( InvocationTargetException e )
        {
            throw new CommandException("Ошибка при валидации данных: " + e.getCause());
        }
        catch (Exception e)
        {
            throw new CommandException("Непредвиденная ошибка рефлексии методов: " + e.getMessage());
        }
    }

    @Override
    public String getName()
    {
        return "update_id";
    }
    @Override
    public String getDescription()
    {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}
