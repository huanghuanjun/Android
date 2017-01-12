package dhu.cst.zjm.encrypt.stores;

/**
 * Created by zjm on 2016/11/7.
 */

public interface ChangePoint {
    String LOGIN_INTERNET = "login_internet";
    String IS_REGISTER_TRY_CONNECT = "is_register_try_connect";
    String IS_REGISTER = "is_register";
    String GET_USER_INF = "get_user_inf";
    String IS_MENU_TRY_CONNECT = "is_menu_try_connect";
    String MENU_CONNECT_ERROR="menu_connect_error";
    String MENU_CONNECT_MESSAGE="menu_connect_message";
    String MENU_CONNECT_CLOSE="menu_connect_close";
    String GET_MENU_FILE_LIST="get_menu_file_list";
    String GET_MENU_FILE_TYPE="get_menu_file_type";
    String UPLOAD_FILE_PROGRESS="upload_file_progress";
    String DOWNLOAD_FILE_PROGRESS="download_file_progress";
    String SET_ENCRYPT_FILE = "set_encrypt_file";
}
