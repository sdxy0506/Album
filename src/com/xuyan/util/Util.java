package com.xuyan.util;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

/**
 * @author xuyan 2013-11-26
 * 
 */
public class Util {
	public static final int REQUEST_IMAGE_FILE = 1;
	public static final int REQUEST_IMAGE_CAMERA = 0;
	public static final int REQUEST_IMAGE_PAGER = 2;
	public static final int MAX_PHOTOS = 9;// 能选择的图片的最大数量
	public static final String SELECT_PIC = "selected";// 传递已选择照片的key
	public static final String POSITION_PIC = "position";// 当前图片的位置
	public static final String PUT_BAIDU = "http://bcs.duapp.com/xalbum?";
	public static final String USER_NAME = "13000000000";
	public static final String PASSWORD = "123456";

	/**
	 * 获取图片所在文件夹名称
	 * 
	 * @param path
	 */
	public static String getDir(String path) {
		String subString = path.substring(0, path.lastIndexOf('/'));
		return subString.substring(subString.lastIndexOf('/') + 1,
				subString.length());
	}

	/**
	 * 获得手机相册列表
	 * 
	 * @param context
	 */
	public static ArrayList<Album> getAlbums(Context context) {
		ArrayList<Album> albums = new ArrayList<Album>();
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		Cursor cursor = contentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);

		cursor.moveToFirst();
		int fileNum = cursor.getCount();

		for (int counter = 0; counter < fileNum; counter++) {
			String path = cursor.getString(cursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			// 获取路径中文件的目录
			String file_dir = getDir(path);

			// 判断该目录是否已经存在于albums中，如果存在，则不添加到albums中；不存在则添加。
			boolean in_albums = false;// 默认不存在于albums中
			for (Album temp_album : albums) {
				if (temp_album.mName.equals(file_dir)) {
					// 存在于albums中
					in_albums = true;
					break;
				}
			}

			if (!in_albums) {
				Album album = new Album();
				album.mName = getDir(path);
				album.mNum = "(" + getPicNum(context, album.mName) + ")";
				album.mCoverUrl = path;
				albums.add(album);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return albums;
	}

	/**
	 * 获得相册中相片的数量
	 * 
	 * @param context
	 * @param album_file_dir
	 *            文件夹名字
	 * @return photo_num 相片数量
	 */
	public static int getPicNum(Context context, String album_file_dir) {
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		Cursor cursor = contentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		int fileNum = cursor.getCount();

		int photo_num = 0;
		for (int counter = 0; counter < fileNum; counter++) {
			Log.w("tag",
					"---file is:"
							+ cursor.getString(cursor
									.getColumnIndex(MediaStore.Video.Media.DATA)));
			String path = cursor.getString(cursor
					.getColumnIndex(MediaStore.Video.Media.DATA));
			// 获取路径中文件的目录
			String file_dir = getDir(path);

			if (album_file_dir.equals(file_dir))
				photo_num++;
			cursor.moveToNext();
		}
		cursor.close();
		return photo_num;
	}

	/**
	 * 根据传入的相册名称，读取相册中的图片名称
	 * 
	 * @param context
	 * @param album_dir
	 *            相册名称
	 * @return 图片名称的集合
	 */
	public static ArrayList<String> getPhotos(Context context, String album_dir) {
		ArrayList<String> photos = new ArrayList<String>();
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		Cursor cursor = contentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		int fileNum = cursor.getCount();

		for (int counter = 0; counter < fileNum; counter++) {
			Log.w("tag",
					"---file is:"
							+ cursor.getString(cursor
									.getColumnIndex(MediaStore.Video.Media.DATA)));
			String path = cursor.getString(cursor
					.getColumnIndex(MediaStore.Video.Media.DATA));
			// 获取路径中文件的目录
			String file_dir = getDir(path);
			if (file_dir.equals(album_dir))
				photos.add(path);
			cursor.moveToNext();
		}
		cursor.close();

		return photos;
	}
}
