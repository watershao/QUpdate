package com.qrobot.update;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.qrobot.update.check.QRClientManager;
import com.qrobot.update.check.QrModuleFileInfo;
import com.qrobot.update.check.QrModuleVerInfo;
import com.qrobot.update.database.DBOpenHelper;
import com.qrobot.update.database.VersionDB;
import com.qrobot.update.sensor.SensorManager;
import com.qrobot.update.util.JsonTool;
import com.qrobot.update.util.ZipTool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * ���³������
 * @author water
 *
 */
public class UpdateService extends Service {

	private static final String TAG = "UpdateService:";
	
	/**
	 * ����İ汾��Ϣ
	 */
	private static final String VERSION_UPDATE = "/sdcard/qrobot/update/record/update.txt";
//	private static final String VERSION_UPDATE = "/sdcard/qrobot/update.txt";
	
	private UpdateManager uManager;
	
	private QRClientManager qrClientManager;
	
	private int mOldVersion;
	
	private int mNewVersion;
	
	private List<String> zipList = new ArrayList<String>();
	
	private static final String DOWNLOAD_SAVE = "/sdcard/qrobot/update/zip";
	
	private static final String UNZIP_SAVE = "/sdcard/qrobot/update/unzip";
	private static final String UPDATE_SAVE = "/sdcard/qrobot/update/";
	
	private static final String UPDATE_TTS = "/sdcard/qrobot/update/tts/";
	
	private static final String CONFIG_FILENAME = "readMe.txt";
	
	private List<UpdateFileInfo> installList = new ArrayList<UpdateFileInfo>();
	
	private List<UpdateFileInfo> copyList = new ArrayList<UpdateFileInfo>();
	
	private static final String BACKUP_PATH = "/sdcard/qrobot/backup/";
	
	private static final String TTS_ACTION = "com.qrobot.update";
	private static final String TTS_KEY = "tts_update";
	
	private List<QrModuleVerInfo> versionList = new ArrayList<QrModuleVerInfo>();
	
	private static final String MAIN_PACKAGE_NAME = "com.qrobot.qrobotmanager";
	
	private SensorManager sensorManager;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		uManager = new UpdateManager(getApplicationContext());
		qrClientManager = new QRClientManager();
		
		sensorManager = new SensorManager(UpdateService.this);
		sensorManager.bindService();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		registReceiver();
		
		update();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(batteryChangedReceiver);
		sensorManager.unbindService();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	private void update(){
		if (!isNetworkAvailable(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), "û������", Toast.LENGTH_LONG).show();
			return;
		}
		
		new Thread(){
			public void run(){
				
				if (checkUpdate()) {
					boolean down = download(mOldVersion, mNewVersion, DOWNLOAD_SAVE);
//					boolean down =true;
					if (down) {
						uManager.playMusic();
						printW("download success");
//						zipList.add("20130327test.zip");
//						zipList.add("20130328test.zip");
						unzip(zipList, UNZIP_SAVE,3);
					}
				}}
		}.start();
	}
	
    /**
     * ��鵱ǰ�����Ƿ����
     * @param context
     * @return
     */
    private static boolean isNetworkAvailable(Context context) {
    	try{
    	
    		ConnectivityManager cm = (ConnectivityManager)context
    				.getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo netWorkInfo = cm.getActiveNetworkInfo();
    		return (netWorkInfo != null && netWorkInfo.isAvailable());//��������Ƿ����
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
	}
    
    /**
     * ��ȡ���ر���İ汾������Ϣ
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
     * ���±��ذ汾��Ϣ
     * @param newVersionCode
     * @return
     */
    private boolean updateLocalVersion(String newVersionCode){
    	uManager.copyFile(newVersionCode, VERSION_UPDATE);
    	return true;
    }
    
    
    
    /**
     * ����Ƿ���Ҫ����
     * @return
     */
    private boolean checkUpdate(){
    	String robId = sensorManager.getRobId();
    	if (robId == null) {
//			return false;
		}
    	boolean check = qrClientManager.checkToLogin(robId);
    	printW("check:"+check);
    	if (!check) {
			return false;
		}
    	
    	int serverVersion = 1;
    	List<QrModuleVerInfo> moduleVerInfos = qrClientManager.getModuleVerInfos();
    	
    	if (moduleVerInfos != null && moduleVerInfos.size() > 0) {
    		
    		versionList = moduleVerInfos;
    		
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
     * ���͸�����ʾ��������
     * @param tts
     */
    private void sendUpdateTts(String tts){
    	Intent intent = new Intent(TTS_ACTION);
    	intent.putExtra(TTS_KEY, tts);
    	sendBroadcast(intent);
    	
    }
 
    /**
     * ���ظ����ļ�
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
     * ��Handler��������������������ĵ�ǰ�̵߳���Ϣ���У�������������Ϣ���з�����Ϣ
     * ��Ϣ�����е���Ϣ�ɵ�ǰ�߳��ڲ����д���
     * ʹ��Handler����UI������Ϣ��
     */
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {			
			switch (msg.what) {
			case 1:				
				
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
				uManager.stopPlay();
				
//				saveToDB();
				saveToFile();
				
				startMainApp(MAIN_PACKAGE_NAME);
				if (bCopy) {
					
				}
				break;
			case 4:
				printW("deal config 4");
//				dealConfig();
				uManager.recoveryQrobot(BACKUP_PATH);
				
				break;	
			case -1:
				//��ʾ���ش�����Ϣ
				Toast.makeText(getApplicationContext(), R.string.error, 1).show();
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
     * ֹͣ�����������е�Qrobot����
     */
    private void stopAllRunningApps(){
    	
    	uManager.stopQroRunningApp(uManager.getQroRunningApp());
    	
    }
    
    /**
     * ����������
     * @param pkgName
     */
    private void startMainApp(String pkgName){
    	uManager.startApp(pkgName);
    }
    
    /**
     * ���������ļ�
     */
	private void dealConfig() {
		uManager.dealConfigFile(UNZIP_SAVE, CONFIG_FILENAME);
	}
    
	/**
	 * �����µİ汾��Ϣ���浽���ݿ�
	 */
	private void saveToDB(){
		
		if (versionList != null && versionList.size() > 0) {
			VersionDB versionDB = new VersionDB(getApplicationContext());
			
			for (int i = 0; i < versionList.size(); i++) {
				QrModuleVerInfo modVerInfo= versionList.get(i);
				versionDB.save(modVerInfo.getModuleID(), modVerInfo.getMinVersion(),
						modVerInfo.getNewestVersion(), modVerInfo.isTest(), modVerInfo.getReleaseDate(),
						false, modVerInfo.getVersionDesc());
				printW("saveToDB"+modVerInfo.getNewestVersion());
			}
		}
	}
	
	/**
	 * �����µİ汾��Ϣ���浽�ļ�
	 */
	private void saveToFile(){
		
		if (versionList != null && versionList.size() > 0) {

			File ttsFile = new File(UPDATE_TTS);
			if (!ttsFile.exists()) {
				ttsFile.mkdirs();
			}
			for (int i = 0; i < versionList.size(); i++) {
				QrModuleVerInfo modVerInfo= versionList.get(i);
				String fileName = String.valueOf(modVerInfo.getNewestVersion());
				String time = "��������"+modVerInfo.getReleaseDate();
				String desc = modVerInfo.getVersionDesc();
				boolean isTest = modVerInfo.isTest();
				
				String content = time + "," + desc;
				File verFile = new File(ttsFile, fileName+".txt");
				
				FileWriter fw = null;
			    BufferedWriter br = null;
			    try {
			        fw = new FileWriter(verFile);
			        //����һ���������д����
			        br = new BufferedWriter(fw);
			        br.write(content);
			        //�ѻ����е�����ȫ���Ƶ��ļ���
			        br.flush();
			    } catch (IOException e) {
			        e.printStackTrace();
			    } finally{
			        try {
			            br.close();
			            fw.close();
			        } catch (IOException e) {
			            br = null;
			            fw = null;
			        }
			    }
				

				printW("saveToFile"+modVerInfo.getNewestVersion());
			}
		}
	}
	
	private void registReceiver(){
		// �����ص������¹㲥�Ĺ�����,ֻ���ܴ���ACTION_BATTERRY_CHANGED�¼���Intent  
	    IntentFilter batteryChangedReceiverFilter = new IntentFilter();  
	    batteryChangedReceiverFilter.addAction(Intent.ACTION_BATTERY_CHANGED);  
	      
	    // ��ϵͳע��batteryChangedReceiver������������������ʵ�ּ������ֶδ�  
	    registerReceiver(batteryChangedReceiver, batteryChangedReceiverFilter);  
	 
	}
	
	private static volatile int curPower = 0;
	 /**
	  *  ���ܵ����Ϣ���µĹ㲥  
	  */
	private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {  
	    public void onReceive(Context context, Intent intent) {  
	    	// ��ؽ������������Ҳ��һ������  
            // BatteryManager.BATTERY_HEALTH_GOOD ����  
            // BatteryManager.BATTERY_HEALTH_OVERHEAT ����  
            // BatteryManager.BATTERY_HEALTH_DEAD û��  
            // BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE ����ѹ  
            // BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE δ֪����  
            int health = intent.getIntExtra("health", 0); //��ؽ������  
	           // ���״̬��������һ������  
            // BatteryManager.BATTERY_STATUS_CHARGING ��ʾ�ǳ��״̬  
            // BatteryManager.BATTERY_STATUS_DISCHARGING �ŵ���  
            // BatteryManager.BATTERY_STATUS_NOT_CHARGING δ���  
            // BatteryManager.BATTERY_STATUS_FULL �����  
		      int scale = intent.getIntExtra("scale", 100);   //�����ʱ�ٷֱ�
		      int status = intent.getIntExtra("status", 0);  //���״̬ 
              int nVoltage = intent.getIntExtra("voltage", 0); // ��صĵ�ѹ  
              int level = intent.getIntExtra("level", 0); // ��صĵ���������  
              int temperature = intent.getIntExtra("temperature", 0); // ��ص��¶�  
              curPower = (level/scale)*100;
		      // �����ڳ��  
		      if (status == BatteryManager.BATTERY_STATUS_CHARGING){  
		        printW("���ڳ��");  
		      }else {
		        printW("�뼰ʱ���"); 
		      }
	    }  
	  };  
	
    private static void printW(String msg){
    	Log.w(TAG, msg);
    }
	
    /**
     * ���ø�������
     * @param b
     */
	private void setReminder(boolean b) {  
        
        // get the AlarmManager instance   
        AlarmManager am= (AlarmManager) getSystemService(ALARM_SERVICE);  
        // create a PendingIntent that will perform a broadcast  
        PendingIntent pi= PendingIntent.getBroadcast(UpdateService.this, 0, new Intent(this,UpdateReceiver.class), 0);  
          
        if(b){  
            // just use current time as the Alarm time.   
            Calendar c=Calendar.getInstance();  
            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            c.set(Calendar.HOUR_OF_DAY, 11);
            c.set(Calendar.MINUTE, 18);
            // schedule an alarm  
            am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1000*10, pi);
            
        }  
        else{  
            // cancel current alarm  
            am.cancel(pi);  
        }  
          
    } 
}
