package lk.filetributed.client.rpc.serviceHandlers;

import lk.filetributed.client.rpc.services.JoinNode;
import org.apache.thrift.TException;

/**
 * Created by sudheera on 4/6/15.
 */
public class JoinNodeHandler implements JoinNode.Iface {

    @Override
    public String joinRequest(String ipAddress, int port, int clusterID) throws TException {
        System.out.println("join request came from "+ipAddress+" : "+port);

        return "success";
    }
}
