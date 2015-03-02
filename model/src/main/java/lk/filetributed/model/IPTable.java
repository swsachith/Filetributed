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

    public void removeTableEntry(TableEntry oldEntry) {
        entries.remove(oldEntry);
    }

}
