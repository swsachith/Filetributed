package lk.filetributed.client;

import lk.filetributed.model.FileTableEntry;
import lk.filetributed.model.IPTable;
import lk.filetributed.model.Node;
import lk.filetributed.model.TableEntry;
import lk.filetributed.model.protocols.JoinProtocol;
import lk.filetributed.util.Utils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;


public class Client extends Node{

    private static Logger logger = Logger.getLogger(Client.class);

    private static final String SERVER_NAME = "127.0.0.1";
    private static final int PORT = 9889;

    private static final String CLIENT_IP = "147.0.0.1";
    private static final int CLIENT_PORT = 9888;
    private static final String USERNAME = "sachith";
    private static final int NO_CLUSTERS = 3;

    private static final String[] FILE_NAMES = {"Adventures of Tintin","Jack and Jill"};

    public Client() {
        super(CLIENT_IP, PORT, NO_CLUSTERS);
        initialize();
        try {
            runUDPServer();
        } catch (SocketException e) {
            logger.error("Error creating the datagram socket ... "+e.getStackTrace());
        }
    }

    public void initialize() {
        String result = BootstrapConnector.connectToBootstrap(SERVER_NAME, PORT, CLIENT_IP, CLIENT_PORT, USERNAME);
        try {
            process(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<FileTableEntry> searchFile(String filename) {
        return this.fileTable.searchTable(filename);
    }

    public void runUDPServer() throws SocketException {
        DatagramSocket serverSocket = new DatagramSocket(CLIENT_PORT);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true)
        {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
                String sentence = new String( receivePacket.getData());
                logger.info("RECEIVED: " + sentence);
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                DatagramPacket sendPacket =
                        new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            } catch (IOException e) {
                logger.error("Error receiving the UDP packet ..."+e.getStackTrace());
            }

        }
    }
    public static void main(String[] args) {
        Client client = new Client();
    }

    public static void process(String server_response) throws IOException {
        String[] response_data = server_response.split(" ");
        if (!response_data[1].equals("REGOK")) {
            System.err.println("Registration Failed. Try Again!");
            System.exit(0);
        }

        int no_nodes = Integer.parseInt(response_data[2]);
        String RECIEVED_IP_01;
        String RECIEVED_IP_02;
        int RECIEVED_PORT_01;
        int RECIEVED_PORT_02;
        int clusterID01;

        switch (no_nodes) {
            case 0:
                break;
            case 1:
                RECIEVED_IP_01 = response_data[3];
                RECIEVED_PORT_01 = Integer.parseInt(response_data[4]);
                clusterID01 = Utils.getClusterID(RECIEVED_IP_01, RECIEVED_PORT_01, NO_CLUSTERS);
                Node node01 = new Node(RECIEVED_IP_01,RECIEVED_PORT_01,clusterID01);
                String JOIN_MSG = JoinProtocol.getJoinMessage(CLIENT_IP,CLIENT_PORT);

                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName(RECIEVED_IP_01);
                byte[] sendData = new byte[1024];
                byte[] receiveData = new byte[1024];
                sendData = JOIN_MSG.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, RECIEVED_PORT_01);
                clientSocket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);


                /*TableEntry tableEntry = new TableEntry(node01.getIpAddress(),node01.getPort()+"",node01.getClusterID());

                IPTable ipTable = new IPTable();
                ipTable.addTableEntry(tableEntry);*/

                break;
            case 2:
                RECIEVED_IP_01 = response_data[3];
                RECIEVED_PORT_01 = Integer.parseInt(response_data[4]);
                RECIEVED_IP_02 = response_data[5];
                RECIEVED_PORT_02 = Integer.parseInt(response_data[6]);
                break;
            case 9999:
                System.out.println("failed, there is some error in the command");
                break;
            case 9998:
                System.out.println("failed, already registered to you, unregister first");
                break;
            case 9997:
                System.out.println("failed, registered to another user, try a different IP and port");
                break;
            case 9996:
                System.out.println("failed, can’t register. BS full.");
                break;
        }
    }


}