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
import ru.itmo.lab.serverNetManager.ResponseSender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server
{
    private static final int port = 8080;

    public static void main(String[] args)
    {
        /// Серверный менеджер коллекцией
        CollectionManager mainCollection = CollectionManager.createCollection();
        StudyGroup.setIdGenerator( new BasicGenerator(mainCollection) );
        Invoker invoker = new Invoker( mainCollection );
        CommandProccessor mainProccessor = new CommandProccessor( invoker );

        ServerConsoleHandler console = new ServerConsoleHandler();
        GroupsFileManager.setErrorPrinter(console);
        new Launcher(mainCollection, console).launchCollection();
        console.setProvider(invoker);

        /// Сеть
        console.printInfo("Сервер запустился. Порт: " + port);
        try( ServerSocket serverSocket = new ServerSocket(port) )
        {
            while( true )
            {
                console.techPrint("Ожидание подключения клиента...");
                Socket clientSocket = serverSocket.accept();
                console.printInfo("Клиент подключен: " + clientSocket.getInetAddress());
                handleClient(clientSocket, console, mainProccessor);
            }
        }
        catch( IOException e )
        {
            console.printError("Ошибка сервера: " + e.getMessage());
        }
    }

    public static void handleClient( Socket clientSocket, ServerConsoleHandler console, CommandProccessor proccessor )
    {
        console.printInfo("Потоки ввода-вывода инициализированы.");
        CommandProccessor.restartServerProgramm();
        while( !clientSocket.isClosed() )
        {
            try
            {
                InputStream is = clientSocket.getInputStream();
                OutputStream os = clientSocket.getOutputStream();
                ObjectInputStream input = new ObjectInputStream(is);

                // логика обработки поступившей информации
                // читаем запрос
                Request request = RequestReader.read(input);
                console.techPrint("Получен запрос: " + request.getCommandType() + "\n");

                // обрабатываем
                Response response = proccessor.ProcessRequest( request );

                if( proccessor.isProgrammFinished() )
                {
                    console.printInfo("Клиент завершил работу приложения.");
                    console.printInfo("Сохранение коллекции..");
                    proccessor.saveCollection();
                    console.printInfo("Колекция успешно сохранена!");
                    ObjectOutputStream output = new ObjectOutputStream(os);
                    // отправляем обратно ответ
                    ResponseSender.sendResponse(output, response);
                    console.printInfo("Завершение данной сессии...");
                    break;
                }
                console.printInfo("Запрос обработан!");
                console.techPrint("------------------------------------------");
                console.techPrint("Success: " + response.isSuccess() + ";");
                console.techPrint("Message: " + response.getMessage() + ";");
                console.techPrint("------------------------------------------");

                ObjectOutputStream output = new ObjectOutputStream(os);
                // отправляем обратно ответ
                ResponseSender.sendResponse(output, response);
                console.printInfo("Ответ отправлен!\n");
            }
            catch( ClassNotFoundException e )
            {
                console.printError("Некорректные полученные данные");
            }
            catch( EOFException e )
            {
                console.printInfo("Клиент завершил сессию.");
                break;
            }
            catch( SocketException e )
            {
                console.printError("Соединение с клиентом потеряно.");
                break;
            }
            catch( IOException e )
            {
                console.printError("Ошибка потоков ввода-вывода: " + e.getMessage());
                break;
            }
            catch ( Exception e )
            {
                console.printError("Неизвестная ошибка при обработке запроса: " + e.getMessage());
            }
        }
        try
        {
            if (!clientSocket.isClosed())
            {
                clientSocket.close();
            }
        }
        catch (IOException e)
        {
            console.printError("Ошибка при закрытии сокета.");
        }
        console.printInfo("Сессия завершена!\n");
    }

}
