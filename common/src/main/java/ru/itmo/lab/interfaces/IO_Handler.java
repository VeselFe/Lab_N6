package ru.itmo.lab.interfaces;


import ru.itmo.lab.model.Person;
import ru.itmo.lab.model.StudyGroup;

/**
 * Интерфейс обработки консольного ввода-вывода для команд имеющий вводные значение и вывод резуьтата
 */
public interface IO_Handler extends OutputHandler
{
    String readline();
    StudyGroup readNewStudyGroup();
    Person readPerson();
}
