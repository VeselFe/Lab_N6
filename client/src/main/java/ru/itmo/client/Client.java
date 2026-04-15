package ru.itmo.client;

import ru.itmo.client.clientTerminal.ClientConsoleHandler;
import ru.itmo.client.network.NetworkManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client
{
    private static final String host = "localhost";
    private static final int port = 3030;
    private static final int connectionDelay = 5000;

    public static void main(String[] args)
    {
        ClientConsoleHandler console = new ClientConsoleHandler();
        console.initRequestCreator( new ClientConsoleHandler() );
        while( true )
        {
            console.printRequest("Выводить коллекцию после получения результатов? (y/n): ");
            String ans = console.readline();
            ans = ans.toLowerCase().trim();
            if(ans.equals("y") || ans.equals("yes") || ans.equals("да"))
            {
                console.turnPrinterCollection(true);
                break;
            }
            else if(ans.equals("n") || ans.equals("no") || ans.equals("нет"))
            {
                console.turnPrinterCollection(false);
                break;
            }
            else
            {
                console.printError("Некорректный ввод. Попробуйте снова.\n");
            }
        }

        while( true )
        {
            try( SocketChannel channel = connectToServer() )
            {
                if (channel != null)
                {
                    NetworkManager networkManager = new NetworkManager(channel);
                    console.setProvider(networkManager);
                    console.welcomMessage();

                    boolean exit = false;
                    while (!exit)
                    {
                        exit = console.executing();
                    }
                    if (exit) break;
                }
            }
            catch( Exception e )
            {
                System.out.println("Ошибка при работе приложения: " + e.getMessage());
            }
        }
    }

    private static SocketChannel connectToServer()
    {
        SocketChannel socketChannel = null;
        boolean conection = false;
        ClientConsoleHandler console = new ClientConsoleHandler();
        console.printInfo("Подключение к серверу " + host + ":" + port);

        while( !conection )
        {
            try
            {
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                socketChannel.connect( new InetSocketAddress(host, port) );

                while( !socketChannel.finishConnect() )
                {
                    console.printRequest(".");
                    Thread.sleep(500);
                }
                conection = true;
                console.printInfo("Успешно подключено к серверу!");
            }
            catch( IOException e )
            {
                console.printError("Сервер недоступен. Повторная попытка через " + (connectionDelay / 1000) + " секунд...");
                try
                {
                    if( socketChannel != null ) { socketChannel.close(); }
                    Thread.sleep(connectionDelay);
                }
                catch(InterruptedException | IOException exception )
                {
                    console.printError("Ошибка при переподключении");
                    break;
                }
            }
            catch( InterruptedException e )
            {
                console.printError("Подключение прервано");
                break;
            }
        }
        return socketChannel;
    }
}