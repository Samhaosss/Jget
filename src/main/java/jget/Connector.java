package jget;

import jget.tools.FilenameResolver;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connector {
    private URL urlHandler;
    private static Logger logger=Logger.getLogger("Jget");

    public Connector(URL urlHandler){
        this.urlHandler = urlHandler;
    }

    public MIssionInfo cookMission(MIssionInfo missionInfo) throws IOException {
        try{
            URLConnection connection = this.urlHandler.openConnection();
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U;" +
                            " Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
            // TODO: ADD OPT HERE
            connection.connect();
            if(connection instanceof HttpsURLConnection
                    || connection instanceof HttpURLConnection){
                // ignore https
                connection = (HttpURLConnection)connection;
//                if(!connection.getHeaderField("Location").equals(this.urlHandler.toString()))
//                {
//                    missionInfo.setFileName(FilenameResolver.resolveFileName(
//                            connection.getHeaderField("Location")
//                    ));
//                }
                missionInfo.setFileSize(connection.getContentLengthLong());
            }
            else{
                // TODO: handle ftp
            }
        }catch (IOException e ){
            logger.log(Level.SEVERE,"Connection set up failed");
            throw e;
        }
        return missionInfo;
    }

}
