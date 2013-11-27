package com.xuyan.album.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ToggleButton;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.xuyan.album.Constants;
import com.xuyan.album.R;
import com.xuyan.album.Util;
import com.xuyan.album.R.id;
import com.xuyan.album.R.layout;
import com.xuyan.album.Util.AnimateFirstDisplayListener;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA. User: hetuo Date: 13-10-28 Time: 上午10:12 To
 * change this template use File | Settings | File Templates.
 */
public class GridViewAdapter extends BaseAdapter implements OnClickListener {
	private ImageLoadingListener animateFirstListener = new Util.AnimateFirstDisplayListener();

	private LayoutInflater mInflater;
	private Context context;
	private ArrayList<String> mPhotos;
	private ArrayList<String> mSelectedPhotos;

	private String TAG = GridViewAdapter.class.getSimpleName();

	/**
	 * 列表项目View持有者
	 * 
	 */
	public class ViewHolder {
		public ImageView mPic;
		public ToggleButton mToggleButton;
	}

	/**
	 * 适配器构造器
	 * 
	 * @param ctx
	 *            上下文
	 * @param mPhotos
	 *            相册中的图片路径数组
	 * @param mSelectedPhotos
	 *            相册中被选中的图片路径数组
	 * 
	 */
	public GridViewAdapter(Context ctx, ArrayList<String> mPhotos,
			ArrayList<String> mSelectedPhotos) {
		this.setContext(ctx);
		this.mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mPhotos = mPhotos;
		this.mSelectedPhotos = mSelectedPhotos;
	}

	/**
	 * 设置新的数据
	 */
	public void setPhotosList(ArrayList<String> photos) {
		mPhotos.clear();
		mPhotos.addAll(photos);
		notifyDataSetChanged();// 通知刷新GridView。
	}

	@Override
	public int getCount() {
		int count = 0;
		if (mPhotos != null) {
			count = mPhotos.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return mPhotos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			view = mInflater.inflate(R.layout.select_imageview, parent, false);
			holder = new ViewHolder();
			// 获取到资源
			holder.mPic = (ImageView) view.findViewById(R.id.image_view);
			holder.mToggleButton = (ToggleButton) view
					.findViewById(R.id.toggle_button);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Log.d(TAG, String.format(" poistion = %d", position));
		Constants.imageLoader.displayImage("file://" + mPhotos.get(position),
				holder.mPic, Constants.image_display_options,
				animateFirstListener);

		holder.mToggleButton.setTag(position);
		holder.mToggleButton.setOnClickListener(this);

		if (isInSelectedDataList(mPhotos.get(position))) {
			holder.mToggleButton.setChecked(true);
		} else {
			holder.mToggleButton.setChecked(false);
		}

		return view;
	}

	private boolean isInSelectedDataList(String selectedString) {
		for (int i = 0; i < mSelectedPhotos.size(); i++) {
			if (mSelectedPhotos.get(i).equals(selectedString)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		if (view instanceof ToggleButton) {
			ToggleButton toggleButton = (ToggleButton) view;
			int position = (Integer) toggleButton.getTag();
			if (mPhotos != null && mOnItemClickListener != null
					&& position < mPhotos.size()) {
				mOnItemClickListener.onItemClick(toggleButton, position,
						mPhotos.get(position), toggleButton.isChecked());
			}
		}
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public interface OnItemClickListener {
		public void onItemClick(ToggleButton view, int position, String path,
				boolean isChecked);
	}
}
