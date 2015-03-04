package lk.filetributed.dispatcher;

import lk.filetributed.model.protocols.MessageProtocol;

import java.util.LinkedList;

public class MessageBuffer<T extends MessageProtocol> {
    public static MessageBuffer instance;
    private LinkedList<T> queue;

    private MessageBuffer() {
        queue = new LinkedList<T>();
    }

    /**
     * Returns a singleton dispatch queue object
     *
     * @return Dispatch queue object
     */
    public static MessageBuffer getInstance() {
        if (instance == null) {
            instance = new MessageBuffer();
        }
        return instance;
    }

    /**
     * Put a message into the dispatch queue
     *
     */
    public synchronized void add(T item) {
        queue.add(item);
        notifyAll();
    }

    /**
     * Returns a FileMetadata object from the dispatcher queue
     *
     * @return FileMetadata object
     */
    public synchronized T getMessage() {
        T item = (T) queue.pollFirst();
        while (item == null) {

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            item = (T) queue.pollFirst();
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