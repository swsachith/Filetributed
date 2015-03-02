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

}
