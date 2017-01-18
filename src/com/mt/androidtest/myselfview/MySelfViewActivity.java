package com.mt.androidtest.myselfview;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
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
import com.mt.androidtest.image.ImageDecodeInfo;
import com.mt.androidtest.listview.ListViewTestAdapter_MultiLayout;
import com.mt.androidtest.listview.ListViewTestAdapter_SingleLayout;
import com.mt.androidtest.myselfview.ViewPagerComponets.MyFragmentOnPageChangeListener;
import com.mt.androidtest.myselfview.ViewPagerComponets.MyFragmentPagerAdapter;
import com.mt.androidtest.myselfview.ViewPagerComponets.MyFragmentStatePagerAdapter;
import com.mt.androidtest.myselfview.ViewPagerComponets.MyPagerAdapter;

/**
 * �Զ���ؼ���Ϊ���ࣺ�Լ����ơ���ϡ��̳У���activity_customed_controller�е�SelfDrawnView��CombinedView��InheritedView��
 * @author Mengtao1
 *
 */
public class MySelfViewActivity extends BaseActivity {
	private static final int Menu_Common = 0;
	private static final int Menu_Scroll = 1;	
	private static final int Menu_TabHost = 2;		
	//
    public static final int NUM_PAGES = 10;
    private ViewPager mViewPager;
    private OnPageChangeListener mMyonPageChangeListener=null, mMyonPageChangeListener2=null;
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
    private GridView mGridView=null;
    //
    private TextView tv_img_desc = null;
    private LinearLayout ll_dot_group = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mListAdapter_SingleLayout = new ListViewTestAdapter_SingleLayout(this);
        mListAdapter_MultiLayout = new ListViewTestAdapter_MultiLayout(this);
        ImageDecodeInfo.setAssetManager(getAssetManager());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// -------------��menu����������С���Ӳ˵�-------------
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu_Common, 0, "Common");
		menu.add(0, Menu_Scroll, 0, "ScrollView");
		menu.add(0, Menu_TabHost, 0, "TabHost");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)	{
		ScreenSlidePageFragment.clearFragments();
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
	//HorizontalScrollView��ʹ��
	public void initHorizontalScrollView(){
		int scale = (int) (0.03125 * getDensityDpi());
		int columnWidth = scale * 26;
		int horizontalSpacing = scale;
		//GridView���Ը���mListAdapter_SingleLayout����Ŀ�����Լ���ʾ����NumColumns���Զ�ȷ������
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
        //TabHost��ʹ��
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(mTabListener);
        mTabHost.clearAllTabs();
        mTabHost.addTab(buildTabSpec("0","title0"));
        mTabHost.addTab(buildTabSpec("1","title1"));
        mTabHost.addTab(buildTabSpec("2","title2"));
        mTabHost.setCurrentTab(0);
        //mTabWidget��ÿ��TextView�����ݶ���ҪĬ��Ϊ��д
        if(null!=mTabWidget && mTabWidget.getChildCount()>0){
	        for (int i = 0; i < mTabWidget.getChildCount(); i++) {
	            TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(android.R.id.title);
	            tv.setTransformationMethod(null);//������Ϊ��д
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

	//ViewPager��ʹ��
	public void initMyScrollView(){
		mTextView = (TextView)findViewById(R.id.mytextview);
		mTextView.setOnClickListener(mOnClickListener);
        mViewPager = (ViewPager) findViewById(R.id.myviewpager);
        mPagerAdapter = new MyFragmentStatePagerAdapter(getFragmentManager());//ViewPager��ʾFragment���ȸ��Ƽ�����
        mViewPager.setAdapter(mPagerAdapter);
        mMyonPageChangeListener = new MyFragmentOnPageChangeListener();
		mViewPager.addOnPageChangeListener(mMyonPageChangeListener);
        //
        mListView = (ListView) findViewById(R.id.mylistview);
        mListView.setAdapter(mListAdapter_SingleLayout);
        setListViewHeightBasedOnChildren(mListView);
        //
		tv_img_desc = (TextView) findViewById(R.id.tv_img_desc);
		ll_dot_group = (LinearLayout) findViewById(R.id.ll_dot_group); //�������СԲ��
		tv_img_desc.setVisibility(View.GONE);
		ll_dot_group.setVisibility(View.GONE);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener(){
		private int index=0;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mViewPager.removeOnPageChangeListener(mMyonPageChangeListener);
			if(null != mMyonPageChangeListener2)mViewPager.removeOnPageChangeListener(mMyonPageChangeListener2);
			ScreenSlidePageFragment.clearFragments();
			tv_img_desc.setVisibility(View.GONE);
			ll_dot_group.setVisibility(View.GONE);
			int value = (index++%3);
			switch(value){
			case 0:
				mTextView.setText("FragmentPagerAdapter");
				mPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());//ViewPager��ʾFragment���ȸ��Ƽ�����
				mViewPager.addOnPageChangeListener(mMyonPageChangeListener);
				break;
			case 1:
				mTextView.setText("PagerAdapter");
				mPagerAdapter = new MyPagerAdapter(MySelfViewActivity.this, mViewPager);//ViewPager��ʾ�Զ���View
				mMyonPageChangeListener2 = (OnPageChangeListener)mPagerAdapter;
				mViewPager.addOnPageChangeListener(mMyonPageChangeListener2);
				break;
			case 2:
				mTextView.setText("FragmentStatePagerAdapter");
				mPagerAdapter = new MyFragmentStatePagerAdapter(getFragmentManager());//ViewPager��ʾFragment���ȸ��Ƽ�����
				mViewPager.addOnPageChangeListener(mMyonPageChangeListener);
				break;
			}
	        mViewPager.setAdapter(mPagerAdapter);
		}
	};
	
	@Override
	public void onStop(){
		super.onStop();
		if(null != mViewPager){
			mViewPager.removeOnPageChangeListener(mMyonPageChangeListener);
			mViewPager.removeOnPageChangeListener(mMyonPageChangeListener2);
		}
	}

}
