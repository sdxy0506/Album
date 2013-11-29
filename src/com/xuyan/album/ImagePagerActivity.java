/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.xuyan.album;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xuyan.album.adapter.ImagePagerAdapter;
import com.xuyan.util.Util;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImagePagerActivity extends Activity {

	private ViewPager pager;
	private Context mContext;
	private ArrayList<String> mSelectedPhotos = new ArrayList<String>();

	private ImageButton btn_del;
	private Button btn_ok;
	private TextView tv_noPic;
	FinalBitmap fb;
	private ImagePagerAdapter imagePagerAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_pager);
		mContext = this;

		fb = FinalBitmap.create(mContext);
		fb.configLoadfailImage(R.drawable.ic_error);
		fb.configLoadingImage(R.drawable.ic_stub);
		findViews();
		init();

	}

	private void init() {
		Bundle bundle = getIntent().getExtras();
		mSelectedPhotos.addAll(bundle.getStringArrayList(Util.SELECT_PIC));
		int pagerPosition = bundle.getInt(Util.POSITION_PIC, 0);
		imagePagerAdapter = new ImagePagerAdapter(mContext, mSelectedPhotos, fb);
		pager.setAdapter(imagePagerAdapter);
		pager.setCurrentItem(pagerPosition);
		btn_del.setOnClickListener(btnListener);
		btn_ok.setOnClickListener(btnListener);
		pager.setClickable(false);
	}

	private void findViews() {
		pager = (ViewPager) findViewById(R.id.image_pager);
		btn_ok = (Button) findViewById(R.id.image_ok);
		btn_del = (ImageButton) findViewById(R.id.image_del);
		tv_noPic = (TextView) findViewById(R.id.image_no);
	}

	private OnClickListener btnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.image_del:
				if (mSelectedPhotos.size() > 0) {
					int position = pager.getCurrentItem();
					mSelectedPhotos.remove(position);
					imagePagerAdapter.notifyDataSetChanged();
					if (mSelectedPhotos.size() == 0) {
						tv_noPic.setVisibility(View.VISIBLE);
					}
				}
				break;
			case R.id.image_ok:
				Intent data = new Intent();
				data.putStringArrayListExtra(Util.SELECT_PIC, mSelectedPhotos);
				setResult(RESULT_OK, data);
				finish();
			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}