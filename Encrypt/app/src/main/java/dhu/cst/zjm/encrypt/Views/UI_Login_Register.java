package dhu.cst.zjm.encrypt.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import dhu.cst.zjm.encrypt.action.ActionsCreator;
import dhu.cst.zjm.encrypt.dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.models.User;
import dhu.cst.zjm.encrypt.R;
import dhu.cst.zjm.encrypt.stores.ChangePoint;
import dhu.cst.zjm.encrypt.stores.LoginStore;

/**
 * Created by zjm on 2016/11/6.
 */

public class UI_Login_Register extends Activity {
    private UI_Login_Register instance;
    private Dispatcher dispatcher;
    private LoginStore loginStore;
    private ActionsCreator actionsCreator;
    private ViewGroup ll_ui_register;
    private EditText et_register_name, et_register_password, et_register_confirm_password;
    private Button b_register_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_register);
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
        ll_ui_register = (ViewGroup) findViewById(R.id.ll_ui_register);
        b_register_ok = (Button) findViewById(R.id.b_register_ok);
        et_register_name = (EditText) findViewById(R.id.et_register_name);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_confirm_password = (EditText) findViewById(R.id.et_register_confirmPassword);

        b_register_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }

    /**
     * 注册
     */
    private void Register() {
        String password = getPassword();
        if (!ConfirmPassword(password)) {
            updateConfirmError();
        } else {
            String name = getName();
            User user = new User(name, password);
            actionsCreator.Register_Try(user.RegisterToJson());
        }
    }


    /**
     * 检查两次密码正确性
     *
     * @param password 密码
     * @return 检查状态
     */
    private boolean ConfirmPassword(String password) {
        String confirm = getConfirmPassword();
        if (!confirm.equals(password)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取密码
     *
     * @return 用户密码
     */
    private String getPassword() {
        return et_register_password.getText().toString();
    }

    /**
     * 获取确认密码
     *
     * @return 用户确认密码
     */
    private String getConfirmPassword() {
        return et_register_confirm_password.getText().toString();
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    private String getName() {
        return et_register_name.getText().toString();
    }

    /**
     * 两次密码不一致
     */
    private void updateConfirmError() {
        Toast.makeText(getApplicationContext(), "Confirm error!",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 更新注册信息
     *
     * @param state 注册状态
     * @param id    注册id
     */
    private void updateIsRegister(boolean state, int id) {
        String message = "";
        Snackbar snackbar = Snackbar.make(ll_ui_register, message, Snackbar.LENGTH_LONG);
        if (state == true) {
            message = "Success register : Your id is " + id;
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else {
            message = "Register failed!";
        }
        snackbar.setText(message);
        snackbar.show();
    }


    @Subscribe
    public void onRegisterStoreChange(LoginStore.LoginStoreChangeEvent event) {
        switch (event.getChangePoint()) {
            case ChangePoint.IS_REGISTER:
                updateIsRegister(event.getIsRegister(), event.getRegisterID());
                break;
        }
    }
}
