package lk.filetributed.model.protocols;

import lk.filetributed.model.FileTable;
import lk.filetributed.model.FileTableEntry;
import lk.filetributed.util.Utils;

import java.util.List;

/**
 * Returns a message of type
 * <length QUERYHIT messageID recv_ip recv port #files filename:ip:port, filename:ip:port
 */
public class QueryHitProtocol extends MessageProtocol {

    private String ipAddress;
    private int port;
    private List<FileTableEntry> results;

    public QueryHitProtocol(String ipAddress, int port, List<FileTableEntry> results) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.results = results;
        this.messageID = Utils.getMessageID();
        this.messageType = "QUERYHIT";
    }

    @Override
    public String toString() {
        String msg = "QUERYHIT " + messageID + " " + ipAddress + " " + port +" "+results.size()+" "+getResultListString();
        String length = String.format("%04d", msg.length() + 5);
        return length + " " + msg;
    }

    @Override
    public void initialize(String message) {
        //TODO: Code to resolve the QUERYHIT message String
    }

    @Override
    public String getMessageType() {
        return this.getMessageType();
    }

    @Override
    public String getMessageID() {
        return this.getMessageID();
    }

    public String getResultListString() {
        String result = "";
        if (results != null) {
            for (FileTableEntry entry : results) {
                result += "#"+entry.getFilename() + ":" + entry.getIpAddress() + ":" + entry.getPort() + " ";
            }
        }
        return result;
    }
}
