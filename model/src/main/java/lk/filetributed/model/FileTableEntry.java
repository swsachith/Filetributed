package lk.filetributed.model;

public class FileTableEntry {

    private String filename;
    private String ipAddress;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileTableEntry that = (FileTableEntry) o;

        if (!filename.equals(that.filename)) return false;
        if (!ipAddress.equals(that.ipAddress)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filename.hashCode();
        result = 31 * result + ipAddress.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return filename + ' ' + ipAddress;
    }
}
