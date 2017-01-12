package dhu.cst.zjm.encrypt.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import dhu.cst.zjm.encrypt.action.ActionsCreator;
import dhu.cst.zjm.encrypt.dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.base_data.map_key.LoginMap;
import dhu.cst.zjm.encrypt.R;
import dhu.cst.zjm.encrypt.stores.ChangePoint;
import dhu.cst.zjm.encrypt.stores.LoginStore;

/**
 * Created by zjm on 2016/11/3.
 */

public class UI_Login extends Activity {

    private Dispatcher dispatcher;
    private LoginStore loginStore;
    private ActionsCreator actionsCreator;
    private UI_Login instance;
    private ViewGroup ll_ui_login;
    private Button b_register, b_login_internet;
    private EditText et_password, et_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login);
        init();
        setupView();
    }

    private void init() {
        instance = this;
        dispatcher = Dispatcher.get(EventBus.getDefault());
        actionsCreator = ActionsCreator.get(dispatcher);
        loginStore = LoginStore.get(dispatcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dispatcher.register(this);
        dispatcher.register(loginStore);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dispatcher.unregister(this);
        dispatcher.unregister(loginStore);
    }

    private void setupView() {
        ll_ui_login = (ViewGroup) findViewById(R.id.ll_ui_login);
        b_login_internet = (Button) findViewById(R.id.b_login_internet);
        b_register = (Button) findViewById(R.id.b_register);
        et_password = (EditText) findViewById(R.id.et_login_password);
        et_id = (EditText) findViewById(R.id.et_login_id);

        b_login_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InternetLogin();
            }
        });

        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

    }

    /**
     * 登录
     */
    private void InternetLogin() {
        int id = getET_Id();
        String password = getET_Password();
        if (!ConfirmUserInf(id, password)) {
            updateUserInfError();
        } else {
            actionsCreator.Login_Internet(LoginToJson(id, password));
        }
    }

    /**
     * 检查输入
     *
     * @param id 用户id
     * @param password 用户密码
     *
     * @return 检查状态
     */
    private boolean ConfirmUserInf(int id, String password) {
        if (id == -1 || password.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 输入错误
     */
    private void updateUserInfError() {
        Toast.makeText(instance, "ID or Password can not be empty!",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 转换登录信息为json
     *
     * @param id 用户id
     * @param password 用户密码
     *
     * @return Map<String, String>的json
     */
    private String LoginToJson(int id, String password) {
        Map<String, String> login = new HashMap<String, String>();
        login.put(LoginMap.LOGIN_ID_KEY, id + "");
        login.put(LoginMap.LOGIN_PASSWORD_KEY, password);
        Gson gson = new Gson();
        String json = gson.toJson(login);
        return json;
    }

    /**
     * 注册连接测试
     */
    private void Register() {
        actionsCreator.Register_Try_Connect();
    }

    /**
     * 获取密码
     *
     * @return 用户密码
     */
    private String getET_Password() {
        return et_password.getText() + "";
    }

    /**
     * 获取id
     *
     * @return 用户id
     */
    private int getET_Id() {
        String id = et_id.getText() + "";
        try {
            return Integer.parseInt(id);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 响应是否登录
     *
     * @param event
     */
    private void updateIsInternetState(LoginStore.LoginStoreChangeEvent event) {
        String message;
        boolean state = event.getIsLoginInternet();
        if (state == true) {
            Intent Menu = new Intent(instance, UI_Menu.class);
            startActivity(Menu);
        } else {
            message = event.getNotLoginReason();
            Snackbar snackbar = Snackbar.make(ll_ui_login, message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    /**
     * 响应是否注册连接成功
     *
     * @param state 连接状态
     */
    private void updateIsRegisterTryConnect(boolean state) {
        String message = "";
        Snackbar snackbar = Snackbar.make(ll_ui_login, message, Snackbar.LENGTH_LONG);
        if (state == true) {
            Intent register = new Intent(instance, UI_Login_Register.class);
            startActivity(register);
        } else {
            message = "Server connection failed!";
            snackbar.setText(message);
            snackbar.show();
        }

    }

    @Subscribe
    public void onLoginStoreChange(LoginStore.LoginStoreChangeEvent event) {
        switch (event.getChangePoint()) {
            case ChangePoint.LOGIN_INTERNET:
                updateIsInternetState(event);
                break;
            case ChangePoint.IS_REGISTER_TRY_CONNECT:
                updateIsRegisterTryConnect(event.getIsRegisterTryConnect());
                break;

        }
    }
}
