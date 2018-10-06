package jget;

import java.net.URL;

// check url,connection. split name of file from url, generate tmp file name,set default store path
// all info can be set by user
public class MIssionInfo {
    // 目前仅仅支持http,https,ftp,file
    // 接受URL 使用url库对URL和连接性进行检查
    // 从URL分割出文件名字,如果URL中不存在文件名则生成临时文件名
    // 默认下载到当前目录,检查文件是否存在,如果存在则文件名末尾添加后缀
    // 下载完成前会使用以tmp结尾的文件名 下载完成后会修改
    private String url;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private Integer threadNum;
    private URL urlHandler;

    public MIssionInfo() {
        this.fileSize = -1l;
        this.threadNum = 1;
        this.urlHandler = null;
    }

    public void setUrlHandler(URL urlHandler) {
        this.urlHandler = urlHandler;
    }

    public URL getUrlHandler() {
        return urlHandler;
    }

    public String missionInfo() {
        return "\n| Url:         " + this.url + "\n" +
                "| File:       " + this.fileName + "\n" +
                "| Size:       " + (this.fileSize == -1 ? "Resolve failed" : this.fileSize) + "\n" +
                "| OutputPath: " + this.filePath + "\n" +
                "| ThreadNum:  " + this.threadNum + "\n";
//                "| BlockSize:  " + (this.fileSize>0?this.fileSize/this.threadNum:0) ;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void setThreadNum(Integer threadNum) {
        this.threadNum = threadNum;
    }


    public Integer getThreadNum() {
        return threadNum;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getTmpName() {
        String tmpName = this.fileName + "_tmp";
        return tmpName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
