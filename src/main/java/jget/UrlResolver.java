package jget;

import jget.tools.FilenameResolver;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UrlResolver {
    // URL class in java lib helps a lot,
    // support http,https,ftp
    // TODO: Implements url resolve myself if time agrees

    private String url;
    private URL urlHandler;
    private static Logger logger = Logger.getLogger("Jget");

    public UrlResolver(String url) throws MalformedURLException {

        this.url = url;
        try{
            this.urlHandler = new URL(url);
        }catch (MalformedURLException e){
            logger.log(Level.SEVERE,"-- Resolve Url  Failed");
            throw e;
        }
        logger.log(Level.INFO,"-- Resolve Url OK");
    }

    public MIssionInfo cookMission(MIssionInfo mission){
        // cook mission, return cooked mission
        if(!urlHandler.getFile().endsWith(File.separator))
            mission.setFileName(FilenameResolver.resolveFileName(this.urlHandler.getFile()));
        mission.setUrl(this.url);
        mission.setUrlHandler(this.urlHandler);
        return mission;
    }

    public URL getUrlHandler() {
        return urlHandler;
    }

}
