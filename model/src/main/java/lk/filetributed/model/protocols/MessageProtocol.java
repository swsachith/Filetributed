package lk.filetributed.model.protocols;

public interface MessageProtocol {
    public static final int NO_CLUSTERS = 3;

    public MessageProtocol initialize(String message);

    public String toString();
}
