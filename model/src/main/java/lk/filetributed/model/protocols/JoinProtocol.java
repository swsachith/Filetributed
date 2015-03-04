package lk.filetributed.model.protocols;

import lk.filetributed.util.Utils;

import java.util.StringTokenizer;

public class JoinProtocol extends MessageProtocol{
    private String ipAddress;
    private int port;
    private int clusterID;

    public JoinProtocol(String ipAddress, int port) {
        this.messageType = "JOIN";
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public static String getJoinResponse(JoinStatus status) {
        String msg = "";
        if (status.equals(JoinStatus.SUCCESS)) {
            msg= "JOINOK 0";
        } else if (status.equals(JoinStatus.FAILED)) {
            msg= "JOINOK 9999";
        } else
            return "";
        String length = String.format("%04d", msg.length()+5);
        return length+" "+msg;
    }


    @Override
    public void initialize(String message) {
        String[] receivedMessage = message.split(" ");
        this.messageID = receivedMessage[2];
        this.ipAddress = receivedMessage[3];
        this.port = Integer.parseInt(receivedMessage[4]);
        this.clusterID = Utils.getClusterID(ipAddress, port, NO_CLUSTERS);
    }

    @Override
    public String toString() {
        String msg = "JOIN " +messageID+" "+ ipAddress + " " + port;
        String length = String.format("%04d", msg.length()+5);
        return length+" "+msg;
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
