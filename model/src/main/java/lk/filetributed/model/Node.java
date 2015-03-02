package lk.filetributed.model;

public class Node {

    private final int NO_CLUSTERS;
    private String ipAddress;
    private String port;
    private String username;
    private String clusterID;


    public Node(String ipAddress, String port,int clusters) {
        this.ipAddress = ipAddress;
        this.port = port;
        NO_CLUSTERS = clusters;
        setCluster();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClusterID() {
        return clusterID;
    }

    public void setCluster() {

        int hashCode = hashCode();
        clusterID = String.valueOf(hashCode % NO_CLUSTERS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (!ipAddress.equals(node.ipAddress)) return false;
        if (!port.equals(node.port)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ipAddress.hashCode();
        result = 31 * result + port.hashCode();
        return result;
    }


}
