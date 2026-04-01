package ru.itmo.lab.terminal;

public class Terminal
{
    public static void printInfo( String messege )
    {
        System.out.println("<i> " + messege);
    }
    public static void techPrint( String messege )
    {
        System.out.println(messege);
    }
    public static void printError( String errorMessege )
    {
        System.out.println("<e> " + errorMessege);
    }

}
