package lk.filetributed.model.protocols;

import lk.filetributed.util.Utils;

import java.util.StringTokenizer;

public class GroupFindProtocol implements MessageProtocol{

    private String clientIP;
    private int port;
    private int clusterID;

    public GroupFindProtocol(String clientIP, int port) {

        this.clientIP = clientIP;
        this.port = port;
        this.clusterID = Utils.getClusterID(clientIP, port, NO_CLUSTERS);
    }

    @Override
    public String toString() {
        String msg = "GROUP "+clientIP+" "+port+ " " + clusterID;
        String length = String.format("%04d", msg.length()+5);
        return length + " " + msg;
    }

    @Override
    public MessageProtocol resolveMessage(String message) {
        StringTokenizer tokenizer = new StringTokenizer(message);

        //disregard the length and the message type
        tokenizer.nextElement();
        tokenizer.nextElement();
        return null;
    }
}
