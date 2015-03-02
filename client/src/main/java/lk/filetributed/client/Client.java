package lk.filetributed.client;

import java.net.*;
import java.io.*;
import org.apache.log4j.Logger;


public class Client {

    private static Logger logger = Logger.getLogger(Client.class);

    private static final String SERVER_NAME = "127.0.0.1";
    private static final int PORT = 9889;

    private static final String CLIENT_IP = "147.0.0.1";
    private static final int CLIENT_PORT = 9888;
    private static final String USERNAME = "sachith";

    private static String message = "REG " + CLIENT_IP + " " + CLIENT_PORT + " " + USERNAME;
    //private static String message = "REG 129.82.123.45 5001 1234abcd";

    public static void main(String[] args) {

        connectToBootstrap();
    }

    private static void connectToBootstrap() {
        try {
            int size = message.length() + 5;
            message = "00" + size + " " + message;
            System.out.println("MESSAGE: " +message);

            logger.info("Connecting to " + SERVER_NAME
                    + " on port " + PORT);
            Socket client = new Socket(SERVER_NAME, PORT);
            logger.info("Just connected to "
                    + client.getRemoteSocketAddress());

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println(message);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            logger.info(in.readLine());

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}