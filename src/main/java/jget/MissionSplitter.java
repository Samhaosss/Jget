package jget;

import java.util.ArrayList;
import java.util.List;

public class MissionSplitter {
    private Long fileSize;
    private Integer threadNum;
    public static final int maxThreadNum = 8;
    public MissionSplitter(Long fileSize){
        this.fileSize = fileSize;
        this.threadNum = 1;
    }
    public List<Integer> splitMission(MIssionInfo missionInfo){
        this.threadNum =  this.getDynamicThradNum();
        ArrayList<Integer> startPosList = new ArrayList<Integer>(8);
        Integer blockSize = Math.toIntExact(this.fileSize / this.threadNum);
        for(int i=0; i<threadNum; i++){
            startPosList.add(blockSize*i);
        }
        missionInfo.setThreadNum(startPosList.size());
        return startPosList;
    }
    private Integer getDynamicThradNum(){
        long perfectSizePerThread = 1024*1024*5;
        long tmp = 1;
        if(this.fileSize != -1)
            tmp = this.fileSize.longValue()/perfectSizePerThread;
        // if filesize < perfectSizePerThread, need set this to 1
        this.threadNum = Math.toIntExact((tmp == 0) ? 1 : tmp);
        return threadNum > maxThreadNum ? maxThreadNum : this.threadNum;
    }
}
