package com.mt.androidtest.showview.fragmentdemo;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.BaseFragment;
import com.mt.androidtest.R;

public class FragmentTestActivity extends BaseActivity  implements OnClickListener, SummaryListFragment.Callback{
	private static final int Menu_Fragment1 = 0x00;
	private static final int Menu_Fragment2 = 0x01;

	private FragmentManager mFragmentManager=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getFragmentManager();
	}

	@Override
	public void onItemSelected(Integer id){
		Bundle arguments = new Bundle();
		arguments.putInt(DetailInfoFragment.ITEM_ID, id);
		Fragment fragment = new DetailInfoFragment();
		fragment.setArguments(arguments);
		//
	    FragmentTransaction mFragmentTransaction=mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.info_detail_container, fragment);//mFragmentTransaction.add(R.id.info_detail_container, fragment);
        mFragmentTransaction.addToBackStack(null);//可实现类似于按手机返回键返回上一页的效果，因为将上面的add/replace操作压入栈内
		mFragmentTransaction.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// -------------向menu中添加字体大小的子菜单-------------
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu_Fragment1, 0, "Info");
		menu.add(0, Menu_Fragment2, 0, "QQ");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)	{
		switch (mi.getItemId()){
		case Menu_Fragment1:
			setContentView(R.layout.activity_fragment_test);
			break;
		case Menu_Fragment2:
			setContentView(R.layout.activity_fragment_test2);
			doInit();
			break;
		}
		return super.onOptionsItemSelected(mi);
	}	

	private void doInit() {
		for(int i=0;i<InfoHolder.BaseViewIDs.length;i++){
			mInfoHolderAL.add(new InfoHolder(this,InfoHolder.BaseViewIDs[i],InfoHolder.ImageViewIDs[i],
					InfoHolder.TextViewIDs[i]));
		}
		setTabSelection(0);
	}

	
	private ArrayList<InfoHolder>mInfoHolderAL=new ArrayList<InfoHolder>();

	
	
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
			transaction.add(R.id.content, InfoHolder.mBaseFragments[index]);
		}else{
			transaction.show(mFragment);
		}
		transaction.commit();
	}

	private int[] DrawableUnSelectedIDs={
			R.drawable.message_unselected,
			R.drawable.contacts_unselected,
			R.drawable.news_unselected,
			R.drawable.setting_unselected
	};
	
	private void clearSelection() {
		InfoHolder mInfoHolder = null;
		for(int i=0;i<mInfoHolderAL.size();i++){
			mInfoHolder = mInfoHolderAL.get(i);
			mInfoHolder.setUnSelected(DrawableUnSelectedIDs[i]);
		}
		
		
//		messageImage.setImageResource(R.drawable.message_unselected);
//		messageText.setTextColor(Color.parseColor("#82858b"));
//		contactsImage.setImageResource(R.drawable.contacts_unselected);
//		contactsText.setTextColor(Color.parseColor("#82858b"));
//		newsImage.setImageResource(R.drawable.news_unselected);
//		newsText.setTextColor(Color.parseColor("#82858b"));
//		settingImage.setImageResource(R.drawable.setting_unselected);
//		settingText.setTextColor(Color.parseColor("#82858b"));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		BaseFragment mFragment = null;
		for(int i=0;i<InfoHolder.mBaseFragments.length;i++){
			mFragment = mInfoHolderAL.get(i).getFragment();
			if(null!=mFragment)transaction.hide(mFragment);
		}
		
		
		
//		
//		if (messageFragment != null) {
//			transaction.hide(messageFragment);
//		}
//		if (contactsFragment != null) {
//			transaction.hide(contactsFragment);
//		}
//		if (newsFragment != null) {
//			transaction.hide(newsFragment);
//		}
//		if (settingFragment != null) {
//			transaction.hide(settingFragment);
//		}
	}
}
