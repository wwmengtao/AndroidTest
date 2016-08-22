package com.mt.androidtest.listview;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.WrapperListAdapter;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class ListViewTestActivity extends BaseActivity implements RecyclerListener, OnScrollListener, View.OnClickListener,
OnCreateContextMenuListener,OnItemClickListener{
	ListView mListView;
	ListViewTestAdapter_SingleLayout listAdapter_S;//表明ListView显示的item只有一种layout
	ListViewTestAdapter_MultiLayout listAdapter_M;//表明ListView显示的item有多种不同的layout
	ListViewAdapter listAdapter_LM = null;//表明ListView显示的item是分步加载的
	ArrayList <HashMap<String, Object>> mArrayListLM = null;
	HashMap<String, Object> map = null;
    // 最后可见条目的索引
    private int lastVisibleIndex=0;
    //mArrayListLM显示的原始item个数
    final int originalNum_listAdapter_LM = 20;    
    //mArrayListLM每次加载的item个数
    final int addedNum_listAdapter_LM = 5;
    //允许mArrayListLM显示的最大item个数
    final int maxNum_listAdapter_LM = 30;
    // ListView底部的“加载更多数据提示”View
    private View viewAddMoreData=null;
    private Button btn_addMoreData=null;
    private ProgressBar pg_addMoreData=null;
    //
	final int Menu_Single=0x00;
	final int Menu_Multi=0x01;
	final int Menu_Load=0x02;	
	//
	String ActivityTitle = null;
	final int ContextMenu_delete=0x10;
	final int ContextMenu_cancel=0x11;
	//
    private Handler mHandler=null;
    private int delayTime=200;
    //
    private LayoutInflater mLayoutInflater = null;
    private FrameLayout contentView = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_test);
		contentView = (FrameLayout)findViewById(android.R.id.content);
		mLayoutInflater = LayoutInflater.from(this);
		ActivityTitle = getTitle().toString();
		mHandler=new Handler();
		mListView = (ListView)this.findViewById(R.id.listview);
		mListView.setRecyclerListener(this);
		mListView.setOnScrollListener(this);
		listAdapter_S = new ListViewTestAdapter_SingleLayout(this);
		listAdapter_M = new ListViewTestAdapter_MultiLayout(this);
	}

	@Override
	public void onResume(){
		super.onResume();
		mListView.setAdapter(listAdapter_S);
	}

	@Override
	public void onDestroy(){
		mHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// -------------向menu中添加字体大小的子菜单-------------
		super.onCreateOptionsMenu(menu);
		addMenu(menu);
		return true;
	}
	
	public void addMenu(Menu menu){
		menu.add(0, Menu_Single, 0, "SingleLayout");
		menu.add(0, Menu_Multi, 0, "MultiLayout");
		menu.add(0, Menu_Load, 0, "LoadMore_Del_Add");
	}
	
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        scheduleRemoveDelCopyView();
        return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem mi)
	{
		switch (mi.getItemId()){
		case Menu_Single:
			if(null!=viewAddMoreData)mListView.removeFooterView(viewAddMoreData);
			mListView.setAdapter(listAdapter_S);
			reset();
			break;
		case Menu_Multi:
			if(null!=viewAddMoreData)mListView.removeFooterView(viewAddMoreData);
			mListView.setAdapter(listAdapter_M);
			reset();
			break;		
		case Menu_Load:
			initListViewLM();
			break;					
		}
		return super.onOptionsItemSelected(mi);
	}
	
	public void reset(){
		mListView.setOnCreateContextMenuListener(null);
		mListView.setOnItemClickListener(null);
	}
	
    @Override
    public void onMovedToScrapHeap(View view) {
    	TextView mTextView = ((TextView)view.findViewById(R.id.textview));
    	if(null!=mTextView)mTextView.setTextColor(getResources().getColor(R.color.royalblue));    	
    }
    
	public void initListViewLM(){
        // 实例化底部布局
		viewAddMoreData = getLayoutInflater().inflate(R.layout.linearlayout_loadmoredata, null);
		mListView.addFooterView(viewAddMoreData);
		btn_addMoreData = (Button) viewAddMoreData.findViewById(R.id.bt_load);
		btn_addMoreData.setOnClickListener(this);
		pg_addMoreData = (ProgressBar) viewAddMoreData.findViewById(R.id.pg);
		//
		mArrayListLM =  new ArrayList<HashMap<String, Object>>();
		for(int i = 0; i < originalNum_listAdapter_LM; i++){
			map = new HashMap<String, Object>();
			map.put("itemText", "This is "+i);
			mArrayListLM.add(map);
		}
		//
		listAdapter_LM = new ListViewAdapter(this);
		listAdapter_LM.setMode(2);
		listAdapter_LM.setupList(mArrayListLM);
		mListView.setAdapter(listAdapter_LM);
		mListView.setOnCreateContextMenuListener(this);
		mListView.setOnItemClickListener(this);
	}
	
	int selectedPosition = 0;
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo){
		scheduleRemoveDelCopyView();
		//
		menu.setHeaderTitle("Delete ContextMenu?");   
		menu.add(1, ContextMenu_delete, 0, "Yes");
		menu.add(1, ContextMenu_cancel, 0, "No");   
		selectedPosition = ((AdapterContextMenuInfo)menuInfo).position;//菜单对应的ListView行数
		setTitle("Item "+selectedPosition+" selected.");
	}
	
	@Override

	public boolean onContextItemSelected(MenuItem item){
		
		switch (item.getItemId()){
			case ContextMenu_delete:
				listAdapter_LM.mList.remove(selectedPosition);
				listAdapter_LM.notifyDataSetChanged();
				break;
		}
		setTitle(ActivityTitle);
		return super.onContextItemSelected(item);
	}
	
	/**
	 * onScroll：ListView滑动的时候调用
	 */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    	if(mListView.getAdapter() instanceof WrapperListAdapter){//由于mListView.addFooterView，因此此时的ListView的Adapter变成WrapperListAdapter类型的
    		lastVisibleIndex=firstVisibleItem+visibleItemCount;
        	ALog.Log("onScroll_firstVisibleItem:"+firstVisibleItem+" visibleItemCount:"+visibleItemCount+" totalItemCount:"+totalItemCount);
    	}
    }
    
    /**
     * onScrollStateChanged：ListView状态改变时调用，比如快速滑动，停止等，看标记位scrollState
     * AbsListView.OnScrollListener中对于用户的滑动状态有如下定义：
     * public static int SCROLL_STATE_IDLE = 0;//表示ListView已经停止滚动
     * public static int SCROLL_STATE_TOUCH_SCROLL = 1;// 手指接触滑动状态
     * public static int SCROLL_STATE_FLING = 2;//表示手指做了抛的动作（手指离开屏幕前，用力滑了一下，屏幕产生惯性滑动）
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    	//1、删除自定义删除、添加View
    	scheduleRemoveDelCopyView();
    	if(0==scrollState && (firstItemNotShowComplete || lastItemNotShowComplete)){
    		scheduleShowDelCopyView();
    		firstItemNotShowComplete = false;
    		lastItemNotShowComplete = false;
    		ALog.fillInStackTrace("###onScrollStateChanged");
    	}
		//2、ListView滚动净值之后适当时间加载数据
    	if(mListView.getAdapter() instanceof WrapperListAdapter){
    		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex == listAdapter_LM.getCount()+1) {
    			ALog.Log("onScrollStateChanged_lastVisibleIndex:"+lastVisibleIndex+" count:"+listAdapter_LM.getCount());
    			updateListViewLM();
    		}
    	}

    }
    
	public void updateListViewLM(){
		btn_addMoreData.setVisibility(View.GONE);
		pg_addMoreData.setVisibility(View.VISIBLE);
		mHandler.removeCallbacks(mRunnableLoadMoreData);
		mHandler.postDelayed(mRunnableLoadMoreData, delayTime);
	}
    
    Runnable mRunnableLoadMoreData = new Runnable() {
        @Override
        public void run() {
        	loadMoreData();
        }
    };
    
    private void loadMoreData() {
    	int count = listAdapter_LM.getCount();
    	if(count==maxNum_listAdapter_LM){
    		mListView.removeFooterView(viewAddMoreData);
    		Toast.makeText(this, "All data loaded！", Toast.LENGTH_LONG).show();
    		return;
    	}
		for(int i=0;i<addedNum_listAdapter_LM;i++){
			map = new HashMap<String, Object>();
			map.put("itemText", "This is new added "+(count+i));
			mArrayListLM.add(map);
	    	if((count+i+1) == maxNum_listAdapter_LM)break;
		}
		listAdapter_LM.setupList(mArrayListLM);
        listAdapter_LM.notifyDataSetChanged();// 通知listView刷新数据
		btn_addMoreData.setVisibility(View.VISIBLE);
		pg_addMoreData.setVisibility(View.GONE);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bt_load:
			updateListViewLM();
			break;
		}
	}
	
	int listItemPosition = 0;
	View.OnClickListener mDelAddOnClickListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.item_del:
				//ALog.Log("item_del");
				listAdapter_LM.mList.remove(listItemPosition);
				break;
			case R.id.item_add:
				//ALog.Log("item_add");
				listAdapter_LM.mList.add(listItemPosition, listAdapter_LM.mList.get(listItemPosition));
				break;
			case R.id.item_cancel:
				//ALog.Log("item_cancel");
				break;
			}
			scheduleRemoveDelCopyView();
			listAdapter_LM.notifyDataSetChanged();
		}
	};
	
	boolean isDelCopyViewShown = false;
	View delCopyView = null;
	FrameLayout.LayoutParams mParams = null;
	int listViewTop = 0, listViewBottom = 0;
	int horizontal_margin =0, vertical_margin =0;
	boolean firstItemNotShowComplete = false;
	boolean lastItemNotShowComplete = false;
	@SuppressLint("InflateParams")
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		// TODO Auto-generated method stub
		listItemPosition = position;
		if(null==delCopyView){
			listViewTop = mListView.getTop();
			listViewBottom = mListView.getBottom();
			horizontal_margin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
			vertical_margin = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
			mParams = new FrameLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,      
					LinearLayout.LayoutParams.WRAP_CONTENT);  		
			mParams.height = view.getHeight();
			mParams.width = view.getWidth();
			mParams.leftMargin = view.getLeft()+horizontal_margin;
			//
			delCopyView = mLayoutInflater.inflate(R.layout.item_del_add, null);
			TextView mTextView = (TextView)delCopyView.findViewById(R.id.item_del);
			mTextView.setOnClickListener(mDelAddOnClickListener);
			mTextView = (TextView)delCopyView.findViewById(R.id.item_add);
			mTextView.setOnClickListener(mDelAddOnClickListener);
			mTextView = (TextView)delCopyView.findViewById(R.id.item_cancel);
			mTextView.setOnClickListener(mDelAddOnClickListener);
		}
		firstItemNotShowComplete = (view.getTop()<listViewTop-vertical_margin);
		lastItemNotShowComplete = (view.getBottom()>listViewBottom-vertical_margin);
		ALog.Log("====firstItemNotShowComplete: "+firstItemNotShowComplete+" lastItemNotShowComplete: "+lastItemNotShowComplete);
		if(firstItemNotShowComplete || lastItemNotShowComplete){//如果第一项或者最后一项显示不全那么调用smoothScrollToPosition
//			ALog.Log("====smoothScrollToPosition");
			mListView.smoothScrollToPosition(position);//会触发onScrollStateChanged的回调
			mParams.topMargin = (firstItemNotShowComplete) ? vertical_margin : (listViewBottom-mParams.height);
		}else{
			mParams.topMargin = view.getTop()+vertical_margin;
			scheduleRemoveDelCopyView();
			scheduleShowDelCopyView();
		}
	}
	
    Runnable mRunnableRemoveView = new Runnable() {
        @Override
        public void run() {
        	contentView.removeView(delCopyView);
        	isDelCopyViewShown = false;
        }
    };
    
    Runnable mRunnableAddView = new Runnable() {
        @Override
        public void run() {
        	contentView.addView(delCopyView,mParams);
        	isDelCopyViewShown = true;
        }
    };    
    
    public void scheduleShowDelCopyView(){
    	if(!isDelCopyViewShown){
    		mHandler.removeCallbacks(mRunnableAddView);
    		mHandler.post(mRunnableAddView);
    	}
    }

    public void scheduleRemoveDelCopyView(){
    	if(isDelCopyViewShown){
    		mHandler.removeCallbacks(mRunnableRemoveView);
    		mHandler.post(mRunnableRemoveView);
    	}
    }
    
    @Override 
    public void onBackPressed() {  
        // do something what you want  
    	if(isDelCopyViewShown){
    		scheduleRemoveDelCopyView();
    		return;
    	}
        super.onBackPressed();  
    }  
}
