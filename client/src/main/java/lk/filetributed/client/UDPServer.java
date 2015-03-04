package lk.filetributed.client;

import lk.filetributed.cache.MessageCache;
import lk.filetributed.dispatcher.MessageBuffer;
import lk.filetributed.model.protocols.FileTableProtocol;
import lk.filetributed.model.protocols.IPTableProtocol;
import lk.filetributed.model.protocols.JoinProtocol;
import lk.filetributed.model.protocols.MessageProtocolType;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer extends Thread {
    private static Logger logger = Logger.getLogger(UDPServer.class);
    private final int server_port;
    private MessageBuffer messageBuffer;
    private MessageCache messageCache;

    public UDPServer(int server_port) {
        this.messageBuffer = MessageBuffer.getInstance();
        this.server_port = server_port;
        this.messageCache = new MessageCache(1000,1000,1000);
    }

    @Override
    public void run() {
        try {
            runUDPServer();
        } catch (SocketException e) {
            logger.error("Cannot run UDP server ..."+e.getMessage());
        }
    }

    public void runUDPServer() throws SocketException {
        DatagramSocket serverSocket = new DatagramSocket(server_port);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true)
        {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
                String recv_message = new String( receivePacket.getData());
                recv_message=recv_message.trim();
                logger.info("RECEIVED: " + recv_message);

                messageResolver(recv_message);

                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

//                String capitalizedSentence = recv_message.toUpperCase();
//                sendData = capitalizedSentence.getBytes();
//                DatagramPacket sendPacket =
//                        new DatagramPacket(sendData, sendData.length, IPAddress, port);
//                serverSocket.send(sendPacket);
            } catch (IOException e) {
                logger.error("Error receiving the UDP packet ..."+e.getStackTrace());
            }

        }
    }
    private void messageResolver(String message) {
        message = message.trim();
        //expected message type <length MessageType MessageID Payload>
        String[] receivedMessage = message.split(" ");
        String messageID = receivedMessage[2];
        if(messageCache.get(messageID)==null)
            messageCache.put(messageID,messageID);
        else
            return;

        switch (MessageProtocolType.valueOf(receivedMessage[1])) {
            case JOIN:
                JoinProtocol joinMessage = new JoinProtocol();
                joinMessage.initialize(message);
                messageBuffer.add(joinMessage);
                break;
            case GROUP:
                break;
            case IPTABLE:
                logger.info(message);
                break;
            case FILETABLE:
                FileTableProtocol fileTableProtocol = new FileTableProtocol();
                fileTableProtocol.initialize(message);
                messageBuffer.add(fileTableProtocol);
                break;
            default:
                break;
        }

    }
}
