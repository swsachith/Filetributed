package lk.filetributed.model;

import junit.framework.Assert;
import lk.filetributed.model.protocols.GroupFindProtocol;
import org.junit.Test;

public class GroupFindProtocolTester {
    @Test
    public void testGetGroupFindMessage() {
        String clientIP = "127.0.0.1";
        String clusterID = "2";
        int port = 9889;
        GroupFindProtocol groupFindProtocol = new GroupFindProtocol(clientIP, port);
        String result = groupFindProtocol.toString();
        String expected = "0027 GROUP 127.0.0.1 9889 2";
        Assert.assertEquals(expected,result);
    }
}
