package lk.filetributed.model.protocols;

import lk.filetributed.model.FileTable;
import lk.filetributed.model.FileTableEntry;
import lk.filetributed.util.Utils;

/**
 * Created by sudheera on 3/4/15.
 */
public class FileTableProtocol extends MessageProtocol {

    private String ipAddress;
    private int port;
    private int clusterID;
    private FileTable table;

    public FileTableProtocol() {
    }

    public FileTableProtocol(String ipAddress, int port, FileTable table) {
        this.messageType = "FILETABLE";
        this.ipAddress = ipAddress;
        this.port = port;
        this.table=table;
        this.messageID=Utils.getMessageID();
    }
    @Override
    public String toString() {
        String msg = "FILETABLE " +messageID+" "+ ipAddress + " " + port+" "+table.toString();
        String length = String.format("%04d", msg.length()+5);
        return length+" "+msg;
    }

    @Override
    public void initialize(String message) {
        String[] receivedMessage = message.split(" ");
        this.messageID = receivedMessage[2];
        this.ipAddress = receivedMessage[3];
        this.port = Integer.parseInt(receivedMessage[4]);
        String tableString = receivedMessage[5];
        this.clusterID = Utils.getClusterID(ipAddress, port, NO_CLUSTERS);

        String[] tableEntryString = tableString.split(";");
        int count = Integer.parseInt(tableEntryString[0]);
        for (int i = 1; i <= count; i++) {
            String[] entry = tableEntryString[i].split(":");
            table.addTableEntry(new FileTableEntry(entry[0],entry[1],Integer.parseInt(entry[2])));
        }

    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getClusterID() {
        return clusterID;
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }
}
