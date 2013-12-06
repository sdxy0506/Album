package com.xuyan.album;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.xuyan.album.http.RequestAid;
import com.xuyan.util.SharpParameter;
import com.xuyan.util.MyParameters;
import com.xuyan.util.Util;

public class SplashActivity extends Activity {
	private static Context mContext;
	private static int LOGIN_OK = 100;
	private static int LOGIN_FAIL = 101;
	private String userName = "";
	private String passWord = "";
	private boolean login = false;
	private SharedPreferences dataBase;
	private String msg;
	private static final String TAG = "SplashActivity";

	private int errorCode = 0;

	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		mContext = this;
		dataBase = getSharedPreferences(SharpParameter.dataBase, MODE_PRIVATE);
		login = true;
		userName = Util.USER_NAME;
		passWord = Util.PASSWORD;
		SharpParameter.sessionId = SayActivity.getRandomString(12);
		RequestAid.setRequestHeader(SharpParameter.APP_SESSION_ID_NAME,
				SharpParameter.sessionId);
		if (login && !userName.equals("false") && !passWord.equals("false")) {
			MyParameters srxParameters = new MyParameters();
			srxParameters.add("username", userName);
			srxParameters.add("password", passWord);
			new LoginGenericTask().execute(srxParameters);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	private class LoginGenericTask extends
			AsyncTask<MyParameters, Object, Integer> {

		@Override
		protected Integer doInBackground(MyParameters... params) {
			MyParameters param = params[0];
			String user = (String) param.getValue("username");
			String pass = (String) param.getValue("password");

			try {
				String ret = RequestAid.openUrl(mContext,
						SharpParameter.loginUrl, "POST", param);
				JSONObject jsonObject = new JSONObject(ret);
				if (jsonObject.getInt("s") == 200) {
					JSONObject mJsonObject = jsonObject.getJSONObject("data");
					int id = mJsonObject.getInt("id");
					String access_token = mJsonObject.getString("access_token");
					SharpParameter.UId = String.valueOf(id);
					msg = jsonObject.getString("msg");
					dataBase.edit().putString("msg", msg).commit();
					dataBase.edit().putInt("id", id).commit();
					dataBase.edit().putString("access_token", access_token)
							.commit();
					// 保存帐号
					dataBase.edit().putString("userName", user).commit();
					dataBase.edit()
							.putString("passWord",
									SharpParameter.sharpDES.encrypt(pass))
							.commit();
					dataBase.edit().putBoolean("login", true).commit();
					dataBase.edit().putString("avatar",
							mJsonObject.getString("avatar"));
					dataBase.edit().putString("nickname",
							mJsonObject.getString("nickname"));
					SharpParameter.nickname = mJsonObject.getString("nickname");
					SharpParameter.avatar = mJsonObject.getString("avatar");
					String cookie = "id=" + id + "; access_token="
							+ access_token;
					SharpParameter.cookie = cookie;
					Log.i("cookiee", cookie);
					RequestAid.setRequestHeader("Cookie", cookie);
					RequestAid.setRequestHeader("APPSESSIONID",
							SharpParameter.sessionId);
				} else if (jsonObject.getInt("s") == 301) {
					JSONObject mJsonObject = jsonObject.getJSONObject("data");
					errorCode = mJsonObject.getInt("errorCode");
					msg = jsonObject.getString("msg");
					return LOGIN_FAIL;
				}

			} catch (Exception e) {
				e.printStackTrace();
				msg = "登录失败,请稍候重试!";
				return LOGIN_FAIL;
			}
			return LOGIN_OK;
		}

		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			switch (result) {
			case 100:
				Intent intent = new Intent();
				intent.setClass(mContext, SayActivity.class);
				startActivity(intent);
				finish();
				break;
			case 101:
				Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	}
}
