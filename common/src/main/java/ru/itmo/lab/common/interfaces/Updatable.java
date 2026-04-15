package ru.itmo.lab.common.interfaces;

import ru.itmo.lab.common.model.Person;
import ru.itmo.lab.common.myRecords.UpdatedFieldDescriptor;

public interface Updatable
{
    void setUpdatedFieldName( UpdatedFieldDescriptor upName );
    void setUpdatedPerson(Person newPerson );
    void setArgument( String value );
    void setUpID( Long id );
}
