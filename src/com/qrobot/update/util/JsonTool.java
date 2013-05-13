package com.qrobot.update.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class JsonTool {

	/**
	 * �������ȡjson����
	 * @param VerJSONPath
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONArray getUpdateJSON(String VerJSONPath) throws ClientProtocolException, IOException, JSONException{
		StringBuilder VerJSON = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpResponse response;
		response = client.execute(new HttpGet(VerJSONPath));
		//����ɹ�
		System.out.print("���������룺");
		System.out.println(response.getStatusLine().getStatusCode());
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			Log.i("ConOK","���ӳɹ�");
			HttpEntity entity = response.getEntity();
			if(entity != null){
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"), 8192);
				String line = null;
				while((line = reader.readLine()) != null){
					VerJSON.append(line+"\n");
				}
				reader.close();
				JSONArray verJSONArray = new JSONArray(VerJSON.toString());
				if(verJSONArray.length() > 0){
//					JSONObject obj = verJSONArray.getJSONObject(0);
					return verJSONArray;
				}
			}
			
			Log.i("ContFail","��ȡJSONObjectʧ�ܣ�");
			return null;
		}
		Log.i("ConFail","����ʧ�ܣ�");
		return null;
	}
	
	/**
	 * ���ļ��л�ȡjson����
	 * @param fileName
	 * @return
	 */
	public static JSONObject getJsonObject(String fileName){
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file) ));
			String line = null;
			StringBuilder VerJSON = new StringBuilder();
			while((line = reader.readLine()) != null){
				VerJSON.append(line+"\n");
			}
			reader.close();
			JSONObject verJSON = new JSONObject(VerJSON.toString());
			if(verJSON.length() > 0){
//				JSONObject obj = verJSONArray.getJSONObject(0);
				return verJSON;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
