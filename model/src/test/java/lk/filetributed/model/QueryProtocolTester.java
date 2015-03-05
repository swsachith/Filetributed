package lk.filetributed.model;

import junit.framework.Assert;
import lk.filetributed.model.protocols.QueryProtocol;
import lk.filetributed.util.Utils;
import org.junit.Test;


public class QueryProtocolTester {
    @Test
    public void testQueryMessage() {
        String messageID = Utils.getMessageID();
        String message = "0056 QUERY " + messageID + " 2 127.0.0.1 9889 \"Adventures of Tin\"";
        QueryProtocol queryProtocol = new QueryProtocol();
        queryProtocol.initialize(message);
        String result = queryProtocol.toString();
        String expected = message;
        Assert.assertEquals(expected, result);
    }
}
