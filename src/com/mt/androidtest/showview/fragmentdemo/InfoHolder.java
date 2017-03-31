package com.mt.androidtest.showview.fragmentdemo;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mt.androidtest.BaseFragment;
import com.mt.androidtest.R;

public class InfoHolder{
	private View mBaseView;
	private ImageView mImageView;
	private TextView mTextView;
	
	public InfoHolder(Activity mActivity, int BaseViewID, int ImageViewID, int TextViewID){
		mBaseView    = mActivity.findViewById(BaseViewID);
		mBaseView.setOnClickListener((OnClickListener)mActivity);
		mImageView = (ImageView) mActivity.findViewById(ImageViewID);
		mTextView     = (TextView) mActivity.findViewById(TextViewID);
	}
	
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
	
	public static final int[] TextViewIDs={
		R.id.message_text,
		R.id.contacts_text,
		R.id.news_text,
		R.id.setting_text
	};
	
	public static final int[] DrawableSelectedIDs={
		R.drawable.message_selected,
		R.drawable.contacts_selected,
		R.drawable.news_selected,
		R.drawable.setting_selected
	};
	
	public static final int[] DrawableUnSelectedIDs={
		R.drawable.message_unselected,
		R.drawable.contacts_unselected,
		R.drawable.news_unselected,
		R.drawable.setting_unselected
	};
	
	public void setSelected(int drawableSelectedID){
		mImageView.setImageResource(drawableSelectedID);
		mTextView.setTextColor(Color.WHITE);
	}
	
	public void setUnSelected(int drawableUnSelectedID){
		mImageView.setImageResource(drawableUnSelectedID);
		mTextView.setTextColor(Color.parseColor("#82858b"));
	}
	
	//以下为fragments信息
	public static final String[] FragmentsTAG={
		MessageFragment.class.getName(),
		ContactsFragment.class.getName(),
		NewsFragment.class.getName(),
		SettingFragment.class.getName(),
	};
	
	/**
	 * getFragment：不使用下列静态数组的原因是FragmentManager可以有效管理fragments，下列写法会造成多余的内存开支，
	 * 增加内存泄露风险。
		public static final BaseFragment[] mBaseFragments={
				new MessageFragment(),
				new ContactsFragment(),
				new NewsFragment(),
				new SettingFragment()
		};	 
	 * @param index
	 * @return
	 */
	public static Fragment getFragment(int index){
		Fragment mFragment = null;
		switch(index){
			case 0:
				mFragment = new InfoHolder.MessageFragment();
				break;
			case 1:
				mFragment = new InfoHolder.ContactsFragment();
				break;
			case 2:
				mFragment = new InfoHolder.NewsFragment();
				break;
			case 3:
				mFragment = new InfoHolder.SettingFragment();
				break;
		}
		return mFragment;
	}
	
	public static class MessageFragment extends BaseFragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			View messageLayout = inflater.inflate(R.layout.message_fragment,	container, false);
			return messageLayout;
		}
	}
	
	public static class ContactsFragment extends BaseFragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			View contactsLayout = inflater.inflate(R.layout.contacts_fragment, container, false);
			return contactsLayout;
		}
	}
	
	public static class NewsFragment extends BaseFragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			View newsLayout = inflater.inflate(R.layout.news_fragment, container, false);
			return newsLayout;
		}
	}
	
	public static class SettingFragment extends BaseFragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			View settingLayout = inflater.inflate(R.layout.setting_fragment, container, false);
			return settingLayout;
		}
	}

}
