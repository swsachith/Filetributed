package lk.filetributed.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FileTable {
    List<FileTableEntry> entries;

    public FileTable() {
        entries = new LinkedList<FileTableEntry>();
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
        List<FileTableEntry> receivedTableEntries = receivedTable.getEntries();
        for (Iterator<FileTableEntry> iterator = receivedTableEntries.iterator(); iterator.hasNext();) {
            FileTableEntry entry = iterator.next();
            addTableEntry(entry);
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
}
