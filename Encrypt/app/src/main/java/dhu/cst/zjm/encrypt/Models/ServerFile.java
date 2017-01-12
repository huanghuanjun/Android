package dhu.cst.zjm.encrypt.models;

/**
 * Created by zjm on 2017/1/3.
 */

public class ServerFile {
    private int id;
    private String name;
    private String uploadTime;
    private String lastDownloadTime;
    private String lastEncryptTime;
    private int owner;
    private String size;
    private String path;

    public ServerFile(int id, String name,int owner, String uploadTime, String size,String path) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.uploadTime = uploadTime;
        this.size = size;
        this.path=path;
    }


    public void setLastDownloadTime(String lastDownloadTime) {
        this.lastDownloadTime = lastDownloadTime;
    }

    public void setLastEncryptTime(String lastEncryptTime){
        this.lastEncryptTime=lastEncryptTime;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public String getSize() {
        return size;
    }

    public String getPath(){
        return path;
    }
}
