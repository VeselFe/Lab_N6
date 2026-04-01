package ru.itmo.lab;

import ru.itmo.lab.generators.BasicGenerator;
import ru.itmo.lab.manager.CollectionManager;
import ru.itmo.lab.manager.Invoker;
import ru.itmo.lab.model.StudyGroup;

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
        /// Серверный менеджер коллекцией
        CollectionManager mainCollection = CollectionManager.createCollection();
        StudyGroup.setIdGenerator( new BasicGenerator(mainCollection) );
        Invoker invoker = new Invoker( mainCollection );
        ServerConsoleHandler console = new ServerConsoleHandler();
        console.setInvoker(invoker);

        /// Сеть
        System.out.println("Сервер запустился. Порт: " + port);
        try( ServerSocket serverSocket = new ServerSocket(port) )
        {
            while( true )
            {
                console.techPrint("Ожидание подключения клиента...");
                Socket clientSocket = serverSocket.accept();
                console.printInfo("Клиент подключен: " + clientSocket.getInetAddress());

                handleClient(clientSocket, console);
            }
        }
        catch( IOException e )
        {
            console.printError(e.getMessage());
        }
    }

    public static void handleClient( Socket clientSocket, ServerConsoleHandler console )
    {
        try( InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream(); )
        {
            console.printInfo("Потоки ввода-вывода инициализированы.");
            // логика обработки поступившей информации
        }
        catch( IOException e )
        {
            console.printError("Ошибка при обмене данными с клиентом: " + e.getMessage());
        }
    }
}