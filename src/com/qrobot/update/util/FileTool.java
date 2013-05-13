package com.qrobot.update.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * 对文件或文件夹进行复制等操作
 * @author water
 *
 */
public class FileTool {

	/**
	 *  复制文件
	 * @param sourceFile 源文件
	 * @param targetFile 目标文件
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile) {
		try {
			// 新建文件输入流并对它进行缓冲
			FileInputStream input;
			input = new FileInputStream(sourceFile);
			BufferedInputStream inBuff = new BufferedInputStream(input);
			
			// 新建文件输出流并对它进行缓冲
			FileOutputStream output = new FileOutputStream(targetFile);
			BufferedOutputStream outBuff = new BufferedOutputStream(output);
			
			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
			
			// 关闭流
			inBuff.close();
			outBuff.close();
			output.close();
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *  复制文件夹
	 * @param sourceDir 源文件夹
	 * @param targetDir 目标文件夹
	 * @throws IOException
	 */
	public static void copyDirectory(String sourceDir, String targetDir) {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + File.separator + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + File.separator + file[i].getName();
				copyDirectory(dir1, dir2);
			}
		}
	}
	
	/**
	 * 获取配置文件列表
	 * @param unzipPath
	 * @param configFileName
	 * @return
	 */
	public static List<File> getConfigFiles(String unzipPath, String configFileName){
    	File updateFile = new File(unzipPath);
    	if (!updateFile.exists()) {
			return null;
		}
    	File[] configDirs = updateFile.listFiles();
    	List<File> configList = new ArrayList<File>();
    	if (configDirs != null && configDirs.length > 0) {
    		String tempFilePath;
    		File tempFile;
			for(int i=0;i<configDirs.length;i++){
				tempFilePath = configDirs[i].getAbsolutePath()+ File.separator+configFileName;
				Log.w("FileTool", configDirs.length+"path:"+tempFilePath);
				tempFile = new File(tempFilePath);
				if (tempFile.exists()) {
					configList.add(tempFile);
				}
			}
		}
    	return configList;
	}
	
	
	/**
	 * 递归删除文件及文件夹
	 * @param file
	 */
	public static boolean deleteFiles(File file) {
		if (!file.exists()) {
			return false;
		}
		
		boolean delete = false;
		if (file.isFile()) {
			delete = file.delete();
			return delete;
		}

		if(file.isDirectory()){
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				delete = file.delete();
				return delete;
			}
	
			for (int i = 0; i < childFiles.length; i++) {
				deleteFiles(childFiles[i]);
			}
			
			delete = file.delete();
			return delete;
		}
		
		return false;
	}
	
}
