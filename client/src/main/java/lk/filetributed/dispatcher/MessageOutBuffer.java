package lk.filetributed.dispatcher;

import lk.filetributed.model.DispatchMessage;
import lk.filetributed.model.protocols.MessageProtocol;

import java.util.LinkedList;

public class MessageOutBuffer {
    public static MessageOutBuffer instance;
    private LinkedList<DispatchMessage> queue;

    private MessageOutBuffer() {
        queue = new LinkedList<DispatchMessage>();
    }

    /**
     * Returns a singleton dispatch queue object
     *
     * @return Dispatch queue object
     */
    public static MessageOutBuffer getInstance() {
        if (instance == null) {
            instance = new MessageOutBuffer();
        }
        return instance;
    }

    /**
     * Put a message into the dispatch queue
     *
     */
    public synchronized void add(DispatchMessage message) {
        queue.add(message);
        notifyAll();
    }

    /**
     * Returns a FileMetadata object from the dispatcher queue
     *
     * @return FileMetadata object
     */
    public synchronized DispatchMessage getMessage() {
        DispatchMessage item = (DispatchMessage) queue.pollFirst();
        while (item == null) {

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            item = (DispatchMessage) queue.pollFirst();
        }
        return item;
    }

    /**
     * Returns dispatch queue size
     *
     * @return queue size
     */
    public long getQueueSize() {
        return queue.size();
    }
}