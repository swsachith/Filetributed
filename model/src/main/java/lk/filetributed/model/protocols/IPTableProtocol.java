package lk.filetributed.model.protocols;

import lk.filetributed.model.IPTable;
import lk.filetributed.model.Node;
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
        this.ipAddress = ipAddress;
        this.port = port;
        this.clusterID = clusterID;
        this.ipTable = ipTable;
    }

    public void getIPTableResponse(String msg) {
        String[] recievedMessage = msg.split("#");
        this.ipTable.setEntries(recievedMessage[1],
                Utils.getClusterID(recievedMessage[0].split(" ")[2], Integer.parseInt(recievedMessage[0].split(" ")[3]), NO_CLUSTERS));

    }

    @Override
    public String toString() {
        String msg = "IPTABLE " + this.messageID + " " + ipAddress + " " + port + " " + ipTable;
        String length = String.format("%04d", msg.length()+5);
        return length+" "+msg;
    }

    @Override
    public void initialize(String message) {
        String[] tokenz = message.split(" ");
        this.messageID = tokenz[2];
        this.ipAddress = tokenz[3];
        this.port = Integer.parseInt(tokenz[4]);
        this.clusterID = Utils.getClusterID(this.ipAddress, this.port, NO_CLUSTERS);
        this.ipTable = getIpTable();
        this.entries = ipTable.convertToString();
    }

    public IPTable getIpTable() {
        return ipTable;
    }

    public void setIpTable(IPTable ipTable) {
        this.ipTable = ipTable;
    }
}
