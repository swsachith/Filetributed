package lk.filetributed.model.protocols;

import lk.filetributed.model.IPTable;
import lk.filetributed.model.Node;
import lk.filetributed.model.TableEntry;
import lk.filetributed.util.Utils;

/**
 * Created by Dammina on 3/4/2015.
 */
public class IPTableProtocol extends MessageProtocol {

    private String ipAddress;
    private int port;
    private int clusterID;
    private IPTable ipTable;
    private String entries;
    private String messageID;


    public IPTableProtocol() {

    }

    public IPTableProtocol(String ipAddress, int port, int clusterID, IPTable ipTable) {
        this.messageType="IPTABLE";
        this.ipAddress = ipAddress;
        this.port = port;
        this.clusterID = Utils.getClusterID(ipAddress, port, NO_CLUSTERS);
        this.ipTable = ipTable;
        this.messageID = Utils.getMessageID();
        if(this.clusterID == clusterID) {
            this.entries = ipTable.convertToString();
            this.ipTable.setEntries(entries, clusterID);
        }
        else{
            if(getIpTable().searchClusterID(this.clusterID+"")==null){
                this.entries = "#";
                getIpTable().addTableEntry(new TableEntry(this.ipAddress, this.port+"", this.clusterID+""));
            }
        }
    }

    public void getIPTableResponse(String msg) {
        String[] recievedMessage = msg.split("#");
        this.ipTable.setEntries(recievedMessage[1],
                Utils.getClusterID(recievedMessage[0].split(" ")[2], Integer.parseInt(recievedMessage[0].split(" ")[3]), NO_CLUSTERS));

    }

    public void addNewEntriesToIPTable(int clusterID){
        if(this.clusterID == clusterID) {

            this.ipTable.setEntries(entries, clusterID);

        }
        else{

            TableEntry tableEntry = new TableEntry(this.ipAddress, this.port + "", this.clusterID + "");
            if(this.ipTable==null){
                IPTable  tempIPTable = new IPTable();
                tempIPTable.addTableEntry(tableEntry);
                this.ipTable = tempIPTable;
            }
            else if(this.ipTable.searchClusterID(this.clusterID+"")==null){
                this.ipTable.addTableEntry(tableEntry);
            }
        }

    }

    @Override
    public String toString() {
        String msg = "IPTABLE " + this.messageID + " " + this.ipAddress + " " + this.port + " " + this.entries;
        String length = String.format("%04d", msg.length()+5);
        return length+" "+msg;
    }

    @Override
    public void initialize(String message) {
        String message_data = message.split("#")[0];
        this.messageType="IPTABLE";
        String[] tokenz = message_data.split(" ");

        this.messageID = tokenz[2];
        this.ipAddress = tokenz[3];
        this.port = Integer.parseInt(tokenz[4]);
        this.clusterID = Utils.getClusterID(this.ipAddress, this.port, NO_CLUSTERS);

        if(message.length()!=message.indexOf('#')+1) {
            this.entries = message.split("#")[1];
        }
        else{
            System.out.println("Received IPTable is empty.");
        }

    }

    public IPTable getIpTable() {
        return ipTable;
    }

    public void setIpTable(IPTable ipTable) {
        this.ipTable = ipTable;
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

    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }

    @Override
    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }
}
