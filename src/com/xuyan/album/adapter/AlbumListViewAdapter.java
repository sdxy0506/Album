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

import com.xuyan.album.Album;
import com.xuyan.album.R;

/**
 * Created with IntelliJ IDEA.
 * User: hetuo
 * Date: 13-10-21
 * Time: 下午3:46
 * To change this template use File | Settings | File Templates.
 */

/**
 * 相册列表适配器
 */
public class AlbumListViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Album> mAlbums;
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
		TextView mAlbumNum;
	}

	/**
	 * 适配器构造器
	 * 
	 * @param ctx
	 *            Context 上下文
	 */
	public AlbumListViewAdapter(Context ctx, FinalBitmap fBitmap) {
		this.setContext(ctx);
		mAlbums = new ArrayList<Album>(0);
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		fb = fBitmap;
	}

	/**
	 * 设置新的相册数据
	 */
	public void setAlbumsList(List<Album> albums) {
		mAlbums.clear();
		mAlbums.addAll(albums);
		notifyDataSetChanged();// 通知刷新listView。
	}

	/**
	 * 添加相册数据
	 */
	public void addAlbums(List<Album> albums) {
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
			view = mInflater.inflate(R.layout.album_list_item, null);
			holder = new ViewHolder();
			// 获取到资源
			holder.mAlbumCover = (ImageView) view
					.findViewById(R.id.album_cover);
			holder.mAlbumName = (TextView) view.findViewById(R.id.album_name);
			holder.mAlbumNum = (TextView) view.findViewById(R.id.album_num);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Log.d(TAG, String.format(" poistion = %d", position));

		Album album = mAlbums.get(position);
		holder.mAlbumName.setText(album.mName);
		holder.mAlbumNum.setText(album.mNum);
		fb.display(holder.mAlbumCover, album.mCoverUrl);

		return view;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
