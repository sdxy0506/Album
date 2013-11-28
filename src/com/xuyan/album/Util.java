package com.xuyan.album;

import android.util.Log;

/**
 * Created with IntelliJ IDEA. User: hetuo Date: 13-10-28 Time: 上午11:14 To
 * change this template use File | Settings | File Templates.
 */
public class Util {
	// 获取图片所在文件夹名称
	public static String getDir(String path) {
		Log.i("file", "1" + path);
		String subString = path.substring(0, path.lastIndexOf('/'));
		Log.i("file", "2" + subString);
		return subString.substring(subString.lastIndexOf('/') + 1,
				subString.length());
	}
}
