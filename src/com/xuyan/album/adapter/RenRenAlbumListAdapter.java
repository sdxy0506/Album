package com.xuyan.album.adapter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuyan.album.R;
import com.xuyan.album.object.RenRenAlbum;

/**
 * 文件名称：RenRenAlbumListAdapter.java 类说明：
 * 
 * @create 2014-2-19 下午5:52:59 项目名称：MyAlbum
 * @author xuyan
 * @version 1.0
 */
public class RenRenAlbumListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<RenRenAlbum> mAlbums;
	private Context context;
	private FinalBitmap fb;

	private String TAG = AlbumListViewAdapter.class.getSimpleName();

	/**
	 * 列表项目View持有者
	 * 
	 */
	public class ViewHolder {
		ImageView mAlbumCover;
		TextView mAlbumName;
	}

	/**
	 * 适配器构造器
	 * 
	 * @param ctx
	 *            Context 上下文
	 */
	public RenRenAlbumListAdapter(Context ctx, FinalBitmap fBitmap) {
		this.setContext(ctx);
		mAlbums = new ArrayList<RenRenAlbum>();
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		fb = fBitmap;
	}

	/**
	 * 设置新的相册数据
	 */
	public void setAlbumsList(List<RenRenAlbum> albums) {
		mAlbums.clear();
		mAlbums.addAll(albums);
		notifyDataSetChanged();// 通知刷新listView。
	}

	/**
	 * 添加相册数据
	 */
	public void addAlbums(List<RenRenAlbum> albums) {
		mAlbums.addAll(albums);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mAlbums.size();
	}

	/**
	 * 清除所有项目。
	 */
	public void clear() {
		mAlbums.clear();
	}

	@Override
	public Object getItem(int position) {
		if (mAlbums.isEmpty() || position >= mAlbums.size()) {
			return null;
		}
		return mAlbums.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		if (view == null) {
			view = mInflater.inflate(R.layout.renren_album_list_item, null);
			holder = new ViewHolder();
			// 获取到资源
			holder.mAlbumCover = (ImageView) view
					.findViewById(R.id.album_list_item_cover);
			holder.mAlbumName = (TextView) view
					.findViewById(R.id.album_list_item_name);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Log.d(TAG, String.format(" poistion = %d", position));

		RenRenAlbum album = mAlbums.get(position);
		holder.mAlbumName.setText(album.getName());
		fb.display(holder.mAlbumCover, album.getCover().get(1).getUrl());

		return view;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
