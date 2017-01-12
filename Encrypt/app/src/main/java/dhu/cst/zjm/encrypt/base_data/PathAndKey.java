package dhu.cst.zjm.encrypt.base_data;

import java.io.File;

/**
 * Created by zjm on 2017/1/11.
 */

public class PathAndKey {

    private static String Path = "/storage/sdcard/";
    private static String appPath="/encrypt/";


    private String filePath;

    private String fileSavePath;

    private String decryptPath;

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