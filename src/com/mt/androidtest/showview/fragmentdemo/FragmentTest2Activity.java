package com.mt.androidtest.showview.fragmentdemo;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.BaseFragment;
import com.mt.androidtest.R;

public class FragmentTest2Activity extends BaseActivity implements OnClickListener{
	private FragmentManager mFragmentManager=null;
	private ArrayList<InfoHolder>mInfoHolderAL=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_test2);
		mFragmentManager = getFragmentManager();
		doInit();
	}

	private void doInit() {
		mInfoHolderAL=new ArrayList<InfoHolder>();
		for(int i=0;i<InfoHolder.BaseViewIDs.length;i++){
			mInfoHolderAL.add(new InfoHolder(this,InfoHolder.BaseViewIDs[i],InfoHolder.ImageViewIDs[i],
					InfoHolder.TextViewIDs[i]));
		}
		setTabSelection(0);
	}
	

	@Override
	public void onClick(View v) {
		int layoutID = v.getId();
		for(int i=0;i<InfoHolder.BaseViewIDs.length;i++){
			if(layoutID==InfoHolder.BaseViewIDs[i]){
				setTabSelection(i);
				break;
			}
		}
	}

	private void setTabSelection(int index) {
		clearSelection();
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		hideFragments(transaction);
		InfoHolder mInfoHolder = mInfoHolderAL.get(index);
		mInfoHolder.setSelected(InfoHolder.DrawableSelectedIDs[index]);
		BaseFragment mFragment = mInfoHolder.getFragment();
		if (mFragment == null) {
			mInfoHolder.setFragment(InfoHolder.mBaseFragments[index]);
			transaction.add(R.id.content, mInfoHolder.getFragment());
		}else{
			transaction.show(mFragment);
		}
		transaction.commit();
	}
	
	private void clearSelection() {
		InfoHolder mInfoHolder = null;
		for(int i=0;i<mInfoHolderAL.size();i++){
			mInfoHolder = mInfoHolderAL.get(i);
			mInfoHolder.setUnSelected(InfoHolder.DrawableUnSelectedIDs[i]);
		}
	}

	private void hideFragments(FragmentTransaction transaction) {
		BaseFragment mFragment = null;
		for(int i=0;i<InfoHolder.mBaseFragments.length;i++){
			mFragment = mInfoHolderAL.get(i).getFragment();
			if(null!=mFragment)transaction.hide(mFragment);
		}
	}
}
