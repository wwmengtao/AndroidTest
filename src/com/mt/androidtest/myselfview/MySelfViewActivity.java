package com.mt.androidtest.myselfview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    //
    private ListView mListView;
    private ListViewTestAdapter_SingleLayout mListAdapter_SingleLayout;
    private ListViewTestAdapter_MultiLayout mListAdapter_MultiLayout;
	//
    private TabHost mTabHost;
    private TabWidget mTabWidget;
    private ListView mListViewTabHost;
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
	
	public void initTabHostView(){
	    mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        mListViewTabHost = (ListView) findViewById(android.R.id.list);
        //
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
	//
	public void initMyScrollView(){
        mViewPager = (ViewPager) findViewById(R.id.myviewpager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        //
        mListView = (ListView) findViewById(R.id.mylistview);
        mListView.setAdapter(mListAdapter_SingleLayout);
        setListViewHeightBasedOnChildren(mListView);
	}
	
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
