package com.qrobot.update.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import android.util.Log;
/**
 *  解压Zip文件工具类
 *
 */
public class ZipTool {

	private static final int buffer = 2048;

	  public static void main(String[] args)
	    {
//	        unZip("D:\\1\\你好.zip");
	    }
	/**
	 * 解压Zip文件
	 * @param zipFilePath zip文件路径
	 * @param unzipPath 解压文件路径
	 */
    public static boolean unZip(String zipFilePath, String unzipPath)
        {
    	 int count = -1;
//         String savepath = "";

         File file = null;
         InputStream is = null;
         FileOutputStream fos = null;
         BufferedOutputStream bos = null;

//         savepath = zipFilePath.substring(0, zipFilePath.lastIndexOf(".")) + File.separator; //保存解压文件目录
//         new File(savepath).mkdir(); //创建保存目录
         
         File outFile = new File(unzipPath);
         if (!outFile.exists()) {
			outFile.mkdirs();
		 }
         
         ZipFile zipFile = null;
         try
         {
         	 zipFile = new ZipFile(zipFilePath,"gb2312"); //解决中文乱码问题
             Enumeration<?> entries = zipFile.getEntries();

             while(entries.hasMoreElements())
             {
                 byte buf[] = new byte[buffer];

                 ZipEntry entry = (ZipEntry)entries.nextElement();

                 String filename = entry.getName();
                 boolean ismkdir = false;
                 if(filename.lastIndexOf("/") != -1){ //检查此文件是否带有文件夹
                 	ismkdir = true;
                 }
//                 filename = savepath + filename;
                 filename = unzipPath +File.separator+ filename;

                 if(entry.isDirectory()){ //如果是文件夹先创建
                 	file = new File(filename);
                 	file.mkdirs();
                 	 continue;
                 }
                 file = new File(filename);
                 if(!file.exists()){ //如果是目录先创建
                 	if(ismkdir){
                 	new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建
                 	}
                 }
                 file.createNewFile(); //创建文件

                 is = zipFile.getInputStream(entry);
                 fos = new FileOutputStream(file);
                 bos = new BufferedOutputStream(fos, buffer);

                 while((count = is.read(buf)) > -1)
                 {
                     bos.write(buf, 0, count);
                 }
                 bos.flush();
                 bos.close();
                 fos.close();

                 is.close();
             }

             zipFile.close();
             Log.w("ZipTool", "unzip over");
             return true;
         }catch(IOException ioe){
             ioe.printStackTrace();
             return false;
         }finally{
            	try{
            	if(bos != null){
            		bos.close();
            	}
            	if(fos != null) {
            		fos.close();
            	}
            	if(is != null){
            		is.close();
            	}
            	if(zipFile != null){
            		zipFile.close();
            	}
            	}catch(Exception e) {
            		e.printStackTrace();
            		return false;
            	}
            }
        }
    
    
    
	/**
	 * 取得压缩包中的 文件列表(文件夹,文件自选)
	 * @param zipFileString		压缩包名字
	 * @param bContainFolder	是否包括 文件夹
	 * @param bContainFile		是否包括 文件
	 * @return
	 * @throws Exception
	 */
	public static java.util.List<java.io.File> GetFileList(String zipFileString, boolean bContainFolder, boolean bContainFile)throws Exception {
		
		android.util.Log.v("XZip", "GetFileList(String)");
		
		java.util.List<java.io.File> fileList = new java.util.ArrayList<java.io.File>();
		java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(new java.io.FileInputStream(zipFileString));
		java.util.zip.ZipEntry zipEntry;
		String szName = "";
		
		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();
		
			if (zipEntry.isDirectory()) {
		
				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				java.io.File folder = new java.io.File(szName);
				if (bContainFolder) {
					fileList.add(folder);
				}
		
			} else {
				java.io.File file = new java.io.File(szName);
				if (bContainFile) {
					fileList.add(file);
				}
			}
		}//end of while
		
		inZip.close();
		
		return fileList;
	}

	/**
	 * 返回压缩包中的文件InputStream
	 * @param zipFileString		压缩文件的名字
	 * @param fileString	解压文件的名字
	 * @return InputStream
	 * @throws Exception
	 */
	public static java.io.InputStream UpZip(String zipFileString, String fileString)throws Exception {
		android.util.Log.v("XZip", "UpZip(String, String)");
		java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(zipFileString);
		java.util.zip.ZipEntry zipEntry = zipFile.getEntry(fileString);
		
		return zipFile.getInputStream(zipEntry);

	}
	
	
	/**
	 * 解压一个压缩文档 到指定位置
	 * @param zipFileString	压缩包的名字
	 * @param outPathString	指定的路径
	 * @throws Exception
	 */
	public static void UnZipFolder(String zipFileString, String outPathString) {
		android.util.Log.v("XZip", "UnZipFolder(String, String)");
		java.util.zip.ZipInputStream inZip;
		try {
			Log.w("ZipUtil", "zipFile:"+zipFileString);
			inZip = new java.util.zip.ZipInputStream(new java.io.FileInputStream(zipFileString));
//			java.util.zip.ZipEntry zipEntry;
			InputStream is;
			org.apache.tools.zip.ZipEntry zipEntry;
			String szName = "";
			
			
			ZipFile zipFile = new ZipFile(zipFileString,"GB2312");
			Enumeration<?> entries = zipFile.getEntries();
			
			while (entries != null && entries.hasMoreElements()) {
				zipEntry = (org.apache.tools.zip.ZipEntry)entries.nextElement();
//			while ((zipEntry = inZip.getNextEntry()) != null) {
				szName = zipEntry.getName();
				Log.w("szName:", szName);
				if (zipEntry.isDirectory()) {
					
					// get the folder name of the widget
					szName = szName.substring(0, szName.length() - 1);
					java.io.File folder = new java.io.File(outPathString + java.io.File.separator + szName);
//					File parentFile = folder.getParentFile();
//					while (!parentFile.exists()) {
//						parentFile.mkdir();
//						parentFile = parentFile.getParentFile();
//					}
					Log.w("mkDir:", ""+folder.getAbsolutePath());
					folder.mkdirs();
					
				} else {
					
					java.io.File file = new java.io.File(outPathString + java.io.File.separator + szName);
					File parentFile = file.getParentFile();
					while (!parentFile.exists()) {
						parentFile.mkdir();
						Log.w("mkFile:", ""+parentFile.getAbsolutePath());
						parentFile = parentFile.getParentFile();
					}
					Log.w("zip:", outPathString + java.io.File.separator + szName+",file path:"+file.getAbsolutePath());
					file.createNewFile();
					// get the output stream of the file
					java.io.FileOutputStream out = new java.io.FileOutputStream(file);
					int len;
					byte[] buffer = new byte[1024];
					is = zipFile.getInputStream(zipEntry);
					// read (len) bytes into buffer
					while ((len = is.read(buffer)) != -1) {
						// write (len) byte from buffer at the position 0
						out.write(buffer, 0, len);
						out.flush();
					}
					out.close();
					is.close();
				}
			}//end of while
			
//			inZip.close();
			zipFile.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}//end of func
	

	/**
	 * 压缩文件,文件夹
	 * @param srcFileString	要压缩的文件/文件夹名字
	 * @param zipFileString	指定压缩的目的和名字
	 * @throws Exception
	 */
	public static void ZipFolder(String srcFileString, String zipFileString) {
		android.util.Log.v("XZip", "ZipFolder(String, String)");
		
		//创建Zip包
		java.util.zip.ZipOutputStream outZip;
		try {
			outZip = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFileString));
			//打开要输出的文件
			java.io.File file = new java.io.File(srcFileString);
			
			//压缩
			ZipFiles(file.getParent()+java.io.File.separator, file.getName(), outZip);
			
			//完成,关闭
			outZip.finish();
			outZip.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}//end of func
	
	/**
	 * 压缩文件
	 * @param folderString
	 * @param fileString
	 * @param zipOutputSteam
	 * @throws Exception
	 */
	private static void ZipFiles(String folderString, String fileString, java.util.zip.ZipOutputStream zipOutputSteam)throws Exception{
		android.util.Log.v("XZip", "ZipFiles(String, String, ZipOutputStream)");
		
		if(zipOutputSteam == null)
			return;
		
		java.io.File file = new java.io.File(folderString+fileString);
		
		//判断是不是文件
		if (file.isFile()) {

			java.util.zip.ZipEntry zipEntry =  new java.util.zip.ZipEntry(fileString);
			java.io.FileInputStream inputStream = new java.io.FileInputStream(file);
			zipOutputSteam.putNextEntry(zipEntry);
			
			int len;
			byte[] buffer = new byte[4096];
			
			while((len=inputStream.read(buffer)) != -1)
			{
				zipOutputSteam.write(buffer, 0, len);
			}
			
			zipOutputSteam.closeEntry();
		}
		else {
			
			//文件夹的方式,获取文件夹下的子文件
			String fileList[] = file.list();
			
			//如果没有子文件, 则添加进去即可
			if (fileList.length <= 0) {
				java.util.zip.ZipEntry zipEntry =  new java.util.zip.ZipEntry(fileString+java.io.File.separator);
				zipOutputSteam.putNextEntry(zipEntry);
				zipOutputSteam.closeEntry();				
			}
			
			//如果有子文件, 遍历子文件
			for (int i = 0; i < fileList.length; i++) {
				ZipFiles(folderString, fileString+java.io.File.separator+fileList[i], zipOutputSteam);
			}//end of for
	
		}//end of if
		
	}//end of func
}