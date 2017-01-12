package dhu.cst.zjm.encrypt.util.encrypt.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.util.Date;

import org.apaches.commons.codec.binary.Base64;

import dhu.cst.zjm.encrypt.util.encrypt.base.des.DesUtil;
import dhu.cst.zjm.encrypt.util.encrypt.base.md5.Md5Util;
import dhu.cst.zjm.encrypt.util.encrypt.base.rsa.RSASignature;
import dhu.cst.zjm.encrypt.util.encrypt.base.rsa.RSAUtil;
import dhu.cst.zjm.encrypt.util.Transform;

/**
 * Created by zjm on 2017/01/07.
 */
public class Decrypt {
    /**
     * 密钥存储路径
     */
    private String keyPath;
    /**
     * 加密文件存储路径
     */
    private String encryptPath;
    /**
     * 加密文件存储名称
     */
    private String encryptName;
    /**
     * DES密钥
     */
    private String desKey;
    /**
     * MD5签名摘要
     */
    private String hashSign;
    /**
     * 解密后存储路径
     */
    private String decryptPath;
    /**
     * 解密后名称
     */
    private String decryptName;
    /**
     * 签名
     */
    private String sign;
    /**
     * 加密后DES密钥
     */
    private String desEncrypt;

    public Decrypt(String decryptPath, String decryptName, String encryptPath, String encryptName) {
        this.decryptPath = decryptPath;
        this.decryptName = decryptName;
        this.encryptPath = encryptPath;
        this.encryptName = encryptName;
        this.keyPath = encryptPath;
    }

    /**
     * 获取MD5文件摘要
     */
    public void setHashSign() {
        System.out.println("---------------Md5获取文件摘要------------------");
        DateFormat df = DateFormat.getDateInstance();// 设置日期格式
        System.out.println(df.format(new Date()));
        hashSign = Md5Util.getMd5ByFile(new File(decryptPath + decryptName));
        System.out.println(hashSign);
        System.out.println("---------------Md5获取摘要成功------------------");
        System.out.println(df.format(new Date()));
    }

    /**
     * DES解密
     *
     * @throws Exception
     */
    public void desDecrypt() throws Exception {
        System.out.println("---------------DES解密文件------------------");
        DateFormat df = DateFormat.getDateInstance();// 设置日期格式
        System.out.println(df.format(new Date()));
        byte[] decrypt = DesUtil.decrypt(Transform.File2byte(encryptPath + encryptName), desKey.getBytes());
        Transform.byte2File(decrypt, decryptPath, decryptName);
        System.out.println("---------------DES解密成功------------------");
        System.out.println(df.format(new Date()));
    }

    /**
     * RSA公钥验证签名
     *
     * @throws Exception
     */
    public boolean publicKeyConfirmSign() throws Exception {
        System.out.println("---------------RSA公钥校验签名------------------");
        DateFormat df = DateFormat.getDateInstance();// 设置日期格式
        System.out.println(df.format(new Date()));
        System.out.println("签名串：" + sign);
        boolean result=RSASignature.doCheck(hashSign, sign, RSAUtil.loadPublicKeyByFile(keyPath));
        System.out.println("验签结果：" + result);
        System.out.println(df.format(new Date()));
        System.out.println();
        return result;
    }

    /**
     * RSA公钥解密加密的DES密钥
     *
     * @throws Exception
     */
    public void publicKeyDesDecrypt() throws Exception {
        System.out.println("--------------RSA公钥解密过程-------------------");
        DateFormat df = DateFormat.getDateInstance();// 设置日期格式
        System.out.println(df.format(new Date()));

        // 公钥解密过程
        byte[] res = RSAUtil.decrypt(RSAUtil.loadPublicKeyByStr(RSAUtil.loadPublicKeyByFile(keyPath)),
                Base64.decodeBase64(desEncrypt));
        desKey = new String(res);

        System.out.println("加密：" + desEncrypt);
        System.out.println("解密：" + desKey);
        System.out.println(df.format(new Date()));
        System.out.println();
    }

    /**
     * RSA私钥解密加密的DES密钥
     *
     * @throws Exception
     */
    public void privateKeyDesDecrypt() throws Exception {
        System.out.println("--------------RSA私钥解密过程-------------------");
        DateFormat df = DateFormat.getDateInstance();// 设置日期格式
        System.out.println(df.format(new Date()));

        // 私钥解密过程
        byte[] res = RSAUtil.decrypt(RSAUtil.loadPrivateKeyByStr(RSAUtil.loadPrivateKeyByFile(keyPath)),
                Base64.decodeBase64(desEncrypt));
        desKey = new String(res);

        System.out.println("加密：" + desEncrypt);
        System.out.println("解密：" + desKey);
        System.out.println(df.format(new Date()));
        System.out.println();
    }

    /**
     * 载入签名 加密后DES密钥 RSA公钥存储路径 及加密后文件路径
     *
     * @param getPath 压缩文件路径
     * @throws Exception
     */
    public void loadKeystoreAndSign(String getPath) throws Exception {
        String zipPath = getPath;
        BufferedReader signBR = new BufferedReader(new FileReader(zipPath + "sign.sign"));
        String signReadLine = null;
        StringBuilder signSB = new StringBuilder();
        while ((signReadLine = signBR.readLine()) != null) {
            signSB.append(signReadLine);
        }
        signBR.close();
        sign = signSB.toString();

        BufferedReader desKeyBR = new BufferedReader(new FileReader(zipPath + "desKey.keystore"));
        String desKeyReadLine = null;
        StringBuilder desKeySB = new StringBuilder();
        while ((desKeyReadLine = desKeyBR.readLine()) != null) {
            desKeySB.append(desKeyReadLine);
        }
        desKeyBR.close();
        desEncrypt = desKeySB.toString();

        keyPath = zipPath;

        encryptPath = zipPath;
        File f = new File(encryptPath);
        if (f.isDirectory()) {
            File[] fs = f.listFiles();
            for (File file : fs) {
                String s = file + "";
                if (s.endsWith(".encrypt")) {
                    encryptName = file.getName();
                }
            }
        }
    }
}
