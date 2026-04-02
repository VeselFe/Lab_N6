package ru.itmo.lab;

import ru.itmo.lab.clientTerminal.ClientConsoleHandler;
import ru.itmo.lab.network.NetworkManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client
{
    private static final String host = "localhost";
    private static final int port = 8080;
    private static final int connectionDelay = 5000;

    public static void main(String[] args)
    {
        SocketChannel channel = connectToServer();
        if( channel != null )
        {
            try
            {
                ClientConsoleHandler console = new ClientConsoleHandler();
                NetworkManager networkManager = new NetworkManager(channel);
                console.setProvider(networkManager);
                console.start();
            }
            catch( Exception e )
            {
                System.out.println("Ошибка при работе приложения: " + e.getMessage());
            }
            finally
            {
                try
                {
                    channel.close();
                }
                catch( IOException e )
                {
                    System.err.println("Ошибка при закрытии канала.");
                }
            }
        }
    }

    private static SocketChannel connectToServer()
    {
        SocketChannel socketChannel = null;
        boolean conection = false;
        ///  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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