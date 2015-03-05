package lk.filetributed.model.protocols;

import lk.filetributed.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QueryProtocol extends MessageProtocol {

    /*
    * Message Type
    * length SER IP port hops file_name
    * 0047 SER 129.82.62.142 5070 "Lord of the rings"
    * */

    private String ipAddress;
    private int port;
    private String keyword;
    private int noOfHops;

    public QueryProtocol(){
    }

    public QueryProtocol(String ipAddress, int port, int noOfHops, String keyword) {
        this.messageType = "QUERY";
        this.ipAddress = ipAddress;
        this.port = port;
        this.messageID = Utils.getMessageID();
        this.noOfHops = noOfHops;
        this.keyword = keyword;
    }

    @Override
    public void initialize(String message) {
        String[] receivedMessage = message.split(" ");
        this.messageID = receivedMessage[2];
        this.noOfHops = Integer.parseInt(receivedMessage[3]);
        this.ipAddress = receivedMessage[4];
        this.port = Integer.parseInt(receivedMessage[5]);
        this.messageType = "QUERY";

        // get the keywords within quotes
        List<String> keywordList = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(message);
        while(regexMatcher.find()) {
            keywordList.add(regexMatcher.group());
        }
        this.keyword = keywordList.get(6);
    }

    @Override
    public String getMessageType() {
        return this.messageType;
    }

    @Override
    public String getMessageID() {
        return this.messageID;
    }

    @Override
    public String toString() {
        String msg = "QUERY " + messageID + " " + noOfHops + " " + ipAddress + " " + port + " " + keyword;
        String length = String.format("%04d", msg.length() + 5);
        return length + " " + msg;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getNoOfHops() {
        return noOfHops;
    }

    public void setNoOfHops(int noOfHops) {
        this.noOfHops = noOfHops;
    }
}
