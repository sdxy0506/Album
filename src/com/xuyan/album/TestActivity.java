package com.xuyan.album;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.xuyan.album.adapter.SendGridViewAdapter;
import com.xuyan.album.application.SetApplication;
import com.xuyan.album.application.UILApplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity implements SetApplication {

	private Context mContext;

	private Button btn_cancel;
	private Button btn_send;
	private Button btn_camera;
	private Button btn_album;
	private TextView tv_say;
	private GridView send_gridView;

	private File mImageFile;
	private Uri mImageUri;

	private SendGridViewAdapter gridViewAdapter;

	private ArrayList<String> mSelectedPhotos = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send);
		mContext = this;
		findViews();
		setListener();

	}

	private void findViews() {
		btn_album = (Button) findViewById(R.id.send_album);
		btn_camera = (Button) findViewById(R.id.send_camera);
		btn_cancel = (Button) findViewById(R.id.send_cancel);
		btn_send = (Button) findViewById(R.id.send_send);
		tv_say = (TextView) findViewById(R.id.send_say);
		send_gridView = (GridView) findViewById(R.id.send_grid);
	}

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
				intent.setClass(mContext, MyAlbumActivity.class);
				startActivityForResult(intent, Constants.REQUEST_IMAGE_FILE);
				break;
			case R.id.send_camera:
				openImageCamera();
				break;
			case R.id.send_send:
				setApplication().getSelectedPhotos().clear();
				break;
			default:
				break;
			}
		}
	};

	private void openImageCamera() {
		try {
			// TODO: API < 1.6, images size too small
			String filename = _getPhotoFilename(new Date());
			mImageFile = new File(FileHelper.getBasePath(), filename);
			mImageUri = Uri.fromFile(mImageFile);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
			startActivityForResult(intent, Constants.REQUEST_IMAGE_CAMERA);
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
		if (requestCode == Constants.REQUEST_IMAGE_FILE
				&& resultCode == RESULT_OK) {
			Toast.makeText(mContext, "从相册返回", Toast.LENGTH_SHORT).show();
			mSelectedPhotos = setApplication().getSelectedPhotos();
			gridViewAdapter = new SendGridViewAdapter(mContext, mSelectedPhotos);
			send_gridView.setAdapter(gridViewAdapter);

		} else if (requestCode == Constants.REQUEST_IMAGE_FILE
				&& resultCode == RESULT_OK) {
			mImageUri = data.getData();
		}
	}

	@Override
	public UILApplication setApplication() {
		return ((UILApplication) super.getApplicationContext());
	}

}
