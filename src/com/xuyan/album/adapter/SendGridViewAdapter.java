package com.xuyan.album.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.xuyan.album.Constants;
import com.xuyan.album.R;
import com.xuyan.album.Util;

public class SendGridViewAdapter extends BaseAdapter {

	private ImageLoadingListener animateFirstListener = new Util.AnimateFirstDisplayListener();

	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<String> mSelectedPhotos;

	public SendGridViewAdapter(Context context, ArrayList<String> photos) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mSelectedPhotos = photos;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (mSelectedPhotos != null) {
			count = mSelectedPhotos.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return mSelectedPhotos.get(position);
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
			holder.mPic = (ImageView) view.findViewById(R.id.image_view);
			holder.mButton = (ToggleButton) view
					.findViewById(R.id.toggle_button);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.mButton.setClickable(false);
		Constants.imageLoader.displayImage("file://" + getItem(position),
				holder.mPic, Constants.image_display_options,
				animateFirstListener);
		return view;
	}

	private class ViewHolder {
		private ImageView mPic;
		private ToggleButton mButton;
	}
}
