package com.xuyan.album;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.xuyan.album.adapter.GridViewAdapter;

public class AlbumActivity extends Activity {
	private Context mContext;
	private ArrayList<Album> albums;
	private Bundle bundle;
	private int AlbumId;

	private GridView gridView;
	private GridViewAdapter gridViewAdapter;
	private ArrayList<String> mPhotos = new ArrayList<String>();
	private ArrayList<String> mSelectedPhotos = new ArrayList<String>();
	private HashMap<String, ImageView> hashMap = new HashMap<String, ImageView>();

	private LinearLayout selectedImageLayout;
	private HorizontalScrollView scroll_view;
	private TextView title;
	private Button okButton;

	private ProgressDialog progressDialog;

	private Button album_btn;

	// 避免list，grid滑动滞后
	PauseOnScrollListener pauseListener = new PauseOnScrollListener(
			Constants.imageLoader, false, true);

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.album_grid);
		mContext = this;
		bundle = getIntent().getExtras();
		AlbumId = bundle.getInt("AlbumId");
		Log.i("Id", "" + AlbumId);

		album_btn = (Button) findViewById(R.id.choise_album);
		album_btn.setOnClickListener(btnListener);

		gridView = (GridView) this.findViewById(R.id.grid_view);

		// 避免list，grid滑动滞后
		gridView.setOnScrollListener(pauseListener);

		selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layout);
		scroll_view = (HorizontalScrollView) findViewById(R.id.scrollview);
		title = (TextView) this.findViewById(R.id.title);
		okButton = (Button) this.findViewById(R.id.ok_button);

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在加载中...");
		progressDialog.show();

		new LoadImgTask().execute();
	}

	private UILApplication setApplication() {
		return ((UILApplication) super.getApplicationContext());

	}

	public ArrayList<String> getPhotos(String album_dir) {
		ArrayList<String> photos = new ArrayList<String>();
		ContentResolver contentResolver = getContentResolver();
		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		Cursor cursor = contentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		int fileNum = cursor.getCount();

		for (int counter = 0; counter < fileNum; counter++) {
			Log.w("tag",
					"---file is:"
							+ cursor.getString(cursor
									.getColumnIndex(MediaStore.Video.Media.DATA)));
			String path = cursor.getString(cursor
					.getColumnIndex(MediaStore.Video.Media.DATA));
			// 获取路径中文件的目录
			String file_dir = Util.getDir(path);
			if (file_dir.equals(album_dir))
				photos.add(path);
			cursor.moveToNext();
		}
		cursor.close();

		return photos;
	}

	private boolean removePath(String path) {
		if (hashMap.containsKey(path)) {
			selectedImageLayout.removeView(hashMap.get(path));
			hashMap.remove(path);
			removeOneData(mSelectedPhotos, path);
			okButton.setText("完成(" + mSelectedPhotos.size() + "/8)");
			return true;
		} else {
			return false;
		}
	}

	private void removeOneData(ArrayList<String> arrayList, String s) {
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).equals(s)) {
				arrayList.remove(i);
				return;
			}
		}
	}

	private void setGridView() {
		gridViewAdapter = new GridViewAdapter(getApplicationContext(), mPhotos,
				mSelectedPhotos);
		gridView.setAdapter(gridViewAdapter);
		gridViewAdapter
				.setOnItemClickListener(new GridViewAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(final ToggleButton toggleButton,
							int position, final String path, boolean isChecked) {
						if (mSelectedPhotos.size() >= 8) {
							toggleButton.setChecked(false);
							if (!removePath(path)) {
								Toast.makeText(AlbumActivity.this, "只能选择8张图片",
										Toast.LENGTH_SHORT).show();
							}
							return;
						}

						if (isChecked) {
							if (!hashMap.containsKey(path)) {
								ImageView imageView = (ImageView) LayoutInflater
										.from(AlbumActivity.this).inflate(
												R.layout.choose_imageview,
												selectedImageLayout, false);
								selectedImageLayout.addView(imageView);
								imageView.postDelayed(new Runnable() {
									@Override
									public void run() {

										int off = selectedImageLayout
												.getMeasuredWidth()
												- scroll_view.getWidth();
										if (off > 0) {
											scroll_view.smoothScrollTo(off, 0);
										}

									}
								}, 100);

								hashMap.put(path, imageView);
								mSelectedPhotos.add(path);
								Constants.imageLoader.displayImage("file://"
										+ mPhotos.get(position), imageView,
										Constants.image_display_options,
										new Util.AnimateFirstDisplayListener());
								imageView
										.setOnClickListener(new View.OnClickListener() {

											@Override
											public void onClick(View v) {
												toggleButton.setChecked(false);
												removePath(path);

											}
										});
								okButton.setText("完成(" + mSelectedPhotos.size()
										+ "/8)");
							}
						} else {
							removePath(path);
						}
					}
				});
	}

	// 异步读取图片
	private class LoadImgTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			albums = setApplication().getAlbums();
			if (albums == null) {
				albums = Constants.getAlbums(mContext);
			}
			String album_dir = albums.get(AlbumId).mName;
			title.setText(album_dir);
			mPhotos.addAll(Constants.getPhotos(mContext, album_dir));
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			setGridView();
			progressDialog.dismiss();
		}

	}

	private OnClickListener btnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.choise_album:
				Intent intent = new Intent();
				intent.setClass(mContext, AlbumsListActivity.class);
				startActivity(intent);
				finish();
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onPause() {
		super.onPause();
	}

}
