package lk.filetributed.client.rpc;

import lk.filetributed.client.rpc.serviceHandlers.JoinNodeHandler;
import lk.filetributed.client.rpc.services.JoinNode;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by sudheera on 4/6/15.
 */
public class RPCServer extends Thread{

    private final int server_port;

    public RPCServer(int server_port) {
        this.server_port = server_port;
    }

    @Override
    public void run() {
        serverStart();
    }

    private void serverStart(){
        try {
            // Set port
            TServerSocket serverTransport = new TServerSocket(server_port);

            JoinNodeHandler handler = new JoinNodeHandler();
            JoinNode.Processor<JoinNode.Iface> processor = new JoinNode.Processor<JoinNode.Iface>(handler);

            TServer server = new TThreadPoolServer(
                    new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting server on port "+server_port+" ...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }


}
