package lk.filetributed.client;

import lk.filetributed.model.protocols.MessageProtocol;
import lk.filetributed.model.protocols.MessageProtocolType;

import java.util.StringTokenizer;

public class MessageResolver {
    public static MessageProtocol resolveMessage(String message) {
        MessageProtocol protocol = null;
        StringTokenizer tokenizer = new StringTokenizer(message);

        //ignore the length of the message
        tokenizer.nextElement();

        String messageType = tokenizer.nextToken().toUpperCase();

        //compare the msg type to the enums
        switch (MessageProtocolType.valueOf(messageType)) {
            case JOIN:
                break;

            case GROUPFIND:
                break;

            default:
                break;
        }
        return null;
    }

}
