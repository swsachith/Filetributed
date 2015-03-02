package lk.filetributed.model.protocols;

public class JoinProtocol {

    public static String getJoinMessage(String ipaddress, String port) {
        return "JOIN " + ipaddress + " " + port;
    }

    public static String getJoinResponse(JoinStatus status) {
        if (status.equals(JoinStatus.SUCCESS)) {
            return "JOINOK 0";
        }else if (status.equals(JoinStatus.FAILED)) {
            return "JOINOK 9999";
        }else
            return "";

    }
}
