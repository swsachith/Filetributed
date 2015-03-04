package lk.filetributed.dispatcher;

import lk.filetributed.model.DispatchMessage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;

public class MessageDispatcher implements Runnable{
    private static Logger logger = Logger.getLogger(MessageDispatcher.class);

    private MessageOutBuffer outBuffer;
    private static final int SEND_PORT = 9111;
    private DatagramSocket clientSocket;
    private boolean runServer = false;

    public MessageDispatcher() {
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            logger.error("Error creating the client socket for message dispatcher ..."+e.getMessage());
        }
        outBuffer = MessageOutBuffer.getInstance();
        runServer = true;
    }

    @Override
    public void run() {
        while(runServer) {
            DispatchMessage message = outBuffer.getMessage();
            InetAddress IPAddress = null;
            try {
                IPAddress = InetAddress.getByName(message.getIpAddress());
                byte[] sendData = new byte[1024];
                byte[] receiveData = new byte[1024];
                sendData = message.getMessage().getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, message.getPort());
                clientSocket.send(sendPacket);

            } catch (UnknownHostException e) {
                logger.error("Error resolving IPAddress "+e.getMessage());
            } catch (IOException e) {
                logger.error("Error occurred sending packet "+e.getMessage());
            }

        }
    }
}
