package dhu.cst.zjm.encrypt.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类
 * 
 * @author ZJM
 *
 */
public class ZipUtil {

	/**
	 * 压缩加密后文件夹
	 * 
	 * @param encryptPath
	 *            加密文件夹路径
	 * @param zipPath
	 *            压缩后文件路径
	 * @param zipName
	 *            压缩后文件名称
	 * @throws Exception
	 */
	public static void ZipEncrypt(String encryptPath, String zipPath, String zipName) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
		System.out.println("---------------压缩文件------------------");
		System.out.println(df.format(new Date()));
		File zipFile = new File(encryptPath);
		File toFile = new File(zipPath + zipName);
		InputStream inputStream = null;
		ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(toFile));
		if (zipFile.isDirectory()) {
			File[] files = zipFile.listFiles();
			for (int i = 0; i < files.length; ++i) {
				inputStream = new FileInputStream(files[i]);
				BufferedInputStream buffIn = new BufferedInputStream(inputStream, 4096);
				zipOutputStream.putNextEntry(new ZipEntry(zipFile.getName() + File.separator + files[i].getName()));
				System.out.println("压缩" + files[i].getName() + "文件");
				int temp = 0;
				byte data[] = new byte[4096];
				while ((temp = buffIn.read(data)) != -1) {
					zipOutputStream.write(data, 0, temp);
				}
				buffIn.close();
				inputStream.close();
			}
		}
		zipOutputStream.close();
		System.out.println("---------------压缩成功------------------");
		System.out.println(df.format(new Date()));
	}

	/**
	 * 解压加密后文件夹
	 * 
	 * @param zipPath
	 *            压缩文件路径
	 * @param zipName
	 *            压缩文件名称
	 * @param outPath
	 *            解压路径
	 * @throws Exception
	 */
	public static void ZipDecrypt(String zipPath, String zipName, String outPath) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// 设置日期格式
		System.out.println("---------------解压文件------------------");
		System.out.println(df.format(new Date()));
		File sendFile = new File(zipPath + zipName);
		File outFile = null;
		ZipFile sendZipFile = new ZipFile(sendFile);
		ZipInputStream zipInput = new ZipInputStream(new FileInputStream(sendFile));
		ZipEntry entry = null;
		InputStream input = null;
		OutputStream output = null;
		while ((entry = zipInput.getNextEntry()) != null) {
			System.out.println("解压缩" + entry.getName() + "文件");
			String name=entry.getName();
			if(entry.getName().startsWith("Encrypt\\")){
				String s[]=entry.getName().split("\\\\");{
					name=s[1];
					System.out.println(name);
				}
			}
			outFile = new File(outPath + File.separator + name);
			System.out.println(outFile.getPath());
			if (!outFile.getParentFile().exists()) {
				outFile.getParentFile().mkdir();
			}
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			input = sendZipFile.getInputStream(entry);
			BufferedInputStream buffIn = new BufferedInputStream(input, 4096);
			output = new FileOutputStream(outFile);
			int temp = 0;
			byte data[] = new byte[4096];
			while ((temp = buffIn.read(data)) != -1) {
				output.write(data, 0, temp);
			}
			buffIn.close();
			input.close();
			output.close();
		}
		System.out.println("---------------解压成功------------------");
		System.out.println(df.format(new Date()));
		System.out.println();
	}

}
