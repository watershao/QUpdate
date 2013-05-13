package com.qrobot.update;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qrobot.update.check.QRClientManager;
import com.qrobot.update.check.QrModuleFileInfo;
import com.qrobot.update.check.QrModuleVerInfo;
import com.qrobot.update.util.JsonTool;
import com.qrobot.update.util.ZipTool;

public class MainActivity extends Activity {

	private static final String TAG = "QroUpdate:";
	
	/**
	 * 保存的版本信息
	 */
	private static final String SHARE_PREF_NAME = "version_info";
	
	/**
	 * 保存的版本信息
	 */
	private static final String VERSION_UPDATE = "/sdcard/qrobot/update/record/update.txt";
//	private static final String VERSION_UPDATE = "/sdcard/qrobot/update.txt";
	/**
	 * 下载文件大小
	 */
	private static int downloadFileSize = 0;
	
	private UpdateManager uManager;
	
	private QRClientManager qrClientManager;
	
	private int mOldVersion;
	
	private int mNewVersion;
	
	private List<String> zipList = new ArrayList<String>();
	
	private static final String DOWNLOAD_SAVE = "/sdcard/qrobot/update/zip";
	
	private static final String UNZIP_SAVE = "/sdcard/qrobot/update/unzip";
	private static final String UPDATE_SAVE = "/sdcard/qrobot/update/";
	
	private static final String CONFIG_FILENAME = "readMe.txt";
	
	private List<UpdateFileInfo> installList = new ArrayList<UpdateFileInfo>();
	
	private List<UpdateFileInfo> copyList = new ArrayList<UpdateFileInfo>();
	
	private static final String BACKUP_PATH = "/sdcard/qrobot/backup/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		uManager = new UpdateManager(getApplicationContext());
		qrClientManager = new QRClientManager();
		
		Button updateButton = (Button)findViewById(R.id.update);
		updateButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
/*				if (!isNetworkAvailable(getApplicationContext())) {
					Toast.makeText(getApplicationContext(), "没有网络", Toast.LENGTH_LONG).show();
					return;
				}
				
				new Thread(){
					public void run(){
						
						if (checkUpdate()) {
//							boolean down = download(mOldVersion, mNewVersion, DOWNLOAD_SAVE);
							boolean down =true;
							if (down) {
								printW("download success");
								zipList.add("20130327test.zip");
								zipList.add("20130328test.zip");
								unzip(zipList, UNZIP_SAVE,3);
							}
					}}
				}.start();*/
				getApplicationContext().startService(new Intent("com.qrobot.update.UpdateService"));
			//	FileTool.copyDirectory("/sdcard/temp", "/sdcard/update");
			//	FileTool.copyFile(new File("/sdcard/Youku.apk"), new File("/sdcard/update/a.apk"));
//				AppManager.getRunningApps(getApplicationContext());
//				
//				uManager = new UpdateManager(getApplicationContext());
//				uManager.stopQroRunningApp(uManager.getQroRunningApp());
//				uManager.startApp("com.qrobot.speech");
//				
//				uManager.copyDir("/sdcard/temp", "/sdcard/update");
//				uManager.copyFile("/sdcard/Youku.apk", "/sdcard/update/b.apk");

				
				//				AppManager.getRecentTastInfos(getApplicationContext());
//				AppManager.getRunningServiceInfos(getApplicationContext());
//				AppManager.getRunningTaskInfos(getApplicationContext());
//				Log.w("install", "1 time"+System.currentTimeMillis());
//				AppManager.installQuietly("/sdcard/SpeechDemo.apk");
//				Log.w("install", "2 time"+System.currentTimeMillis());
//				AppManager.installQuietly("/sdcard/Youku.apk");
//				Log.w("install", "3 time"+System.currentTimeMillis());
//				AppManager.installQuietly("/sdcard/antutu.apk");
//				Log.w("install", "4 time"+System.currentTimeMillis());
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//				Log.w("install", "5 time"+System.currentTimeMillis());
//				AppManager.installQuiet("/sdcard/SpeechDemo.apk");
//				Log.w("install", "6 time"+System.currentTimeMillis());
//				AppManager.installQuietly("/sdcard/Youku.apk");
//				Log.w("install", "7 time"+System.currentTimeMillis());
//				AppManager.forceStopPackage(getApplicationContext(), "com.qrobot.qrobotmanager");
//				Log.w("install", "8 time"+System.currentTimeMillis());
//				AppManager.killAppProcess(1562);
//				Log.w("install", "9 time"+System.currentTimeMillis());
//				startDownload("http://192.168.1.207:8080/file/file.zip", "/sdcard/update");
			}
		});
	}

	
    /**
     * 检查当前网络是否可用
     * @param context
     * @return
     */
    private static boolean isNetworkAvailable(Context context) {
    	try{
    	
    		ConnectivityManager cm = (ConnectivityManager)context
    				.getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo netWorkInfo = cm.getActiveNetworkInfo();
    		return (netWorkInfo != null && netWorkInfo.isAvailable());//检测网络是否可用
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
	}
    
    /**
     * 获取本地保存的版本更新信息
     * @return
     */
    private int getLocalVersion(){
    	JSONObject verObject = JsonTool.getJsonObject(VERSION_UPDATE);
    	int versionCode = 1;
    	if (verObject != null && verObject.length() > 0) {
			try {
				versionCode = verObject.getInt("versionCode");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    	return versionCode;
    	
    }
    
    /**
     * 更新本地版本信息
     * @param newVersionCode
     * @return
     */
    private boolean updateLocalVersion(String newVersionCode){
    	uManager.copyFile(newVersionCode, VERSION_UPDATE);
    	return true;
    }
    
    
    
    /**
     * 检查是否需要更新
     * @return
     */
    private boolean checkUpdate(){
    	
    	boolean check = qrClientManager.checkToLogin("");
    	printW("check:"+check);
    	if (!check) {
			return false;
		}
    	
    	int serverVersion = 1;
    	List<QrModuleVerInfo> moduleVerInfos = qrClientManager.getModuleVerInfos();
    	
    	if (moduleVerInfos != null && moduleVerInfos.size() > 0) {
    		int localVersion = getLocalVersion();
    		QrModuleVerInfo moduleVerInfo = new QrModuleVerInfo();
			for (int i = 0; i < moduleVerInfos.size(); i++) {
				moduleVerInfo = moduleVerInfos.get(i);
				serverVersion = moduleVerInfo.getNewestVersion();
		    	if (serverVersion > localVersion) {
		    		mNewVersion = serverVersion;
		    		mOldVersion = localVersion;
		    		printW("oldVersion"+mOldVersion+"newVersion"+mNewVersion);
		    		return true;
				}
			}
		}
    	
    	return false;
    }
    
    /**
     * 发送更新提示到主程序
     * @param tts
     */
    private void sendUpdateTts(String tts){
    	Intent intent = new Intent("");
    	intent.putExtra("", tts);
    	sendBroadcast(intent);
    	
    }
 
    /**
     * 下载更新文件
     * @param oldVer
     * @param newVer
     * @param saveDir
     * @return
     */
	private boolean download(int oldVer, int newVer, String saveDir) {
		List<QrModuleFileInfo> modFileInfos = qrClientManager
				.getModuleFileInfos(oldVer, newVer);

		if (modFileInfos != null && modFileInfos.size() > 0) {
			File saveFile = new File(saveDir);
			if (!saveFile.exists()) {
				saveFile.mkdirs();
			}
			QrModuleFileInfo mfInfo = new QrModuleFileInfo();
			String fileName;
			int fileSize;
			int fileVer;
			for (int i = 0; i < modFileInfos.size(); i++) {
				mfInfo = modFileInfos.get(i);
				fileName = mfInfo.getFileName();
				fileSize = mfInfo.getSize();
				fileVer = mfInfo.getVersion();
				printW("download info name:" + fileName + ",size:" + fileSize
						+ ",ver:" + fileVer);
				String destFile = saveDir + File.separator + fileName;
				int num = 0;
				int startPos = 0;
				while (startPos != fileSize) {

					num = qrClientManager.downloadSpecifiedModuleFile(fileVer,
							fileName, startPos, destFile);
					startPos += num;
				}
				printW("download ret" + fileSize + fileName);

				zipList.add(fileName);
			}
			return true;
		}
		return false;
	}
    
    /**
     * 当Handler被创建会关联到创建它的当前线程的消息队列，该类用于往消息队列发送消息
     * 消息队列中的消息由当前线程内部进行处理
     * 使用Handler更新UI界面信息。
     */
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {			
			switch (msg.what) {
			case 1:				
/*				progressBar.setProgress(msg.getData().getInt("size"));
				float num = (float)progressBar.getProgress()/(float)progressBar.getMax();
				int result = (int)(num*100);
				resultView.setText(result+ "%");
				
				//显示下载成功信息
				if(progressBar.getProgress()==progressBar.getMax()){
					Toast.makeText(MainActivity.this, R.string.success, 1).show();
				}*/
				
				int size = msg.getData().getInt("size");
				if (downloadFileSize > 0 && downloadFileSize == size) {
					Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_LONG).show();
				}
				Log.w(TAG, downloadFileSize+".download size:"+size);
				break;
				
			case 2:
				printW("file download over.");
//				Thread.sleep(500);
				printW("unzip:"+System.currentTimeMillis());
				ZipTool.unZip("/sdcard/update/file.zip", "/sdcard/update");
				printW("unzip over:"+System.currentTimeMillis());
				break;
				
			case 3:
				printW("deal config 3");
				stopAllRunningApps();
				dealConfig();
				boolean bInstall = uManager.installUpdateApps(uManager.getInstallUpdateList(), 3);
				boolean bCopy = uManager.copyFiles(uManager.getCopyUpdateList());
				printW("install copy success."+bInstall+bCopy);
				if (bInstall && bCopy) {
//					updateLocalVersion(uManager.getNewVersionFile());
				}
				updateLocalVersion(uManager.getNewVersionFile());
				uManager.deleteFile(UNZIP_SAVE);
				
				if (bCopy) {
					
				}
				break;
			case 4:
				printW("deal config 4");
//				dealConfig();
				uManager.recoveryQrobot(BACKUP_PATH);
				break;	
			case -1:
				//显示下载错误信息
				Toast.makeText(MainActivity.this, R.string.error, 1).show();
				break;
			}
		}
    };
    
    
    private void unzip(final List<String> zipList, final String unzipPath ,final int tryCount){
    	new Thread(){
    		@Override
			public void run(){
    			boolean unzip = false;
    			if (zipList != null && zipList.size() > 0) {
					String zipName;
					int count = tryCount;
					for (int i = 0; i < zipList.size(); i++) {
						zipName = zipList.get(i);
						while (count > 0) {
							unzip = ZipTool.unZip(DOWNLOAD_SAVE+File.separator+zipName, UNZIP_SAVE);
							if (unzip) {
								break;
							}
							count--;
						}
					}
				}
    			printW("unzip:"+unzip);
//    			ZipTool.unZip(zipFilePath, unzipPath);
    			if (unzip) {
    				Message msg = new Message();
    				msg.what = 3;
    				handler.sendMessageDelayed(msg, 500);
				}else {
    				Message msg = new Message();
    				msg.what = 4;
    				handler.sendMessageDelayed(msg, 500);
				}
    			
    		}
    	}.start();
    }
    
    /**
     * 停止所有正在运行的Qrobot程序
     */
    private void stopAllRunningApps(){
    	
    	uManager.stopQroRunningApp(uManager.getQroRunningApp());
    	
    }
    
    /**
     * 启动主程序
     * @param pkgName
     */
    private void startMainApp(String pkgName){
    	uManager.startApp(pkgName);
    }
    
    /**
     * 处理配置文件
     */
	private void dealConfig() {
		uManager.dealConfigFile(UNZIP_SAVE, CONFIG_FILENAME);
	}
    
	
	
    private static void printW(String msg){
    	Log.w(TAG, msg);
    }
}
