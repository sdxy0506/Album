package com.xuyan.album;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.xuyan.album.adapter.SendGridViewAdapter;
import com.xuyan.album.application.GetApplication;
import com.xuyan.album.application.UILApplication;
import com.xuyan.util.Album;
import com.xuyan.util.FileHelper;
import com.xuyan.util.Util;

public class SayActivity extends Activity implements GetApplication {
	private Context mContext;
	private Button btn_cancel;
	private Button btn_send;
	private Button btn_camera;
	private Button btn_album;
	private EditText tv_say;
	private ArrayList<Album> albums;

	private GridView send_gridView;
	private SendGridViewAdapter gridViewAdapter;
	private ArrayList<String> mSelectedPhotos = new ArrayList<String>();

	private File mImageFile;
	private Uri mImageUri;

	private FinalBitmap fb;
	private ArrayList<Integer> isCamera = new ArrayList<Integer>();
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send);
		mContext = this;

		fb = FinalBitmap.create(mContext);
		fb.configLoadingImage(R.drawable.ic_stub);

		findViews();
		setListener();

		progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage("正在加载...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new loadAlbums().execute();
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
				intent.putIntegerArrayListExtra("isCamera", isCamera);
				intent.setClass(mContext, MyAlbumActivity.class);
				startActivityForResult(intent, Util.REQUEST_IMAGE_FILE);
				break;
			case R.id.send_camera:
				openImageCamera();
				break;
			case R.id.send_send:
				break;
			default:
				break;
			}
		}
	};

	private void openImageCamera() {
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
		if (requestCode == Util.REQUEST_IMAGE_FILE && resultCode == RESULT_OK) {
			mSelectedPhotos.clear();
			mSelectedPhotos.addAll(data
					.getStringArrayListExtra(Util.SELECT_PIC));

		} else if (requestCode == Util.REQUEST_IMAGE_CAMERA
				&& resultCode == RESULT_OK) {
			// mImageUri = data.getData();
			mSelectedPhotos.add(mImageUri.toString());
			int camera = mSelectedPhotos.size() - 1;
			isCamera.add(camera);
			Log.i("uri", "" + mImageUri);
		}
		gridViewAdapter = new SendGridViewAdapter(mContext, mSelectedPhotos, fb);
		send_gridView.setAdapter(gridViewAdapter);
	}

	@Override
	public UILApplication getMyApplication() {
		return ((UILApplication) super.getApplicationContext());
	}

	private class loadAlbums extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			Util.ALBUMS = Util.getAlbums(mContext);
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
		}

	}

}
