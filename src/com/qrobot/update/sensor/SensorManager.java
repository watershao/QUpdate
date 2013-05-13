package com.qrobot.update.sensor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

public class SensorManager {

	private static final String SENSOR_ACTION = "";
	
	private Context mContext;
	
	private QrobotSensorService qSensorService = null;

	public SensorManager() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public SensorManager(Context context){
		mContext = context;
	}
	
	private ServiceConnection sensorConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			qSensorService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			qSensorService = QrobotSensorService.Stub.asInterface(service);
		}
	};
	
	/**
	 * 绑定服务
	 */
	public void bindService(){
		mContext.bindService(new Intent(SENSOR_ACTION), sensorConnection, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * 解除绑定
	 */
	public void unbindService(){
		mContext.unbindService(sensorConnection);
	}
	
	/**
	 * 获取小Q id
	 */
	public String getRobId(){
		if (qSensorService != null) {
			try {
				byte[] id = qSensorService.lktID();
				String idStr = new String(id);
				return idStr;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
