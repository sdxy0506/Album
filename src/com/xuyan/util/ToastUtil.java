package com.xuyan.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * 文件名称：ToastUtil.java 类说明：
 * 
 * @create 2014-2-19 下午3:31:47 项目名称：MyAlbum
 * @author xuyan
 * @version 1.0
 */
public class ToastUtil {
	/**
	 * 显示Toast
	 * 
	 * @param act
	 *            context上下文对象
	 * @param msg
	 *            文字
	 */
	public static void showMessage(final Context act, final String msg) {
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
			}

		});

	}

	public static void showMessage(final Context act, final int msg) {
		Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
	}
}
