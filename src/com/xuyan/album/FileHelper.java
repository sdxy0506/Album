package com.xuyan.album;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

/**
 * 
 * 
 */
public class FileHelper {
	private static final String BASE_PATH = "SRX";
	private static final String VOICE_PATH = "voiceFile";
	private static final String PIC_PATH = "imageFile";
	private static final String TXT_PATH = "txtFile";

	public static File getBasePath() throws IOException {
		File basePath = new File(Environment.getExternalStorageDirectory(),
				BASE_PATH);

		if (!basePath.exists()) {
			basePath.mkdirs();
		}
		return basePath;
	}

	public static File getVoiceFile() {
		File voiceFile = new File(Environment.getExternalStorageDirectory()
				.toString() + "/" + BASE_PATH + "/" + VOICE_PATH + "/");

		if (!voiceFile.exists()) {
			voiceFile.mkdirs();
		}
		return voiceFile;
	}

	public static File getPicFile() {
		File voiceFile = new File(Environment.getExternalStorageDirectory()
				.toString() + "/" + BASE_PATH + "/" + PIC_PATH + "/");

		if (!voiceFile.exists()) {
			voiceFile.mkdirs();
		}
		return voiceFile;
	}

	public static File getTxtFile() {
		File voiceFile = new File(Environment.getExternalStorageDirectory()
				.toString() + "/" + BASE_PATH + "/" + TXT_PATH + "/");

		if (!voiceFile.exists()) {
			voiceFile.mkdirs();
		}
		return voiceFile;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param dirName
	 */
	public static void MakeDir(String dirName) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			dirName = dirName + "/";
			File destDir = new File(dirName);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
		}
	}
}
