package lk.filetributed.client;

import lk.filetributed.model.FileTableEntry;
import lk.filetributed.model.Node;
import org.apache.log4j.Logger;

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
    }

    public void initialize() {
        String result = BootstrapConnector.connectToBootstrap(SERVER_NAME, PORT, CLIENT_IP, CLIENT_PORT, USERNAME);
        process(result);
    }
    public List<FileTableEntry> searchFile(String filename) {
        return this.fileTable.searchTable(filename);
    }

    public static void main(String[] args) {
        Client client = new Client();

    }

    public static void process(String server_response){
        String[] response_data = server_response.split(" ");
        if (!response_data[1].equals("REGOK")) {
            System.err.println("Registration Failed. Try Again!");
            System.exit(0);
        }

        int no_nodes = Integer.parseInt(response_data[2]);

        switch (no_nodes) {
            case 0:
                break;
            case 1:
                break;
            case 2:
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