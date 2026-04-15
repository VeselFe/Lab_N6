package ru.itmo.lab.interfaces;

import ru.itmo.lab.model.Person;
import ru.itmo.lab.myRecords.UpdatedFieldDescriptor;

public interface Updatable
{
    void setUpdatedFieldName( UpdatedFieldDescriptor upName );
    void setUpdatedPerson(Person newPerson );
    void setArgument( String value );
    void setUpID( Long id );
}
