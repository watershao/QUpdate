package com.qrobot.update.check;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

public class QRClientUtil {

	public static String jsessionId = null;
	
	public static String getContent(String url, JSONObject param
			) throws Exception {
		StringBuilder sb = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();

		// �������糬ʱ����
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpPost request = new HttpPost(url);
		if (null != jsessionId)
			request.setHeader("Cookie", "JSESSIONID=" + jsessionId);
		request.addHeader("Content-Type", "application/json");
		StringEntity se = new StringEntity(param.toString());
		request.setEntity(se);

		HttpResponse response = client.execute(request);

		// �õ�cookie
		CookieStore mCookieStore = ((AbstractHttpClient) client)
				.getCookieStore();
		List<Cookie> cookies = mCookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			if ("JSESSIONID".equals(cookies.get(i).getName())) {
				jsessionId = cookies.get(i).getValue();
				break;
			}
		}

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"), 8192);
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		}

		return sb.toString();
	}

	public static int downloadContent(String url, JSONObject param,
			 String fileName, int pos) throws Exception {
		StringBuilder sb = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();

		// �������糬ʱ����
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpPost request = new HttpPost(url);

		/*if (null != jsessionId)
			request.setHeader("Cookie", "JSESSIONID=" + jsessionId);*/
		request.addHeader("Content-Type", "application/json");
		StringEntity se = new StringEntity(param.toString());
		request.setEntity(se);
		HttpResponse response = client.execute(request);

		// �õ�cookie
		/*CookieStore mCookieStore = ((AbstractHttpClient) client)
				.getCookieStore();
		List<Cookie> cookies = mCookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			if ("JSESSIONID".equals(cookies.get(i).getName())) {
				jsessionId = cookies.get(i).getValue();
				break;
			}
		}*/

		HttpEntity entity = response.getEntity();
		int count = 0;
		if (entity != null) {
			InputStream in = entity.getContent();
			// ����ѹ���ļ������
//			FileOutputStream fout = new FileOutputStream(fileName);
			RandomAccessFile objFile = new RandomAccessFile(fileName,"rwd");			
			objFile.seek(pos);
			
			// �趨���뻺�����ߴ�
			byte[] buf = new byte[1024];
			int num;
			
			while ((num = in.read(buf, 0 , 1024)) != -1) {
				objFile.write(buf, 0, num);
				count += num;
			}
			objFile.close();
		}
		return count;
	}
}
