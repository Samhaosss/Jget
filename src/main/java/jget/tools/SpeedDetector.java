package jget.tools;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class SpeedDetector extends TimerTask {
    private long totalTime;
    private AtomicInteger downloadedSize;
    private int lastSize;
    private Long fileSize;
    private long basicStep;
    private Timer timer;

    public SpeedDetector(Long fileSize, long basicStep){
        this.totalTime = 0;
        this.downloadedSize = new AtomicInteger();
        this.lastSize = 0;
        this.fileSize = fileSize;
        this.basicStep += basicStep;
    }

    public void start(){
        if(this.timer != null){
            this.timer.cancel();
        }
        else
            this.timer = new Timer();
        this.timer.scheduleAtFixedRate(this, 0, this.basicStep);
    }

    public void stop(){
        if(this.timer != null)
            this.timer.cancel();
    }

    public int increaseSize(int size){
        return this.downloadedSize.addAndGet(size);
    }

    @Override
    public void run() {
        this.totalTime += this.basicStep;
        int diff = this.downloadedSize.get() - this.lastSize;
        this.lastSize = this.downloadedSize.get();
        float speed = (float) (diff/(1024.0*1024)/(this.basicStep/1000.0));
        String str = "Speed: " +  Float.valueOf(speed).toString().substring(0, 2) + " M/s" +
                ", Progress: " + Float.valueOf((this.lastSize/(float)this.fileSize)*100).toString().substring(0, 2) + "%";
        System.out.print(str);
        for (int i = 0; i <= str.length()-1; i++)
            System.out.print("\b");
    }

    public float stopAndGetSpeed(){
        if(this.timer != null)
            this.timer.cancel();
        return this.totalTime>0?(float)(this.downloadedSize.get()/1024.0/1024.0)/(this.totalTime/1000):0;
    }

}
