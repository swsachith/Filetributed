package lk.filetributed.client;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BootstrapConnector {
    private static Logger logger = Logger.getLogger(BootstrapConnector.class);

    public static String connectToBootstrap(final String SERVER_NAME, final int PORT, final String CLIENT_IP,
                                      final int CLIENT_PORT, final String USERNAME){
        Socket client = null;
        try {
            String REGISTER_MSG = "REG " + CLIENT_IP + " " + CLIENT_PORT + " " + USERNAME;

            //setting the length
            String length = String.format("%04d", REGISTER_MSG.length()+5);
            REGISTER_MSG = length + " " + REGISTER_MSG;

            logger.info("Connecting to " + SERVER_NAME
                    + " on port " + PORT);
            client = new Socket(SERVER_NAME, PORT);
            logger.info("Just connected to "
                    + client.getRemoteSocketAddress());

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.print(REGISTER_MSG);
            out.flush();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            String result = in.readLine();
            logger.info("REG Response from Bootrstrap recieved: " + result);
            return result;


        } catch (IOException e) {
            logger.error("Error in connecting to the Bootstrap Server ... "+ e.getStackTrace());
        }finally {
            if(client != null && !client.isClosed())
                try {
                    client.close();
                } catch (IOException e) {
                    logger.error("Error occured in closing the connection with the Bootsrap server .."+e.getMessage());
                }
        }
        return null;
    }
}
