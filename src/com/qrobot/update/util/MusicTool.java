package com.qrobot.update.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;


public class MusicTool {
	
	private static final String MUSIC_LIBRARY = "/sdcard/qrobot/lib/Music";
	
	private static MediaPlayer myPlayer = new MediaPlayer();
	private static boolean bLoop = false;	
	
	
	private static OnCompletionListener mediaCompletion = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {

			if (bLoop) {
				playMedia(MusicTool.getSongInLib(),bLoop);
			}else {
				myPlayer.reset();
			}
			
			
		}
	};

	private static OnErrorListener errorListener = new OnErrorListener() {
		
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// TODO Auto-generated method stub
			myPlayer.release();
			myPlayer = null;
			return false;
		}
	};
	
	
	/**
	 * 播放媒体文件
	 * @param fileName
	 * @param key
	 * @return
	 */
	public static boolean playMedia(String fileName,boolean isLoop) {

		try {
			
			File file = new File(fileName);
			Log.w("MusicTool", "fileName:"+fileName);
			if (!file.exists()) {
				return false;
			}
			if (myPlayer == null) {
				myPlayer = new MediaPlayer();
				myPlayer.setOnCompletionListener(mediaCompletion);
				myPlayer.setOnErrorListener(errorListener);
				
			}else {
				myPlayer.reset();
			}
			FileInputStream fis = new FileInputStream(file);
			myPlayer.setDataSource(fis.getFD());
			myPlayer.prepare();
			
			myPlayer.start();
			
			bLoop =isLoop;
			//音频持续时间
			int duration = myPlayer.getDuration();
//			Thread.sleep(duration+1000);
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void stopPlay(){
		if (myPlayer != null && myPlayer.isPlaying()) {
			myPlayer.stop();
			myPlayer.reset();
		}
	}
	
	public static void pausePlay(){
		if (myPlayer != null ) {
			if (myPlayer.isPlaying()) {
				myPlayer.pause();
			}else {
				myPlayer.start();
			}
		}
	}
	
	/**
	 * 获取音乐库中的音乐
	 * @return
	 */
	public static String getSongInLib(){
		File lib = new File(MUSIC_LIBRARY);
		if (!lib.exists()) {
			return null;
		}
		File[] childFiles = lib.listFiles();
		int count = 0;
		if (childFiles != null && childFiles.length > 0) {
			count = childFiles.length;
			
		}
		int rand = (int)Math.random()*count;
		String fileName = "";
		if (rand == 0) {
			rand = 1;
		}
		if (rand > count) {
			rand =count;
		}
		Log.w("MusicTool", "random:"+rand);
		if (rand < 10) {
			fileName = "00" + String.valueOf(rand)+".mp3";
		}
		if (rand < 100 && rand >10) {
			fileName = "0" + String.valueOf(rand)+".mp3";
		}
		fileName = MUSIC_LIBRARY+File.separator+fileName;
		return fileName;
	}
}
