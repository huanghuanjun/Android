package dhu.cst.zjm.encrypt.Views;

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

import dhu.cst.zjm.encrypt.Action.ActionsCreator;
import dhu.cst.zjm.encrypt.Dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.Models.User;
import dhu.cst.zjm.encrypt.R;
import dhu.cst.zjm.encrypt.Stores.ChangePoint;
import dhu.cst.zjm.encrypt.Stores.LoginStore;

/**
 * Created by admin on 2016/11/6.
 */

public class UI_Login_Register extends Activity {
    private UI_Login_Register instance;
    private Dispatcher dispatcher;
    private LoginStore loginStore;
    private ActionsCreator actionsCreator;


    //Ui相关
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


    private boolean ConfirmPassword(String password) {
        String confirm = getConfirmPassword();
        if (!confirm.equals(password)) {
            return false;
        } else {
            return true;
        }
    }

    private String getPassword() {
        return et_register_password.getText().toString();
    }

    private String getName() {
        return et_register_name.getText().toString();
    }

    private void updateConfirmError() {
        Toast.makeText(getApplicationContext(), "Confirm error!",
                Toast.LENGTH_SHORT).show();
    }

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


    private String getConfirmPassword() {
        return et_register_confirm_password.getText().toString();
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
