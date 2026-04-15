package ru.itmo.lab.manager.collection;

import ru.itmo.lab.model.StudyGroup;
import java.util.Comparator;

public class StudyGroupByNameComparator implements Comparator<StudyGroup>
{
    @Override
    public int compare(StudyGroup group1, StudyGroup group2 )
    {
        return group1.getName().compareTo(group2.getName());
    }
}
