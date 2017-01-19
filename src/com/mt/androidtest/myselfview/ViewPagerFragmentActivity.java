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
	
	//ViewPager��ʹ��
	private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.myviewpager);
        mTV = (TextView) findViewById(R.id.tv_fragment_adapter);
        mFragmentPagerAdapter = new MyFragmentStatePagerAdapter(getFragmentManager());//ViewPager��ʾFragment���ȸ��Ƽ�����
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mTV.setText("FragmentStatePagerAdapter");
        mMyonPageChangeListener = new MyFragmentOnPageChangeListener();
		mViewPager.addOnPageChangeListener(mMyonPageChangeListener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// -------------��menu����������С���Ӳ˵�-------------
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
			mFragmentPagerAdapter = new MyFragmentStatePagerAdapter(getFragmentManager());//ViewPager��ʾFragment���ȸ��Ƽ�����
			mTV.setText("FragmentStatePagerAdapter");
			break;		
		case Menu_adatper:
			mFragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());//ViewPager��ʾFragment���ȸ��Ƽ�����
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
	 * FragmentPagerAdapter��FragmentStatePagerAdapter����Ҫ������������fragment�Ƿ����٣�����ϸ˵��
	 * ����1)FragmentPagerAdapter�����ڲ�����Ҫ��fragment��ѡ�����onDestroyView��������������ͼ������������fragmentʵ����
	 * ���֮ǰ���ع���fragment�ᱣ�����ڴ��У���������ﵽһ��ֵ�����������ڴ档
	 * ����2)FragmentStatePagerAdapter�������ٲ�����Ҫ��fragment������ǰ�����ύ�Ժ󣬻᳹�׵Ľ�fragmeng�ӵ�ǰActivity
	 * ��FragmentManager���Ƴ�����ȻҲ����ڴ����Ƴ�(������ǰ��Ŀ���Ҹ�һ��������ĵ���onDestroyView��onDestroy��onDetach��������)��state������
	 * ����ʱ���Ὣ��onSaveInstanceState(Bundle outState)�е�bundle��Ϣ�������������û��л�����������ͨ����bundle�ָ������µ�fragment��
	 * Ҳ����˵���������onSaveInstanceState(Bundle outState)�����б���һЩ���ݣ���onCreate�н��лָ�������
	 * ��֮��ʹ��FragmentStatePagerAdapter��Ȼ��ʡ�ڴ棬���������½�Ҳ����Ҫʱ��ġ�һ������£��������������ҳ�棬
	 * ��3��4��Tab����ô����ѡ��ʹ��FragmentPagerAdapter�������������ViewPagerչʾ�����ر�����Ŀʱ����ô����ʹ��
	 * FragmentStatePagerAdapter��
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
	 * FragmentPagerAdapter��רΪ���Fragment���
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
        public Object instantiateItem(ViewGroup container, int position) {  //�����������ʵ����ҳ�� 
             return super.instantiateItem(container, position); 
        }  
    }
	
	/**
	 * ViewPager+Fragment��ʽ��ҳ��䶯ʱ�ļ�����
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
