package dhu.cst.zjm.encrypt.Stores;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dhu.cst.zjm.encrypt.Action.Action;
import dhu.cst.zjm.encrypt.Action.Action_Menu;
import dhu.cst.zjm.encrypt.Dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.Models.EncryptFile;
import dhu.cst.zjm.encrypt.Models.EncryptType;
import dhu.cst.zjm.encrypt.Models.ServerFile;
import dhu.cst.zjm.encrypt.Util.Encrypt.Base.BaseDecrypt;

/**
 * Created by lenovo on 2016/11/30.
 */

public class MenuStore extends Store {
    private static MenuStore instance;
    private boolean isMenuTryConnect;
    private int conncetCloseCode;
    private String connectCloseReason;
    private Exception connectErrorExc;
    private List<ServerFile> souceServerFileList = new ArrayList<ServerFile>();
    private List<EncryptType> souceServerTypeList = new ArrayList<EncryptType>();
    private int uploadFileProgress;
    private int downloadFileProgress;
    private Queue<String> connectMessageInf = new LinkedList<String>();
    private String fileJson;
    private int userId;

    protected MenuStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public static MenuStore get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new MenuStore(dispatcher);
        }
        return instance;
    }


    @Override
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onAction(Action action) {
        switch (action.getType()) {
            case Action_Menu.GET_MENU_FILE_LIST_SUCCESS:
                getSouceServerFileList(action);
                emitStoreChange(ChangePoint.GET_MENU_FILE_LIST);
                break;
            case Action_Menu.GET_MENU_FILE_TYPE_SUCCESS:
                getSouceServerFileType(action);
                emitStoreChange(ChangePoint.GET_MENU_FILE_TYPE);
                break;
            case Action_Menu.SET_ENCRYPT_FILE_SUCCESS:
                getFileJson(action);
                emitStoreChange(ChangePoint.SET_ENCRYPT_FILE);
                break;
            case Action_Menu.TRY_CONNECT_MENU_SUCCESS:
                isMenuTryConnect = true;
                emitStoreChange(ChangePoint.IS_MENU_TRY_CONNECT);
                break;
            case Action_Menu.TRY_CONNECT_MENU_FAIL:
                isMenuTryConnect = false;
                emitStoreChange(ChangePoint.IS_MENU_TRY_CONNECT);
                break;
            case Action_Menu.WEB_SOCKET_MENU_CLOSE:
                getConnectCloseDate(action);
                emitStoreChange(ChangePoint.MENU_CONNECT_CLOSE);
                break;
            case Action_Menu.WEB_SOCKET_MENU_ERROR:
                getConnectErrorException(action);
                emitStoreChange(ChangePoint.MENU_CONNECT_ERROR);
                break;
            case Action_Menu.WEB_SOCKET_MENU_MESSAGE:
                getConnectMessageInf(action);
                emitStoreChange(ChangePoint.MENU_CONNECT_MESSAGE);
                break;
            case Action_Menu.UPLOAD_FILE_PROGRESS:
                getUploadFileProgress(action);
                emitStoreChange(ChangePoint.UPLOAD_FILE_PROGRESS);
                break;
            case Action_Menu.DOWNLOAD_FILE_PROGRESS:
                getDownloadFileProgress(action);
                emitStoreChange(ChangePoint.DOWNLOAD_FILE_PROGRESS);
                break;
            case Action_Menu.DECRYPT_FILE:
                try {
                    decryptFile(action);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void getSouceServerFileList(Action action) {
        String json = (String) action.getData().get(Action_Menu.GET_MENU_FILE_LIST_SUCCESS_JSON);
        Gson gson = new Gson();
        souceServerFileList = gson.fromJson(json, new TypeToken<List<ServerFile>>() {
        }.getType());
    }

    private void getFileJson(Action action) {
        fileJson = (String) action.getData().get(Action_Menu.SET_ENCRYPT_FILE_SUCCESS_JSON);
    }

    private void getSouceServerFileType(Action action) {
        String json = (String) action.getData().get(Action_Menu.GET_MENU_FILE_TYPE_SUCCESS_JSON);
        Gson gson = new Gson();
        souceServerTypeList = gson.fromJson(json, new TypeToken<List<EncryptType>>() {
        }.getType());
    }

    private void getUploadFileProgress(Action action) {
        uploadFileProgress = (int) action.getData().get(Action_Menu.UPLOAD_FILE_PROGRESS_INT);
    }

    private void getDownloadFileProgress(Action action) {
        downloadFileProgress = (int) action.getData().get(Action_Menu.DOWNLOAD_FILE_PROGRESS_INT);
    }

    private void getConnectCloseDate(Action action) {
        conncetCloseCode = (Integer) action.getData().get(Action_Menu.WEB_SOCKET_MENU_CLOSE_CODE);
        connectCloseReason = (String) action.getData().get(Action_Menu.WEB_SOCKET_MENU_CLOSE_REASON);
    }

    private void decryptFile(Action action) throws Exception {
        String json = (String) action.getData().get(Action_Menu.DECRYPT_FILE_INF_JSON);
        String fileName = getDecryptFileName(json);
        BaseDecrypt baseDecrypt=new BaseDecrypt(fileName);
        baseDecrypt.startDecrypt(userId);
    }

    private String getDecryptFileName(String json) {
        Gson gson = new Gson();
        EncryptFile get = gson.fromJson(json, new TypeToken<EncryptFile>() {
        }.getType());
        String decryptFileName = get.getFileName();
        userId = get.getUserId();
        return decryptFileName;
    }


    private void getConnectErrorException(Action action) {
        connectErrorExc = (Exception) action.getData().get(Action_Menu.WEB_SOCKET_MENU_ERROR_EXC);
    }

    private void getConnectMessageInf(Action action) {
        connectMessageInf.add((String) action.getData().get(Action_Menu.WEB_SOCKET_MENU_MESSAGE_INF));
    }


    @Override
    StoreChangeEvent changeEvent(String changePoint) {
        return new MenuStoreChangeEvent(changePoint);
    }

    public class MenuStoreChangeEvent implements StoreChangeEvent {
        private final String changePoint;

        public MenuStoreChangeEvent(String changePoint) {
            this.changePoint = changePoint;
        }

        public String getChangePoint() {
            return changePoint;
        }

        public boolean getIsMenuTryConnect() {
            return isMenuTryConnect;
        }

        public String getConnectCloseReason() {
            return conncetCloseCode + " : " + connectCloseReason;
        }

        public Exception getConnectErrorExc() {
            return connectErrorExc;
        }

        public String getConnectMessageInf() {
            return connectMessageInf.remove();
        }

        public List<ServerFile> getSouceServerFileList() {
            return souceServerFileList;
        }

        public List<EncryptType> getSouceServerTypeList() {
            return souceServerTypeList;
        }

        public int getUploadFileProgress() {
            return uploadFileProgress;
        }

        public int getDownloadFileProgress() {
            return downloadFileProgress;
        }

        public String getEncryptFileJson() {
            return fileJson;
        }
    }
}
