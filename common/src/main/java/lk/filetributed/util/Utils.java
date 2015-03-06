package lk.filetributed.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class Utils {
    public static int getClusterID(String ipAddress, int port,int no_clusters){
        int result = ipAddress.hashCode();
        result = 31 * result + port;
        int clusterID = Math.abs(result%no_clusters);
        return clusterID;
    }

    public static String getMessageID() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public static Properties loadPropertyFile(String location){
        Properties properties = new Properties();
        try {

            File file = new File(location);
            FileInputStream fileInput = new FileInputStream(file);

            properties.loadFromXML(fileInput);
            fileInput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return properties;
        }
    }

    public static String[] getFileList(String path){
        FileInputStream fis = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            fis = new FileInputStream(path);
             //Construct BufferedReader from InputStreamReader
             BufferedReader br = new BufferedReader(new InputStreamReader(fis));
             String line = null;

                while ((line = br.readLine()) != null) {
                    line=line.replaceAll(";"," ").replaceAll(":"," ");
                    list.add(line.trim());
                }

        } catch (IOException e) {
            e.printStackTrace();
        }


        String[] fileArray=new String[list.size()];
        list.toArray(fileArray);
        return fileArray;
    }
}