package com.mt.androidtest.myselfview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.mt.androidtest.ALog;

public class ViewPagerStateFragmentActivity extends ViewPagerFragmentActivity{
    
	@Override
	public void doInit(){
		ALog.Log2("doInit2");
        mTV.setText("FragmentStatePagerAdapter");
        mFragmentPagerAdapter = new MyFragmentStatePagerAdapter(getFragmentManager());//ViewPager显示Fragment，谷歌推荐做法
	}
	/**
	 * FragmentStatePagerAdapter
	 * @author Mengtao1
	 *
	 */
	public static class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

		public MyFragmentStatePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment mFragment = ScreenSlidePageFragment.get(position);
			ALog.Log1("FragmentStatePagerAdapter.getItem_position: "+position);
//			ScreenSlidePageFragment.scanFragments();
			return mFragment;
		}
		
		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}
}
