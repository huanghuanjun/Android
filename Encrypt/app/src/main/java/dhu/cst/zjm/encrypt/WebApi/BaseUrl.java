package dhu.cst.zjm.encrypt.WebApi;

/**
 * Created by admin on 2016/11/6.
 */

public interface BaseUrl {
    String BASEHTTP = "http://";
    String BASEWEBSOCKET = "ws://";
    String BASEPORT = ":8080/Encrypt/";
    String BASEIP="192.168.31.219";
     //String BASEIP = "115.159.73.148";
    //String BASEIP = "10.202.119.28";
    //String BASEIP = "10.0.2.2";
    String WEB_SERVICE = "web_service/";
    String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    String LOGIN_INTERNET = "login_internet";
    String REGISTER_TRY_Connect = "register_try_connect";
    String REGISTER_TRY = "register_try";
    String SAVE_FILE = "save_file";
    String UPLOAD_FILE="upload_file";
    String DOWNLOAD_FILE = "download_file";
    String DOWNLOAD_FILE_TYPE = ".zip";
    String GET_MENU_FILE_LIST="get_menu_file_list";
    String GET_MENU_FILE_TYPE="get_menu_file_type";
    String ENCRYPT_FILE="encrypt_file";
    String SET_ENCRYPT_INF = "set_encrypt_inf";
}
