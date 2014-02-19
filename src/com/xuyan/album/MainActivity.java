package com.xuyan.album;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.exception.RennException;
import com.renn.rennsdk.param.ListUserFriendParam;
import com.xuyan.album.object.FriendInfo;
import com.xuyan.album.object.RenRenAlbum;
import com.xuyan.util.ToastUtil;

/**
 * 文件名称：LoginActivity.java 类说明：
 * 
 * @create 2014-2-19 上午11:38:23 项目名称：MyAlbum
 * @author xuyan
 * @version 1.0
 */
public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private RennClient rennClient;
	private ListView lv_friendList;
	private List<FriendInfo> friendList;
	private List<String> nameList;
	private ProgressDialog mProgressDialog;
	private Context mContext;
	private int pageNumber = 1;
	private int pageSize = 100;
	private Button btn_page_up, btn_page_down;
	private TextView tv_page_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mContext = this;
		rennClient = RennClient.getInstance(this);
		init();
	}

	private void init() {
		btn_page_up = (Button) findViewById(R.id.page_up);
		btn_page_up.setOnClickListener(this);
		btn_page_down = (Button) findViewById(R.id.page_down);
		btn_page_down.setOnClickListener(this);
		tv_page_number = (TextView) findViewById(R.id.tv_page);
		tv_page_number.setText("" + pageNumber);
		lv_friendList = (ListView) findViewById(R.id.friend_list);
		lv_friendList.setOnItemClickListener(this);
		getFriendList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.page_up:
			if (pageNumber > 1) {
				pageNumber--;
				getFriendList();
			} else {
				ToastUtil.showMessage(mContext, "已经是第一页");
			}
			break;
		case R.id.page_down:
			pageNumber++;
			getFriendList();
			break;

		default:
			break;
		}
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

						if (friendList.size() == 0) {
							ToastUtil.showMessage(mContext, "已经是最后一页了");
							pageNumber--;
							mProgressDialog.dismiss();
							mProgressDialog = null;
							tv_page_number.setText("" + pageNumber);
							return;
						}
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
					tv_page_number.setText("" + pageNumber);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		Intent intent = new Intent();
		intent.putExtra("fid", friendList.get(position).getId());
		intent.putExtra("name", friendList.get(position).getName());
		intent.setClass(mContext, FriendAlbumListActivity.class);
		startActivity(intent);
		// finish();
		overridePendingTransition(R.anim.fadding_in, R.anim.fadding_out);
	}
}
