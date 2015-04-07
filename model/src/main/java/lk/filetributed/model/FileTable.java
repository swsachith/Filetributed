package lk.filetributed.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FileTable {
    List<FileTableEntry> entries;

    public FileTable() {
        entries = new LinkedList<FileTableEntry>();
    }

    public FileTable(LinkedList<FileTableEntry> entries){
        this.entries = entries;
    }

    public List<FileTableEntry> getEntries() {
        return entries;
    }

    public void addTableEntry(FileTableEntry newEntry) {
        for (Iterator<FileTableEntry> iterator = entries.iterator(); iterator.hasNext();) {
            FileTableEntry entry = iterator.next();
            if (entry.equals(newEntry))
                return;
        }
        entries.add(newEntry);
    }

    public void removeTableEntry(FileTableEntry oldEntry) {
        entries.remove(oldEntry);
    }

    public List<FileTableEntry> searchTable(String filename) {
        List<FileTableEntry> results = new LinkedList<FileTableEntry>();
        for (Iterator<FileTableEntry> iterator = entries.iterator(); iterator.hasNext(); ) {
            FileTableEntry entry = iterator.next();
            if (entry.getFilename().toLowerCase().matches("(.*)"+filename.toLowerCase()+"(.*)")) {
                results.add(entry);
            }
        }
        if(results.size() <1 )
            return null;
        else
            return results;
    }
    public void mergeEntriesFromTable(FileTable receivedTable){
        if(receivedTable != null){
            List<FileTableEntry> receivedTableEntries = receivedTable.getEntries();
            for (Iterator<FileTableEntry> iterator = receivedTableEntries.iterator(); iterator.hasNext();) {
                FileTableEntry entry = iterator.next();
                addTableEntry(entry);
            }
        }
    }

    @Override
    public String toString() {
        int size = entries.size();
        String entryList=";"+size+";";
        for (Iterator<FileTableEntry> iterator = entries.iterator(); iterator.hasNext(); ) {
            FileTableEntry entry = iterator.next();
            entryList+=entry+";";
        }
    return entryList;
    }

    public List<FileTableEntry> toList(String entries){
        String[] entryList = entries.split(";");
        int numOfEntries = Integer.parseInt(entryList[1]);
        List<FileTableEntry> fileList=new LinkedList<FileTableEntry>();
        for (int i = 2; i < numOfEntries+2; i++){
            String[] fileDetails = entryList[i].split(":");
            fileList.add(new FileTableEntry(fileDetails[0],fileDetails[1],Integer.parseInt(fileDetails[2])));
        }
        return fileList;
    }

    public void removeEntries(String ipAddress, int port){
        for (Iterator<FileTableEntry> iterator = entries.iterator(); iterator.hasNext();) {
            FileTableEntry entry = iterator.next();
            if(entry.getIpAddress().equals(ipAddress) && entry.getPort() == port){
                entries.remove(entry);
            }
        }
    }
}
