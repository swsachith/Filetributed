package lk.filetributed.debug;

import lk.filetributed.debug.services.log_services;
import org.apache.log4j.Logger;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by sudheera on 4/9/15.
 */
public class Server {

    private static Logger logger = Logger.getLogger(Server.class);

    public static void main(String[] args) {

        Server debug_server = new Server();
        if (args.length>=1) {
            debug_server.serverStart(Integer.parseInt(args[0]));
        }else {
            debug_server.serverStart(9000);
        }
    }
    private void serverStart(int port){
        try {
            // Set port
            TServerSocket serverTransport = new TServerSocket(port);

            LogHandler handler = new LogHandler();
            log_services.Processor<log_services.Iface> processor = new log_services.Processor<log_services.Iface>(handler);

            TServer server = new TThreadPoolServer(
                    new TThreadPoolServer.Args(serverTransport).processor(processor));

            logger.info("Starting server on port " + port + " ...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
