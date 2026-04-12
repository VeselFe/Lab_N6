package ru.itmo.lab.network;

import ru.itmo.lab.commonNet.Response;
import ru.itmo.lab.model.StudyGroup;
import ru.itmo.lab.myExceptions.ConnectionException;
import ru.itmo.lab.myExceptions.ResponseException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collection;

public class NetworkManager
{
    private final SocketChannel channel;
    private final ByteBuffer buffer = ByteBuffer.allocate(16384);

    public NetworkManager( SocketChannel channel )
    {
        this.channel = channel;
    }

    public void network( ru.itmo.lab.commonNet.Request request ) throws IOException
    {
        try
        {
            sendRequest( request );
        }
        catch( IOException e )
        {
            throw new ConnectionException("Сервер разрвал соединение.");
        }
    }

    private void sendRequest( ru.itmo.lab.commonNet.Request request ) throws IOException
    {
        ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
        ObjectOutputStream objectOS = new ObjectOutputStream( byteOS );
        objectOS.writeObject( request );
        objectOS.flush();

        byte[] data = byteOS.toByteArray();
        ByteBuffer outBuffer = ByteBuffer.wrap(data);

        while( outBuffer.hasRemaining() )
        {
            channel.write( outBuffer );
        }
    }

    public String getServerResponse()
    {
        try
        {
            Response serverResponse = recieveResponse();
            boolean success = serverResponse.isSuccess();
            String responseMessage = serverResponse.getMessage();
            Collection<StudyGroup> responeCollection = serverResponse.getCollection();

            if( success )
            {
                return "Команда выполнена успешно:\n___________________\n" + responseMessage + "\n___________________\n";
            }
            else
            {
                return "Возникла ошибка при выполнении команды:\n___________________\n" + responseMessage + "\n___________________\n";
            }
        }
        catch (ResponseException responseException)
        {
            throw responseException;
        }
        catch( Exception e )
        {
            throw new ResponseException("Не обработался ответ: " + e.getMessage());
        }
    }

    private Response recieveResponse() throws IOException, ClassNotFoundException
    {
        buffer.clear();
        int bytes;

        try
        {
            while( (bytes = channel.read(buffer)) == 0 )
            {
                System.out.println("Ожидание ответа сервера..");
                Thread.sleep(500);
            }
        }
        catch( InterruptedException e )
        {
            // Восстанавливаем статус прерывания для текущего потока
            Thread.currentThread().interrupt();
            throw new ConnectionException("Ожидание ответа прерывано.");
        }

        if( bytes == -1 )
            throw new ConnectionException("Сервер разорвал соединение");

        try
        {
            Thread.sleep(50);
            int extraBytes;
            while ((extraBytes = channel.read(buffer)) > 0) {
                Thread.sleep(1);
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        buffer.flip();

        if (buffer.remaining() < 4)
        {
            throw new IOException("Пришло слишком мало данных (меньше заголовка)");
        }

        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        try( ByteArrayInputStream byteIStream = new ByteArrayInputStream( data );
             ObjectInputStream objectIStream = new ObjectInputStream( byteIStream ) )
        {
            return ResponseReader.read( objectIStream );
        }
    }
}
