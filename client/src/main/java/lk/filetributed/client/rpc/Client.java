package lk.filetributed.client.rpc;

import lk.filetributed.client.BootstrapConnector;
import lk.filetributed.client.rpc.services.JoinNode;
import lk.filetributed.model.*;
import lk.filetributed.util.Utils;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.util.List;
import java.util.Properties;


public class Client extends Node implements JoinNode.Iface {

    private static Logger logger = Logger.getLogger(Client.class);

    private static final String SERVER_NAME = "127.0.0.1";
    private static final int PORT = 9889;

    private static String CLIENT_IP;
    private static int CLIENT_PORT;
    private static String USERNAME;
    private static int NO_CLUSTERS;

    private static String[] FILE_NAMES;


    public Client() {
        configClient("client/config/client2.xml");

        super.ipAddress = CLIENT_IP;
        super.port=CLIENT_PORT;
        super.NO_CLUSTERS=NO_CLUSTERS;
        super.setCluster();
        super.ipTable.setSelf(new TableEntry(CLIENT_IP, String.valueOf(CLIENT_PORT),
                String.valueOf(getClusterID())));


        Thread t_rpcServer = new Thread(new RPCServer(CLIENT_PORT,this));
        t_rpcServer.start();
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
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

    private void initFileTable(String[] fileNames) {
        for (String fileName : fileNames) {
            fileTable.addTableEntry(new FileTableEntry(fileName, CLIENT_IP, CLIENT_PORT));
        }
    }

    private void configClient(String configFileLocation){

        Properties properties = Utils.loadPropertyFile(configFileLocation);

        CLIENT_IP=properties.getProperty("CLIENT_IP");
        CLIENT_PORT=Integer.parseInt(properties.getProperty("CLIENT_PORT"));
        USERNAME=properties.getProperty("USERNAME");
        NO_CLUSTERS=Integer.parseInt(properties.getProperty("NO_CLUSTERS"));

        String fileListPath = properties.getProperty("FILE_LIST_PATH");

        FILE_NAMES= Utils.getFileList(fileListPath);
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
            System.exit(0);
        }

        int no_nodes = Integer.parseInt(response_data[2]);
        selectProcessType(no_nodes, response_data);

    }

    public void process() {

    }

    public void process(String RECIEVED_IP, int RECIEVED_PORT) throws IOException {
        int clusterID;

        clusterID = Utils.getClusterID(RECIEVED_IP, RECIEVED_PORT, NO_CLUSTERS);
        Node receivedNode = new Node(RECIEVED_IP, RECIEVED_PORT, NO_CLUSTERS);

        this.getIpTable().addTableEntry(new TableEntry(RECIEVED_IP, RECIEVED_PORT + "", clusterID + ""));
        sendJoin(receivedNode);

    }

    public void process(String RECIEVED_IP_01, int RECIEVED_PORT_01, String RECIEVED_IP_02, int RECIEVED_PORT_02) {
        int clusterID01;
        int clusterID02;

        clusterID01 = Utils.getClusterID(RECIEVED_IP_01, RECIEVED_PORT_01, NO_CLUSTERS);
        Node receivedNode1 = new Node(RECIEVED_IP_01, RECIEVED_PORT_01, NO_CLUSTERS);

        this.getIpTable().addTableEntry(new TableEntry(RECIEVED_IP_01, RECIEVED_PORT_01 + "", clusterID01 + ""));

        //generating the join message
        sendJoin(receivedNode1);


        clusterID02 = Utils.getClusterID(RECIEVED_IP_02, RECIEVED_PORT_02, NO_CLUSTERS);
        Node receivedNode2 = new Node(RECIEVED_IP_02, RECIEVED_PORT_02, NO_CLUSTERS);

        this.getIpTable().addTableEntry(new TableEntry(RECIEVED_IP_02, RECIEVED_PORT_02 + "", clusterID02 + ""));

        //generating the join message
        sendJoin(receivedNode2);

    }

    private void sendJoin(Node receivedNode ){
        TTransport transport;
        try {
            transport = new TSocket(receivedNode.getIpAddress(), receivedNode.getPort());
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            JoinNode.Client client = new JoinNode.Client(protocol);

            System.out.println("sending join request...");
            String s = client.joinRequest(CLIENT_IP, CLIENT_PORT, Utils.getClusterID(CLIENT_IP,CLIENT_PORT,NO_CLUSTERS));
            System.out.println(s);
            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String joinRequest(String ipAddress, int port, int clusterID) throws TException {
        System.out.println("join request came from "+ipAddress+" : "+port);
        System.out.println("setting iptable...");

        return "success yakoo";
    }

//    public void processBuffer() {
//        MessageProtocol message = messageBuffer.getMessage();
//        switch (MessageProtocolType.valueOf(message.getMessageType())) {
//
//            case JOIN:
//                if (message instanceof JoinProtocol)
//                    process_JoinMessage((JoinProtocol) message);
//                break;
//
//            case IPTABLE:
//                if(message instanceof IPTableProtocol){
//                    process_IPTableMessage((IPTableProtocol)message);
//                }
//                //logger.info("IPTable Merging should happen here!");
//                break;
//            case FILETABLE:
//                if(message instanceof FileTableProtocol){
//                    processFileTableMessage((FileTableProtocol) message);
//                }
//                break;
//            case GROUP:
//                if (message instanceof GroupProtocol)
//                    process_groupMessage((GroupProtocol) message);
//                break;
//            case QUERY:
//                if (message instanceof QueryProtocol)
//                    process_queryMessage((QueryProtocol) message);
//                break;
//            default:
//                break;
//        }
//        //logger.info("IP TABLE LOG @ PORT" + CLIENT_PORT + " : " + getIpTable().toString());
//    }

//    private void processFileTableMessage(FileTableProtocol message) {
//        FileTable receivedFileTable= message.getFileTable();
//
//        //check if this is the reply to JOIN
//        TableEntry entry = new TableEntry(message.getIpAddress(),message.getPort()+"",Utils.getClusterID(message.getIpAddress(),message.getPort(),NO_CLUSTERS)+"");
//        if(sentJoins.contains(entry) && this.getClusterID()==message.getClusterID()){
//            sentJoins.remove(entry);
//
//            // if fileTable has already merged with one node in the same cluster, no need to distribute fileTable again
//            if (sentJoins.isEmpty()){
//                return;
//            }
//
//            //send my filetable to all nodes in the same cluster
//            if (!this.getIpTable().isEmpty()) {
//
//                logger.info("Distributing FileTable : " + fileTable.toString());
//                for (Iterator<TableEntry> iterator = getIpTable().getEntries().iterator(); iterator.hasNext();) {
//                    TableEntry tableEntry=iterator.next();
//                    if(Integer.parseInt(tableEntry.getClusterID())==getClusterID()){
//                        FileTableProtocol newMessage = new FileTableProtocol(getIpAddress(),getPort(),fileTable);
//                        outBuffer.add(new DispatchMessage(newMessage.toString(),
//                                tableEntry.getIpAddress(), Integer.parseInt(tableEntry.getPort())));
//                        }
//                }
//
//            }
//
//            fileTable.mergeEntriesFromTable(receivedFileTable);
//            logger.info("File Table merged using the table sent by "+message.getIpAddress()+":"+message.getPort());
//        }
//
//        else {
//
//            fileTable.mergeEntriesFromTable(receivedFileTable);
//            logger.info("File Table merged using distributed table by "+message.getIpAddress()+":"+message.getPort());
//        }
//
//    }


//    private void process_IPTableMessage(IPTableProtocol message) {
//        ((IPTableProtocol) message).mergeIPTables(getIpTable(), this.clusterID);
//        logger.info("IP : "+getIpAddress()+" PORT : "+getPort()+
//                " IPTABLE : "+getIpTable().toString());
//    }


//    private void process_JoinMessage(JoinProtocol message) {
//        int clusterID = message.getClusterID();
//
//        if (clusterID == this.getClusterID()) { //check if it's in the same cluster
//            String ipAddress = message.getIpAddress();
//            int port = message.getPort();
//
//            //adds the new node to the IPTable
//            TableEntry entry = new TableEntry(message.getIpAddress(),String.valueOf(message.getPort()),
//                    String.valueOf(message.getClusterID()));
//            this.getIpTable().addTableEntry(entry);
//
//            if (!this.getIpTable().isEmpty()) {
//                IPTableProtocol ipMessage = new IPTableProtocol(ipAddress, port, clusterID,
//                        getIpAddress(), getPort(), getClusterID(), getIpTable());
//                for (Iterator<TableEntry> iterator = getIpTable().getEntries().iterator(); iterator.hasNext();) {
//                    TableEntry tableEntry=iterator.next();
//                    if(Integer.parseInt(tableEntry.getClusterID())==getClusterID()){
//                        outBuffer.add(new DispatchMessage(ipMessage.toString(),
//                                tableEntry.getIpAddress(), Integer.parseInt(tableEntry.getPort())));
//                        logger.info("Sending IPTables ...: " + ipMessage.toString());
//                    }
//                }
//
//            }
//
//            sendMyFileTable(message.getIpAddress(),message.getPort());
//            logger.info("Sending FileTable to new Node : " + message.getIpAddress()+":"+message.getPort()+"  "+fileTable.toString());
//
//            //If not in the same cluster
//        } else {
//            TableEntry entry = this.ipTable.searchClusterID(String.valueOf(clusterID));
//
//            if (entry == null) { //there's no entry in the IP table for this cluster
//                entry = new TableEntry(message.getIpAddress(),String.valueOf(message.getPort()),
//                        String.valueOf(message.getClusterID()));
//                this.getIpTable().addTableEntry(entry);
//
//            } else { //there's an entry in the IP table for this cluster
//                GroupProtocol groupMessage = new GroupProtocol(entry.getIpAddress(), Integer.parseInt(entry.getPort()));
//                outBuffer.add(new DispatchMessage(groupMessage.toString(), ipAddress, port));
//            }
//        }
//        logger.info("IP : "+getIpAddress()+" PORT : "+getPort()+
//                " IPTABLE : "+getIpTable().toString());
//
//    }

//    public void process_groupMessage(GroupProtocol message) {
//        int clusterID = message.getClusterID();
//        if (clusterID == this.getClusterID()) { //check if it's in the same cluster
//            JoinProtocol joinMessage = new JoinProtocol(message.getClientIP(), message.getPort());
//            outBuffer.add(new DispatchMessage(joinMessage.toString(), message.getClientIP(), message.getPort()));
//        }
//    }

//    public void sendJoinMessage(String RECIEVED_IP, int RECIEVED_PORT) {
//        sentJoins.add(new TableEntry(RECIEVED_IP,RECIEVED_PORT+"",Utils.getClusterID(RECIEVED_IP,RECIEVED_PORT,NO_CLUSTERS)+""));
//        //generating the join message
//        JoinProtocol joinProtocol = new JoinProtocol(CLIENT_IP, CLIENT_PORT);
//        String JOIN_MSG = joinProtocol.toString();
//
//        outBuffer.add(new DispatchMessage(JOIN_MSG, RECIEVED_IP, RECIEVED_PORT));
//
//    }

//    public void sendMyFileTable(String RECIEVED_IP, int RECIEVED_PORT) {
//
//        FileTableProtocol fileTableProtocol = new FileTableProtocol(CLIENT_IP,CLIENT_PORT,fileTable);
//        String fileTableMSG = fileTableProtocol.toString();
//
//        outBuffer.add(new DispatchMessage(fileTableMSG,RECIEVED_IP,RECIEVED_PORT));
//
//    }

//    public void process_queryMessage(QueryProtocol message) {
//        int hopCount = message.getNoOfHops();
//        if(hopCount > 0) {
//            hopCount--;
//            List<FileTableEntry> results = searchFile(message.getKeyword());
//            String ipAddress = message.getIpAddress();
//            int port = message.getPort();
//
//            if(results != null && results.size()>0) {
//                // generating QueryHit message
//                QueryHitProtocol queryHitMessage = new QueryHitProtocol(ipAddress, port, results);
//                outBuffer.add(new DispatchMessage(queryHitMessage.toString(), ipAddress, port));
//            }
//        } if(hopCount > 0){ // check hopCount and forward QUERY message
//            message.setNoOfHops(hopCount);
//            int clusterID = this.clusterID;
//            if(!this.getIpTable().isEmpty()) {
//                List<TableEntry> ipTable = this.ipTable.getEntries();
//                for (TableEntry entry : ipTable) {
//                    // send QUERY message to other clusters
//                    // FIXME filter out the query source's cluster before forwarding
//                    if(!entry.getClusterID().equals(clusterID)) {
//                        outBuffer.add(new DispatchMessage(message.toString(),entry.getIpAddress(),
//                                Integer.parseInt(entry.getPort())));
//                    }
//                }
//            }
//        }
//
//
//
//    }

    public void selectProcessType(int no_nodes, String[] response_data) throws IOException {
        switch (no_nodes) {
            case 0:
                process();
                break;
            case 1:
                process(response_data[3], Integer.parseInt(response_data[4]));
                break;
            case 2:
            case 3:
            case 4:
                process(response_data[3], Integer.parseInt(response_data[4]), response_data[6], Integer.parseInt(response_data[7]));
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