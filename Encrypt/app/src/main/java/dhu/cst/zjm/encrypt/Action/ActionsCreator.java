package dhu.cst.zjm.encrypt.action;


import dhu.cst.zjm.encrypt.dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.adapter.web_adapter.LoginAdapter;
import dhu.cst.zjm.encrypt.adapter.web_adapter.MenuAdapter;

/**
 * Created by zjm on 2016/11/3.
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

    /**
     * 用户登录
     *
     * @param json Map<String, String>的json
     */
    public void Login_Internet(String json) {
        LoginAdapter loginAdapter = LoginAdapter.get(dispatcher);
        loginAdapter.Login_Internet(json);
    }

    /**
     * 登录返回
     *
     * @param infJson 登录失败原因 或 成功后User的json
     */
    public void Login_Internet_Resp(String infJson) {
        dispatcher.dispatch(Action_Login.LOGIN_INTERNET_RESP, Action_Login.LOGIN_INTERNET_RESP_INF_JSON, infJson);
    }

    /**
     * 注册前进行网络连接测试
     */
    public void Register_Try_Connect() {
        LoginAdapter loginAdapter = LoginAdapter.get(dispatcher);
        loginAdapter.Register_Try_Connect();
    }


    /**
     * 注册网络测试成功
     */
    public void Register_Try_Connect_Success() {
        dispatcher.dispatch(Action_Login.REGISTER_TRY_CONNECT_SUCCESS);
    }

    /**
     * 注册网络测试失败
     */
    public void Register_Try_Connect_Fail() {
        dispatcher.dispatch(Action_Login.REGISTER_TRY_CONNECT_FAIL);
    }


    /**
     * 注册用户
     *
     * @param json Map<String, String>的json
     */
    public void Register_Try(String json) {
        LoginAdapter loginAdapter = LoginAdapter.get(dispatcher);
        loginAdapter.Register_Try(json);
    }

    /**
     * 注册成功返回
     *
     * @param id 用户注册id
     */
    public void Register_Success(String id) {
        dispatcher.dispatch(Action_Login.REGISTER_SUCCESS, Action_Login.REGISTER_SUCCESS_ID, id);
    }

    /**
     * 注册失败返回
     */
    public void Register_Fail() {
        dispatcher.dispatch(Action_Login.REGISTER_FAIL);
    }


    /**
     * websocket 连接服务器成功
     */
    public void Try_Connect_Menu_Success() {
        dispatcher.dispatch(Action_Menu.TRY_CONNECT_MENU_SUCCESS);
    }

    /**
     * websocket 连接服务器失败
     */
    public void Try_Connect_Menu_Fail() {
        dispatcher.dispatch(Action_Menu.TRY_CONNECT_MENU_FAIL);
    }

    /**
     * websocket 服务器关闭
     *
     * @param closeCode   关闭代码
     * @param closeReason 关闭原因
     */
    public void Web_Socket_Menu_Close(int closeCode, String closeReason) {
        dispatcher.dispatch(Action_Menu.WEB_SOCKET_MENU_CLOSE, Action_Menu.WEB_SOCKET_MENU_CLOSE_CODE, closeCode, Action_Menu.WEB_SOCKET_MENU_CLOSE_REASON, closeReason);
    }

    /**
     * websocket 服务器出错
     *
     * @param e 出错原因
     */
    public void Web_Socket_Menu_Error(Exception e) {
        dispatcher.dispatch(Action_Menu.WEB_SOCKET_MENU_ERROR, Action_Menu.WEB_SOCKET_MENU_ERROR_EXC, e);
    }

    /**
     * websocket 接受服务器消息
     *
     * @param message 消息
     */
    public void Web_Socket_Menu_Message(String message) {
        dispatcher.dispatch(Action_Menu.WEB_SOCKET_MENU_MESSAGE, Action_Menu.WEB_SOCKET_MENU_MESSAGE_INF, message);
    }

    /**
     * 获取用户信息
     */
    public void Get_User_Inf() {
        dispatcher.dispatch(Action_Menu.GET_USER_INF);
    }


    /**
     * 获取用户储存上传的文件列表
     *
     * @param id 用户id
     */
    public void Get_Menu_File_List(int id) {
        MenuAdapter menuAdapter = MenuAdapter.get(dispatcher);
        menuAdapter.Get_Menu_File_List(id);
    }

    /**
     * 获取上传列表成功
     *
     * @param json List<ServerFile>的json
     */
    public void Get_Menu_File_List_Success(String json) {
        dispatcher.dispatch(Action_Menu.GET_MENU_FILE_LIST_SUCCESS, Action_Menu.GET_MENU_FILE_LIST_SUCCESS_JSON, json);
    }

    /**
     * 获取文件加密方式列表
     */
    public void Get_Menu_File_Type() {
        MenuAdapter menuAdapter = MenuAdapter.get(dispatcher);
        menuAdapter.Get_Menu_File_Type();
    }

    /**
     * 获取加密列表成功
     *
     * @param json List<EncryptType>的json
     */
    public void Get_Menu_File_Type_Success(String json) {
        dispatcher.dispatch(Action_Menu.GET_MENU_FILE_TYPE_SUCCESS, Action_Menu.GET_MENU_FILE_TYPE_SUCCESS_JSON, json);
    }

    /**
     * 更新上传文件进度
     *
     * @param progress 进度
     */
    public void Upload_File_Progress(int progress) {
        dispatcher.dispatch(Action_Menu.UPLOAD_FILE_PROGRESS, Action_Menu.UPLOAD_FILE_PROGRESS_INT, progress);
    }

    /**
     * 更新下载文件进度
     *
     * @param progress 进度
     */
    public void Download_File_Progress(int progress) {
        dispatcher.dispatch(Action_Menu.DOWNLOAD_FILE_PROGRESS, Action_Menu.DOWNLOAD_FILE_PROGRESS_INT, progress);
    }


    /**
     * 设置加密需要的额外信息
     *
     * @param json EncryptFile的json
     */
    public void Set_Encrypt_Inf(String json) {
        MenuAdapter menuAdapter = MenuAdapter.get(dispatcher);
        menuAdapter.Set_Encrypt_Inf(json);
    }

    /**
     * 设置额外信息成功
     *
     * @param json EncryptFile的json
     */
    public void Set_Encrypt_Inf_Success(String json) {
        dispatcher.dispatch(Action_Menu.SET_ENCRYPT_FILE_SUCCESS, Action_Menu.SET_ENCRYPT_FILE_SUCCESS_JSON, json);
    }

    /**
     * 加密文件
     *
     * @param json EncryptFile的json
     */
    public void Encrypt_File(String json) {
        MenuAdapter menuAdapter = MenuAdapter.get(dispatcher);
        menuAdapter.Encrypt_File(json);
    }

    /**
     * 解密文件
     *
     * @param json EncryptFile的json
     */
    public void Decrypt_File(String json) {
        dispatcher.dispatch(Action_Menu.DECRYPT_FILE, Action_Menu.DECRYPT_FILE_INF_JSON, json);
    }
}
