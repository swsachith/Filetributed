package lk.filetributed.querier;

import lk.filetributed.dispatcher.MessageOutBuffer;
import lk.filetributed.model.DispatchMessage;
import lk.filetributed.model.protocols.QueryProtocol;
import org.apache.log4j.Logger;

import java.util.Random;

public class QueryBot implements Runnable {
    private static Logger logger = Logger.getLogger(QueryBot.class);

    private MessageOutBuffer outBuffer;


    public QueryBot() {
        outBuffer = MessageOutBuffer.getInstance();
    }

    @Override
    public void run() {
        int i = 2;
        String query = "";
        while (i > 0) {
            query = getRandomQuery();
            String ipaddress = "127.0.0.1";
            int port = 9888;
            QueryProtocol queryMessage = new QueryProtocol(ipaddress, port, 2, query);
            outBuffer.add(new DispatchMessage(queryMessage.toString(),ipaddress,port));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("Query bot got interrupted ... "+e.getMessage());
            }
            i--;
        }

    }

    private String getRandomQuery() {
        String[] queries = {"adventures", "tin", "Jill", "happy"};
        int queryLength = queries.length;
        Random r = new Random();
        int low = 1;
        int random = r.nextInt(queryLength-low) + low;
        return queries[random - 1];
    }
}
