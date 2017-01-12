package dhu.cst.zjm.encrypt.models;

/**
 * Created by zjm on 2017/1/8.
 */

public class EncryptType {

    private String name;
    private int id;
    private String inf;

    public EncryptType(int id, String name, String inf) {
        this.id = id;
        this.name = name;
        this.inf = inf;
    }

    public String getName() {
        return name;
    }

    public String getInf() {
        return inf;
    }

    public int getId(){
        return id;
    }
}
