package ru.itmo.lab.myEnums;

public enum Commands
{
    HELP("help", false),
    INFO("info", false),
    SHOW("show", false),
    INSERT("insert_element", true),
    UPDATE("update_id", true),
    REMOVE("remove_id", true),
    CLEAR("clear", false),
    EXECUTE("execute_script", true),
    EXIT("exit", false),
    REMOVE_GREATER("remove_greater", true),
    REMOVE_LOWER("remove_lower", true),
    //REMOVE_LOWER("remove_lower", true),
    FILTER_SEM("filter_by_semester_enum", true),
    FILTER_NAME("filter_starts_with_name", true),
    PRINT("print_ascending", false);

    private final String name;
    private final boolean arguments;
    Commands(String name, boolean arguments)
    {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() { return name; }
    public  boolean haveArguments() { return arguments; }
    public static Commands find( String name )
    {
        for( Commands command : values() )
        {
            if( command.name.equalsIgnoreCase(name) ) return command;
        }
        return null;
    }
}
