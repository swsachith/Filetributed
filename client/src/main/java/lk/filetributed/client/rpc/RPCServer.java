package lk.filetributed.client.rpc;


import lk.filetributed.client.rpc.services.services;
import org.apache.log4j.Logger;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by sudheera on 4/6/15.
 */
public class RPCServer extends Thread{

    private static Logger logger = Logger.getLogger(Client.class);

    private final int server_port;
    private Client client;

    public RPCServer(int server_port, Client client) {
        this.server_port = server_port;
        this.client=client;
    }

    @Override
    public void run() {
        serverStart();
    }

    private void serverStart(){
        try {
            // Set port
            TServerSocket serverTransport = new TServerSocket(server_port);

            services.Processor<services.Iface> processor = new services.Processor<services.Iface>(client);

            TServer server = new TThreadPoolServer(
                    new TThreadPoolServer.Args(serverTransport).processor(processor));

            logger.info("Starting server on port " + server_port + " ...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }


}
