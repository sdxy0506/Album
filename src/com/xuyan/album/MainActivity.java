package com.xuyan.album;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennClient.LoginListener;
import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.exception.RennException;
import com.renn.rennsdk.param.ListUserFriendParam;
import com.xuyan.album.object.FriendInfo;

/**
 * 文件名称：LoginActivity.java 类说明：
 * 
 * @create 2014-2-19 上午11:38:23 项目名称：MyAlbum
 * @author xuyan
 * @version 1.0
 */
public class MainActivity extends Activity implements OnClickListener {
	private RennClient rennClient;
	private ListView lv_friendList;
	private List<FriendInfo> friendList;
	private List<String> nameList;
	private ProgressDialog mProgressDialog;
	private Context mContext;
	private int pageNumber = 1;
	private int pageSize = 50;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		mContext = this;

		rennClient = RennClient.getInstance(this);
		init();
	}

	private void init() {
		lv_friendList = (ListView) findViewById(R.id.friend_list);
		getFriendList();
	}

	@Override
	public void onClick(View v) {

	}

	private void getFriendList() {
		lv_friendList.setVisibility(View.VISIBLE);
		ListUserFriendParam param = new ListUserFriendParam();
		param.setUserId(rennClient.getUid());
		param.setPageSize(pageSize);
		param.setPageNumber(pageNumber);
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setCancelable(true);
			mProgressDialog.setTitle("请等待");
			mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
			mProgressDialog.setMessage("正在获取信息");
			mProgressDialog.show();
		}
		try {
			rennClient.getRennService().sendAsynRequest(param, new CallBack() {

				@Override
				public void onSuccess(RennResponse response) {
					try {
						friendList = JSON.parseArray(response
								.getResponseArray().toString(),
								FriendInfo.class);
						nameList = new ArrayList<String>();
						for (int i = 0; i < friendList.size(); i++) {
							nameList.add(friendList.get(i).getName());
						}
						lv_friendList.setAdapter(new ArrayAdapter<String>(
								mContext, android.R.layout.simple_list_item_1,
								nameList));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Toast.makeText(MainActivity.this, "获取成功",
							Toast.LENGTH_SHORT).show();
					if (mProgressDialog != null) {
						mProgressDialog.dismiss();
						mProgressDialog = null;
					}
				}

				@Override
				public void onFailed(String errorCode, String errorMessage) {
					Toast.makeText(MainActivity.this, "获取失败",
							Toast.LENGTH_SHORT).show();
					if (mProgressDialog != null) {
						mProgressDialog.dismiss();
						mProgressDialog = null;
					}
				}
			});
		} catch (RennException e1) {
			e1.printStackTrace();
		}
	}
}
