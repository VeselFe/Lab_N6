package ru.itmo.lab.interfaces;

public interface OutputHandler
{
    void printInfo( String messege );
    void printRequest( String request );
    void printError( String messege );
}
