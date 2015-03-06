package lk.filetributed.model.protocols;

import lk.filetributed.model.FileTable;
import lk.filetributed.model.FileTableEntry;
import lk.filetributed.util.Utils;

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
        table=new FileTable();
        this.messageType = "FILETABLE";
        String[] receivedMessage = message.split(" ");
        this.messageID = receivedMessage[2];
        this.ipAddress = receivedMessage[3];
        this.port = Integer.parseInt(receivedMessage[4]);
        this.clusterID = Utils.getClusterID(ipAddress, port, NO_CLUSTERS);

        String[] tableEntryString = message.split(";");
        int count = Integer.parseInt(tableEntryString[1]);
        for (int i = 2; i <= count+1; i++) {
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

    @Override
    public String getMessageType() {
        return messageType;
    }

    @Override
    public String getMessageID() {
        return messageID;
    }

    public FileTable getFileTable() {
        return table;
    }
}
