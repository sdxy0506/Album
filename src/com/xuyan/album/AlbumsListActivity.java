package com.xuyan.album;

import java.util.ArrayList;

import com.xuyan.album.adapter.AlbumListViewAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class AlbumsListActivity extends Activity {
	private ListView listView;
	private AlbumListViewAdapter listViewAdapter;
	private ArrayList<Album> albums;
	private Context mContext;
	private ProgressDialog progressDialog;

	private Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_list);
		mContext = this;
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(mContext, MyAlbumActivity.class);
				startActivity(intent);
				finish();
			}
		});
		listView = (ListView) this.findViewById(R.id.list_view);
		listViewAdapter = new AlbumListViewAdapter(mContext);
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
				setApplication().setAlbums(albums);
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("AlbumId", i);
				intent.setClass(mContext, AlbumActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();

			}
		});
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
			setApplication().setAlbums(albums);
			progressDialog.dismiss();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

}
