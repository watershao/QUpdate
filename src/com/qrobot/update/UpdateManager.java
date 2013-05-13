package com.qrobot.update;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.qrobot.update.util.AppManager;
import com.qrobot.update.util.FileTool;
import com.qrobot.update.util.JsonTool;
import com.qrobot.update.util.MusicTool;
import com.qrobot.update.util.XMLParse;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

/**
 * 更新信息的管理
 * @author water
 *
 */
public class UpdateManager {

	private Context context;
	
	private static final String tag = "UpdateManager:";
	
	private List<UpdateFileInfo> copyList = new ArrayList<UpdateFileInfo>();
	
	private List<UpdateFileInfo> installList = new ArrayList<UpdateFileInfo>();
	
	private String newVersionInfo = "";
	
	public UpdateManager(Context context){
		this.context = context;
	}
	
	/**
	 * 返回正在运行的Qrobot应用的包名列表
	 * @return
	 */
	public List<String> getQroRunningApp(){
		List<String> qroRuningList = new ArrayList<String>();
		List<RunningAppProcessInfo> rapiList = AppManager.getRunningApps(context);
		RunningAppProcessInfo rapi = new RunningAppProcessInfo();
		for (int i = 0; i < rapiList.size(); i++) {
			rapi = rapiList.get(i);
			String[] pkgList = rapi.pkgList; 
			for (int j = 0; j < pkgList.length; j++) {
//				if(pkgList[j].contains("com.qrobot") ||pkgList[j].contains("com.qrb")){
				if(isInQrobot(pkgList[j])){	
					if (pkgList[j].equalsIgnoreCase("com.qrobot.update")) {
						continue;
					}
					Log.w(tag, "pkg:"+pkgList[j]);
					qroRuningList.add(pkgList[j]);
				}
			}
		}
		
		return qroRuningList;
	}
	
	private boolean isInQrobot(String pkgName){
		List<String> qrobotList = new XMLParse().getQrobotAppPackages();
		if (qrobotList!= null && qrobotList.size() >0) {
			
			for (int i = 0; i < qrobotList.size(); i++) {
				String qrobotPkg = qrobotList.get(i);
				if (pkgName.equalsIgnoreCase(qrobotPkg)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 停止所有正在运行的Qrobot程序
	 * @param qroRunningList 需要停止的程序列表
	 */
	public void stopQroRunningApp(List<String> qroRunningList){
		if (qroRunningList != null && qroRunningList.size() > 0) {
			String pkg = "";
			for (int i = 0; i < qroRunningList.size(); i++) {
				pkg = qroRunningList.get(i);
				Log.w(tag, "stop pkg:"+pkg);
				AppManager.forceStopPackage(context, pkg);
			}
		}
	}
	
	/**
	 * 静默安装apk
	 * @param apkPathList 待安装的apk路径列表
	 * @return 安装失败的apk路径列表，返回列表大小为0则全部安装成功
	 */
	public List<String> installApps(List<String> apkPathList){
		String apkPath = "";
		String result = "";
		List<String> failList = new ArrayList<String>();
		for (int i = 0; i < apkPathList.size(); i++) {
			apkPath = apkPathList.get(i); 
			result = AppManager.installQuietly(apkPath);
			if (!result.contains("Success")) {
				failList.add(apkPath);
			}
		}
		return failList;
	}
	
	/**
	 * 	启动app
	 * @param packageName
	 */
	public void startApp(String packageName){
		AppManager.startApp(context, packageName);
	}
	
	/**
	 * 复制文件
	 * @param sourcePath
	 * @param targetPath
	 */
	public void copyFile(String sourcePath, String targetPath){
		File sourceFile = new File(sourcePath);
		File targetFile = new File(targetPath);
		
		if (!sourceFile.exists() || !sourceFile.isFile()) {
			return;
		}
		
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		try {
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileTool.copyFile(sourceFile, targetFile);
	}
	
	/**
	 * 复制文件夹
	 * @param sourceDir
	 * @param targetDir
	 */
	public void copyDir(String sourceDir, String targetDir){
		FileTool.copyDirectory(sourceDir, targetDir);
	}
	

	/**
	 * 安装需要更新的应用程序
	 * @param updateList
	 * @param tryCount 
	 * @return
	 */
	public boolean installUpdateApps(List<UpdateFileInfo> updateList, int tryCount){
		if (updateList != null && updateList.size() > 0) {
			UpdateFileInfo ufi = new UpdateFileInfo();
			List<String> apkPathList = new ArrayList<String>();
			String apkPath;
			for (int i = 0; i < updateList.size(); i++) {
				ufi = updateList.get(i);
				apkPath = ufi.getSourceDir()+File.separator+ufi.getFileName();
				apkPathList.add(apkPath);
			}
			
			if (apkPathList.size() > 0) {
				
				while (tryCount > 0) {
					apkPathList = installApps(apkPathList);
					
					if (apkPathList == null || apkPathList.size() == 0) {
						return true;
					}
					tryCount--;
				}
				return false;
			}
		}
		return false;
	}
	
	/**
	 * copy文件到目标文件夹
	 * @param updateList
	 * @return
	 */
	public boolean copyFiles(List<UpdateFileInfo> updateList){
		if (updateList != null && updateList.size() > 0) {
			UpdateFileInfo ufi = new UpdateFileInfo();
			for (int i = 0; i < updateList.size(); i++) {
				ufi = updateList.get(i);
				if (ufi.getFileType().equalsIgnoreCase("file")) {
					printW("source:"+ufi.getSourceDir()+File.separator+ufi.getFileName()
							+"target:"+ufi.getTargetDir()+File.separator+ufi.getFileName());
					
					copyFile(ufi.getSourceDir()+File.separator+ufi.getFileName(), ufi.getTargetDir()+File.separator+ufi.getFileName());
				}
				if (ufi.getFileType().equalsIgnoreCase("directory")) {
					copyDir(ufi.getSourceDir()+File.separator+ufi.getFileName(), ufi.getTargetDir());
				}
			}
			return true;
		}
		return false;
	}
	
	
	/**
	 * 处理配置文件
	 * @param unzipPath
	 * @param configFileName
	 */
	public void dealConfigFile(String unzipPath, String configFileName) {
		List<File> configFiles = FileTool.getConfigFiles(unzipPath,
				configFileName);
		if (configFiles != null && configFiles.size() > 0) {
			try {

				File configFile;
				JSONObject jsonObject = new JSONObject();
				JSONArray jArray = new JSONArray();
				int versionCode;
				String time;
				copyList.clear();
				installList.clear();
				
				int newestVerCode = 0;
				
				for (int i = 0; i < configFiles.size(); i++) {
					configFile = configFiles.get(i);
					jsonObject = JsonTool.getJsonObject(configFile
							.getAbsolutePath());
					printW("json:"+jsonObject.toString());
					
					versionCode = jsonObject.getInt("versionCode");
					time = jsonObject.getString("time");
					jArray = jsonObject.getJSONArray("content");
					
					if (newestVerCode < versionCode) {
						newestVerCode = versionCode;
						newVersionInfo = configFile.getAbsolutePath();
					}
					
					for (int j = 0; j < jArray.length(); j++) {

						UpdateFileInfo ufi = new UpdateFileInfo();
						ufi.setVersionCode(versionCode);
						ufi.setTime(time);
						ufi.setSourceDir(configFile.getParent());
						
						
						JSONObject uJsonObject = jArray.getJSONObject(j);
						ufi.setFileName(uJsonObject.getString("fileName"));
						ufi.setFileType(uJsonObject.getString("fileType"));
						ufi.setProcessMode(uJsonObject.getString("processMode"));
						ufi.setTargetDir(uJsonObject.getString("targetDir"));
						if (ufi.getProcessMode().equalsIgnoreCase("copy")) {
							
							if (ufi.getFileType().equalsIgnoreCase("file")) {
								ufi.setSourceDir(configFile.getParent()+File.separator+"apks");
							}
							
							if (copyList != null && copyList.size() > 0) {
								boolean exist = false;
								for (int k = 0; k < copyList.size(); k++) {
									UpdateFileInfo tempUFI = copyList.get(k);
									if (tempUFI.getFileName().equalsIgnoreCase(
											ufi.getFileName())) {
										exist = true;
										
										if (tempUFI.getVersionCode() < ufi.getVersionCode()) {
											boolean remove = copyList.remove(tempUFI);

											printW("dealConfigFile copy1 fileName:"+ufi.getFileName());
											copyList.add(ufi);
										} else {
											break;
										}
									}
								}
//								printW("dealConfigFile copy exist:"+exist);
								if (!exist) {
									printW("dealConfigFile copy2 fileName:"+ufi.getFileName());
									copyList.add(ufi);
								}
							} else {
								printW("dealConfigFile copy3 fileName:"+ufi.getFileName());
								copyList.add(ufi);
							}
						}
						if (ufi.getProcessMode().equalsIgnoreCase("install")) {
							ufi.setSourceDir(configFile.getParent()+File.separator+"apks");
							if (installList != null && installList.size() > 0) {

								boolean exist = false;
								for (int k = 0; k < installList.size(); k++) {
									UpdateFileInfo tempUFI = installList.get(k);
									if (tempUFI.getFileName().equalsIgnoreCase(
											ufi.getFileName())) {
										exist = true;
										
										if (tempUFI.getVersionCode() < ufi.getVersionCode()) {
											boolean remove = installList.remove(tempUFI);
											printW("dealConfigFile install1 fileName:"+ufi.getFileName());
											installList.add(ufi);
										} else {
											break;
										}
									}
								}
//								printW("dealConfigFile install exist:"+exist);
								if (!exist) {
									printW("dealConfigFile install2 fileName:"+ufi.getFileName());
									installList.add(ufi);
								}
							} else {
								printW("dealConfigFile install3 fileName:"+ufi.getFileName());
								installList.add(ufi);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    
	/**
	 * 获取需要copy的信息列表
	 * @return
	 */
	public List<UpdateFileInfo> getCopyUpdateList(){
		return copyList;
	}
	
	/**
	 * 获取需要安装的信息列表
	 * @return
	 */
	public List<UpdateFileInfo> getInstallUpdateList(){
		return installList;
	}
	
	/**
	 * 获取最新的版本文件路径
	 * @return
	 */
	public String getNewVersionFile(){
		return newVersionInfo;
	}
	
	/**
	 * 删除文件或文件夹
	 * @param filePath
	 * @return
	 */
	public boolean deleteFile(String filePath){
		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		}
		
		boolean ret = FileTool.deleteFiles(file);
		return ret;
	}
	
	/**
	 * 恢复系统程序
	 * @param backupPath
	 * @return
	 */
	public boolean recoveryQrobot(String backupPath){
		dealConfigFile(backupPath, "readMe.txt");
		boolean bInstall = installUpdateApps(installList, 3);
		boolean bCopy = copyFiles(copyList);
		
		return bInstall && bCopy;
	}
	
	public boolean playMusic(){
		boolean bPlay = MusicTool.playMedia(MusicTool.getSongInLib(),true);
		return bPlay;
	}
	
	public void stopPlay(){
		MusicTool.stopPlay();
	}
	
	public void pausePlay(){
		MusicTool.pausePlay();
	}
	
	

    private static void printW(String msg){
    	Log.w("UpdateManager", msg);
    }
}
