package com.xuyan.album.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;

/**
 * @author xuyan 2013-11-26
 */
public class UILApplication extends Application {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		super.onCreate();

	}

}