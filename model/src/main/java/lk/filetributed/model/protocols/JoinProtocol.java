package lk.filetributed.model.protocols;

public class JoinProtocol implements MessageProtocol{
    private String ipAddress;
    private int port;

    public JoinProtocol(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public static String getJoinMessage(String ipaddress, int port) {
        String msg = "JOIN " + ipaddress + " " + port;
        String length = String.format("%04d", msg.length()+5);
        return length+" "+msg;
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
    public MessageProtocol resolveMessage(String message) {
        return null;
    }
}
