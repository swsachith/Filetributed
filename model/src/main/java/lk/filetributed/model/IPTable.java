package lk.filetributed.model;

import java.util.*;

public class IPTable {
    List<TableEntry> entries;

    public IPTable() {
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
        entries.add(newEntry);
    }

    public boolean searchClusterID(String clusterid) {
        for (Iterator<TableEntry> iterator = entries.iterator(); iterator.hasNext();) {
            TableEntry entry = iterator.next();
            if (entry.getClusterID().equals(clusterid))
                return false;
        }
        return true;
    }

    public void removeTableEntry(TableEntry oldEntry) {
        entries.remove(oldEntry);
    }

    public String convertToString(){
        String entrylist = "";
        for (Iterator<TableEntry> iterator = entries.iterator(); iterator.hasNext();){
            entrylist += iterator.next().toString()+" ";
        }
        return entrylist;
    }

    public void setEntries(String entrylist, String clusterID) {
        String[] entries = entrylist.trim().split(" ");
        TableEntry newEntry;
        String[] data;
        for(String entry: entries){
            data = entry.split(":");
            if(data[2] == clusterID){
                newEntry = new TableEntry(entry.split(":")[0],entry.split(":")[1],entry.split(":")[2]);
                addTableEntry(newEntry);
            }
            if(searchClusterID(data[2])){
                newEntry = new TableEntry(entry.split(":")[0],entry.split(":")[1],entry.split(":")[2]);
                this.entries.add(newEntry);
            }
        }
    }
}
