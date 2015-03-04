package lk.filetributed.disptacher;

import lk.filetributed.dispatcher.MessageBuffer;
import lk.filetributed.model.protocols.JoinProtocol;
import lk.filetributed.model.protocols.MessageProtocol;
import org.junit.Before;
import org.junit.Test;

public class MessageBufferTester {
    private MessageBuffer messageBuffer;

    @Before
    public void setup() {
        messageBuffer = MessageBuffer.getInstance();
    }

    @Test
    public void testQueue() {
        JoinProtocol joinProtocol1 = new JoinProtocol("127.0.0.1", 9889);
        JoinProtocol joinProtocol2 = new JoinProtocol("127.0.0.1", 9888);
        JoinProtocol joinProtocol3 = new JoinProtocol("127.0.0.1", 9887);
        JoinProtocol joinProtocol4 = new JoinProtocol("127.0.0.1", 9886);
        messageBuffer.add(joinProtocol1);
        messageBuffer.add(joinProtocol2);
        messageBuffer.add(joinProtocol3);
        messageBuffer.add(joinProtocol4);

        MessageProtocol message = messageBuffer.getMessage();
        System.out.println(message);
    }
}
