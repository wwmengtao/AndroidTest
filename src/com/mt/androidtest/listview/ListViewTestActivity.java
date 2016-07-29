package com.mt.androidtest.listview;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AbsListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.WrapperListAdapter;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

public class ListViewTestActivity extends Activity implements RecyclerListener, OnScrollListener, View.OnClickListener,
OnCreateContextMenuListener{
	ListView mListView;
	ListViewTestAdapter_SingleLayout listAdapter_S;//����ListView��ʾ��itemֻ��һ��layout
	ListViewTestAdapter_MultiLayout listAdapter_M;//����ListView��ʾ��item�ж��ֲ�ͬ��layout
	ListViewAdapter listAdapter_LM = null;//����ListView��ʾ��item�Ƿֲ����ص�
	ArrayList <HashMap<String, Object>> mArrayListLM = null;
	HashMap<String, Object> map = null;
    // ���ɼ���Ŀ������
    private int lastVisibleIndex=0;
    //mArrayListLM��ʾ��ԭʼitem����
    final int originalNum_listAdapter_LM = 20;    
    //mArrayListLMÿ�μ��ص�item����
    final int addedNum_listAdapter_LM = 5;
    //����mArrayListLM��ʾ�����item����
    final int maxNum_listAdapter_LM = 30;
    // ListView�ײ��ġ����ظ���������ʾ��View
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_test);
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
		// -------------��menu����������С���Ӳ˵�-------------
		super.onCreateOptionsMenu(menu);
		addMenu(menu);
		return true;
	}
	
	public void addMenu(Menu menu){
		menu.add(0, Menu_Single, 0, "SingleLayout");
		menu.add(0, Menu_Multi, 0, "MultiLayout");
		menu.add(0, Menu_Load, 0, "LoadMore");
	}
	
	public boolean onOptionsItemSelected(MenuItem mi)
	{
		switch (mi.getItemId()){
		case Menu_Single:
			if(null!=viewAddMoreData)mListView.removeFooterView(viewAddMoreData);
			mListView.setAdapter(listAdapter_S);
			break;
		case Menu_Multi:
			if(null!=viewAddMoreData)mListView.removeFooterView(viewAddMoreData);
			mListView.setAdapter(listAdapter_M);
			break;		
		case Menu_Load:
			initListViewLM();
			break;					
		}
		return true;
	}
	
    @Override
    public void onMovedToScrapHeap(View view) {
    	TextView mTextView = ((TextView)view.findViewById(R.id.textview));
    	if(null!=mTextView)mTextView.setTextColor(getResources().getColor(R.color.royalblue));    	
    }
    
	public void initListViewLM(){
        // ʵ�����ײ�����
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
	}
	
	int selectedPosition = 0;
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo){
		menu.setHeaderTitle("Delete ContextMenu?");   
		menu.add(1, ContextMenu_delete, 0, "Yes");
		menu.add(1, ContextMenu_cancel, 0, "No");   
		selectedPosition = ((AdapterContextMenuInfo)menuInfo).position;//�˵���Ӧ��ListView����
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
	 * onScroll��ListView������ʱ�����
	 */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    	if(mListView.getAdapter() instanceof WrapperListAdapter){//����mListView.addFooterView����˴�ʱ��ListView��Adapter���WrapperListAdapter���͵�
    		lastVisibleIndex=firstVisibleItem+visibleItemCount;
        	ALog.Log("onScroll_firstVisibleItem:"+firstVisibleItem+" visibleItemCount:"+visibleItemCount+" totalItemCount:"+totalItemCount);
    	}
    }
    
    /**
     * onScrollStateChanged��ListView״̬�ı�ʱ���ã�������ٻ�����ֹͣ�ȣ������λscrollState
     * AbsListView.OnScrollListener�ж����û��Ļ���״̬�����¶��壺
     * public static int SCROLL_STATE_IDLE = 0;//��ʾListView�Ѿ�ֹͣ����
     * public static int SCROLL_STATE_TOUCH_SCROLL = 1;// ��ָ�Ӵ�����״̬
     * public static int SCROLL_STATE_FLING = 2;//��ʾ��ָ�����׵Ķ�������ָ�뿪��Ļǰ����������һ�£���Ļ�������Ի�����
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
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
		mHandler.removeCallbacks(mRunnable);
		mHandler.postDelayed(mRunnable, delayTime);
	}
    
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
        	loadMoreDate();
        }
    };
    
    private void loadMoreDate() {
    	int count = listAdapter_LM.getCount();
    	if(count==maxNum_listAdapter_LM){
    		mListView.removeFooterView(viewAddMoreData);
    		Toast.makeText(this, "����ȫ��������ɣ�û�и������ݣ�", Toast.LENGTH_LONG).show();
    		return;
    	}
		for(int i=0;i<addedNum_listAdapter_LM;i++){
			map = new HashMap<String, Object>();
			map.put("itemText", "This is new added "+(count+i));
			mArrayListLM.add(map);
	    	if((count+i+1) == maxNum_listAdapter_LM)break;
		}
		listAdapter_LM.setupList(mArrayListLM);
        listAdapter_LM.notifyDataSetChanged();// ֪ͨlistViewˢ������
		btn_addMoreData.setVisibility(View.VISIBLE);
		pg_addMoreData.setVisibility(View.GONE);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(R.id.bt_load==v.getId()){
			updateListViewLM();
		}
	}

}
