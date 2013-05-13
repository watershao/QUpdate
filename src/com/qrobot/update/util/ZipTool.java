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
 *  ��ѹZip�ļ�������
 *
 */
public class ZipTool {

	private static final int buffer = 2048;

	  public static void main(String[] args)
	    {
//	        unZip("D:\\1\\���.zip");
	    }
	/**
	 * ��ѹZip�ļ�
	 * @param zipFilePath zip�ļ�·��
	 * @param unzipPath ��ѹ�ļ�·��
	 */
    public static boolean unZip(String zipFilePath, String unzipPath)
        {
    	 int count = -1;
//         String savepath = "";

         File file = null;
         InputStream is = null;
         FileOutputStream fos = null;
         BufferedOutputStream bos = null;

//         savepath = zipFilePath.substring(0, zipFilePath.lastIndexOf(".")) + File.separator; //�����ѹ�ļ�Ŀ¼
//         new File(savepath).mkdir(); //��������Ŀ¼
         
         File outFile = new File(unzipPath);
         if (!outFile.exists()) {
			outFile.mkdirs();
		 }
         
         ZipFile zipFile = null;
         try
         {
         	 zipFile = new ZipFile(zipFilePath,"gb2312"); //���������������
             Enumeration<?> entries = zipFile.getEntries();

             while(entries.hasMoreElements())
             {
                 byte buf[] = new byte[buffer];

                 ZipEntry entry = (ZipEntry)entries.nextElement();

                 String filename = entry.getName();
                 boolean ismkdir = false;
                 if(filename.lastIndexOf("/") != -1){ //�����ļ��Ƿ�����ļ���
                 	ismkdir = true;
                 }
//                 filename = savepath + filename;
                 filename = unzipPath +File.separator+ filename;

                 if(entry.isDirectory()){ //������ļ����ȴ���
                 	file = new File(filename);
                 	file.mkdirs();
                 	 continue;
                 }
                 file = new File(filename);
                 if(!file.exists()){ //�����Ŀ¼�ȴ���
                 	if(ismkdir){
                 	new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //Ŀ¼�ȴ���
                 	}
                 }
                 file.createNewFile(); //�����ļ�

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
	 * ȡ��ѹ�����е� �ļ��б�(�ļ���,�ļ���ѡ)
	 * @param zipFileString		ѹ��������
	 * @param bContainFolder	�Ƿ���� �ļ���
	 * @param bContainFile		�Ƿ���� �ļ�
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
	 * ����ѹ�����е��ļ�InputStream
	 * @param zipFileString		ѹ���ļ�������
	 * @param fileString	��ѹ�ļ�������
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
	 * ��ѹһ��ѹ���ĵ� ��ָ��λ��
	 * @param zipFileString	ѹ����������
	 * @param outPathString	ָ����·��
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
	 * ѹ���ļ�,�ļ���
	 * @param srcFileString	Ҫѹ�����ļ�/�ļ�������
	 * @param zipFileString	ָ��ѹ����Ŀ�ĺ�����
	 * @throws Exception
	 */
	public static void ZipFolder(String srcFileString, String zipFileString) {
		android.util.Log.v("XZip", "ZipFolder(String, String)");
		
		//����Zip��
		java.util.zip.ZipOutputStream outZip;
		try {
			outZip = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFileString));
			//��Ҫ������ļ�
			java.io.File file = new java.io.File(srcFileString);
			
			//ѹ��
			ZipFiles(file.getParent()+java.io.File.separator, file.getName(), outZip);
			
			//���,�ر�
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
	 * ѹ���ļ�
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
		
		//�ж��ǲ����ļ�
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
			
			//�ļ��еķ�ʽ,��ȡ�ļ����µ����ļ�
			String fileList[] = file.list();
			
			//���û�����ļ�, ����ӽ�ȥ����
			if (fileList.length <= 0) {
				java.util.zip.ZipEntry zipEntry =  new java.util.zip.ZipEntry(fileString+java.io.File.separator);
				zipOutputSteam.putNextEntry(zipEntry);
				zipOutputSteam.closeEntry();				
			}
			
			//��������ļ�, �������ļ�
			for (int i = 0; i < fileList.length; i++) {
				ZipFiles(folderString, fileString+java.io.File.separator+fileList[i], zipOutputSteam);
			}//end of for
	
		}//end of if
		
	}//end of func
}