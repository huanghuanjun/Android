package dhu.cst.zjm.encrypt.Action;


import dhu.cst.zjm.encrypt.Dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.Adapter.WebAdapter.LoginAdapter;
import dhu.cst.zjm.encrypt.Adapter.WebAdapter.MenuAdapter;

/**
 * Created by admin on 2016/11/3.
 */

public class ActionsCreator {
    private static ActionsCreator instance;
    final Dispatcher dispatcher;

    ActionsCreator(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public static ActionsCreator get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new ActionsCreator(dispatcher);
        }
        return instance;
    }

    public void Login_Internet(String json) {
        LoginAdapter loginAdapter = LoginAdapter.get(dispatcher);
        loginAdapter.Login_Internet(json);
    }

    public void Register_Try_Connect() {
        LoginAdapter loginAdapter = LoginAdapter.get(dispatcher);
        loginAdapter.Register_Try_Connect();
    }

    public void Register_Try(String json) {
        LoginAdapter loginAdapter = LoginAdapter.get(dispatcher);
        loginAdapter.Register_Try(json);
    }

    public void Register_Success(String id) {
        dispatcher.dispatch(Action_Login.REGISTER_SUCCESS, Action_Login.REGISTER_SUCCESS_ID, id);
    }

    public void Register_Fail() {
        dispatcher.dispatch(Action_Login.REGISTER_FAIL);
    }

    public void Login_Internet_Resp(String infJson) {
        dispatcher.dispatch(Action_Login.LOGIN_INTERNET_RESP, Action_Login.LOGIN_INTERNET_RESP_INF_JSON, infJson);
    }


    public void Register_Try_Connect_Success() {
        dispatcher.dispatch(Action_Login.REGISTER_TRY_CONNECT_SUCCESS);
    }

    public void Register_Try_Connect_Fail() {
        dispatcher.dispatch(Action_Login.REGISTER_TRY_CONNECT_FAIL);
    }

    public void Try_Connect_Menu_Success() {
        dispatcher.dispatch(Action_Menu.TRY_CONNECT_MENU_SUCCESS);
    }

    public void Try_Connect_Menu_Fail() {
        dispatcher.dispatch(Action_Menu.TRY_CONNECT_MENU_FAIL);
    }

    public void Web_Socket_Menu_Close(int closeCode, String closeReason) {
        dispatcher.dispatch(Action_Menu.WEB_SOCKET_MENU_CLOSE, Action_Menu.WEB_SOCKET_MENU_CLOSE_CODE, closeCode, Action_Menu.WEB_SOCKET_MENU_CLOSE_REASON, closeReason);
    }

    public void Web_Socket_Menu_Error(Exception e) {
        dispatcher.dispatch(Action_Menu.WEB_SOCKET_MENU_ERROR, Action_Menu.WEB_SOCKET_MENU_ERROR_EXC, e);
    }

    public void Web_Socket_Menu_Message(String message) {
        dispatcher.dispatch(Action_Menu.WEB_SOCKET_MENU_MESSAGE, Action_Menu.WEB_SOCKET_MENU_MESSAGE_INF, message);
    }

    public void Get_User_Inf() {
        dispatcher.dispatch(Action_Menu.GET_USER_INF);
    }


    public void Decrypt_File(String json) {
        dispatcher.dispatch(Action_Menu.DECRYPT_FILE, Action_Menu.DECRYPT_FILE_INF_JSON, json);
    }

    public void Get_Menu_File_List(int id) {
        MenuAdapter menuAdapter = MenuAdapter.get(dispatcher);
        menuAdapter.Get_Menu_File_List(id);
    }

    public void Get_Menu_File_List_Success(String json) {
        dispatcher.dispatch(Action_Menu.GET_MENU_FILE_LIST_SUCCESS, Action_Menu.GET_MENU_FILE_LIST_SUCCESS_JSON, json);
    }

    public void Get_Menu_File_Type() {
        MenuAdapter menuAdapter = MenuAdapter.get(dispatcher);
        menuAdapter.Get_Menu_File_Type();
    }

    public void Get_Menu_File_Type_Success(String json) {
        dispatcher.dispatch(Action_Menu.GET_MENU_FILE_TYPE_SUCCESS, Action_Menu.GET_MENU_FILE_TYPE_SUCCESS_JSON, json);
    }

    public void Upload_File_Progress(int progress) {
        dispatcher.dispatch(Action_Menu.UPLOAD_FILE_PROGRESS, Action_Menu.UPLOAD_FILE_PROGRESS_INT, progress);
    }

    public void Download_File_Progress(int progress) {
        dispatcher.dispatch(Action_Menu.DOWNLOAD_FILE_PROGRESS, Action_Menu.DOWNLOAD_FILE_PROGRESS_INT, progress);
    }

    public void Encrypt_File(String json) {
        MenuAdapter menuAdapter = MenuAdapter.get(dispatcher);
        menuAdapter.Encrypt_File(json);
    }

    public void Set_Encrypt_Inf(String json) {
        MenuAdapter menuAdapter = MenuAdapter.get(dispatcher);
        menuAdapter.Set_Encrypt_Inf(json);
    }

    public void Set_Encrypt_Inf_Success(String json) {
        dispatcher.dispatch(Action_Menu.SET_ENCRYPT_FILE_SUCCESS, Action_Menu.SET_ENCRYPT_FILE_SUCCESS_JSON, json);
    }
}
