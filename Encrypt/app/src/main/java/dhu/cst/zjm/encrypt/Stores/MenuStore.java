package dhu.cst.zjm.encrypt.stores;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dhu.cst.zjm.encrypt.action.Action;
import dhu.cst.zjm.encrypt.action.Action_Menu;
import dhu.cst.zjm.encrypt.base_data.map_key.EncryptMap;
import dhu.cst.zjm.encrypt.dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.models.EncryptFile;
import dhu.cst.zjm.encrypt.models.EncryptType;
import dhu.cst.zjm.encrypt.models.ServerFile;
import dhu.cst.zjm.encrypt.util.encrypt.base.BaseDecrypt;

/**
 * Created by zjm on 2016/11/30.
 */

public class MenuStore extends Store {
    private static MenuStore instance;
    private boolean isMenuTryConnect;
    private int connectCloseCode;
    private String connectCloseReason;
    private Exception connectErrorExc;
    private List<ServerFile> sourceServerFileList = new ArrayList<ServerFile>();
    private List<EncryptType> sourceServerTypeList = new ArrayList<EncryptType>();
    private int uploadFileProgress;
    private int downloadFileProgress;
    private Queue<String> connectMessageInf = new LinkedList<String>();
    private String fileJson;
    private int userId;
    private int typeId;

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
                getSourceServerFileList(action);
                emitStoreChange(ChangePoint.GET_MENU_FILE_LIST);
                break;
            case Action_Menu.GET_MENU_FILE_TYPE_SUCCESS:
                getSourceServerFileType(action);
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

    /**
     * 获取用户储存上传的文件列表
     *
     * @param action
     */
    private void getSourceServerFileList(Action action) {
        String json = (String) action.getData().get(Action_Menu.GET_MENU_FILE_LIST_SUCCESS_JSON);
        Gson gson = new Gson();
        sourceServerFileList = gson.fromJson(json, new TypeToken<List<ServerFile>>() {
        }.getType());
    }

    /**
     * 获取文件加密方式列表
     *
     * @param action
     */
    private void getSourceServerFileType(Action action) {
        String json = (String) action.getData().get(Action_Menu.GET_MENU_FILE_TYPE_SUCCESS_JSON);
        Gson gson = new Gson();
        sourceServerTypeList = gson.fromJson(json, new TypeToken<List<EncryptType>>() {
        }.getType());
    }


    /**
     * 获取上传文件进度
     *
     * @param action
     */
    private void getUploadFileProgress(Action action) {
        uploadFileProgress = (int) action.getData().get(Action_Menu.UPLOAD_FILE_PROGRESS_INT);
    }

    /**
     * 获取下载文件进度
     *
     * @param action
     */
    private void getDownloadFileProgress(Action action) {
        downloadFileProgress = (int) action.getData().get(Action_Menu.DOWNLOAD_FILE_PROGRESS_INT);
    }

    /**
     * 获取加密文件的json
     *
     * @param action
     */
    private void getFileJson(Action action) {
        fileJson = (String) action.getData().get(Action_Menu.SET_ENCRYPT_FILE_SUCCESS_JSON);
    }


    /**
     * 加密文件
     *
     * @param action
     * @throws Exception
     */
    private void decryptFile(Action action) throws Exception {
        String json = (String) action.getData().get(Action_Menu.DECRYPT_FILE_INF_JSON);
        String fileName = getDecryptFileName(json);
        switch (typeId) {
            case EncryptMap.BASE:
                BaseDecrypt baseDecrypt = new BaseDecrypt(fileName);
                baseDecrypt.startDecrypt(userId);
                break;
        }
    }

    /**
     * 获取加密文件信息
     *
     * @param json EncryptFile的json
     * @return 文件名称
     */
    private String getDecryptFileName(String json) {
        Gson gson = new Gson();
        EncryptFile get = gson.fromJson(json, new TypeToken<EncryptFile>() {
        }.getType());
        String decryptFileName = get.getFileName();
        userId = get.getUserId();
        typeId = get.getTypeId();
        return decryptFileName;
    }

    /**
     * 获取webSocket关闭数据
     *
     * @param action
     */
    private void getConnectCloseDate(Action action) {
        connectCloseCode = (Integer) action.getData().get(Action_Menu.WEB_SOCKET_MENU_CLOSE_CODE);
        connectCloseReason = (String) action.getData().get(Action_Menu.WEB_SOCKET_MENU_CLOSE_REASON);
    }


    /**
     * 获取webSocket出错原因
     *
     * @param action
     */
    private void getConnectErrorException(Action action) {
        connectErrorExc = (Exception) action.getData().get(Action_Menu.WEB_SOCKET_MENU_ERROR_EXC);
    }

    /**
     * 获取webSocket服务器消息
     *
     * @param action
     */
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
            return connectCloseCode + " : " + connectCloseReason;
        }

        public Exception getConnectErrorExc() {
            return connectErrorExc;
        }

        public String getConnectMessageInf() {
            return connectMessageInf.remove();
        }

        public List<ServerFile> getSourceServerFileList() {
            return sourceServerFileList;
        }

        public List<EncryptType> getSourceServerTypeList() {
            return sourceServerTypeList;
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
