package com.qrobot.update.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XMLParse {

	/*
	 * 场景文件路径
	 */
	private final String SCENE_PATH = "/mnt/sdcard/qrobot/voiceirf/scene/";
	/*
	 * 配置文件
	 */
	private static final String CONFIGURE_PATH = "/mnt/sdcard/qrobot/voiceirf/scene/";
	private static final String CONFIGURE_NAME = "robset.xml";
	/*
	 * 存放已经安装的APK包名
	 */
	private List<String> pkgList = null;

	private XmlPullParserFactory factory = null;
	private XmlPullParser pullparser = null;

	/**
	 * 获取已安装app包名
	 */
	public List<String> getQrobotAppPackages() {
		String configureFileName = CONFIGURE_PATH + CONFIGURE_NAME;
		InputStream fileStream;
		try {
			fileStream = new FileInputStream(configureFileName);

			pkgList = parseXML(fileStream);
			fileStream.close();
			return pkgList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<String> parseXML(InputStream xmlSource) {

		if (xmlSource == null)
			return null;
		List<String> itemInfoList = new ArrayList<String>();

		int eventType = 0;
		String tag;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			pullparser = factory.newPullParser();
			pullparser.setInput(xmlSource, null);
			eventType = pullparser.getEventType();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}

		itemInfoList.clear();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				tag = pullparser.getName();
				if (tag.equalsIgnoreCase("item")) {
					String packagename = pullparser.getAttributeValue(null,
							"packagename");
//					Log.w("XMLParse", packagename);
					itemInfoList.add(packagename);
				}
			}
			try {
				eventType = pullparser.next();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return itemInfoList;

	}

}

