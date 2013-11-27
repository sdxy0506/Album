/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.xuyan.album;

import java.util.ArrayList;

import android.R.integer;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public final class Constants {
	public static final int REQUEST_IMAGE_FILE = 1;
	public static final int REQUEST_IMAGE_CAMERA = 0;
	public static final int MAX_PHOTOS = 4;
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	// RoundedBitmapDisplayer 设置圆角的大小
	public static DisplayImageOptions image_display_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
			.cacheOnDisc(true).build();

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	/**
	 * 获得手机相册列表
	 * 
	 * @param context
	 * @return
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
			Log.w("tag",
					"---file is:"
							+ cursor.getString(cursor
									.getColumnIndex(MediaStore.Video.Media.DATA)));
			String path = cursor.getString(cursor
					.getColumnIndex(MediaStore.Video.Media.DATA));
			// 获取路径中文件的目录
			String file_dir = Util.getDir(path);

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
				album.mName = Util.getDir(path);
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
	 * @return 相片数量
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
			String file_dir = Util.getDir(path);

			if (album_file_dir.equals(file_dir))
				photo_num++;
			cursor.moveToNext();
		}
		cursor.close();
		return photo_num;
	}

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
			String file_dir = Util.getDir(path);
			if (file_dir.equals(album_dir))
				photos.add(path);
			cursor.moveToNext();
		}
		cursor.close();

		return photos;
	}
}
