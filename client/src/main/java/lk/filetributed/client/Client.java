package lk.filetributed.client;

import java.net.*;
import java.io.*;

public class Client {

    private static final String SERVER_NAME = "127.0.0.1";
    private static final int PORT = 9889;

    private static final String CLIENT_IP = "127.0.0.1";
    private static final int CLIENT_PORT = 9888;
    private static final String USERNAME = "sachith";

    private static String message = "REG " + CLIENT_IP + " " + CLIENT_PORT + " " + USERNAME;

    public static void main(String[] args) {

        try {
            int size = message.length() + 4;
            message = "00" + size + " " + message;
            System.out.println("MESSAGE: " +message);

            System.out.println("Connecting to " + SERVER_NAME
                    + " on port " + PORT);
            Socket client = new Socket(SERVER_NAME, PORT);
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out =
                    new DataOutputStream(outToServer);

            out.writeUTF(message);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in =
                    new DataInputStream(inFromServer);
            System.out.println("Server says " + in.readUTF());
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}