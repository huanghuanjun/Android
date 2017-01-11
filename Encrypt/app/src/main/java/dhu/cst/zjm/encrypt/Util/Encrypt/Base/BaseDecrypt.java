package dhu.cst.zjm.encrypt.Util.Encrypt.Base;

import org.greenrobot.eventbus.EventBus;

import dhu.cst.zjm.encrypt.Action.ActionsCreator;
import dhu.cst.zjm.encrypt.Base.PathAndKey;
import dhu.cst.zjm.encrypt.Dispatcher.Dispatcher;
import dhu.cst.zjm.encrypt.Util.ZipUtil;
import dhu.cst.zjm.encrypt.WebApi.BaseUrl;

import static android.R.id.message;

/**
 * Created by 10424 on 2017/1/11.
 */

public class BaseDecrypt {

    private String decryptFileName, fileName;
    private Dispatcher dispatcher;
    private ActionsCreator actionsCreator;


    public BaseDecrypt(String decryptFileName) {
        this.decryptFileName = decryptFileName;
        String[] s = decryptFileName.split("\\.");
        fileName = s[0];
        this.dispatcher = Dispatcher.get(EventBus.getDefault());
        actionsCreator = ActionsCreator.get(dispatcher);
    }

    public void startDecrypt(int userId) throws Exception {
        PathAndKey pathAndKey = new PathAndKey();
        pathAndKey.setPath(PathAndKey.getPath() + userId + "/");
        actionsCreator.Web_Socket_Menu_Message(Base.PROGRESS_CODE_1_INF);
        ZipUtil.ZipDecrypt(pathAndKey.getFileSavePath(), fileName + BaseUrl.DOWNLOAD_FILE_TYPE, pathAndKey.getFileTempPath());
        actionsCreator.Web_Socket_Menu_Message(Base.PROGRESS_CODE_2_INF);
        Decrypt decrypt = new Decrypt(pathAndKey.getFileDecryptPath(), decryptFileName, pathAndKey.getFileTempPath(), fileName);
        decrypt.loadKeystoreAndSign(pathAndKey.getFileTempPath());
        actionsCreator.Web_Socket_Menu_Message(Base.PROGRESS_CODE_3_INF);
        decrypt.publicKeyDesDecrypt();
        actionsCreator.Web_Socket_Menu_Message(Base.PROGRESS_CODE_4_INF);
        decrypt.desDecrypt();
        actionsCreator.Web_Socket_Menu_Message(Base.PROGRESS_CODE_5_INF);
        decrypt.setHashSign();
        actionsCreator.Web_Socket_Menu_Message(Base.PROGRESS_CODE_6_INF);
        decrypt.publicKeyConfirmSign();
        actionsCreator.Web_Socket_Menu_Message(Base.PROGRESS_CODE_7_INF);
    }
}
