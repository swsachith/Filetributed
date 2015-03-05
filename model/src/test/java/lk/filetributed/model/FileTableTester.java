package lk.filetributed.model;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FileTableTester {

    private FileTable fileTable;
    private FileTable fileTable2;

    @Before
    public void setup() {
        fileTable = new FileTable();
        fileTable2 = new FileTable();
    }

    @Test
    public void testSearchTable() {
        generateDummyData();
        List<FileTableEntry> results = fileTable.searchTable("jack");
        Assert.assertEquals(1,results.size());
        results = fileTable.searchTable("American");
        Assert.assertEquals(2,results.size());

        int originalSizeTable1=fileTable.getEntries().size();
        int originalSizeTable2=fileTable2.getEntries().size();

        fileTable.mergeEntriesFromTable(fileTable2);
        Assert.assertEquals(originalSizeTable1+originalSizeTable2,fileTable.getEntries().size());

        fileTable.mergeEntriesFromTable(fileTable2);
        Assert.assertEquals(originalSizeTable1+originalSizeTable2,fileTable.getEntries().size());

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

        FileTableEntry fileTableEntry7 = new FileTableEntry("Song of Ice and Fire", IP, port);
        FileTableEntry fileTableEntry8 = new FileTableEntry("The God Delusion", IP, port);
        FileTableEntry fileTableEntry9 = new FileTableEntry("the War and Peace", IP, port);
        FileTableEntry fileTableEntry10 = new FileTableEntry("The theory of Everything", IP, port);
        FileTableEntry fileTableEntry11 = new FileTableEntry("Fifty shades of gray", IP, port);
        FileTableEntry fileTableEntry12 = new FileTableEntry("House of Cards", IP, port);


        fileTable2.addTableEntry(fileTableEntry7);
        fileTable2.addTableEntry(fileTableEntry8);
        fileTable2.addTableEntry(fileTableEntry9);
        fileTable2.addTableEntry(fileTableEntry10);
        fileTable2.addTableEntry(fileTableEntry11);
        fileTable2.addTableEntry(fileTableEntry12);
    }
}
