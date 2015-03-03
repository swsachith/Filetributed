package lk.filetributed.client;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.*;

public class ClientTester {
    private static Logger logger = Logger.getLogger(ClientTester.class);

    private Client client;

    @Before
    public void setup() {
        client = new Client();
    }

    @Test
    public void testUDPServer() throws IOException {

        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        String sentence = "Hello World";
        sendData = sentence.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9888);
        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String modifiedSentence = new String(receivePacket.getData());
        logger.info("FROM SERVER:" + modifiedSentence);
        clientSocket.close();
    }

    @Test
    public void testProcess() throws IOException {
        String server_response = "0051 REGOK 1 129.82.123.45 5001";
    }
}
