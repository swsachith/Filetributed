package lk.filetributed.model;

public class TableEntry {
    private String ipAddress;
    private String port;
    private String clusterID;

    public TableEntry(String ipAddress, String port, String clusterID) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.clusterID = clusterID;
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

    public String getClusterID() {
        return clusterID;
    }

    public void setClusterID(String clusterID) {
        this.clusterID = clusterID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableEntry entry = (TableEntry) o;

        if (!ipAddress.equals(entry.ipAddress)) return false;
        if (!port.equals(entry.port)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ipAddress.hashCode();
        result = 31 * result + port.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return ipAddress + ":" + port + ":" + clusterID;
    }
}
