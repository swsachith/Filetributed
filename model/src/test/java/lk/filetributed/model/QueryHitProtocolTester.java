package lk.filetributed.model;

import junit.framework.Assert;
import lk.filetributed.model.protocols.QueryHitProtocol;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class QueryHitProtocolTester {

    @Test
    public void testQueryHitProtocolConstruction() {
        String ip = "127.0.0.1";
        int port = 8877;
        List<FileTableEntry> dummyData = createDummyData();
        QueryHitProtocol queryHit = new QueryHitProtocol(ip,port,dummyData);
        System.out.println(queryHit);

    }

    private List<FileTableEntry> createDummyData() {
        List<FileTableEntry> results = new LinkedList<FileTableEntry>();
        String ip = "127.0.0.1";
        int port = 8877;
        FileTableEntry tableEntry1 = new FileTableEntry("Tin Tin", ip, port + 1);
        FileTableEntry tableEntry2 = new FileTableEntry("Happy Feet", ip, port + 2);
        FileTableEntry tableEntry3 = new FileTableEntry("baby one", ip, port + 3);
        FileTableEntry tableEntry4 = new FileTableEntry("distributed", ip, port + 4);

        results.add(tableEntry1);
        results.add(tableEntry2);
        results.add(tableEntry3);
        results.add(tableEntry4);

        return results;
    }

}
