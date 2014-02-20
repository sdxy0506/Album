package com.xuyan.album;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.exception.RennException;
import com.renn.rennsdk.param.ListPhotoParam;
import com.xuyan.album.adapter.GridViewAdapter;
import com.xuyan.album.object.RenRenAlbumPhotoList;
import com.xuyan.util.ToastUtil;

/**
 * 文件名称：RenRenAlbumActivity.java 类说明：用于展示某个好友某个相册的图片
 * 
 * @create 2014-2-20 上午10:30:22 项目名称：MyAlbum
 * @author xuyan
 * @version 1.0
 */

public class RenRenAlbumActivity extends Activity {
	private Context mContext;
	private List<RenRenAlbumPhotoList> albums;

	private GridView gridView;
	private GridViewAdapter gridViewAdapter;
	private ArrayList<String> mPhotos = new ArrayList<String>();

	private Button btn_album;
	private Button btn_cancel;
	private TextView title;
	private int pageNumber = 1;

	private ProgressDialog mProgressDialog;

	private FinalBitmap fb;
	private Intent intent;
	private String fid;
	private String album_name;
	private String album_id;
	private RennClient rennClient;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.renren_album_pic);
		mContext = this;

		intent = getIntent();
		fid = intent.getStringExtra("fid");
		album_name = intent.getStringExtra("album_name");
		album_id = intent.getStringExtra("album_id");
		rennClient = RennClient.getInstance(mContext);

		fb = FinalBitmap.create(this);// 初始化FinalBitmap模块
		fb.configLoadingImage(R.drawable.ic_stub);

		findViews();
		setListeners();
		getAlbum();
	}

	private void findViews() {
		gridView = (GridView) this.findViewById(R.id.renren_album_grid_view);
		title = (TextView) this.findViewById(R.id.renren_album_title);
		title.setText(album_name);
		btn_album = (Button) this.findViewById(R.id.renren_album_photo);
		btn_cancel = (Button) this.findViewById(R.id.renren_album_cancel);
	}

	private void setListeners() {
		btn_album.setOnClickListener(btnListener);
		btn_cancel.setOnClickListener(btnListener);
	}

	private OnClickListener btnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.renren_album_photo:
				title.setText("相册");
				break;
			case R.id.renren_album_cancel:
				setResult(RESULT_CANCELED);
				finish();
				break;
			default:
				break;
			}
		}
	};

	private void getAlbum() {
		ListPhotoParam param = new ListPhotoParam();
		param.setAlbumId(Long.valueOf(album_id));
		param.setOwnerId(Long.valueOf(fid));
		param.setPageSize(30);
		param.setPageNumber(pageNumber);

		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(RenRenAlbumActivity.this);
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
						albums = JSON.parseArray(response.getResponseArray()
								.toString(), RenRenAlbumPhotoList.class);
						for (int i = 0; i < albums.size(); i++) {
							mPhotos.add(albums.get(i).getLargeUrl());
						}
						gridViewAdapter = new GridViewAdapter(mContext,
								mPhotos, null, fb);
						gridView.setAdapter(gridViewAdapter);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					ToastUtil.showMessage(mContext, "获取成功");
					if (mProgressDialog != null) {
						mProgressDialog.dismiss();
						mProgressDialog = null;
					}
				}

				@Override
				public void onFailed(String errorCode, String errorMessage) {
					ToastUtil.showMessage(mContext, "获取失败");
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
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
