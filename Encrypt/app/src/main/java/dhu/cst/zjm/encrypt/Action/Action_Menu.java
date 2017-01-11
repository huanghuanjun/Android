package dhu.cst.zjm.encrypt.Action;

/**
 * Created by lenovo on 2016/11/30.
 */

public interface Action_Menu {
    String TRY_CONNECT_MENU_SUCCESS = "try_connect_menu_success";
    String TRY_CONNECT_MENU_FAIL = "try_connect_menu_fail";
    String WEB_SOCKET_MENU_CLOSE = "web_socket_menu_close";
    String WEB_SOCKET_MENU_CLOSE_CODE = "web_socket_menu_close_code";
    String WEB_SOCKET_MENU_CLOSE_REASON = "web_socket_menu_close_reason";
    String WEB_SOCKET_MENU_ERROR = "web_socket_menu_error";
    String WEB_SOCKET_MENU_ERROR_EXC = "web_socket_menu_error_exc";
    String WEB_SOCKET_MENU_MESSAGE = "web_socket_menu_message";
    String WEB_SOCKET_MENU_MESSAGE_INF = "web_socket_menu_message_inf";
    String GET_USER_INF = "get_user_inf";
    String DECRYPT_FILE = "decrypt_file";
    String DECRYPT_FILE_INF_JSON = "decrypt_file_inf_json";
    String GET_MENU_FILE_LIST_SUCCESS = "get_menu_file_list_success";
    String GET_MENU_FILE_LIST_SUCCESS_JSON = "get_menu_file_list_success_json";
    String UPLOAD_FILE_PROGRESS = "upload_file_proress";
    String UPLOAD_FILE_PROGRESS_INT = "upload_file_progress_int";
    String DOWNLOAD_FILE_PROGRESS = "download_file_proress";
    String DOWNLOAD_FILE_PROGRESS_INT = "download_file_progress_int";
    String GET_MENU_FILE_TYPE_SUCCESS = "get_menu_file_type_success";
    String GET_MENU_FILE_TYPE_SUCCESS_JSON = "get_menu_file_type_success_json";
    String SET_ENCRYPT_FILE_SUCCESS = "set_encrypt_file_success";
    String SET_ENCRYPT_FILE_SUCCESS_JSON = "set_encrypt_file_success_json";
}
