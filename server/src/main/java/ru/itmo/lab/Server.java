package ru.itmo.lab;

import ru.itmo.lab.terminal.Terminal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private static final int port = 8080;

    public static void main(String[] args)
    {
        System.out.println("Сервер запустился. Порт: " + port);
        try( ServerSocket serverSocket = new ServerSocket(port) )
        {
            while( true )
            {
                Terminal.techPrint("Ожидание подключения клиента...");
                Socket clientSocket = serverSocket.accept();
                Terminal.printInfo("Клиент подключен: " + clientSocket.getInetAddress());

                handleClient(clientSocket);
            }
        }
        catch( IOException e )
        {
            Terminal.printError(e.getMessage());
        }
    }

    public static void handleClient( Socket clientSocket )
    {
        try( InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream(); )
        {
            Terminal.printInfo("Потоки ввода-вывода инициализированы.");
        }
        catch( IOException e )
        {
            Terminal.printError("Ошибка при обмене данными с клиентом: " + e.getMessage());
        }
    }
}