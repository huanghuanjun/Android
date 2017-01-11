package dhu.cst.zjm.encrypt.Models;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import dhu.cst.zjm.encrypt.Base.MapKey.LoginMap;

/**
 * Created by admin on 2016/11/3.
 */

public class User {
    private int id = 0;
    private String name;
    private String password;
    private boolean isLogin;

    public User() {

    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String RegisterToJson() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(LoginMap.REGISTER_ID_KEY, id + "");
        map.put(LoginMap.REGISTER_NAME_KEY, name);
        map.put(LoginMap.REGISTER_PASSWORD_KEY, password);
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    public void setIsLogin(boolean state) {
        isLogin = state;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
