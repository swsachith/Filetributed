package lk.filetributed.model.protocols;

import lk.filetributed.util.Utils;

import java.util.StringTokenizer;
import java.util.UUID;

public class GroupProtocol extends MessageProtocol{

    /**
     * Message type
     * 0098 GROUP 2wq12sad 127.0.0.1 9888
     */

    private String clientIP;
    private int port;
    private int clusterID;

    public GroupProtocol() {
    }

    public GroupProtocol(String clientIP, int port) {
        this.messageID = Utils.getMessageID();
        this.messageType = "GROUP";
        this.clientIP = clientIP;
        this.port = port;
        this.clusterID = Utils.getClusterID(clientIP, port, NO_CLUSTERS);
    }

    @Override
    public void initialize(String message) {
        String[] receivedMessage = message.split(" ");
        this.messageID = receivedMessage[2];
        this.clientIP = receivedMessage[3];
        this.port = Integer.parseInt(receivedMessage[4]);
        this.messageType = "GROUP";
        this.clusterID = Utils.getClusterID(clientIP, port, NO_CLUSTERS);

    }

    @Override
    public String toString() {
        String msg = "GROUP "+ messageID+" "+clientIP+" "+port+ " " + clusterID;
        String length = String.format("%04d", msg.length()+5);
        return length + " " + msg;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
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
