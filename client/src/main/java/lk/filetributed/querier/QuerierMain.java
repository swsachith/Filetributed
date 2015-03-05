package lk.filetributed.querier;

public class QuerierMain {
    public static void main(String[] args) {
        Thread queryBot = new Thread(new QueryBot());
        queryBot.start();

    }
}
