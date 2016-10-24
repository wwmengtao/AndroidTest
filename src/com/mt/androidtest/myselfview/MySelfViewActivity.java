package com.mt.androidtest.myselfview;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
import com.mt.androidtest.listview.ListViewTestAdapter_MultiLayout;
import com.mt.androidtest.listview.ListViewTestAdapter_SingleLayout;
/**
 * 自定义控件分为三类：自己绘制、组合、继承，即activity_customed_controller中的SelfDrawnView、CombinedView、InheritedView。
 * @author Mengtao1
 *
 */
public class MySelfViewActivity extends BaseActivity {
	private static final int Menu_Common = 0;
	private static final int Menu_Scroll = 1;	
	private static final int Menu_TabHost = 2;		
	//
    private static final int NUM_PAGES = 5;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;	
    private TextView mTextView;
    //
    private ListView mListView;
    private ListViewTestAdapter_SingleLayout mListAdapter_SingleLayout;
    private ListViewTestAdapter_MultiLayout mListAdapter_MultiLayout;
	//
    private TabHost mTabHost;
    private TabWidget mTabWidget;
    private ListView mListViewTabHost;
    //
    GridView mGridView=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mListAdapter_SingleLayout = new ListViewTestAdapter_SingleLayout(this);
        mListAdapter_MultiLayout = new ListViewTestAdapter_MultiLayout(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// -------------向menu中添加字体大小的子菜单-------------
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu_Common, 0, "Common");
		menu.add(0, Menu_Scroll, 0, "ScrollView");
		menu.add(0, Menu_TabHost, 0, "TabHost");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)	{
		switch (mi.getItemId()){
		case Menu_Common:
			setContentView(R.layout.activity_myselfview_common);
			initHorizontalScrollView();
			break;
		case Menu_Scroll:
			setContentView(R.layout.activity_myselfview_scrollview);
			initMyScrollView();
			break;			
		case Menu_TabHost:
			setContentView(R.layout.activity_myselfview_tabhost);
			initTabHostView();
			break;				
		}
		return super.onOptionsItemSelected(mi);
	}	
	//HorizontalScrollView的使用
	public void initHorizontalScrollView(){
		int scale = (int) (0.03125 * getDensityDpi());
		int columnWidth = scale * 26;
		int horizontalSpacing = scale;
		//GridView可以根据mListAdapter_SingleLayout中条目个数以及显示列数NumColumns来自动确定行数
		int NumColumns = mListAdapter_SingleLayout.getCount()/2;
		mGridView = (GridView) findViewById(R.id.mygridview);
		ViewGroup.LayoutParams lp = mGridView.getLayoutParams();
		lp.width = NumColumns * (columnWidth+horizontalSpacing);
		mGridView.setLayoutParams(lp);
		mGridView.setColumnWidth(columnWidth);
		mGridView.setHorizontalSpacing(horizontalSpacing);
		mGridView.setVerticalSpacing(horizontalSpacing/2);
		mGridView.setStretchMode(GridView.NO_STRETCH);
		mGridView.setNumColumns(NumColumns);
		mGridView.setAdapter(mListAdapter_SingleLayout);
	}
	//
	public void initTabHostView(){
	    mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        mListViewTabHost = (ListView) findViewById(android.R.id.list);
        //TabHost的使用
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(mTabListener);
        mTabHost.clearAllTabs();
        mTabHost.addTab(buildTabSpec("0","title0"));
        mTabHost.addTab(buildTabSpec("1","title1"));
        mTabHost.addTab(buildTabSpec("2","title2"));
        mTabHost.setCurrentTab(0);
        //mTabWidget中每个TextView的内容都不要默认为大写
        if(null!=mTabWidget && mTabWidget.getChildCount()>0){
	        for (int i = 0; i < mTabWidget.getChildCount(); i++) {
	            TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(android.R.id.title);
	            tv.setTransformationMethod(null);//不设置为大写
	        }
        }
	}
	
    private OnTabChangeListener mTabListener = new OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
        	ALog.Log("tabId:"+tabId);
        	final int slotId = Integer.parseInt(tabId);
        	switch(slotId){
	        	case 0:
	        		mListViewTabHost.setAdapter(mListAdapter_SingleLayout);
	        		break;
	        	default:
	        		mListViewTabHost.setAdapter(mListAdapter_MultiLayout);
        	}
        }
    };
	
    private TabContentFactory mEmptyTabContent = new TabContentFactory() {
        @Override
        public View createTabContent(String tag) {
            return new View(mTabHost.getContext());
        }
    };

    private TabSpec buildTabSpec(String tag, String title) {
        return mTabHost.newTabSpec(tag).setIndicator(title).setContent(
                mEmptyTabContent);
    }
    
    
    private List<Fragment> mFragments=null;
    private List<View> mListViews=null;
	//ViewPager的使用
	public void initMyScrollView(){
		mTextView = (TextView)findViewById(R.id.mytextview);
		mTextView.setOnClickListener(mOnClickListener);
		mFragments = new ArrayList<Fragment>();
		mListViews = new ArrayList<View>();
		for(int i=0;i<NUM_PAGES;i++){
			mFragments.add(ScreenSlidePageFragment.create(i));
			mListViews.add(new CombinedView(this));
		}
        mViewPager = (ViewPager) findViewById(R.id.myviewpager);
        mPagerAdapter = new MyFragmentStatePagerAdapter(getFragmentManager(),mFragments);//ViewPager显示Fragment，谷歌推荐做法
        mViewPager.setAdapter(mPagerAdapter);
        //
        mListView = (ListView) findViewById(R.id.mylistview);
        mListView.setAdapter(mListAdapter_SingleLayout);
        setListViewHeightBasedOnChildren(mListView);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener(){
		private int index=0;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int value = (index++%3);
			ALog.Log("value:"+value);
			switch(value){
			case 0:
				mTextView.setText("FragmentPagerAdapter");
				mPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager(),mFragments);//ViewPager显示Fragment，谷歌推荐做法
				break;
			case 1:
				mTextView.setText("PagerAdapter");
				mPagerAdapter = new MyPagerAdapter(mListViews);//ViewPager显示自定义View
				break;
			case 2:
				mTextView.setText("FragmentStatePagerAdapter");
				mFragments.clear();
				for(int i=0;i<NUM_PAGES;i++){
					mFragments.add(ScreenSlidePageFragment.create(i));
				}
				mPagerAdapter = new MyFragmentStatePagerAdapter(getFragmentManager(),mFragments);//ViewPager显示Fragment，谷歌推荐做法
				break;
			}
	        mViewPager.setAdapter(mPagerAdapter);
		}
	};
	
	/**
	 * FragmentPagerAdapter与FragmentStatePagerAdapter的主要区别就在与对于fragment是否销毁，下面细说：
	 * 区别1)FragmentPagerAdapter：对于不再需要的fragment，选择调用detach方法，仅销毁视图，并不会销毁fragment实例。
	 * 区别2)FragmentStatePagerAdapter：会销毁不再需要的fragment，当当前事务提交以后，会彻底的将fragmeng从当前Activity
	 * 的FragmentManager中移除，state标明，销毁时，会将其onSaveInstanceState(Bundle outState)中的bundle信息保存下来，
	 * 当用户切换回来，可以通过该bundle恢复生成新的fragment，也就是说，你可以在onSaveInstanceState(Bundle outState)
	 * 方法中保存一些数据，在onCreate中进行恢复创建。
	 * 总之：使用FragmentStatePagerAdapter当然更省内存，但是销毁新建也是需要时间的。一般情况下，如果你是制作主页面，
	 * 就3、4个Tab，那么可以选择使用FragmentPagerAdapter，如果你是用于ViewPager展示数量特别多的条目时，那么建议使用
	 * FragmentStatePagerAdapter。
	 */
	
	/**
	 * FragmentStatePagerAdapter
	 * @author Mengtao1
	 *
	 */
	private class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
		private List<Fragment> mFragments; 
		public MyFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			mFragments = fragments;
		}
		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}
		
		@Override
		public int getCount() {
			return mFragments.size();
		}
	}
	
	/**
	 * FragmentPagerAdapter：专为添加Fragment设计
	 * @author Mengtao1
	 *
	 */
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    	private List<Fragment> mFragments; 
        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
    
    /**
     * PagerAdapter:添加自定义View
     * @author Mengtao1
     *
     */
	private class MyPagerAdapter extends PagerAdapter {
        private List<View> mListViews;  
        
        public MyPagerAdapter(List<View> mListViews) {  
            this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。  
        }  
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

	@Override  
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡         
         container.addView(mListViews.get(position), 0);//添加页卡  
         return mListViews.get(position);  
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
	}
}
