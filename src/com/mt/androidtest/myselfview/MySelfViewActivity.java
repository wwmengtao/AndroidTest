package com.mt.androidtest.myselfview;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
	private static final int Menu_DrawView = 3;
    private ListViewTestAdapter_SingleLayout mListAdapter_SingleLayout;
    private ListViewTestAdapter_MultiLayout mListAdapter_MultiLayout;
	//
    private TabHost mTabHost;
    private TabWidget mTabWidget;
    private ListView mListViewTabHost;
    //
    private ListView mListView = null;
    private ListView mListView2 = null;

    private GridView mGridView=null;
    
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
		menu.add(0, Menu_DrawView, 0, "SelfDrawView");
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
		case Menu_DrawView:
			ALog.Log("Menu_DrawView");
			setContentView(R.layout.activity_myselfview_drawview);
			break;	
		}
		return super.onOptionsItemSelected(mi);
	}	
	
	private void initMyScrollView(){
		initHorizontalScrollView();
        //
        mListView = (ListView) findViewById(R.id.mylistview);
        mListView.setAdapter(mListAdapter_SingleLayout);
        mListView2 = (ListView) findViewById(R.id.mylistview2);
        mListView2.setAdapter(mListAdapter_SingleLayout);
      /*
       * 此时MySelfViewActivity中，mListView可以在显示一行的情况下上下滑动，由于 mListView2使用setListViewHeightBasedOnChildren
       * 测量了整体的高度，因此可以整体显示
       */
        setListViewHeightBasedOnChildren(mListView2);
	}
	//HorizontalScrollView的使用
	public void initHorizontalScrollView(){
		int scale = (int) (0.03125 * getDensityDpi());
		int columnWidth = scale * 26;
		int horizontalSpacing = scale;
		//GridView可以根据mListAdapter_SingleLayout中条目个数以及显示列数NumColumns来自动确定行数
		int NumRows = 4;
		int NumColumns = mListAdapter_SingleLayout.getCount()/NumRows;
		mGridView = (GridView) findViewById(R.id.mygridview);
		ViewGroup.LayoutParams lp = mGridView.getLayoutParams();
		lp.width = NumColumns * (columnWidth+horizontalSpacing);
		mGridView.setLayoutParams(lp);
		mGridView.setColumnWidth(columnWidth);
		mGridView.setHorizontalSpacing(horizontalSpacing);
		mGridView.setVerticalSpacing(horizontalSpacing/NumRows);
		mGridView.setStretchMode(GridView.NO_STRETCH);
		mGridView.setNumColumns(NumColumns);
		mGridView.setAdapter(mListAdapter_SingleLayout);
	}
	//
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



}
