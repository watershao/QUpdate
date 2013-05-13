package com.qrobot.update.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Process;
import android.util.Log;

/**
 * 管理app，包括停止，启动等操作
 * @author water
 *
 */
public class AppManager {

	/**
	 * 获取正在运行的程序列表
	 * @param context
	 * @return
	 */
	public static List<RunningAppProcessInfo> getRunningApps(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);      
		List<RunningAppProcessInfo> list =manager.getRunningAppProcesses();       
		RunningAppProcessInfo rap = new RunningAppProcessInfo();  
		for (int j = 0; j < list.size(); j++) {  
			rap = list.get(j);
			Log.w(j+",RunningAppProcessInfo", rap.processName + ">>"
			    		+rap.pid+">>"+rap.uid+">>"+rap.pkgList.length);   
		    if (rap.pkgList != null && rap.pkgList.length>0) {
				for (int i = 0; i < rap.pkgList.length; i++) {
					Log.w(j+">>", "pkgList:"+rap.pkgList[i]);
				}
			}
		}
		return list;
	}
	
	public static List<RecentTaskInfo> getRecentTastInfos(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);      
		List<RecentTaskInfo> list =manager.getRecentTasks(20, 0);       
		RecentTaskInfo rti = new RecentTaskInfo();  
		for (int j = 0; j < list.size(); j++) {  
			rti = list.get(j);
		    Log.w(j+",RecentTaskInfo", rti.baseIntent + ">>"+rti.origActivity+">>"
		    		+rti.id);  
		    
		}
		return list;
	}
	
	
	public static List<RunningServiceInfo> getRunningServiceInfos(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);      
		List<RunningServiceInfo> list =manager.getRunningServices(20);       
		RunningServiceInfo rsi = new RunningServiceInfo();  
		for (int j = 0; j < list.size(); j++) {  
			rsi = list.get(j);
			Log.w(j+",RunningServiceInfo", rsi.process + ">>"+rsi.clientCount+">>"
			    		+rsi.clientPackage+">>"+rsi.uid);   
		    
		}
		return list;
	}
	
	
	public static List<RunningTaskInfo> getRunningTaskInfos(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);      
		List<RunningTaskInfo> list =manager.getRunningTasks(20);       
		RunningTaskInfo rti = new RunningTaskInfo();  
		for (int j = 0; j < list.size(); j++) {  
			rti = list.get(j);
			Log.w(j+",RunningTaskInfo", rti.numActivities+">>"
			    		+rti.numRunning+">>"+rti.baseActivity+">>"+rti.description
			    		+">>"+rti.topActivity);   
		    
		}
		return list;
	}
	
	public static void killAppProcess(int pid){
		Process.killProcess(pid);
	}
	
	
	/**
     * Have the system perform a force stop of everything associated with
     * the given application package.  All processes that share its uid
     * will be killed, all services it has running stopped, all activities
     * removed, etc.  In addition, a {@link Intent#ACTION_PACKAGE_RESTARTED}
     * broadcast will be sent, so that any of its registered alarms can
     * be stopped, notifications removed, etc.
     * 
     * <p>You must hold the permission
     * {@link android.Manifest.permission#FORCE_STOP_PACKAGES} to be able to
     * call this method.
     * 
     * @param packageName The name of the package to be stopped.
     * 
     * @hide This is not available to third party applications due to
     * it allowing them to break other applications by stopping their
     * services, removing their alarms, etc.
     */
	public static void forceStopPackage(Context context, String packageName) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		Method forceStopPackage;
		try {
			forceStopPackage = manager.getClass().getMethod("forceStopPackage", new Class[] {String.class});
			forceStopPackage.invoke(manager, new Object[]{new String(packageName)});
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 静默安装apk
	 * @param apkPath
	 * @return 返回Success为成功
	 */
	public static String installQuietly(String apkPath){
		String result = "";
		DataInputStream dis = null;
		try {
			java.lang.Process process = Runtime.getRuntime().exec("pm install -r " + apkPath);
			process.getOutputStream();
			dis = new DataInputStream(process.getInputStream());
			
            String line = null; 
            while ((line = dis.readLine()) != null) { 
                Log.w("result", line); 
                result += line; 
            } 
            
            process.waitFor();
			
			Log.w("installQuietly:", apkPath+",result:"+result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return result;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return result;
		}
		
		return result;
	}
	
	
	public static String  installQuiet(String apkAbsolutePath){
		String[] args = { "pm", "install", "-r", apkAbsolutePath };  
		String result = "";  
		ProcessBuilder processBuilder = new ProcessBuilder(args);  
		java.lang.Process process = null;  
		InputStream errIs = null;  
		InputStream inIs = null;  
		try {  
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		    int read = -1;  
		    process = processBuilder.start();  
		    errIs = process.getErrorStream();  
		    while ((read = errIs.read()) != -1) {  
		        baos.write(read);  
		    }  
		    baos.write('\n');  
		    inIs = process.getInputStream();  
		    while ((read = inIs.read()) != -1) {  
		        baos.write(read);  
		    }  
		    byte[] data = baos.toByteArray();  
		    result = new String(data);  
		    
		    Log.w("installQuiet:", "result:"+result);
		} catch (IOException e) {  
		    e.printStackTrace();  
		} catch (Exception e) {  
		    e.printStackTrace();  
		} finally {  
		    try {  
		        if (errIs != null) {  
		            errIs.close();  
		        }  
		        if (inIs != null) {  
		            inIs.close();  
		        }  
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    }  
		    if (process != null) {  
		        process.destroy();  
		    }  
		}  
		return result;
	}
	
	/**
	 * 启动应用程序
	 * @param context
	 * @param packageName 应用程序包名
	 */
	public static void startApp(Context context, String packageName) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(
					packageName, 0);
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(pi.packageName);
			
			List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
			
			ResolveInfo ri = apps.iterator().next();
			if (ri != null) {
				String pkgName = ri.activityInfo.packageName;
				String className = ri.activityInfo.name;
				
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ComponentName cn = new ComponentName(pkgName, className);
				
				intent.setComponent(cn);
				context.startActivity(intent);
			}
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
