package com.mt.androidtest.showview.fragmentdemo;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mt.androidtest.BaseFragment;
import com.mt.androidtest.R;

public class InfoHolder{

	public static final BaseFragment[] mBaseFragments={
			new MessageFragment(),
			new ContactsFragment(),
			new NewsFragment(),
			new SettingFragment()
	};
	
	public static final int[] BaseViewIDs={
			R.id.message_layout,
			R.id.contacts_layout,
			R.id.news_layout,
			R.id.setting_layout
	};

	public static final int[] ImageViewIDs={
			R.id.message_image,
			R.id.contacts_image,
			R.id.news_image,
			R.id.setting_image
	};
	
	public static final int[] DrawableSelectedIDs={
			R.drawable.message_selected,
			R.drawable.contacts_selected,
			R.drawable.news_selected,
			R.drawable.setting_selected
	};
	
	public static final int[] TextViewIDs={
			R.id.message_text,
			R.id.contacts_text,
			R.id.news_text,
			R.id.setting_text
	};

	private BaseFragment mFragment=null;
	private View mBaseView;
	private ImageView mImageView;
	private TextView mTextView;
	
	public InfoHolder(Activity mActivity, int BaseViewID, int ImageViewID, int TextViewID){
		mBaseView    = mActivity.findViewById(BaseViewID);
		mBaseView.setOnClickListener((OnClickListener)mActivity);
		mImageView = (ImageView) mActivity.findViewById(ImageViewID);
		mTextView     = (TextView) mActivity.findViewById(TextViewID);
	}
	
	public void setFragment(BaseFragment mFragment){
		this.mFragment = mFragment;
	}
	
	public BaseFragment getFragment(){
		return mFragment;
	}
	
//	public ImageView getImageView(){
//		return mImageView;
//	}
//	public TextView getTextView(){
//		return mTextView;
//	}
	public void setSelected(int drawableSelectedID){
		mImageView.setImageResource(drawableSelectedID);
		mTextView.setTextColor(Color.WHITE);
	}
	
	public void setUnSelected(int drawableUnSelectedID){
		mImageView.setImageResource(drawableUnSelectedID);
		mTextView.setTextColor(Color.parseColor("#82858b"));
	}
}
