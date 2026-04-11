package ru.itmo.lab.network;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
        sendRequest( request );
    }

    private void  sendRequest( ru.itmo.lab.commonNet.Request request ) throws IOException
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

//    private Response recieveResponse()
}
