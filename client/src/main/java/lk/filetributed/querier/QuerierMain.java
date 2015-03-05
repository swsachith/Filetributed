package lk.filetributed.querier;

import lk.filetributed.dispatcher.MessageDispatcher;

public class QuerierMain {
    public static void main(String[] args) {
        Thread dispatcher = new Thread(new MessageDispatcher());
        dispatcher.start();

        Thread queryBot = new Thread(new QueryBot("127.0.0.1", 9888));
        queryBot.start();



    }
}
