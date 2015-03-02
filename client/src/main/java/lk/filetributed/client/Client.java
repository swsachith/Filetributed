package lk.filetributed.client;

import java.net.*;
import java.io.*;

import lk.filetributed.model.FileTable;
import lk.filetributed.model.IPTable;
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
    private IPTable ipTable;
    private FileTable fileTable;

    public Client() {
        this.currentNode = new Node(CLIENT_IP, PORT, NO_CLUSTERS);
        this.ipTable = new IPTable();
        this.fileTable = new FileTable();
    }

    public void initialize() {
        String result = BootstrapConnector.connectToBootstrap(SERVER_NAME, PORT, CLIENT_IP, CLIENT_PORT, USERNAME);
    }


}