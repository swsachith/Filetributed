package lk.filetributed.client;

import lk.filetributed.dispatcher.MessageBuffer;
import lk.filetributed.dispatcher.MessageDispatcher;
import lk.filetributed.dispatcher.MessageOutBuffer;
import lk.filetributed.model.DispatchMessage;
import lk.filetributed.model.FileTableEntry;
import lk.filetributed.model.Node;
import lk.filetributed.model.TableEntry;
import lk.filetributed.model.protocols.*;
import lk.filetributed.model.protocols.FileTableProtocol;
import lk.filetributed.model.protocols.JoinProtocol;
import lk.filetributed.model.protocols.JoinStatus;
import lk.filetributed.model.protocols.MessageProtocolType;
import lk.filetributed.util.Utils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;


public class Client extends Node{

    private static Logger logger = Logger.getLogger(Client.class);

    private static final String SERVER_NAME = "127.0.0.1";
    private static final int PORT = 9889;

    private static final String CLIENT_IP = "127.0.0.1";
    private static final int CLIENT_PORT = 9886;
    private static final String USERNAME = "666ith";
    private static final int NO_CLUSTERS = 3;

    private static final String[] FILE_NAMES = {"Adventures of Tintin","Jack and Jill"};

    private MessageBuffer messageBuffer;
    private MessageOutBuffer outBuffer;

    public Client() {
        super(CLIENT_IP, PORT, NO_CLUSTERS);
        messageBuffer = MessageBuffer.getInstance();
        outBuffer = MessageOutBuffer.getInstance();

        Thread t_udpServer = new Thread(new UDPServer(CLIENT_PORT));
        Thread t_messageDispatcher = new Thread(new MessageDispatcher());
        t_udpServer.start();
        t_messageDispatcher.start();
        initFileTable(FILE_NAMES);
        initialize();
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
    private void initFileTable(String[] fileNames){
        for (String fileName:fileNames){
            fileTable.addTableEntry(new FileTableEntry(fileName,CLIENT_IP,CLIENT_PORT));
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
        while(true){
            processBuffer();
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
        sendJoinMessage(RECIEVED_IP, RECIEVED_PORT);


        FileTableProtocol fileTableProtocol = new FileTableProtocol(CLIENT_IP,CLIENT_PORT,fileTable);
        String fileTableMSG = fileTableProtocol.toString();

        outBuffer.add(new DispatchMessage(fileTableMSG,RECIEVED_IP,RECIEVED_PORT));



    }
    public void process(String RECIEVED_IP_01, int RECIEVED_PORT_01,String RECIEVED_IP_02, int RECIEVED_PORT_02){
        int clusterID01;
        int clusterID02;

        clusterID01 = Utils.getClusterID(RECIEVED_IP_01, RECIEVED_PORT_01, NO_CLUSTERS);
        Node node01 = new Node(RECIEVED_IP_01,RECIEVED_PORT_01,NO_CLUSTERS);

        //generating the join message
        sendJoinMessage(RECIEVED_IP_01, RECIEVED_PORT_01);

        clusterID02 = Utils.getClusterID(RECIEVED_IP_02, RECIEVED_PORT_02, NO_CLUSTERS);
        Node node02 = new Node(RECIEVED_IP_02,RECIEVED_PORT_02,NO_CLUSTERS);

        //generating the join message
        sendJoinMessage(RECIEVED_IP_02, RECIEVED_PORT_02);

    }

    public void processBuffer() {
        MessageProtocol message = messageBuffer.getMessage();

        switch (MessageProtocolType.valueOf(message.getMessageType())) {
            case JOIN:
                String msg;

                if(message instanceof JoinProtocol){
                    msg = message.toString();
                    String[] tokenz = msg.split(" ");
                    String ipAddress = tokenz[3];
                    String port = tokenz[4];

                    IPTableProtocol ipMessage = new IPTableProtocol(ipAddress,Integer.parseInt(port),
                            Integer.parseInt(this.getClusterID()), this.getIpTable());

                    outBuffer.add(new DispatchMessage(ipMessage.toString(),ipMessage.getIpAddress(),ipMessage.getPort()));
                    logger.info("MessageID : "+ipMessage.getMessageID()+" IPTable SENT from: "+getIpAddress()+":"+getPort()+" TO "+
                            ipMessage.getIpAddress()+":"+ipMessage.getPort()+" ----- "+ipMessage.toString());
                }
                else{
                    msg=null;
                }

                break;
            case IPTABLE:
                break;
            case FILETABLE:
                System.out.println("#######"+message.toString());
                break;
            default:
                break;
        }
    }

    public void sendJoinMessage(String RECIEVED_IP, int RECIEVED_PORT){
        //generating the join message
        JoinProtocol joinProtocol = new JoinProtocol(CLIENT_IP, CLIENT_PORT);
        String JOIN_MSG = joinProtocol.toString();

        outBuffer.add(new DispatchMessage(JOIN_MSG,RECIEVED_IP,RECIEVED_PORT));

    }



}