package com.xuyan.album.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

public class FileHelper {
	private static final String BASE_PATH = "Album";
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

	public static File compressAndTransfer(File sourceFile)
			throws FileNotFoundException {

		// int desiredWidth = 480;
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inJustDecodeBounds = true;
		// BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);
		// int srcWidth = options.outWidth;
		// int srcHeight = options.outHeight;
		// if (desiredWidth > srcWidth)
		// desiredWidth = srcWidth;
		// int inSampleSize = 1;
		// while (srcWidth / 2 > desiredWidth) {
		// srcWidth /= 2;
		// srcHeight /= 2;
		// inSampleSize *= 2;
		// }
		//
		// float desiredScale = (float) desiredWidth / srcWidth;
		//
		// // Decode with inSampleSize
		// options.inJustDecodeBounds = false;
		// options.inDither = false;
		// options.inSampleSize = inSampleSize;
		// options.inScaled = false;
		// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(
		// sourceFile.getAbsolutePath(), options);

		/**
		 * 获取图片的旋转角度
		 */
		int degree = readPictureDegree(sourceFile.getAbsolutePath());

		Bitmap bitmap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
		bitmap = rotaingImageView(degree, bitmap);
		// sourceFile.delete();

		// // 旋转图片
		// sampledSrcBitmap = rotaingImageView(degree, sampledSrcBitmap);
		//
		// // Resize
		// Matrix matrix = new Matrix();
		// matrix.postScale(desiredScale, desiredScale);
		// Bitmap scaledBitmap = Bitmap.createBitmap(sampledSrcBitmap, 0, 0,
		// sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(),
		// matrix, true);
		// sampledSrcBitmap = null;

		// Save

		// File retFile = new File(tempFile, sourceFile.getName());
		FileOutputStream out = new FileOutputStream(sourceFile);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
		bitmap.recycle();
		bitmap = null;
		// scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
		// scaledBitmap.recycle();
		// scaledBitmap = null;
		return sourceFile;

	}

	/**
	 * 旋转图片
	 * 
	 * @param degree
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int degree, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}
