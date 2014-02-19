package com.xuyan.album;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.exception.RennException;
import com.renn.rennsdk.param.ListAlbumParam;
import com.xuyan.album.adapter.RenRenAlbumListAdapter;
import com.xuyan.album.object.RenRenAlbum;

/**
 * 文件名称：FriendAlbumListActivity.java 类说明：
 * 
 * @create 2014-2-19 下午4:51:33 项目名称：MyAlbum
 * @author xuyan
 * @version 1.0
 */
public class FriendAlbumListActivity extends Activity {

	private String fid = null;
	private TextView tv_name;
	private ListView renren_album_list;
	private RennClient rennClient;
	private ProgressDialog mProgressDialog;
	private List<RenRenAlbum> albumList;
	private RenRenAlbumListAdapter adapter;
	private FinalBitmap fb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friend_album_list);
		rennClient = RennClient.getInstance(this);
		fb = FinalBitmap.create(this);
		Intent intent = getIntent();
		fid = intent.getStringExtra("fid");
		tv_name = (TextView) findViewById(R.id.album_owner);
		tv_name.setText(intent.getStringExtra("name"));
		renren_album_list = (ListView) findViewById(R.id.renren_album_list);
		getAlbumList();

	}

	private void getAlbumList() {
		ListAlbumParam param = new ListAlbumParam();
		param.setOwnerId(Long.valueOf(fid));
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(FriendAlbumListActivity.this);
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
						albumList = JSON.parseArray(response.getResponseArray()
								.toString(), RenRenAlbum.class);
						adapter = new RenRenAlbumListAdapter(
								FriendAlbumListActivity.this, fb);
						adapter.setAlbumsList(albumList);
						renren_album_list.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Toast.makeText(FriendAlbumListActivity.this, "获取成功",
							Toast.LENGTH_SHORT).show();
					if (mProgressDialog != null) {
						mProgressDialog.dismiss();
						mProgressDialog = null;
					}
				}

				@Override
				public void onFailed(String errorCode, String errorMessage) {
					Toast.makeText(FriendAlbumListActivity.this, "获取失败",
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
