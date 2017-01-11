package dhu.cst.zjm.encrypt.Models;

/**
 * Created by admin on 2017/1/7.
 */

public class EncryptInf {
    private int type;
    private String inf;
    private int state;

    public EncryptInf(String inf){
        this.inf=inf;
    }

    public EncryptInf(int type,String inf){
        this.type=type;
        this.inf=inf;
    }

    public void setState(int state){
        this.state=state;
    }

    public String getInf(){
        return inf;
    }

    public int getState(){
        return state;
    }
}
