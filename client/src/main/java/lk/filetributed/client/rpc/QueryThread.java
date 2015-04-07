package lk.filetributed.client.rpc;

import lk.filetributed.model.FileTable;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by sudheera on 4/7/15.
 */
public class QueryThread extends Thread {

    private static Logger logger = Logger.getLogger(QueryThread.class);

    private Client client;

    public QueryThread(Client client) {
        this.client=client;
    }

    @Override
    public void run() {


        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String text = in.readLine();
            while (true){
                if (text.equals("exit")){
                    client.leaveInvoked();
                }else if(text.length()>=6 && text.substring(0,5).equals("query")){


                    String keyword = text.substring(6).trim();
                    logger.info("search for "+keyword+" started...");
                    FileTable fileTable = client.invokeSearch(keyword, 4);
                    if (fileTable!=null) {
                        System.out.println(fileTable.toString());
                    }else {
                        System.out.println("No file found");
                    }
                }

                text="";
                text = in.readLine();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
