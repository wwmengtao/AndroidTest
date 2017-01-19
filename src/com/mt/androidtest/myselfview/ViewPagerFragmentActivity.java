package com.mt.androidtest.myselfview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class ViewPagerFragmentActivity extends BaseActivity{
	//
	private static final int Menu_state_adatper = 0;
	private static final int Menu_adatper = 1;	
	//
    public static final int NUM_PAGES = 10;
    private ViewPager mViewPager;
    private TextView mTV = null;
    private OnPageChangeListener mMyonPageChangeListener=null, mMyonPageChangeListener2=null;
    private PagerAdapter mFragmentPagerAdapter;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager_fragment);
		initView();
	}
	
	//ViewPager的使用
	private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.myviewpager);
        mTV = (TextView) findViewById(R.id.tv_fragment_adapter);
        mFragmentPagerAdapter = new MyFragmentStatePagerAdapter(getFragmentManager());//ViewPager显示Fragment，谷歌推荐做法
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mTV.setText("FragmentStatePagerAdapter");
        mMyonPageChangeListener = new MyFragmentOnPageChangeListener();
		mViewPager.addOnPageChangeListener(mMyonPageChangeListener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// -------------向menu中添加字体大小的子菜单-------------
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu_state_adatper, 0, "FragmentStatePagerAdapter");
		menu.add(0, Menu_adatper, 0, "FragmentPagerAdapter");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)	{
		mViewPager.removeOnPageChangeListener(mMyonPageChangeListener);
		ScreenSlidePageFragment.clearFragments();
		switch (mi.getItemId()){
		case Menu_state_adatper:
			mFragmentPagerAdapter = new MyFragmentStatePagerAdapter(getFragmentManager());//ViewPager显示Fragment，谷歌推荐做法
			mTV.setText("FragmentStatePagerAdapter");
			break;		
		case Menu_adatper:
			mFragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());//ViewPager显示Fragment，谷歌推荐做法
			mTV.setText("FragmentPagerAdapter");
			break;		
		}
		mViewPager.addOnPageChangeListener(mMyonPageChangeListener);
		mViewPager.setAdapter(mFragmentPagerAdapter);
		return super.onOptionsItemSelected(mi);
	}	
	
	@Override
	public void onStop(){
		super.onStop();
		if(null != mViewPager){
			mViewPager.removeOnPageChangeListener(mMyonPageChangeListener);
			mViewPager.removeOnPageChangeListener(mMyonPageChangeListener2);
		}
	}
	
	/**
	 * FragmentPagerAdapter与FragmentStatePagerAdapter的主要区别就在与对于fragment是否销毁，下面细说：
	 * 区别1)FragmentPagerAdapter：对于不再需要的fragment，选择调用onDestroyView方法，仅销毁视图，并不会销毁fragment实例。
	 * 因此之前加载过的fragment会保存在内存中，如果数量达到一定值会显著消耗内存。
	 * 区别2)FragmentStatePagerAdapter：会销毁不再需要的fragment，当当前事务提交以后，会彻底的将fragmeng从当前Activity
	 * 的FragmentManager中移除，当然也会从内存中移除(保留当前项目左右各一个，其余的调用onDestroyView、onDestroy、onDetach彻底销毁)。state标明，
	 * 销毁时，会将其onSaveInstanceState(Bundle outState)中的bundle信息保存下来，当用户切换回来，可以通过该bundle恢复生成新的fragment，
	 * 也就是说，你可以在onSaveInstanceState(Bundle outState)方法中保存一些数据，在onCreate中进行恢复创建。
	 * 总之：使用FragmentStatePagerAdapter当然更省内存，但是销毁新建也是需要时间的。一般情况下，如果你是制作主页面，
	 * 就3、4个Tab，那么可以选择使用FragmentPagerAdapter，如果你是用于ViewPager展示数量特别多的条目时，那么建议使用
	 * FragmentStatePagerAdapter。
	 */
	
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
	
	/**
	 * FragmentPagerAdapter：专为添加Fragment设计
	 * @author Mengtao1
	 *
	 */
	public static class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
			Fragment mFragment = ScreenSlidePageFragment.get(position);
			ALog.Log1("FragmentPagerAdapter.getItem_position: "+position);
//			ScreenSlidePageFragment.scanFragments();
			return mFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
        
    	@Override  
        public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡 
             return super.instantiateItem(container, position); 
        }  
    }
	
	/**
	 * ViewPager+Fragment形式的页面变动时的监听器
	 * @author Mengtao1
	 *
	 */
	public static class MyFragmentOnPageChangeListener implements OnPageChangeListener{
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
//			ALog.Log("onPageScrollStateChanged: "+arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
//			ALog.Log("onPageScrolled_arg0: "+arg0+" arg1: "+arg1+" arg2: "+arg2);
		}

		@Override
		public void onPageSelected(int currentIndex) {
			// TODO Auto-generated method stub
			ALog.Log1("mViewPager.getCurrentIndex :"+currentIndex+" "+ScreenSlidePageFragment.get(currentIndex));
//			ScreenSlidePageFragment.scanFragments();
		}
		
	}
}
