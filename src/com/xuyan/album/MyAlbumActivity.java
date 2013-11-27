package com.xuyan.album;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import de.greenrobot.event.EventBus;

public class MyAlbumActivity extends Activity {
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

	// 避免list，grid滑动滞后
	PauseOnScrollListener pauseListener = new PauseOnScrollListener(
			Constants.imageLoader, false, true);

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_main);

		mContext = this;
		eventBus = EventBus.getDefault();
		eventBus.register(this);

		listView = (ListView) this.findViewById(R.id.list_view);
		gridView = (GridView) this.findViewById(R.id.grid_view);

		// 避免list，grid滑动滞后
		listView.setOnScrollListener(pauseListener);
		gridView.setOnScrollListener(pauseListener);

		listViewAdapter = new AlbumListViewAdapter(mContext);

		selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layout);
		scroll_view = (HorizontalScrollView) findViewById(R.id.scrollview);
		btn_album = (Button) this.findViewById(R.id.photo);
		btn_cancel = (Button) this.findViewById(R.id.cancel);
		title = (TextView) this.findViewById(R.id.title);
		okButton = (Button) this.findViewById(R.id.ok_button);
		btn_album.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listView.setVisibility(View.VISIBLE);
				gridView.setVisibility(View.INVISIBLE);
				btn_album.setVisibility(View.INVISIBLE);
				title.setText("相册");
			}
		});

		progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage("正在读取相册列表");
		progressDialog.show();
		new showAlbumsList().execute();
	}

	private void setAlbumsList() {

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
				okButton.setText("完成(" + mSelectedPhotos.size() + "/8)");
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
			mPhotos = Constants.getPhotos(mContext, album_dir);
			gridViewAdapter = new GridViewAdapter(getApplicationContext(),
					mPhotos, mSelectedPhotos);
			gridView.setAdapter(gridViewAdapter);
			gridViewAdapter
					.setOnItemClickListener(new GridViewAdapter.OnItemClickListener() {
						@Override
						public void onItemClick(
								final ToggleButton toggleButton, int position,
								final String path, boolean isChecked) {
							if (mSelectedPhotos.size() >= 8) {
								toggleButton.setChecked(false);
								if (!removePath(path)) {
									Toast.makeText(MyAlbumActivity.this, "只能选择8张图片",
											Toast.LENGTH_SHORT).show();
								}
								return;
							}

							if (isChecked) {
								if (!hashMap.containsKey(path)) {
									ImageView imageView = (ImageView) LayoutInflater
											.from(MyAlbumActivity.this).inflate(
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
												scroll_view.smoothScrollTo(off,
														0);
											}

										}
									}, 100);

									hashMap.put(path, imageView);
									mSelectedPhotos.add(path);
									Constants.imageLoader.displayImage(
											"file://" + mPhotos.get(position),
											imageView,
											Constants.image_display_options,
											new Util.AnimateFirstDisplayListener());
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
											+ mSelectedPhotos.size() + "/8)");

								}
							} else {
								removePath(path);
							}
						}
					});
		}
	}

	private UILApplication setApplication() {
		return ((UILApplication) super.getApplicationContext());
	}

	private class showAlbumsList extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			albums = setApplication().getAlbums();
			if (albums.size() == 0) {
				albums = Constants.getAlbums(mContext);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			setAlbumsList();
			progressDialog.dismiss();
		}

	}
}