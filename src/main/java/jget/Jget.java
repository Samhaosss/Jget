package jget;

import jget.tools.SpeedDetector;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.LinkOption;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jget  {
    private static Logger logger = Logger.getLogger("Jget");
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(6, 8, 5, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(2));

    public static void help() {
        System.out.println("Usage: java -jar jget.jar [url1] [...]");
    }

    public void dealMission(String url) {
        MIssionInfo mIssionInfo = new MIssionInfo();
        SpeedDetector globalSpeedDetector = null;
        try {
            UrlResolver urlResolver = new UrlResolver(url);
            urlResolver.cookMission(mIssionInfo);
            Connector connector = new Connector(urlResolver.getUrlHandler());
            connector.cookMission(mIssionInfo);
            if(mIssionInfo.getFileSize() != -1)
                globalSpeedDetector= new SpeedDetector(mIssionInfo.getFileSize(), 500);
            MissionSplitter missionSplitter = new MissionSplitter(mIssionInfo.getFileSize());
            List<Integer> posList = missionSplitter.splitMission(mIssionInfo);
            logger.log(Level.INFO, mIssionInfo.missionInfo());

            for (int i = 0; i < posList.size(); i++) {
                if (i + 1 < posList.size())
                    threadPoolExecutor.execute(new Worker(mIssionInfo.getUrlHandler(), posList.get(i), posList.get(i + 1), globalSpeedDetector, mIssionInfo.getTmpName()));
                else
                    threadPoolExecutor.execute(new Worker(mIssionInfo.getUrlHandler(), posList.get(i), Math.toIntExact(mIssionInfo.getFileSize()), globalSpeedDetector, mIssionInfo.getTmpName()));
            }
            if (globalSpeedDetector!=null)globalSpeedDetector.start();
            threadPoolExecutor.shutdown();
            while (threadPoolExecutor.getPoolSize() != 0) ;
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }



//        float aveSpeed = globalSpeedDetector.stopAndGetSpeed();
        String targetFile = mIssionInfo.getFilePath() + mIssionInfo.getFileName();
        File file = new File(mIssionInfo.getFilePath() + mIssionInfo.getTmpName());
        file.renameTo(new File(targetFile));
//        System.out.println("\nFinished, average speed: " + aveSpeed + " M/s");
        System.out.print("Calculating SHA-256:  ");
        try {
            Process process = Runtime.getRuntime().exec("sha256sum " + targetFile);
            InputStreamReader reader = new InputStreamReader(process.getInputStream());
            char[] buf = new char[256];
            reader.read(buf, 0, 256);
            String SHA256 = new String(buf);
            System.out.println(SHA256.split(" ")[0]);
        } catch (IOException e) {
            System.err.println("\nSeems your os doesn't have sha256sum, so calculate SHA-256 yourself");
        }
    }



    public static void main(String[] args) {
        Jget jget = new Jget();
        if (args.length < 1){
            Jget.help();
            return;
        }
        for (String i : args) {
            jget.dealMission(i);
        }
        return;
    }
}
