package lk.filetributed.debug;

import lk.filetributed.debug.services.log_services;
import org.apache.log4j.Logger;

/**
 * Created by sudheera on 4/9/15.
 */
public class LogHandler implements log_services.Iface {

    private static Logger logger = Logger.getLogger(LogHandler.class);

    @Override
    public void infoLog(String log) throws org.apache.thrift.TException {
        logger.debug(log);
    }
}
