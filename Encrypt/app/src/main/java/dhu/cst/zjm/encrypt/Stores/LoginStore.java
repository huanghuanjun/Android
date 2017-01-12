package dhu.cst.zjm.encrypt.stores;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import dhu.cst.zjm.encrypt.action.Action;
import dhu.cst.zjm.encrypt.action.Action_Login;
import dhu.cst.zjm.encrypt.action.Action_Menu;
import dhu.cst.zjm.encrypt.base_data.map_key.LoginMap;
import dhu.cst.zjm.encrypt.dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.models.User;

/**
 * Created by zjm on 2016/11/3.
 */

public class LoginStore extends Store {
    private static LoginStore instance;
    private boolean isLoginInternet;
    private String notLoginReason;
    private boolean isRegisterTryConnect;
    private boolean isRegister;
    private int Register_id;
    private int Login_Internet_id;
    private String Login_Internet_name;

    protected LoginStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public static LoginStore get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new LoginStore(dispatcher);
        }
        return instance;
    }

    @Override
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onAction(Action action) {
        switch (action.getType()) {
            case Action_Menu.GET_USER_INF:
                emitStoreChange(ChangePoint.GET_USER_INF);
                break;
            case Action_Login.LOGIN_INTERNET_RESP:
                getLoginInternetInf(action);
                emitStoreChange(ChangePoint.LOGIN_INTERNET);
                break;
            case Action_Login.REGISTER_TRY_CONNECT_SUCCESS:
                isRegisterTryConnect = true;
                emitStoreChange(ChangePoint.IS_REGISTER_TRY_CONNECT);
                break;
            case Action_Login.REGISTER_TRY_CONNECT_FAIL:
                isRegisterTryConnect = false;
                emitStoreChange(ChangePoint.IS_REGISTER_TRY_CONNECT);
                break;
            case Action_Login.REGISTER_SUCCESS:
                getRegisterID(action);
                isRegister = true;
                emitStoreChange(ChangePoint.IS_REGISTER);
                break;
            case Action_Login.REGISTER_FAIL:
                isRegister = false;
                emitStoreChange(ChangePoint.IS_REGISTER);
                break;

        }
    }

    /**
     * 获取注册成功得到的id
     *
     * @param action
     */
    private void getRegisterID(Action action) {
        String id = (String) action.getData().get(Action_Login.REGISTER_SUCCESS_ID);
        Register_id = Integer.parseInt(id);
    }

    /**
     * 获取登录返回信息
     *
     * @param action
     */
    private void getLoginInternetInf(Action action) {
        String s = (String) action.getData().get(Action_Login.LOGIN_INTERNET_RESP_INF_JSON);
        if (s.equals(LoginMap.LOGIN_STATE_NON_EXISTENT)) {
            isLoginInternet = false;
            notLoginReason = s;
        } else if (s.equals(LoginMap.LOGIN_STATE_WRONG_PASSWORD)) {
            isLoginInternet = false;
            notLoginReason = s;
        } else {
            Gson gson = new Gson();
            User user = gson.fromJson(s, new TypeToken<User>() {
            }.getType());
            isLoginInternet = user.getIsLogin();
            Login_Internet_id = user.getId();
            Login_Internet_name = user.getName();
        }
    }


    @Override
    StoreChangeEvent changeEvent(String changePoint) {
        return new LoginStoreChangeEvent(changePoint);
    }

    public class LoginStoreChangeEvent implements StoreChangeEvent {

        private final String changePoint;

        public LoginStoreChangeEvent(String changePoint) {
            this.changePoint = changePoint;
        }

        public String getChangePoint() {
            return changePoint;
        }

        public boolean getIsLoginInternet() {
            return isLoginInternet;
        }

        public String getNotLoginReason() {
            return notLoginReason;
        }

        public boolean getIsRegisterTryConnect() {
            return isRegisterTryConnect;
        }

        public boolean getIsRegister() {
            return isRegister;
        }

        public int getRegisterID() {
            return Register_id;
        }

        public int getLoginInternetID() {
            return Login_Internet_id;
        }

        public String getLoginInternetName() {
            return Login_Internet_name;
        }

    }
}
