package lk.filetributed.client;

import lk.filetributed.dispatcher.MessageBuffer;
import lk.filetributed.dispatcher.MessageDispatcher;
import lk.filetributed.dispatcher.MessageOutBuffer;
import lk.filetributed.model.DispatchMessage;
import lk.filetributed.model.FileTableEntry;
import lk.filetributed.model.Node;
import lk.filetributed.model.TableEntry;
import lk.filetributed.model.protocols.*;
import lk.filetributed.util.Utils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.StringTokenizer;


public class Client extends Node{

    private static Logger logger = Logger.getLogger(Client.class);

    private static final String SERVER_NAME = "127.0.0.1";
    private static final int PORT = 9889;

    private static final String CLIENT_IP = "127.0.0.1";
    private static final int CLIENT_PORT = 9887;
    private static final String USERNAME = "sachith";
    private static final int NO_CLUSTERS = 3;

    private static final String[] FILE_NAMES = {"Adventures of Tintin","Jack and Jill"};

    private MessageBuffer messageBuffer;
    private MessageOutBuffer outBuffer;

    public Client() {
        super(CLIENT_IP, PORT, NO_CLUSTERS);
        messageBuffer = MessageBuffer.getInstance();
        outBuffer = MessageOutBuffer.getInstance();

        initialize();
        Thread t_udpServer = new Thread(new UDPServer(CLIENT_PORT));
        Thread t_messageDispatcher = new Thread(new MessageDispatcher());
        t_udpServer.start();
        t_messageDispatcher.start();
    }

    //connect with the system
    public void initialize() {
        String result = BootstrapConnector.connectToBootstrap(SERVER_NAME, PORT, CLIENT_IP, CLIENT_PORT, USERNAME);
        try {
            response_tokenizer(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<FileTableEntry> searchFile(String filename) {
        return this.fileTable.searchTable(filename);
    }

     public static void main(String[] args) {
        Client client = new Client();
    }

    public void response_tokenizer(String server_response) throws IOException {
        String[] response_data = server_response.split(" ");
        if (!response_data[1].equals("REGOK")) {
            System.err.println("Registration Failed. Try Again!");
            //FIXME : Come up with a better way to handle this?
            System.exit(0);
        }

        int no_nodes = Integer.parseInt(response_data[2]);


        switch (no_nodes) {
            case 0:
                process();
                break;
            case 1:
                process(response_data[3], Integer.parseInt(response_data[4]));
                break;
            case 2:
                process(response_data[3], Integer.parseInt(response_data[4]), response_data[5], Integer.parseInt(response_data[6]));
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

    public String process_JoinMessage(JoinProtocol joinProtocol){
        String response_MSG="";

        try {
            this.getIpTable().addTableEntry(new TableEntry(joinProtocol.getIpAddress(),
                    joinProtocol.getPort() + "", Utils.getClusterID(joinProtocol.getIpAddress(), joinProtocol.getPort(),
                    NO_CLUSTERS) + ""));
            response_MSG = JoinProtocol.getJoinResponse(JoinStatus.SUCCESS);
        }
        catch(Exception ex){
            logger.error("Error in adding entry to the ip table");
            response_MSG = JoinProtocol.getJoinResponse(JoinStatus.FAILED);
        }
        finally {
            return response_MSG;
        }

    }


    public void process(){

    }
    public void process(String RECIEVED_IP, int RECIEVED_PORT) throws IOException {
        int clusterID;

        clusterID = Utils.getClusterID(RECIEVED_IP, RECIEVED_PORT, NO_CLUSTERS);
        Node node = new Node(RECIEVED_IP,RECIEVED_PORT,NO_CLUSTERS);

        //generating the join message
        JoinProtocol joinProtocol = new JoinProtocol(CLIENT_IP, CLIENT_PORT);
        String JOIN_MSG = joinProtocol.toString();

        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(RECIEVED_IP);
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        sendData = JOIN_MSG.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, RECIEVED_PORT);
        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String sentence = new String( receivePacket.getData());
        logger.info("RECEIVED: " + sentence);
        if (sentence.split(" ")[2].equals("0")){

        }

        /*TableEntry tableEntry = new TableEntry(node01.getIpAddress(),node01.getPort()+"",node01.getClusterID());

                IPTable ipTable = new IPTable();
                ipTable.addTableEntry(tableEntry);*/

    }
    public void process(String RECIEVED_IP_01, int RECIEVED_PORT_01,String RECIEVED_IP_02, int RECIEVED_PORT_02){
        int clusterID01;
        int clusterID02;
    }

    public void processBuffer() {
        MessageProtocol message = messageBuffer.getMessage();

        switch (MessageProtocolType.valueOf(message.getMessageType())) {
            case JOIN:
                String msg;
                IPTableProtocol ipMessage = new IPTableProtocol();
                if(message instanceof JoinProtocol){
                    msg = message.toString();
                }
                else{
                    msg=null;
                }
                ipMessage.initialize(msg);
                outBuffer.add(new DispatchMessage(ipMessage.toString(),ipMessage.getIpAddress(),ipMessage.getPort()));
                break;
            case IPTABLE:
                break;
            default:
                break;
        }
    }



}