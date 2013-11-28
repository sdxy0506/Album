package com.xuyan.album.application;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;

import com.xuyan.util.Album;

/**
 * @author xuyan 2013-11-26
 */
public class UILApplication extends Application {

	// 利用全局变量储存相册列表信息
	public ArrayList<Album> albums;
	// 已选择相片的路径集合
	public ArrayList<String> selectedPhotos;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		super.onCreate();

		albums = new ArrayList<Album>();
		selectedPhotos = new ArrayList<String>();
	}

	/**
	 * 读取全局变量的相册列表信息
	 * 
	 * @return
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}

	/**
	 * 设置全局变量的相册列表信息
	 * 
	 * @param albums
	 */
	public void setAlbums(ArrayList<Album> albums) {
		this.albums = albums;
	}

	public ArrayList<String> getSelectedPhotos() {
		return selectedPhotos;
	}

	public void setSelectedPhotos(ArrayList<String> selectedPhotos) {
		this.selectedPhotos = selectedPhotos;
	}
}