package lk.filetributed.model.protocols;

public abstract class MessageProtocol {
    public static final int NO_CLUSTERS=3;
    protected String messageType;
    protected String messageID;

    public abstract String toString();

    public abstract void initialize(String message);

    public abstract String getMessageType();

    public abstract String getMessageID();

}
