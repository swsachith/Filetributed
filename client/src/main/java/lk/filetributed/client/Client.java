package lk.filetributed.client;

import java.net.*;
import java.io.*;

import lk.filetributed.model.Node;
import org.apache.log4j.Logger;


public class Client {

    private static Logger logger = Logger.getLogger(Client.class);

    private static final String SERVER_NAME = "127.0.0.1";
    private static final int PORT = 9889;

    private static final String CLIENT_IP = "147.0.0.1";
    private static final int CLIENT_PORT = 9888;
    private static final String USERNAME = "sachith";
    private static final int NO_CLUSTERS = 3;

    private Node currentNode;

    private static String REGISTER_MSG = "REG " + CLIENT_IP + " " + CLIENT_PORT + " " + USERNAME;

    public Client() {
        this.currentNode = new Node(CLIENT_IP, PORT, NO_CLUSTERS);
    }
    public static void main(String[] args) {

        connectToBootstrap();
    }

    private static void connectToBootstrap() {
        try {
            int size = REGISTER_MSG.length() + 5;
            REGISTER_MSG = "00" + size + " " + REGISTER_MSG;
            System.out.println("MESSAGE: " + REGISTER_MSG);

            logger.info("Connecting to " + SERVER_NAME
                    + " on port " + PORT);
            Socket client = new Socket(SERVER_NAME, PORT);
            logger.info("Just connected to "
                    + client.getRemoteSocketAddress());

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println(REGISTER_MSG);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            logger.info(in.readLine());

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}