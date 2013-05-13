package com.qrobot.update.check;

import java.util.List;

public class QRClientManager {

	private static final String UPDATE_URL = "http://app31363.qzoneapp.com/qr";
	
	private QRClient client = null;
	
	QrResServerInfo serverInfo = null;
	
	/**
	 * ��֤����½
	 * @return
	 */
	public boolean checkToLogin(String robId){
		StringBuffer sb = new StringBuffer();
		// �ڲ��Թ����У��������ñ��������Է����������ʱ�����IP��ַҪ����Ϊ10.0.2.2
//		String url = "http://app31363.qzoneapp.com/qr";
		client = new QRClient(UPDATE_URL);
		/*String robotId = "3B6D000000005124429A9BF1000004000200";
		String sellId = "c92bf50fa7abcf033";
		HashMap mapResult = client.activeCode(0, robotId, sellId);
		*/
		
		String loginStr = "5A1B0000000000000198b1d8fcf84062ff40debe9e69af8890";
//		String loginStr = getLocalID();
		byte[] bData = QRUtil.HexString2Bytes(loginStr);
		client.setLoginData(0, bData);
		boolean ret = client.login();
		
		return ret;
	}
	
	/**
	 * ��ȡ�汾��Ϣ
	 * @return
	 */
	public List<QrModuleVerInfo> getModuleVerInfos(){
		int[] arModuleId = new int[]{2};
		List<QrModuleVerInfo> list = client.querySpecifiedSysModuleVers(5, arModuleId);
		
		return list;
	}
	
	/**
	 * ��ȡ�����ļ���Ϣ
	 * @return
	 */
	public List<QrModuleFileInfo> getModuleFileInfos(int oldVersion, int newVersion){
		List<QrModuleFileInfo> moduleFileInfos = client.querySpecifiedModuleUpdateInfo(5, 2, oldVersion, newVersion);
		
		return moduleFileInfos;
	}
	
	/**
	 * ���ظ����ļ���
	 * @param fileVer
	 * @param fileName
	 * @param startOffset
	 * @param destFile
	 * @return
	 */
	public int downloadSpecifiedModuleFile(int fileVer, String fileName,int startOffset, String destFile){
		if (serverInfo == null) {
			serverInfo = client.queryResServer();
		}
		int num = client.DownloadSpecifiedModuleFile(serverInfo.getServer(), serverInfo.getToken(), 5, 2, fileVer, fileName, startOffset, destFile);
		return num;		
	}
	
	/**
	 * ��ȡ����ʶ��
	 * @return
	 */
	private String getLocalID(){
		String localID = "";
		
		return localID;
	}
}
