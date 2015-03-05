package lk.filetributed.model;

import java.util.*;

public class IPTable {
    List<TableEntry> entries;
    TableEntry self;

    public IPTable() {
        entries = new LinkedList<TableEntry>();
    }

    public IPTable(String ipAddress, int port, int clusterID){
        self= new TableEntry(ipAddress, String.valueOf(port), String.valueOf(clusterID));
        entries = new LinkedList<TableEntry>();
    }

    public List<TableEntry> getEntries() {
        return entries;
    }

    public void addTableEntry(TableEntry newEntry) {
        for (Iterator<TableEntry> iterator = entries.iterator(); iterator.hasNext();) {
            TableEntry entry = iterator.next();
            if (entry.equals(newEntry))
                return;
        }
        if(!newEntry.equals(self))
            entries.add(newEntry);
    }

    public TableEntry searchClusterID(String clusterid) {
        for (Iterator<TableEntry> iterator = entries.iterator(); iterator.hasNext();) {
            TableEntry entry = iterator.next();
            if (entry.getClusterID().equals(clusterid))
                return entry;
        }
        return null;
    }

    public void removeTableEntry(TableEntry oldEntry) {
        entries.remove(oldEntry);
    }

    public String convertToString(){
        String entrylist = "";
        for (Iterator<TableEntry> iterator = entries.iterator(); iterator.hasNext();){
            entrylist += iterator.next().toString()+" ";
        }
        return "#"+entrylist;
    }


    public void setEntries(String entrylist, int clusterID) {
        String[] entries = entrylist.trim().split(" ");
        TableEntry newEntry;
        String[] data;
        for(String entry: entries){
            data = entry.split(":");
            if(Integer.parseInt(data[2]) == clusterID ){
                newEntry = new TableEntry(entry.split(":")[0],entry.split(":")[1],entry.split(":")[2]);
                addTableEntry(newEntry);
            }
            if(searchClusterID(data[2])==null){
                newEntry = new TableEntry(entry.split(":")[0],entry.split(":")[1],entry.split(":")[2]);
                addTableEntry(newEntry);
            }
        }
    }

    @Override
    public String toString() {
        String text="";
        for (Iterator<TableEntry> iterator = entries.iterator(); iterator.hasNext();) {
            TableEntry entry = iterator.next();
            text += entry.getIpAddress()+":"+entry.getPort()+":"+entry.getClusterID()+" ";
        }
        return text;
    }

    public boolean isEmpty() {
        return (entries.size() == 0) ? true : false;
    }

    public TableEntry getSelf() {
        return self;
    }

    public void setSelf(TableEntry self) {
        this.self = self;
    }
}
