package com.qrobot.update;

public class UpdateFileInfo {

	private String fileName = "";
	
	private String fileType = "";
	
	private String processMode = "";
	
	private String targetDir = "";
	
	private String sourceDir = "";
	
	private int versionCode = 0;
	
	private String time = "";

	
	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getProcessMode() {
		return processMode;
	}

	public void setProcessMode(String processMode) {
		this.processMode = processMode;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}
	
	
}
