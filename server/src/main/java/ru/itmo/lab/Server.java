package ru.itmo.lab;

import ru.itmo.lab.commonNet.Response;
import ru.itmo.lab.generators.BasicGenerator;
import ru.itmo.lab.ioHandlers.ServerConsoleHandler;
import ru.itmo.lab.manager.collection.CollectionManager;
import ru.itmo.lab.manager.collection.fileManagement.GroupsFileManager;
import ru.itmo.lab.manager.collection.fileManagement.Launcher;
import ru.itmo.lab.manager.serverLogic.CommandProccessor;
import ru.itmo.lab.manager.serverLogic.Invoker;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.commonNet.Request;
import ru.itmo.lab.serverNetManager.RequestReader;

import java.io.*;
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
        GroupsFileManager.setErrorPrinter(console);
        new Launcher(mainCollection, console).launchCollection();
        console.setProvider(invoker);

        /// Сеть
        System.out.println("Сервер запустился. Порт: " + port);
        try( ServerSocket serverSocket = new ServerSocket(port) )
        {
            while( true )
            {
                console.techPrint("Ожидание подключения клиента...");
                Socket clientSocket = serverSocket.accept();
                console.printInfo("Клиент подключен: " + clientSocket.getInetAddress());
                handleClient(clientSocket, console, invoker);
            }
        }
        catch( IOException e )
        {
            console.printError(e.getMessage());
        }
    }

    public static void handleClient( Socket clientSocket, ServerConsoleHandler console, Invoker invoker )
    {
        try(ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream()); )
        {
            console.printInfo("Потоки ввода-вывода инициализированы.");
            // логика обработки поступившей информации
            // читаем запрос
            Request request = RequestReader.read(input);
            console.techPrint("Получен запрос: " + request.getCommandType());

            // обрабатываем
            Response response = CommandProccessor.ProcessRequest(request, invoker);
            System.out.println(response.isSuccess());
            System.out.println(response.getMessage());
            // отправляем обратно ответ
            //sendResponse(clientSocket, response);

            console.printInfo("Запрос обработан успешно.");
        }
        catch ( ClassNotFoundException e )
        {
            console.printError("Неивестная команда");
        }
        catch( IOException e )
        {
            console.printError("Ошибка при обмене данными с клиентом: " + e.getMessage());
        }
    }

}