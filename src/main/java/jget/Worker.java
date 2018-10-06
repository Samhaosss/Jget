package jget;

import jget.exception.ConnectonException;
import jget.tools.SpeedDetector;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker implements Runnable{
    // worker负责完成子任务 ---> [startPos, endPos)
    // 每个worker都会被装配一个speedDetector, worker的每次下载都会增加总任务下载量

    private final Integer workerId = ++TASKID;
    private URL urlHandler;
    private Integer startPos;
    private Integer endPos;
    private Integer currentPos;
    private SpeedDetector speedDetector;
    private String targetFileName;
    private RandomAccessFile targetFile;
    private static Logger logger = Logger.getLogger("Jget");

    private static Integer TASKID = 0;

    public Worker(URL urlHandler, Integer startPos, Integer endPos, SpeedDetector speedDetector, String targetFileName) {
        this.urlHandler = urlHandler;
        this.startPos = startPos;
        this.currentPos = startPos;
        this.endPos = endPos;
        this.speedDetector = speedDetector;
        this.targetFileName = targetFileName;
        try{
            // need open target file in every thread
            this.targetFile = new RandomAccessFile(targetFileName, "rw");
            this.targetFile.seek(startPos);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        System.out.println("worker: " + workerId + ", startPos: " + startPos + ", endPos: " + endPos );
    }

    public void run() {
        try {
            HttpURLConnection connection = (HttpURLConnection) this.urlHandler.openConnection();
            if (endPos > 0)
                connection.setRequestProperty("Range", "bytes=" + startPos + "-" + (endPos));
            doDownload(connection);
        } catch (IOException | ConnectonException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void doDownload(HttpURLConnection connection) throws ConnectonException {
        InputStream inputStream = null;
        try {
            //  don't know the reason why throws exception here, I regard it as connection Exception
            inputStream = connection.getInputStream();
            int BUFFERSIZE = 1024 * 128;
            byte[] buf = new byte[BUFFERSIZE];
            int tmp = 0 ;
            while ((tmp = inputStream.read(buf, 0, BUFFERSIZE)) != -1) {
                targetFile.write(buf,0, tmp);
                this.currentPos += tmp;
                if(speedDetector!=null)
                     speedDetector.increaseSize(tmp);
            }
        } catch (IOException e) {
            throw new ConnectonException("Set up connection failed, check url or your network ");
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                connection.disconnect();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Closing file failed");
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args){
        String[] urls = {"http","https", "ftp","ftps", "mailto", "telnet", "file", "ldap", "gopher",  "jdbc", "rmi", "jndi", "jar", "doc", "netdoc", "nfs", "verbatim", "finger", "daytime",
                "systemresource"};

            try
            {
                var tmp = new URL("https://www.geeksforgeeks.org/url-class-java-examples/");
                HttpsURLConnection connection = (HttpsURLConnection) tmp.openConnection();
                var fds = connection.getResponseCode();
                System.out.println(fds);
                InputStreamReader in = new InputStreamReader(connection.getInputStream());
                char[] buf = new char[1024];
                while (in.read(buf, 0, 1024)>0)
                    System.out.println(buf);
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
