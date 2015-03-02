package lk.filetributed.util;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class UtilsTester {
    private static Logger logger = Logger.getLogger(UtilsTester.class);


    private Utils utils;

    @Before
    public void setup() {

    }

    @Test
    public void testEncryptData() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        int clusterID = Utils.getClusterID("127.0.0.1", 3343, 3);
        Assert.assertNotNull(clusterID);
        logger.info(clusterID);

    }



}
