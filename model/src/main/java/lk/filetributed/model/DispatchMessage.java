package lk.filetributed.model;

public class DispatchMessage {
    private String message;
    private String ipAddress;
    private int port;

    public DispatchMessage(String message, String ipAddress, int port) {
        this.message = message;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
