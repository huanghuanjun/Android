package dhu.cst.zjm.encrypt.models;

/**
 * Created by zjm on 2017/1/10.
 */

public class EncryptFile {
    private int userId;
    private int fileId;
    private int typeId;
    private String exInf;
    private String fileName;

    public EncryptFile(int userId,int fileId,int typeId){
        this.userId=userId;
        this.fileId=fileId;
        this.typeId=typeId;
    }

    public void setFileName(String fileName){
        this.fileName=fileName;
    }

    public String getFileName(){
        return fileName;
    }

    public void setExInf(String exInf){
        this.exInf=exInf;
    }

    public int getUserId(){
        return userId;
    }

    public int getFileId(){
        return fileId;
    }

    public int getTypeId(){
        return typeId;
    }

    public String getExInf(){
        return exInf;
    }

}
