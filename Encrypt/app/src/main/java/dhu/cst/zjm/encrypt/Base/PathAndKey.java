package dhu.cst.zjm.encrypt.Base;

import java.io.File;

/**
 * Created by 10424 on 2017/1/11.
 */

public class PathAndKey {

    private static String Path = "/storage/sdcard/encrypt/";
    private static String appPath="/encrypt/";

    // RSA密钥保存路径
    private String keyPath;

    // 文件原目录及名称
    private String filePath;

    private String fileSavePath;

    // 文件加密后储存目录及名称
    private String encryptPath;

    // 文件解密后储存目录及名称
    private String decryptPath;

    // 文件压缩后储存目录及名称
    private String sendPath;

    // 文件解压后储存目录
    private String getPath;

    private String fileTempPath;

    public void setPath(String filePath) {
        this.filePath = filePath;
        confirmFile(filePath);
        this.fileTempPath = filePath + "Temp/";
        confirmFile(fileTempPath);
        this.fileSavePath = filePath + "Save/";
        confirmFile(fileSavePath);
        this.decryptPath = filePath + "Decrypt/";
        confirmFile(decryptPath);
    }

    public static void setMainPath(String path) {
        Path = path+appPath;
    }

    public static String getPath() {
        return Path;
    }

    private static void confirmFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public String getFileTempPath() {
        return fileTempPath;
    }

    public String getFileDecryptPath() {
        return decryptPath;
    }

}