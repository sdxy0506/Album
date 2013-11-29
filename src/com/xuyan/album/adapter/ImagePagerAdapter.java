package com.xuyan.album.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xuyan.album.R;

public class ImagePagerAdapter extends PagerAdapter {

	private ArrayList<String> mSelectedImages;
	private LayoutInflater inflater;
	private Context context;
	private FinalBitmap fb;

	public ImagePagerAdapter(Context contexts, ArrayList<String> images,
			FinalBitmap fBitmap) {
		this.mSelectedImages = images;
		this.context = contexts;
		inflater = (LayoutInflater) contexts
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.fb = fBitmap;
		;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView((View) object);
	}

	@Override
	public int getCount() {
		return mSelectedImages.size();
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View imageLayout = inflater.inflate(R.layout.item_pager_image, view,
				false);
		ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		fb.display(imageView, mSelectedImages.get(position));
		view.addView(imageLayout, 0);
		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	/**
	 * 此处是关键，要讲返回值重写为POSITION_NONE，才可以在主界面中调用notifyDataSetChanged()来刷新数据
	 */
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
