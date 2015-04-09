package lk.filetributed.client.rpc;

import lk.filetributed.client.BootstrapConnector;
import lk.filetributed.client.rpc.services.log_services;
import lk.filetributed.client.rpc.services.messageProtocol;
import lk.filetributed.client.rpc.services.searchResponse;
import lk.filetributed.client.rpc.services.services;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


public class Client extends Node implements services.Iface {

    private static Logger logger = Logger.getLogger(Client.class);

    private static String SERVER_NAME;
    private static int PORT;

    private static String CLIENT_IP;
    private static int CLIENT_PORT;
    private static String USERNAME;
    private static int NO_CLUSTERS;
    private static String DEBUG_IP;
    private static int DEBUG_PORT;

    private static String[] FILE_NAMES;


    public Client(String arg) {
        configServer("client/config/server.xml");
        configClient("client/config/"+arg);

        super.ipAddress = CLIENT_IP;
        super.port=CLIENT_PORT;
        super.NO_CLUSTERS=NO_CLUSTERS;
        super.setCluster();
        debugServerLog("initiated Cluster ID : "+clusterID);
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

        Thread t_queryThread = new Thread(new QueryThread(this));
        t_queryThread.start();

        Thread t_heartbeatThread = new Thread(new HeartbeatThread(this));
        t_heartbeatThread.start();
    }

    private void configServer(String configFileLocation) {
        Properties properties = Utils.loadPropertyFile(configFileLocation);

        SERVER_NAME=properties.getProperty("SERVER_NAME");
        PORT=Integer.parseInt(properties.getProperty("PORT"));

        DEBUG_IP=properties.getProperty("DEBUG_IP");
        DEBUG_PORT=Integer.parseInt(properties.getProperty("DEBUG_PORT"));

    }

    //connect with the system
    public void initialize() {
        String result = BootstrapConnector.connectToBootstrap(SERVER_NAME, PORT, CLIENT_IP, CLIENT_PORT, USERNAME);
        debugServerLog("REG message sent to Bootrstrap");
        try {
            response_tokenizer(result);
            debugServerLog(result);
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

    public static void main(String[] args) {
        if (args.length>=1) {
            Client client = new Client(args[0]);
        }else {
            Client client = new Client("client10.xml");
        }
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
        IPTable inc_ipTable = sendJoin(receivedNode);

        if (inc_ipTable!=null){
            for (TableEntry entry : inc_ipTable.getEntries()){
                this.ipTable.addTableEntry(entry);
            }
        }

        FileTable inc_fileTable = sendFileTable(receivedNode);
        logger.info("#  my file table "+ this.fileTable.getEntries().toString());
        if (inc_fileTable!=null){
            this.fileTable.mergeEntriesFromTable(inc_fileTable);
            logger.info("##  received file table " + inc_fileTable.getEntries().toString());
            debugServerLog("##  received file table " + inc_fileTable.getEntries().toString());
            logger.info("###  merged file table " + this.fileTable.getEntries().toString());
            debugServerLog("###  merged file table " + this.fileTable.getEntries().toString());

        }

        Node NodeToMergeFileTables;
        FileTable fileTable;
        for (TableEntry entry : this.ipTable.getEntries()){
            if(this.clusterID == Integer.parseInt(entry.getClusterID())){
                NodeToMergeFileTables = new Node(RECIEVED_IP, RECIEVED_PORT, NO_CLUSTERS);
                fileTable = sendFileTable(NodeToMergeFileTables);
                logger.info("#  my file table "+ this.fileTable.getEntries().toString());
                if (fileTable!=null){
                    this.fileTable.mergeEntriesFromTable(inc_fileTable);
                    logger.info("##  received file table " + fileTable.getEntries().toString());
                    debugServerLog("##  received file table " + fileTable.getEntries().toString());
                    logger.info("###  merged file table " + this.fileTable.getEntries().toString());
                    debugServerLog("###  merged file table " + this.fileTable.getEntries().toString());
                }
            }

        }

    }

    public void process(String RECIEVED_IP_01, int RECIEVED_PORT_01, String RECIEVED_IP_02, int RECIEVED_PORT_02) {
        int clusterID01;
        int clusterID02;

        clusterID01 = Utils.getClusterID(RECIEVED_IP_01, RECIEVED_PORT_01, NO_CLUSTERS);
        Node receivedNode1 = new Node(RECIEVED_IP_01, RECIEVED_PORT_01, NO_CLUSTERS);

        this.getIpTable().addTableEntry(new TableEntry(RECIEVED_IP_01, RECIEVED_PORT_01 + "", clusterID01 + ""));

        //generating the join message
        IPTable inc_ipTable1 = sendJoin(receivedNode1);

        if (inc_ipTable1!=null){
            for (TableEntry entry : inc_ipTable1.getEntries()){
                this.ipTable.addTableEntry(entry);
            }
        }

        FileTable inc_fileTable1 = sendFileTable(receivedNode1);
        logger.info("#  my file table "+ this.fileTable.getEntries().toString());
        if (inc_fileTable1!=null){
            this.fileTable.mergeEntriesFromTable(inc_fileTable1);
            logger.info("##  received iptable 1" + inc_ipTable1.getEntries().toString());
            debugServerLog("##  received iptable 1" + inc_ipTable1.getEntries().toString());
        }

        clusterID02 = Utils.getClusterID(RECIEVED_IP_02, RECIEVED_PORT_02, NO_CLUSTERS);
        Node receivedNode2 = new Node(RECIEVED_IP_02, RECIEVED_PORT_02, NO_CLUSTERS);

        this.getIpTable().addTableEntry(new TableEntry(RECIEVED_IP_02, RECIEVED_PORT_02 + "", clusterID02 + ""));

        //generating the join message
        IPTable inc_ipTable2 = sendJoin(receivedNode2);

        if (inc_ipTable2!=null){
            for (TableEntry entry : inc_ipTable2.getEntries()){
                this.ipTable.addTableEntry(entry);
            }
            logger.info("##  received iptable 2"+ inc_ipTable2.getEntries().toString());
            debugServerLog("##  received iptable 2" + inc_ipTable2.getEntries().toString());
        }




        FileTable inc_fileTable2 = sendFileTable(receivedNode2);

        if (inc_fileTable1!=null){
            logger.info("##  received file table 1 "+ inc_fileTable1.getEntries().toString());
            debugServerLog("##  received file table 1 " + inc_fileTable1.getEntries().toString());
            logger.info("###  merged file table " + this.fileTable.getEntries().toString());
            debugServerLog("###  merged file table " + this.fileTable.getEntries().toString());
        }
        if (inc_fileTable2!=null){
            logger.info("##  received file table 2 "+ inc_fileTable2.getEntries().toString());
            debugServerLog("##  received file table 2 " + inc_fileTable2.getEntries().toString());
            logger.info("###  merged file table " + this.fileTable.getEntries().toString());
            debugServerLog("###  merged file table " + this.fileTable.getEntries().toString());
        }


        if (inc_fileTable1!=null){
            this.fileTable.mergeEntriesFromTable(inc_fileTable2);
        }

    }

    synchronized private IPTable sendJoin(Node receivedNode ){
        TTransport transport;
        try {
            transport = new TSocket(receivedNode.getIpAddress(), receivedNode.getPort());
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            services.Client client = new services.Client(protocol);

            logger.info("Sending join request to " + receivedNode.getIpAddress() + " : " + receivedNode.getPort());
            debugServerLog("Sending join request to " + receivedNode.getIpAddress() + " : " + receivedNode.getPort());
            messageProtocol recvd_ipTable = client.joinRequest(CLIENT_IP, CLIENT_PORT);
            transport.close();

            if(recvd_ipTable.getMyIP()!=null) {
                int myClusterID_m = Utils.getClusterID(recvd_ipTable.getMyIP(), recvd_ipTable.getMyPort(), NO_CLUSTERS);
                IPTable ipTableToJoin = new IPTable(recvd_ipTable.getMyIP(), recvd_ipTable.getMyPort(), myClusterID_m);
                ipTableToJoin.setEntries(recvd_ipTable.entries, myClusterID_m);
                return ipTableToJoin;
            }

            return null;

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    synchronized private FileTable sendFileTable(Node receivedNode){
        TTransport transport;
        try {
            transport = new TSocket(receivedNode.getIpAddress(), receivedNode.getPort());
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            services.Client client = new services.Client(protocol);

            logger.info("Sending file table to " + receivedNode.getIpAddress() + " : " + receivedNode.getPort());
            debugServerLog("Sending file table to " + receivedNode.getIpAddress() + " : " + receivedNode.getPort());

            messageProtocol recvd_fileTable = client.mergeFileTable(CLIENT_IP, CLIENT_PORT, this.getFileTable().toString());
            transport.close();

            if (recvd_fileTable.getMyIP()!=null) {
                FileTable fileTableToJoin = new FileTable();
                LinkedList<FileTableEntry> entryList = (LinkedList<FileTableEntry>) fileTableToJoin.toList(recvd_fileTable.getEntries());
                for (Iterator<FileTableEntry> iterator = entryList.iterator(); iterator.hasNext(); ) {
                    fileTableToJoin.addTableEntry(iterator.next());
                }

                return fileTableToJoin;
            }

            return null;

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    synchronized void debugServerLog(String log){
        TTransport transport;
        try {
            transport = new TSocket(DEBUG_IP, DEBUG_PORT);
            transport.open();


            TProtocol protocol = new TBinaryProtocol(transport);
            log_services.Client client = new log_services.Client(protocol);

            client.infoLog(USERNAME.toUpperCase()+" "+this.getIpAddress()+":"+this.getPort()+"  "+log);
            transport.close();


        } catch (TTransportException e) {
            logger.info("Debug server not found...");
        } catch (TException e) {
            e.printStackTrace();
        }

    }
    @Override
    public messageProtocol mergeFileTable(String inc_ipAddress, int inc_port, String inc_fileTableEntries) throws TException {
        messageProtocol inc_fileTable;
        String existingTableEntries = this.getFileTable().toString();
        int inc_clusterID = Utils.getClusterID(inc_ipAddress,inc_port,NO_CLUSTERS);


        if(clusterID == inc_clusterID)
        {
            logger.info("Incoming file table from " + inc_ipAddress + " : " + inc_port+" : "+inc_fileTableEntries);
            debugServerLog("Incoming file table from " + inc_ipAddress + " : " + inc_port + " : " + inc_fileTableEntries);
            List<FileTableEntry> inc_fileEntryList = this.getFileTable().toList(inc_fileTableEntries);
            this.getFileTable().mergeEntriesFromTable(new FileTable((java.util.LinkedList<FileTableEntry>) inc_fileEntryList));
            logger.info("File table from " + inc_ipAddress + " : " + inc_port + " merged with " + this.getIpAddress() + " : " + this.getPort());
            debugServerLog("File table from " + inc_ipAddress + " : " + inc_port + " merged with " + this.getIpAddress() + " : " + this.getPort());
            logger.info("Merged table : " + this.getFileTable().toString());
            debugServerLog("Merged table : " + this.getFileTable().toString());

            inc_fileTable = new messageProtocol(ipAddress, port, existingTableEntries);

            return inc_fileTable;
        }
        return new messageProtocol();
    }

    @Override
    public messageProtocol joinRequest(String inc_ipAddress, int inc_port) throws TException {

        int inc_clusterID=Utils.getClusterID(inc_ipAddress,inc_port,this.NO_CLUSTERS);
        messageProtocol inc_ipTable;
        logger.info("Incoming join request from " + inc_ipAddress + " : " + inc_port);
        debugServerLog("Incoming join request from " + inc_ipAddress + " : " + inc_port);

        inc_ipTable = new messageProtocol();
        inc_ipTable.setMyIP(ipAddress);
        inc_ipTable.setMyPort(port);
        inc_ipTable.setEntries(this.getIpTable().toString());
        
        //check whether the incoming join request is from a node in same cluster or not
        if(clusterID==inc_clusterID){

            ipTable.addTableEntry(new TableEntry(inc_ipAddress,inc_port+"",inc_clusterID+""));
            logger.info("Returning ipTable for join request form " + inc_ipAddress + " : " + inc_port);
            debugServerLog("Returning ipTable for join request form " + inc_ipAddress + " : " + inc_port);


            //creating messageprotocol with newly added entry
            messageProtocol dist_ipTable;
            dist_ipTable = new messageProtocol();
            dist_ipTable.setMyIP(ipAddress);
            dist_ipTable.setMyPort(port);
            dist_ipTable.setEntries(this.getIpTable().toString());

            // for each node in the same cluster send iptable to merge
            for (TableEntry entry : this.ipTable.getEntries()){
                if (Integer.parseInt(entry.getClusterID())==this.getClusterID() && !entry.equals(new TableEntry(inc_ipAddress,inc_port+"",inc_clusterID+""))){
                    invokeMergeIPTable(entry.getIpAddress(),Integer.parseInt(entry.getPort()), dist_ipTable);
                }
            }
            return inc_ipTable;
        }else {
            //checking whether my iptable has ip from incoming cluster
            if (ipTable.searchClusterID(inc_clusterID+"")==null){
                ipTable.addTableEntry(new TableEntry(inc_ipAddress,inc_port+"",inc_clusterID+""));
            }
        }

        return new messageProtocol();

    }
    synchronized private void invokeMergeIPTable(String invIp, int invPort, messageProtocol dist_ipTable ){

        TTransport transport;
        try {
            transport = new TSocket(invIp, invPort);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            services.Client client = new services.Client(protocol);

            logger.info("Sending ipTable merge request to " + invIp + " : " + invPort);
            debugServerLog("Sending ipTable merge request to " + invIp + " : " + invPort);

            client.mergeIPTable(dist_ipTable);
            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mergeIPTable(messageProtocol ipTable) throws TException {

        int inc_cluster_id = Utils.getClusterID(ipTable.getMyIP(),ipTable.getMyPort(),NO_CLUSTERS);
        if (ipTable!=null && !ipTable.getEntries().isEmpty()){
        IPTable ipTableToMerge = new IPTable(ipTable.getMyIP(),ipTable.getMyPort(),inc_cluster_id);
            ipTableToMerge.setEntries(ipTable.entries,inc_cluster_id);


            for (TableEntry entry : ipTableToMerge.getEntries()){
                this.ipTable.addTableEntry(entry);
            }
        }

        logger.info("Updated IPTable using table from "+ ipTable.getMyIP()+" : " + ipTable.getMyPort());
        debugServerLog("Updated IPTable using table from " + ipTable.getMyIP() + " : " + ipTable.getMyPort());
        logger.info("Updated IPTable " + this.ipTable.toString());
        debugServerLog("Updated IPTable " + this.ipTable.toString());


    }

    @Override
    public searchResponse searchFile(String keyword, int hopCount) throws TException {

        if (hopCount<=0){
            return new searchResponse(" ");
        }
        logger.info("search for " + keyword + " hop count "+hopCount);
        debugServerLog("search for " + keyword + " hop count " + hopCount);
        String result = "";
        List<FileTableEntry> resultEntries;

        resultEntries = this.fileTable.searchTable(keyword);
        if (resultEntries!=null){

            for (FileTableEntry entry : resultEntries){
                result+=entry.toString()+";";
            }
            return new searchResponse(result);
        }else {
            logger.info("File " + keyword+" not in my cluster");
            debugServerLog("File " + keyword + " not in my cluster");
            for (TableEntry entry : this.ipTable.getEntries()){
                if (Integer.parseInt(entry.getClusterID())!=this.clusterID){
                    logger.info("search for " + keyword + ": invoking search in cluster : "+Utils.getClusterID(entry.getIpAddress(),Integer.parseInt(entry.getPort()),NO_CLUSTERS)+entry.getIpAddress()+" : " +entry.getPort());
                    debugServerLog("search for " + keyword + ": invoking search in cluster : " + Utils.getClusterID(entry.getIpAddress(), Integer.parseInt(entry.getPort()), NO_CLUSTERS) + entry.getIpAddress() + " : " + entry.getPort());
                    FileTable fileTable1 = invokeSearch(entry.getIpAddress(), Integer.parseInt(entry.getPort()), keyword, hopCount - 1);

                    if (fileTable1!=null) {
                        result = "";
                        for (FileTableEntry entry2 : fileTable1.getEntries()) {
                            result += entry2.toString() + ";";
                        }
                        return new searchResponse(result);
                    }
                }
            }
        }

        return new searchResponse(" ");
    }

    @Override
    public String sendBeat(){
        return "lubdub";
    }

    public void invokeSendbeat(String in_ipAddr, int in_port){
        TTransport transport;
        try {
            transport = new TSocket(in_ipAddr, in_port);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            services.Client client = new services.Client(protocol);;

            String beatResponse = client.sendBeat();
            //logger.debug("heart beat from "+in_ipAddr+":"+in_port+" "+beatResponse);
            transport.close();


        } catch (TTransportException e) {
            logger.info("HeartBeat not returned...");
            debugServerLog("HeartBeat not returned...");
            this.ipTable.removeTableEntry(new TableEntry(in_ipAddr, in_port + "", Utils.getClusterID(in_ipAddr, in_port, NO_CLUSTERS) + ""));
            logger.info("Removed node " + in_ipAddr + ":" + in_port);
            debugServerLog("Removed node " + in_ipAddr + ":" + in_port);


            this.fileTable.removeEntries(in_ipAddr, in_port);

            logger.info("Updated file table after removing node "+in_ipAddr+":"+in_port+" : "+this.fileTable.toString());
            debugServerLog("Updated file table after removing node " + in_ipAddr + ":" + in_port + " : " + this.fileTable.toString());

            //e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public FileTable invokeSearch(String keyword,int hopCount){
        return invokeSearch(this.ipAddress,this.port,keyword,hopCount);
    }

    private FileTable invokeSearch(String in_ipAddr, int in_port, String keyword,int hopCount) {

        TTransport transport;
        try {
            transport = new TSocket(in_ipAddr, in_port);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            services.Client client = new services.Client(protocol);

            logger.info("invoking search on " + in_ipAddr + " : " + in_port);
            debugServerLog("invoking search on " + in_ipAddr + " : " + in_port);

            searchResponse searchResponse = client.searchFile(keyword, hopCount);
            transport.close();

            FileTable resultTable = new FileTable();
            String result = searchResponse.getResult();
            if (result.contains(";")){
                String[] entriesStr = result.split(";");
                for (String s : entriesStr){
                    String[] split = s.split(":");
                    resultTable.addTableEntry(new FileTableEntry(split[0],split[1], Integer.parseInt(split[2])));
                }
                return resultTable;
            }else {
                return null;
            }

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }

        return null;
    }


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
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
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

    public void leaveInvoked() {
        String result = BootstrapConnector.disconnectFromBootstrap(SERVER_NAME, PORT, CLIENT_IP, CLIENT_PORT, USERNAME);
        debugServerLog("UNREG message sent to Bootrstrap");
        logger.info(result);
        debugServerLog(result);
        System.exit(0);
    }
}