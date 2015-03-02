package lk.filetributed.model;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FileTableTester {

    private FileTable fileTable;

    @Before
    public void setup() {
        fileTable = new FileTable();
    }

    @Test
    public void testSearchTable() {
        generateDummyData();
        List<FileTableEntry> results = fileTable.searchTable("jack");
        Assert.assertEquals(1,results.size());
        results = fileTable.searchTable("American");
        Assert.assertEquals(2,results.size());
    }

    private void generateDummyData() {
        String IP = "127.0.0.1";
        int port = 8834;

        FileTableEntry fileTableEntry1 = new FileTableEntry("Adventures of Tintin", IP, port);
        FileTableEntry fileTableEntry2 = new FileTableEntry("Jack and Jill", IP, port);
        FileTableEntry fileTableEntry3 = new FileTableEntry("Glee", IP, port);
        FileTableEntry fileTableEntry4 = new FileTableEntry("The Vampire Diarie", IP, port);
        FileTableEntry fileTableEntry5 = new FileTableEntry("American Pickers", IP, port);
        FileTableEntry fileTableEntry6 = new FileTableEntry("American Idol", IP, port);

        fileTable.addTableEntry(fileTableEntry1);
        fileTable.addTableEntry(fileTableEntry2);
        fileTable.addTableEntry(fileTableEntry3);
        fileTable.addTableEntry(fileTableEntry4);
        fileTable.addTableEntry(fileTableEntry5);
        fileTable.addTableEntry(fileTableEntry6);
    }
}
