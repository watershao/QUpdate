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
     *  �жϻ���Android�Ƿ��Ѿ�root�����Ƿ��ȡrootȨ�� 
     * @return
     */
    public static boolean haveRoot() { 
        if (!mHaveRoot) { 
            int ret = execRootCmdSilent("echo test"); // ͨ��ִ�в������������ 
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
     *  ִ������������� 
     * @param cmd
     * @return
     */
    public static String execRootCmd(String cmd) { 
        String result = ""; 
        DataOutputStream dos = null; 
        DataInputStream dis = null; 
        try { 
        	// ����Root�����androidϵͳ����su���� 
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
     *  ִ���������ע������ 
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
     * ��Ĭ��װ����
     * @param apkPath
     * @return �ɹ�����Success
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
        	// ����Root�����androidϵͳ����su���� 
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