package lk.filetributed.model;

import lk.filetributed.util.Utils;

import java.util.HashSet;
import java.util.Set;

public class Node {

    public int NO_CLUSTERS;
    public String ipAddress;
    public int port;
    public String username;
    public int clusterID;
    public IPTable ipTable;
    public FileTable fileTable;
    public Set<TableEntry> sentJoins;

    public Node(String ipAddress, int port,int clusters) {
        sentJoins= new HashSet<TableEntry>();
        this.ipAddress = ipAddress;
        this.port = port;
        NO_CLUSTERS = clusters;
        this.ipTable = new IPTable(ipAddress, port, clusters);
        this.fileTable = new FileTable();
        setCluster();
    }
    public Node(){
        sentJoins= new HashSet<TableEntry>();
        this.ipTable = new IPTable();
        this.fileTable = new FileTable();
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public IPTable getIpTable() {
        return ipTable;
    }

    public void setIpTable(IPTable ipTable) {
        this.ipTable = ipTable;
    }

    public FileTable getFileTable() {
        return fileTable;
    }

    public void setFileTable(FileTable fileTable) {
        this.fileTable = fileTable;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getClusterID() {
        return clusterID;
    }

    public void setCluster() {

        clusterID = Utils.getClusterID(this.ipAddress, this.port, NO_CLUSTERS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (port != node.port) return false;
        if (!ipAddress.equals(node.ipAddress)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ipAddress.hashCode();
        result = 31 * result + port;
        return result;
    }
}
