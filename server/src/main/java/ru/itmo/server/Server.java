package ru.itmo.server;

import ru.itmo.lab.common.commonNet.Response;
import ru.itmo.server.manager.collection.generators.BasicGenerator;
import ru.itmo.server.ioHandlers.ServerConsoleHandler;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.server.manager.collection.fileManagement.GroupsFileManager;
import ru.itmo.server.manager.collection.fileManagement.Launcher;
import ru.itmo.server.manager.serverLogic.CommandProccessor;
import ru.itmo.server.manager.serverLogic.Invoker;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.lab.common.commonNet.Request;
import ru.itmo.server.serverNetManager.RequestReader;
import ru.itmo.server.serverNetManager.ResponseSender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class Server
{
    public static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final int port = 8080;

    public static void main(String[] args)
    {
        /// Серверный менеджер коллекцией
        CollectionManager mainCollection = CollectionManager.createCollection();
        StudyGroup.setIdGenerator( new BasicGenerator(mainCollection) );
        Invoker invoker = new Invoker( mainCollection );
        CommandProccessor mainProccessor = new CommandProccessor( invoker, mainCollection );

        ServerConsoleHandler console = new ServerConsoleHandler();
        GroupsFileManager.setErrorPrinter(console);
        new Launcher(mainCollection, console).launchCollection();
        console.setProvider(invoker);

        Thread handleServerTerminal = new Thread(() -> {
            ServerConsoleHandler terminal = new ServerConsoleHandler();
            invoker.initIOput(terminal);
            logger.info("Серверный терминал запущен.");
            while( true )
            {
                String adminCommand = terminal.readline();
                logger.info("Получена команда от Администратора сервера: " + adminCommand);
                switch(adminCommand.toLowerCase().trim())
                {
                    case "exit" -> invoker.executeServerCommand("exit");
                    case "save" -> invoker.executeServerCommand("save");
                    default -> logger.error("Неизвестная серверная команда");
                }
            }
        });
        handleServerTerminal.setDaemon(true);
        handleServerTerminal.start();

        /// Сеть
        logger.info("Сервер запустился. Порт: " + port);
        try( ServerSocket serverSocket = new ServerSocket(port) )
        {
            while( !mainProccessor.isProgrammFinished() )
            {
                logger.info("Ожидание подключения клиента...");
                Socket clientSocket = serverSocket.accept();
                logger.info("Клиент подключен: " + clientSocket.getInetAddress());
                handleClient(clientSocket, mainProccessor);
                if( mainProccessor.isProgrammFinished() )
                {
                    logger.info("Администратор завершил работу сервера.");
                    logger.info("Сохранение коллекции..");
                    mainProccessor.saveCollection();
                    logger.info("Колекция успешно сохранена!");
                    logger.info("Завершение работы сервера");
                    break;
                }
            }
        }
        catch( IOException e )
        {
            logger.error("Ошибка сервера: " + e.getMessage());
        }
    }

    public static void handleClient( Socket clientSocket, CommandProccessor proccessor )
    {
        logger.info("Потоки ввода-вывода инициализированы.");
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
                logger.info("Получен запрос: " + request.getCommandType() + "\n");

                // обрабатываем
                Response response = proccessor.ProcessRequest( request );

                logger.info("Запрос обработан!");
                logger.info("<Началло запроса>");
                logger.info("Success: " + response.isSuccess() + ";");
                logger.info("Message: " + response.getMessage() + ";");
                logger.info("<Конец запроса>");

                ObjectOutputStream output = new ObjectOutputStream(os);
                // отправляем обратно ответ
                ResponseSender.sendResponse(output, response);
                logger.info("Ответ отправлен!\n");
                break;
            }
            catch( ClassNotFoundException e )
            {
                logger.error("Некорректные полученные данные");
            }
            catch( EOFException e )
            {
                logger.info("Клиент завершил сессию.");
                break;
            }
            catch( SocketException e )
            {
                logger.error("Соединение с клиентом потеряно.");
                break;
            }
            catch( IOException e )
            {
                logger.error("Ошибка потоков ввода-вывода: " + e.getMessage());
                break;
            }
            catch ( Exception e )
            {
                logger.error("Неизвестная ошибка при обработке запроса: " + e.getMessage());
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
            logger.error("Ошибка при закрытии сокета.");
        }
        logger.info("Сессия завершена!\n");
    }
}
