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
 * ���ļ����ļ��н��и��ƵȲ���
 * @author water
 *
 */
public class FileTool {

	/**
	 *  �����ļ�
	 * @param sourceFile Դ�ļ�
	 * @param targetFile Ŀ���ļ�
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile) {
		try {
			// �½��ļ����������������л���
			FileInputStream input;
			input = new FileInputStream(sourceFile);
			BufferedInputStream inBuff = new BufferedInputStream(input);
			
			// �½��ļ���������������л���
			FileOutputStream output = new FileOutputStream(targetFile);
			BufferedOutputStream outBuff = new BufferedOutputStream(output);
			
			// ��������
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// ˢ�´˻���������
			outBuff.flush();
			
			// �ر���
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
	 *  �����ļ���
	 * @param sourceDir Դ�ļ���
	 * @param targetDir Ŀ���ļ���
	 * @throws IOException
	 */
	public static void copyDirectory(String sourceDir, String targetDir) {
		// �½�Ŀ��Ŀ¼
		(new File(targetDir)).mkdirs();
		// ��ȡԴ�ļ��е�ǰ�µ��ļ���Ŀ¼
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// Դ�ļ�
				File sourceFile = file[i];
				// Ŀ���ļ�
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// ׼�����Ƶ�Դ�ļ���
				String dir1 = sourceDir + File.separator + file[i].getName();
				// ׼�����Ƶ�Ŀ���ļ���
				String dir2 = targetDir + File.separator + file[i].getName();
				copyDirectory(dir1, dir2);
			}
		}
	}
	
	/**
	 * ��ȡ�����ļ��б�
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
	 * �ݹ�ɾ���ļ����ļ���
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
