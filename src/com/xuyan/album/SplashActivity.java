package com.xuyan.album;

import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennClient.LoginListener;
import com.xuyan.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashActivity extends Activity implements OnClickListener {
	private Context mContext;
	private ImageView splashImg;
	private RennClient rennClient;
	private Button btn_login;
	private static final String TAG = "SplashActivity";

	private static final String APP_ID = "168802";
	private static final String API_KEY = "e884884ac90c4182a426444db12915bf";
	private static final String SECRET_KEY = "094de55dc157411e8a5435c6a7c134c5";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		mContext = this;

		init();
	}

	private void init() {
		btn_login = (Button) findViewById(R.id.login);
		btn_login.setOnClickListener(this);
		splashImg = (ImageView) findViewById(R.id.img_splash);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
		splashImg.setAnimation(animation);
		rennClient = RennClient.getInstance(this);
		rennClient.init(APP_ID, API_KEY, SECRET_KEY);
		rennClient
				.setScope("read_user_blog read_user_photo read_user_status read_user_album "
						+ "read_user_comment read_user_share publish_blog publish_share "
						+ "send_notification photo_upload status_update create_album "
						+ "publish_comment publish_feed");
		rennClient.setTokenType("bearer");
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (rennClient.isLogin()) {
					jump();
				} else {
					btn_login.setVisibility(View.VISIBLE);
				}

			}
		}, 2000);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			rennClient.setLoginListener(new LoginListener() {
				@Override
				public void onLoginSuccess() {
					ToastUtil.showMessage(mContext, "登录成功!");
					btn_login.setVisibility(View.GONE);
					jump();
				}

				@Override
				public void onLoginCanceled() {
					ToastUtil.showMessage(mContext, "登录失败!");
				}

			});
			rennClient.login(this);
			break;

		default:
			break;
		}
	}

	private void jump() {
		Intent intent = new Intent();
		intent.setClass(mContext, MainActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.fadding_in, R.anim.fadding_out);
	}

}
