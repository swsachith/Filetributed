package lk.filetributed.model.protocols;

public abstract class MessageProtocol {
    public static final int NO_CLUSTERS=3;
    protected String messageType;
    protected String messageID;

    public abstract String toString();

    public abstract void initialize(String message);

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageID() {
        return messageID;
    }

}
