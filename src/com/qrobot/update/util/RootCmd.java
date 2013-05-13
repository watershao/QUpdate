package com.qrobot.update.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import android.util.Log;

public class RootCmd {
	private static final String TAG = "RootCmd"; 
    private static boolean mHaveRoot = false; 
 
    /**
     *  判断机器Android是否已经root，即是否获取root权限 
     * @return
     */
    public static boolean haveRoot() { 
        if (!mHaveRoot) { 
            int ret = execRootCmdSilent("echo test"); // 通过执行测试命令来检测 
            if (ret != -1) { 
                Log.d(TAG, "have root!"); 
                mHaveRoot = true; 
            } else { 
                Log.d(TAG, "not root!"); 
            } 
        } else { 
            Log.d(TAG, "mHaveRoot = true, have root!"); 
        } 
        return mHaveRoot; 
    } 
 
    /**
     *  执行命令并且输出结果 
     * @param cmd
     * @return
     */
    public static String execRootCmd(String cmd) { 
        String result = ""; 
        DataOutputStream dos = null; 
        DataInputStream dis = null; 
        try { 
        	// 经过Root处理的android系统即有su命令 
        	Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream()); 
            dis = new DataInputStream(p.getInputStream()); 
 
            Log.i(TAG, cmd); 
            dos.writeBytes(cmd + "\n"); 
            dos.flush(); 
            dos.writeBytes("exit\n"); 
            dos.flush(); 
            String line = null; 
            while ((line = dis.readLine()) != null) { 
                Log.d("result", line); 
                result += line; 
            } 
            p.waitFor(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            if (dos != null) { 
                try { 
                    dos.close(); 
                } catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            } 
            if (dis != null) { 
                try { 
                    dis.close(); 
                } catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            } 
        } 
        return result; 
    } 
 
    /**
     *  执行命令但不关注结果输出 
     * @param cmd
     * @return
     */
    public static int execRootCmdSilent(String cmd) { 
        int result = -1; 
        DataOutputStream dos = null; 
         
        try { 
            Process p = Runtime.getRuntime().exec("su"); 
            dos = new DataOutputStream(p.getOutputStream()); 
             
            Log.i(TAG, cmd); 
            dos.writeBytes(cmd + "\n"); 
            dos.flush(); 
            dos.writeBytes("exit\n"); 
            dos.flush(); 
            p.waitFor(); 
            result = p.exitValue(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            if (dos != null) { 
                try { 
                    dos.close(); 
                } catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            } 
        } 
        return result; 
    } 
    
    /**
     * 静默安装程序
     * @param apkPath
     * @return 成功返回Success
     */
    public static String installApkSilent(String apkPath){
    	String  cmd = "pm install -r ";
    	String result = "";
    	File apkFile = new File(apkPath);
    	if (apkFile.isFile() && apkFile.exists()) {
    		result = execRootCmd(cmd+apkPath);
		} else {
			result = "failed";
		}
    	return result;
    }
    
    public static String installApks(String cmd){
        String result = ""; 
        DataOutputStream dos = null; 
        DataInputStream dis = null; 
        try { 
        	// 经过Root处理的android系统即有su命令 
        	Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream()); 
            dis = new DataInputStream(p.getInputStream()); 
 
            Log.i(TAG, cmd); 
            dos.writeBytes("pm install -r /sdcard/qrobot/update/MainActivity2.6.apk" + "\n"); 
            dos.flush(); 
            dos.writeBytes("pm install -r /sdcard/qrobot/update/QroService.apk" + "\n"); 
            dos.flush(); 
            dos.writeBytes("exit\n"); 
            dos.flush(); 
            String line = null; 
            while ((line = dis.readLine()) != null) { 
                Log.d("result", line); 
                result += line; 
            } 
            p.waitFor(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            if (dos != null) { 
                try { 
                    dos.close(); 
                } catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            } 
            if (dis != null) { 
                try { 
                    dis.close(); 
                } catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            } 
        } 
        return result; 
    }
} 