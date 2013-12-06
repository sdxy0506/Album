package com.xuyan.album;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.xuyan.album.adapter.SendGridViewAdapter;
import com.xuyan.album.http.RequestAid;
import com.xuyan.album.tools.FileHelper;
import com.xuyan.util.MyParameters;
import com.xuyan.util.Util;

public class SayActivity extends Activity {
	private Context mContext;
	private Button btn_cancel;
	private Button btn_send;
	private Button btn_camera;
	private Button btn_album;
	private EditText tv_say;

	private GridView send_gridView;
	private SendGridViewAdapter gridViewAdapter;
	private ArrayList<String> mSelectedPhotos = new ArrayList<String>();

	private File mImageFile;
	private Uri mImageUri;

	private FinalBitmap fb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_main);
		mContext = this;

		fb = FinalBitmap.create(mContext);
		fb.configLoadingImage(R.drawable.ic_stub);

		findViews();
		setListener();
	}

	// 绑定各类View
	private void findViews() {
		btn_album = (Button) findViewById(R.id.send_album);
		btn_camera = (Button) findViewById(R.id.send_camera);
		btn_cancel = (Button) findViewById(R.id.send_cancel);
		btn_send = (Button) findViewById(R.id.send_send);
		tv_say = (EditText) findViewById(R.id.send_say);
		send_gridView = (GridView) findViewById(R.id.send_grid);
	}

	// 绑定各类事件
	private void setListener() {
		btn_album.setOnClickListener(btnListener);
		btn_camera.setOnClickListener(btnListener);
		btn_cancel.setOnClickListener(btnListener);
		btn_send.setOnClickListener(btnListener);
	}

	private OnClickListener btnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.send_album:
				intent.putStringArrayListExtra(Util.SELECT_PIC, mSelectedPhotos);
				intent.setClass(mContext, MyAlbumActivity.class);
				startActivityForResult(intent, Util.REQUEST_IMAGE_FILE);
				break;
			case R.id.send_camera:
				openImageCamera();
				break;
			case R.id.send_send:
				String content = tv_say.getText().toString().trim();
				if (mSelectedPhotos.size() > 0) {
					if (tv_say.length() <= 140) {
						new PostSayTask().execute(content);
					} else {
						Toast.makeText(mContext, "输入数目超过140个字！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					if (tv_say.length() <= 140 && tv_say.length() > 0) {
						new PostSayTask().execute(content);
					} else if (tv_say.length() > 140) {
						Toast.makeText(mContext, "输入数目超过140个字！",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(mContext, "输入内容不能为空！",
								Toast.LENGTH_SHORT).show();
					}
				}

				break;
			case R.id.send_cancel:
				mSelectedPhotos.add(Util.PUT_BAIDU);
				gridViewAdapter = new SendGridViewAdapter(mContext,
						mSelectedPhotos, fb);
				send_gridView.setAdapter(gridViewAdapter);
				setGridAdapter();
				break;
			default:
				break;
			}
		}
	};

	private void openImageCamera() {
		if (mSelectedPhotos.size() == Util.MAX_PHOTOS) {
			Toast.makeText(mContext, "只能选择" + Util.MAX_PHOTOS + "张图片",
					Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			String filename = _getPhotoFilename(new Date());
			mImageFile = new File(FileHelper.getBasePath(), filename);
			mImageUri = Uri.fromFile(mImageFile);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
			startActivityForResult(intent, Util.REQUEST_IMAGE_CAMERA);
		} catch (Exception e) {
			Toast.makeText(mContext, "存储失败！", Toast.LENGTH_SHORT).show();
		}
	}

	private String _getPhotoFilename(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddKms");
		return dateFormat.format(date) + ".jpg";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Util.REQUEST_IMAGE_FILE:
			if (resultCode == RESULT_OK) {
				mSelectedPhotos.clear();
				mSelectedPhotos.addAll(data
						.getStringArrayListExtra(Util.SELECT_PIC));
			}
			break;
		case Util.REQUEST_IMAGE_CAMERA:
			if (resultCode == RESULT_OK) {
				mSelectedPhotos.add(mImageUri.toString());
			}
			break;
		case Util.REQUEST_IMAGE_PAGER:
			if (resultCode == RESULT_OK) {
				mSelectedPhotos.clear();
				mSelectedPhotos.addAll(data
						.getStringArrayListExtra(Util.SELECT_PIC));
			}
			break;
		default:
			break;
		}
		gridViewAdapter = new SendGridViewAdapter(mContext, mSelectedPhotos, fb);
		send_gridView.setAdapter(gridViewAdapter);
		setGridAdapter();

	}

	private void setGridAdapter() {
		send_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long Id) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putStringArrayList(Util.SELECT_PIC, mSelectedPhotos);
				bundle.putInt(Util.POSITION_PIC, position);
				intent.putExtras(bundle);
				intent.setClass(mContext, ImagePagerActivity.class);
				startActivityForResult(intent, Util.REQUEST_IMAGE_PAGER);
			}
		});
	}

	public static String getRandomString(int length) {
		// 定义验证码的字符表
		String chars = "3323456789ABCDEFGHHJKLMNNPQRSTUVWXYZ";
		char[] rands = new char[length];
		for (int i = 0; i < length; i++) {
			int rand = (int) (Math.random() * 36);
			rands[i] = chars.charAt(rand);
		}
		return new String(rands);
	}

	private class PostSayTask extends AsyncTask<String, Object, Integer> {

		@Override
		protected Integer doInBackground(String... params) {

			String content = params[0];
			MyParameters parameters = new MyParameters();
			parameters.add("message", content);
			parameters.add("where", "Android");
			if (mSelectedPhotos.size() > 0) {
				parameters.add("imageFile", mSelectedPhotos.get(0));
			}

			try {
				String ret = RequestAid.openUrl(mContext, Util.Post, "POST",
						parameters);
				JSONObject jsonObject = new JSONObject(ret);
				if (jsonObject.getInt("s") == 200) {
					return Util.LOGIN_OK;
				} else {
					return Util.LOGIN_FAIL;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return Util.LOGIN_FAIL;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {

			switch (result) {
			case Util.LOGIN_OK:
				Toast.makeText(mContext, "发布成功！", Toast.LENGTH_SHORT).show();
				break;
			case Util.LOGIN_FAIL:
				Toast.makeText(mContext, "发布失败！", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		}

	}

}
