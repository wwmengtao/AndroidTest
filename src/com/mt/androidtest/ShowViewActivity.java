package com.mt.androidtest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ShowViewActivity extends BaseActivity{
	boolean isLogRunAll=false;
	boolean isLogRunSpec=true;
	private LinearLayout mLayout=null;
	private TextView mTextViewAdded=null;
	private View mLinearLayout_TextSize=null;
    private TextView mTV1_TextSize=null;
    private TextView mTV2_TextSize=null;    
    private GridLayout mGridLayout_Parent=null;    
    private GridLayout mGridLayout_Calculator=null;    
    private Handler mHandler;
	private final int MSG_INIT_TEXT_VIEW_ADDED=0x000;
	private final int MSG_INIT_TEXT_VIEW_ADDED_WIDTH=0x001;
	private final int MSG_SHOW_VIEW_ADD_VIEW=0x002;
	private final int MSG_SHOW_VIEW_FIXED_LENGTH=0x003;
	private final int MSG_SHOW_VIEW_FINALLY=0x004;	
	boolean mIsProcessTaskRuning = false;
    private ConsumptionRefreshTask mAsyncTask=null;
	private String [] mMethodNameFT={"showViewAdded","showViewFixedLength","showViewFixedSize",
			"AsynctaskCancel","showCalculator"};
	private String [] mActivitiesName={"InflateActivity"};			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isLogRunAll)ALog.Log("onCreate",this);
		setContentView(R.layout.activity_show_view);
		super.initListFTData(mMethodNameFT);
		super.initListActivityData(mActivitiesName);
		mLayout=(LinearLayout) findViewById(R.id.linearlayout_showview);
		mLinearLayout_TextSize = findViewById(R.id.linearlayout_textsize);
	    mTV1_TextSize = (TextView) findViewById(R.id.textview_textsize1);
	    mTV2_TextSize = (TextView) findViewById(R.id.textview_textsize2);
	    mGridLayout_Parent= (GridLayout) findViewById(R.id.parentView);
	    //下列两种添加计算器GridLayout布局方法都可以：
	    //方法1：
	    //mGridLayout_Calculator=(GridLayout) LayoutInflater.from(this).inflate(R.layout.gridlayout_calculator,mGridLayout_Parent);
	    //方法2：
	    //mGridLayout_Calculator=(GridLayout) LayoutInflater.from(this).inflate(R.layout.gridlayout_calculator,null);
	    //或者使用下列语句：
	    mGridLayout_Calculator=(GridLayout) LayoutInflater.from(this).inflate(R.layout.gridlayout_calculator,mGridLayout_Parent,false);
	    mGridLayout_Parent.addView(mGridLayout_Calculator);
	}

	@Override
	public void onResume(){	
		super.onResume();
		if(isLogRunAll)ALog.Log("onResume",this);
		mHandler=getHandler();
        if (!mIsProcessTaskRuning) {//此时自动转屏，那么将会生成新的AsyncTask，并且之前的AsyncTask仍将继续执行
        	mAsyncTask = new ConsumptionRefreshTask();
        	mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);	
        }
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(isLogRunAll)ALog.Log("onPause",this);
        if (mHandler != null) {
        	mHandler.removeCallbacksAndMessages(null);//可以避免内存泄露
        }
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(isLogRunAll)ALog.Log("onDestroy",this);
	}
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		switch(mMethodNameFT[position]){
		case "showViewAdded":
			mHandler.sendEmptyMessage(MSG_SHOW_VIEW_ADD_VIEW);
			break;	
		case "showViewFixedLength":
			mHandler.sendEmptyMessage(MSG_SHOW_VIEW_FIXED_LENGTH);
			break;		
		case "showViewFixedSize":
			if(!mLinearLayout_TextSize.isShown()){
				mLinearLayout_TextSize.setVisibility(View.VISIBLE);
			}else{
				mLinearLayout_TextSize.setVisibility(View.GONE);
			}
			break;
		case "AsynctaskCancel":
	        if (mAsyncTask != null) {
	        	/**
	        	 * 1)在java的线程中，没有办法停止一个正在运行中的线程。在Android的AsyncTask中也是一样的。
	        	 * 2)下列cancel(false)和cancel(true)区别：true可以中断可中断操作，比如Sleep等，但是二者都不能终止
	        	 * doInBackground的调用完成。二者的调用都会使得onPostExecute不被调用而调用onCancelled.
	        	 * 3)cancel()方法不一定能成功，所以 onCancel() 回调方法不一定被调用。
	        	 */
	        	//
	        	mAsyncTask.cancel(false);
	        	//mAsyncTask.cancel(true);
	        	ALog.Log("mAsyncTask.isCancelled():"+mAsyncTask.isCancelled());
	        	mIsProcessTaskRuning = false;
	    	}
	        break;		
		case "showCalculator":
			if(mGridLayout_Parent.isShown()){
				mGridLayout_Parent.setVisibility(View.GONE);
			}else{
				mGridLayout_Parent.setVisibility(View.VISIBLE);
			}
			break;
		}
	}
	
    private class ConsumptionRefreshTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(isLogRunSpec)ALog.Log("onPreExecute");
            mIsProcessTaskRuning = true ;
        }

        @Override
        protected Void doInBackground(Void... params) {
        	if(isLogRunSpec)ALog.Log("doInBackground");
        	//一、下列代码说明：mAsyncTask.cancel(true)可以打断sleep，直接跳到if(isLogRunAll)ALog.Log("doInBackground:sleep end");
        	try {
				Thread.sleep(1000*5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	if(isLogRunSpec)ALog.Log("doInBackground:sleep end");
        	//二、下列代码说明：mAsyncTask.cancel(true)不可以打断for循环，除非添加标记位判断
        	int j=0;
        	for(int i=0;i<0x1F00000;i++){
         		//if(!mIsProcessTaskRuning)break;//只能通过标记位判断跳出for循环
        	}
        	if(isLogRunSpec)ALog.Log("doInBackground:for end");
        	return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mIsProcessTaskRuning = false;
            if(isLogRunSpec)ALog.Log("onPostExecute");
            super.onPostExecute(result);
        }
        
        @Override
        protected void onCancelled(Void result){
        	if(isLogRunSpec)ALog.Log("onCancelled");
        }
    }  
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case MSG_INIT_TEXT_VIEW_ADDED:
			initTextViewAdded();
			if(!mLayout.isShown()){
				mLayout.setVisibility(View.VISIBLE);
			}
			break;
		case MSG_INIT_TEXT_VIEW_ADDED_WIDTH:
			showView();
			break;
		case MSG_SHOW_VIEW_ADD_VIEW:
			mTVAddedParams.isShowAddView=true;
			if(null==mTextViewAdded){
				mHandler.sendEmptyMessage(MSG_INIT_TEXT_VIEW_ADDED);
			}else{
				mHandler.sendEmptyMessage(MSG_SHOW_VIEW_FINALLY);
			}
			break;
		case MSG_SHOW_VIEW_FIXED_LENGTH:
			if(isLogRunAll)ALog.Log("MSG_SHOW_VIEW_FIXED_LENGTH");
			mTVAddedParams.isShowAddView=false;
			if(null==mTextViewAdded){
				mHandler.sendEmptyMessage(MSG_INIT_TEXT_VIEW_ADDED);
			}else{
				mHandler.sendEmptyMessage(MSG_SHOW_VIEW_FINALLY);
			}
			break;			
		case MSG_SHOW_VIEW_FINALLY:
			if(!mLayout.isShown()){
				mLayout.setVisibility(View.VISIBLE);
				showView();
			}else{
				mLayout.setVisibility(View.GONE);
			}
			break;
		}		
		return false;
	}
	
	public class textViewAddedParams{
		int widthOfTextViewAdded=0;
		boolean isShowAddView=false;
		boolean isAddViewInit=false;
		boolean isFixedLengthViewInit=false;
	}
	textViewAddedParams mTVAddedParams = new textViewAddedParams();

	/**
	 * onWindowFocusChanged：
		1、进入组件时执行顺序如下：
		onStart--->onResume--->onAttachedToWindow--->onWindowVisibilityChanged--visibility=0--->onWindowFocusChanged(true)--->
		执行到onWindowFocusChanged表明已经获取焦点，此时View的绘制工作已经完成，可以获取View控件的宽度、高度。
		2、离开组件时
		2.1)onPause---->onStop---->onWindowFocusChanged(false)  --- (lockscreen)
		2.2)onPause--->onWindowFocusChanged(false)--->onWindowVisibilityChanged--visibility=8--->onStop(to another activity)
	 * @param hasFocus
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		String time_now_str = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ987654321";
		showTextSizeView(mTV1_TextSize,time_now_str);
		time_now_str = "123456789123456789123456789";
		showTextSizeView(mTV2_TextSize,time_now_str);
		//if(isLogRunAll)ALog.Log("/------------------------onWindowFocusChanged------------------------/");
		showWidthAndHeightLog();
		//if(isLogRunAll)ALog.Log("/************************onWindowFocusChanged************************/");
	}	
	
	/**
	 * 根据固定控件的大小调整所能显示的最大字体
	 * @param mView
	 * @param str
	 */
	public void showTextSizeView(TextView mView,String str){
		if(null==mView||null==str)return;
		mView.setSingleLine(true);
        int widthofView = mView.getWidth();
        int textSize = (int)mView.getTextSize();
        while((int)mView.getPaint().measureText(str) > widthofView){
        	mView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize--);
            if(1==textSize)break;
        }
        mView.setText(str);
	}
	
	public void showView(){
		if(mTVAddedParams.isShowAddView){
			if(!mTVAddedParams.isAddViewInit){
				showViewAddView();
				mTVAddedParams.isAddViewInit=true;
				mTVAddedParams.isFixedLengthViewInit=false;
			}
		}else{
			if(!mTVAddedParams.isFixedLengthViewInit){
				showViewFixedLength();
				mTVAddedParams.isFixedLengthViewInit=true;
				mTVAddedParams.isAddViewInit=false;
			}
		}
	}
	
	/**
	 * showViewAddView：控件宽度设置为初始宽度
	 */
	public void showViewAddView(){
		mTextViewAdded.setWidth(mTVAddedParams.widthOfTextViewAdded);
		mTextViewAdded.setGravity(Gravity.CENTER_HORIZONTAL);
		mTextViewAdded.setText("showViewByAddView");
	}
	
	/**
	 * showViewFixedLength：根据要显示的内容以及间距精确控制控件的宽度
	 */
	public void showViewFixedLength(){
		String str = "123456789123456789123456789123456789";
		//控件宽度由字符串test_str和dimen值联合确定
        int widthOfView = getWidthByString(str)+2*getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
		mTextViewAdded.setWidth(widthOfView);
		mTextViewAdded.setGravity(Gravity.CENTER_HORIZONTAL);
		mTextViewAdded.setText(str);
	}

	public int getWidthByString(String str){
		TextView mTextView=new TextView(this);
		//方法一
		/*
        Rect bounds = new Rect();
        Paint mTextPaint = mTextView.getPaint();
        mTextPaint.getTextBounds(test_str,0,0,bounds);*/
        //方法二
        TextPaint mTextPaint = mTextView.getPaint(); 
        //widthOfView：根据字符串内容确定控件的精确宽度
        int widthOfView = (int)mTextPaint.measureText(str);
        return widthOfView;
	}

	public void initTextViewAdded(){
		//第一个参数为宽的设置，第二个参数为高的设置。   
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(      
				LinearLayout.LayoutParams.WRAP_CONTENT,      
				LinearLayout.LayoutParams.WRAP_CONTENT );      
		//调用addView()方法增加一个TextView到线性布局中   
		//往mLayout里边添加一个TextView
		mTextViewAdded = new TextView(this);  
		mTextViewAdded.setBackgroundColor(getResources().getColor(R.color.wheat));				
		mLayout.addView(mTextViewAdded, p); //引起onGlobalLayout函数的调用
		mTextViewAdded.setSingleLine(true);
		mTextViewAdded.setText("View added");
		//下列直接获取控件宽度为0，必须使用ViewTreeObserver.OnGlobalLayoutListener监听器
		/*
		textViewAddedParams.widthOfTextViewAdded = mTextViewAdded.getMeasuredWidth();
		if(isLogRunAll)ALog.Log("mTextViewAdded_getWidth:"+textViewAddedParams.widthOfTextViewAdded);
		*/
		//方法1.1、mTextViewAdded直接设置监听器
		setOnGlobalLayoutListener();
		//方法1.2、mTextViewAdded设置mOnGlobalLayoutListener监听器
		//setOnGlobalLayoutListener2();
		//方法2、将一个runnable添加到Layout队列中：View.post()。runnable对象中的方法会在View的measure、layout等事件后触发
		/*
		mTextViewAdded.post(new Runnable() {
			 public void run() {
				 int widthOfView = mTextViewAdded.getWidth();
				 if(isLogRunAll)ALog.Log("post_widthOfView:"+widthOfView);	
			 }
		});
		*/
		if(isLogRunAll)ALog.Log("initTextViewAdded_end");
	}

	public void setOnGlobalLayoutListener(){
		mTextViewAdded.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override 
			public void onGlobalLayout() { 
				int widthOfView=0;			
				if(null!=mTextViewAdded){
					widthOfView = mTextViewAdded.getWidth();
					if(isLogRunAll)ALog.Log("onGlobalLayout_widthOfView:"+widthOfView);	
					if(0!=widthOfView){
						mTVAddedParams.widthOfTextViewAdded = widthOfView; 
						mTextViewAdded.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						mHandler.sendEmptyMessage(MSG_INIT_TEXT_VIEW_ADDED_WIDTH);
					}
				}
			} 
		}); 
	}
	
	public void setOnGlobalLayoutListener2(){
		mTextViewAdded.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
	}
	
	ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener =new ViewTreeObserver.OnGlobalLayoutListener(){
		@Override
		public void onGlobalLayout() {

		}
	};
    
    public void showWidthAndHeightLog(){
    	showWidthAndHeight(mTextViewAdded, "mTextViewAdded");	
    	showWidthAndHeight(mLinearLayout_TextSize, "mLinearLayout_TextSize");	
    	showWidthAndHeight(mLayout, "mLayout");
    	showWidthAndHeight(mTV1_TextSize, "mTV1_TextSize");
    }
    
	String regShowWidthAndHeight = "id\\/[a-zA-Z]+.+\\}";//仅仅获取控件id，其他内容不要
    Pattern mPatternShowWidthAndHeight = Pattern.compile(regShowWidthAndHeight);
    Matcher mMatcher = null;
	boolean is_onWindowFocusChanged = false;
    public void showWidthAndHeight(View mView, String objName){
    	if(null==mView)return;
    	if(!is_onWindowFocusChanged){
    		String betweenTitle=" ";
    		if(isLogRunAll)ALog.Log("getWidth"+betweenTitle+"getMeasuredWidth"+betweenTitle+"getHeight"+betweenTitle+"getMeasuredHeight");
    		is_onWindowFocusChanged = true;
    	}
    	String str_ALog=null;
        String str = mView.toString();
        mMatcher = mPatternShowWidthAndHeight.matcher(str);
        while(mMatcher.find()){
        	str_ALog = mMatcher.group().replace("}", "");
            break;
        }
        String format="%-14d";
        String strgetWidth = String.format(format,mView.getWidth());
        String strgetMeasuredWidth = String.format(format,mView.getMeasuredWidth());
        String strgetHeight = String.format(format,mView.getHeight());
        String strgetMeasuredHeight = String.format(format,mView.getMeasuredHeight());
        if(isLogRunAll)ALog.Log(strgetWidth+
        				 strgetMeasuredWidth+
        				 strgetHeight+
        				 strgetMeasuredHeight+
        				 str_ALog+":"+objName);
    }	
}
