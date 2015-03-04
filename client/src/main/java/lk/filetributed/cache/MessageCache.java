package lk.filetributed.cache;
import java.util.ArrayList;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.LRUMap;

/**
 * @author Crunchify.com
 */

public class MessageCache<K, T> {

    private long timeToLive;
    private LRUMap messageCacheMap;

    protected class CacheObject {
        public long lastAccessed = System.currentTimeMillis();
        public T value;

        protected CacheObject(T value) {
            this.value = value;
        }
    }

    public MessageCache(long ttl, final long timeInterval, int maxItems) {
        this.timeToLive = ttl * 1000;

        messageCacheMap = new LRUMap(maxItems);

        if (timeToLive > 0 && timeInterval > 0) {

            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(timeInterval * 1000);
                        } catch (InterruptedException ex) {
                        }
                        cleanup();
                    }
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    public void put(K key, T value) {
        synchronized (messageCacheMap) {
            messageCacheMap.put(key, new CacheObject(value));
        }
    }

    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (messageCacheMap) {
            CacheObject c = (CacheObject) messageCacheMap.get(key);

            if (c == null)
                return null;
            else {
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }

    public void remove(K key) {
        synchronized (messageCacheMap) {
            messageCacheMap.remove(key);
        }
    }

    public int size() {
        synchronized (messageCacheMap) {
            return messageCacheMap.size();
        }
    }

    @SuppressWarnings("unchecked")
    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey = null;

        synchronized (messageCacheMap) {
            MapIterator itr = messageCacheMap.mapIterator();

            deleteKey = new ArrayList<K>((messageCacheMap.size() / 2) + 1);
            K key = null;
            CacheObject c = null;

            while (itr.hasNext()) {
                key = (K) itr.next();
                c = (CacheObject) itr.getValue();

                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                    deleteKey.add(key);
                }
            }
        }

        for (K key : deleteKey) {
            synchronized (messageCacheMap) {
                messageCacheMap.remove(key);
            }

            Thread.yield();
        }
    }
}