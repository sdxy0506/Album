package com.xuyan.album;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.xuyan.album.adapter.AlbumListViewAdapter;
import com.xuyan.album.adapter.GridViewAdapter;
import com.xuyan.album.application.GetApplication;
import com.xuyan.album.application.UILApplication;
import com.xuyan.util.Album;
import com.xuyan.util.TestEvent;
import com.xuyan.util.Util;

import de.greenrobot.event.EventBus;

public class MyAlbumActivity extends Activity implements GetApplication {
	private Context mContext;
	private ListView listView;
	private AlbumListViewAdapter listViewAdapter;
	private ArrayList<Album> albums;

	private GridView gridView;
	private GridViewAdapter gridViewAdapter;
	private ArrayList<String> mPhotos = new ArrayList<String>();
	private ArrayList<String> mSelectedPhotos = new ArrayList<String>();
	private HashMap<String, ImageView> hashMap = new HashMap<String, ImageView>();

	private LinearLayout selectedImageLayout;
	private HorizontalScrollView scroll_view;
	private Button btn_album;
	private Button btn_cancel;
	private TextView title;
	private Button okButton;

	private EventBus eventBus;

	private ProgressDialog progressDialog;

	private FinalBitmap fb;
	private Intent intent;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_album);
		mContext = this;

		intent = getIntent();
		eventBus = EventBus.getDefault();
		eventBus.register(this);

		fb = FinalBitmap.create(this);// 初始化FinalBitmap模块
		fb.configLoadingImage(R.drawable.ic_stub);

		findViews();
		setListeners();
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage("正在读取相册列表");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new showAlbumsList().execute();
	}

	private void findViews() {
		listView = (ListView) this.findViewById(R.id.list_view);
		gridView = (GridView) this.findViewById(R.id.grid_view);
		selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layout);
		scroll_view = (HorizontalScrollView) findViewById(R.id.scrollview);
		title = (TextView) this.findViewById(R.id.title);
		btn_album = (Button) this.findViewById(R.id.photo);
		btn_cancel = (Button) this.findViewById(R.id.cancel);
		okButton = (Button) this.findViewById(R.id.ok_button);
	}

	private void setListeners() {
		btn_album.setOnClickListener(btnListener);
		btn_cancel.setOnClickListener(btnListener);
		okButton.setOnClickListener(btnListener);
	}

	private OnClickListener btnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.photo:
				listView.setVisibility(View.VISIBLE);
				gridView.setVisibility(View.INVISIBLE);
				btn_album.setVisibility(View.INVISIBLE);
				title.setText("相册");
				break;
			case R.id.cancel:
				setResult(RESULT_CANCELED);
				finish();
				break;
			case R.id.ok_button:

				intent.putStringArrayListExtra(Util.SELECT_PIC, mSelectedPhotos);
				setResult(RESULT_OK, intent);
				finish();
				break;
			default:
				break;
			}
		}
	};

	private void setAlbumsList() {
		listViewAdapter = new AlbumListViewAdapter(mContext, fb);
		listViewAdapter.setAlbumsList(albums);
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				TestEvent event = new TestEvent("list_view_item_click");
				Bundle bundle = new Bundle();
				bundle.putInt("position", i);
				event.set_bundle(bundle);
				eventBus.post(event);

			}
		});
	}

	private boolean removePath(String path) {
		if (hashMap.containsKey(path)) {
			selectedImageLayout.removeView(hashMap.get(path));
			hashMap.remove(path);
			removeOneData(mSelectedPhotos, path);
			if (mSelectedPhotos.size() > 0) {
				okButton.setText("完成(" + mSelectedPhotos.size() + "/"
						+ Util.MAX_PHOTOS + ")");
			} else {
				okButton.setText("完成");
			}
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

	// eventbus接收到通知事件，发生改变
	public void onEvent(TestEvent event) {
		if (event.get_string().equals("list_view_item_click")) {
			Bundle bundle = event.get_bundle();

			listView.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.VISIBLE);
			btn_album.setVisibility(View.VISIBLE);
			String album_dir = albums.get(bundle.getInt("position")).mName;
			title.setText(album_dir);
			mPhotos = Util.getPhotos(mContext, album_dir);
			gridViewAdapter = new GridViewAdapter(mContext, mPhotos,
					mSelectedPhotos, fb);
			gridView.setAdapter(gridViewAdapter);
			gridViewAdapter
					.setOnItemClickListener(new GridViewAdapter.OnItemClickListener() {
						@Override
						public void onItemClick(
								final ToggleButton toggleButton, int position,
								final String path, boolean isChecked) {
							if (mSelectedPhotos.size() >= Util.MAX_PHOTOS) {
								toggleButton.setChecked(false);
								if (!removePath(path)) {
									Toast.makeText(MyAlbumActivity.this,
											"只能选择" + Util.MAX_PHOTOS + "张图片",
											Toast.LENGTH_SHORT).show();
								}
								return;
							}

							if (isChecked) {
								if (!hashMap.containsKey(path)) {
									ImageView imageView = (ImageView) LayoutInflater
											.from(MyAlbumActivity.this)
											.inflate(R.layout.album_choose_imageview,
													selectedImageLayout, false);
									selectedImageLayout.addView(imageView);
									imageView.postDelayed(new Runnable() {
										@Override
										public void run() {
											int off = selectedImageLayout
													.getMeasuredWidth()
													- scroll_view.getWidth();
											if (off > 0) {
												scroll_view.smoothScrollTo(off,
														0);
											}
										}
									}, 100);

									hashMap.put(path, imageView);
									mSelectedPhotos.add(path);
									fb.display(imageView, mPhotos.get(position));
									imageView
											.setOnClickListener(new View.OnClickListener() {

												@Override
												public void onClick(View v) {
													toggleButton
															.setChecked(false);
													removePath(path);
												}
											});
									okButton.setText("完成("
											+ mSelectedPhotos.size() + "/"
											+ Util.MAX_PHOTOS + ")");
								}
							} else {
								removePath(path);
							}
						}
					});
		}
	}

	private class showAlbumsList extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			// albums = Util.ALBUMS;
			albums = Util.getAlbums(mContext);

			mSelectedPhotos.addAll(intent
					.getStringArrayListExtra(Util.SELECT_PIC));

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			int count = mSelectedPhotos.size();
			for (int i = 0; i < count; i++) {
				String mPath = mSelectedPhotos.get(i);
				setGridImageView(mPath, null);
			}

			setAlbumsList();
			progressDialog.dismiss();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public UILApplication getMyApplication() {
		return ((UILApplication) super.getApplicationContext());
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void setGridImageView(final String mPath,
			final ToggleButton toggleBtn) {

		ImageView imageView = (ImageView) LayoutInflater.from(
				MyAlbumActivity.this).inflate(R.layout.album_choose_imageview,
				selectedImageLayout, false);
		selectedImageLayout.addView(imageView);
		imageView.postDelayed(new Runnable() {
			@Override
			public void run() {
				int off = selectedImageLayout.getMeasuredWidth()
						- scroll_view.getWidth();
				if (off > 0) {
					scroll_view.smoothScrollTo(off, 0);
				}
			}
		}, 100);

		hashMap.put(mPath, imageView);
		if (toggleBtn != null) {
			mSelectedPhotos.add(mPath);
		}
		fb.display(imageView, mPath);
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gridViewAdapter != null) {
					gridViewAdapter.notifyDataSetChanged();
				}
				if (toggleBtn != null) {
					toggleBtn.setChecked(false);
				}
				removePath(mPath);
			}
		});
		okButton.setText("完成(" + mSelectedPhotos.size() + "/" + Util.MAX_PHOTOS
				+ ")");

	}

}
