package com.mt.androidtest.showview.fragmentdemo;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

/**
 *AndroidManifest.xml中，FragmentTest2Activity不要加android:configChanges="orientation|keyboardHidden|screenSize"，
 *以此来模拟内存重启(此时为转屏销毁重建)的情况
 * @author Mengtao1
 *
 */
public class FragmentTest2Activity extends BaseActivity implements OnClickListener{
    private static final String STATE_SAVE_IS_SHOWN = "FRAGMENT_SHOWN_ID";
	private FragmentManager mFragmentManager=null;
	private ArrayList<InfoHolder>mInfoHolderAL=null;
	private int currentFragmentID = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_test2);
		doInit();
		mFragmentManager = getFragmentManager();
		if(null != savedInstanceState){//发生内存重启或者转屏时，恢复之前显示的fragment
			currentFragmentID = savedInstanceState.getInt(STATE_SAVE_IS_SHOWN);
		}
		selectFragment(currentFragmentID);
	}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);
    	savedInstanceState.putInt(STATE_SAVE_IS_SHOWN, currentFragmentID);
    }
    
	private void doInit() {
		mInfoHolderAL=new ArrayList<InfoHolder>();
		for(int i=0;i<InfoHolder.BaseViewIDs.length;i++){
			mInfoHolderAL.add(new InfoHolder(this,
					InfoHolder.BaseViewIDs[i],
					InfoHolder.ImageViewIDs[i],
					InfoHolder.TextViewIDs[i]));
		}
	}
	
	@Override
	public void onClick(View v) {
		int layoutID = v.getId();
		for(int i=0;i<InfoHolder.BaseViewIDs.length;i++){
			if(layoutID==InfoHolder.BaseViewIDs[i]){
				currentFragmentID = i;
				selectFragment(i);
				break;
			}
		}
	}

	/**
	 * selectFragment：当指定位置的fragment需要显示的时候需要做好其他位置fragment以及对于控件的资源显示工作。
	 * @param index
	 */
	private void selectFragment(int index) {
		reSetSelection(index);
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		Fragment mFragment = null;
		for(int i=0;i<InfoHolder.BaseViewIDs.length;i++){
			mFragment = mFragmentManager.findFragmentByTag(InfoHolder.FragmentsTAG[i]);
			if(null == mFragment){//为空，表明mFragmentManager没有保存mFragment，需要创建
				mFragment = InfoHolder.getFragment(i);
				transaction.add(R.id.content, mFragment, InfoHolder.FragmentsTAG[i]);
			}
			if(index != i){
				transaction.hide(mFragment);
			}else{
				transaction.show(mFragment);
			}
		}
		transaction.commit();
	}
	
	/**
	 * reSetSelection：重置所有fragments对应的控件显示信息
	 * @param index 此位置的fragment及其对应控件信息设置为“选中”
	 */
	private void reSetSelection(int index) {
		InfoHolder mInfoHolder = null;
		for(int i=0;i<mInfoHolderAL.size();i++){
			mInfoHolder = mInfoHolderAL.get(i);
			if(index != i){
				mInfoHolder.setUnSelected(InfoHolder.DrawableUnSelectedIDs[i]);
			}else{
				mInfoHolder.setSelected(InfoHolder.DrawableSelectedIDs[i]);
			}
		}
	}

}
