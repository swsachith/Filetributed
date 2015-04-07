package lk.filetributed.client.rpc;

import lk.filetributed.model.TableEntry;
import org.apache.log4j.Logger;

/**
 * Created by sudheera on 4/7/15.
 */
public class HeartbeatThread extends Thread {


    private static Logger logger = Logger.getLogger(HeartbeatThread.class);

    private Client client;

    public HeartbeatThread(Client client) {
        this.client=client;
    }

    @Override
    public void run() {

        while (true){

            try {
                Thread.sleep(5000);
                for (TableEntry entry : client.getIpTable().getEntries()){
                    client.invokeSendbeat(entry.getIpAddress(),Integer.parseInt(entry.getPort()));
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
