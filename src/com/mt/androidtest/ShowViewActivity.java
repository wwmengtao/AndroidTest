package com.mt.androidtest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowViewActivity extends Activity implements Handler.Callback, View.OnClickListener{
	int [] buttonID = {
			  R.id.btn_showview,
			  R.id.btn_showfixedlength,
			  R.id.btn_showtextsize};
	private LinearLayout mLayout=null;
	private TextView mTextViewAdded=null;
	private View mLinearLayout_TextSize=null;
    private TextView mTV1_TextSize=null;
    private TextView mTV2_TextSize=null;    
    private Handler mHandler;
	private final int MSG_INIT_TEXT_VIEW_ADDED=0x000;
	private final int MSG_INIT_TEXT_VIEW_ADDED_WIDTH=0x001;
	private final int MSG_SHOW_VIEW_ADD_VIEW=0x002;
	private final int MSG_SHOW_VIEW_FIXED_LENGTH=0x003;
	private final int MSG_SHOW_VIEW_FINALLY=0x004;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ALog.Log("onCreate",this);
		setContentView(R.layout.activity_show_view);
		mLayout=(LinearLayout) findViewById(R.id.linearlayout_showview);
		Button btn=null;
		for(int i=0;i<buttonID.length;i++){
			btn = (Button)findViewById(buttonID[i]);
			btn.setOnClickListener(this);
		}
		mLinearLayout_TextSize = findViewById(R.id.linearlayout_textsize);
	    mTV1_TextSize = (TextView) findViewById(R.id.textview_textsize1);
	    mTV2_TextSize = (TextView) findViewById(R.id.textview_textsize2);
	}

	@Override
	protected void onResume(){	
		super.onResume();
		ALog.Log("onResume",this);
        if (mHandler == null) {
        	mHandler = new Handler(this);
        }
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		ALog.Log("onPause",this);
        if (mHandler != null) {
        	mHandler.removeCallbacksAndMessages(null);
        }
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ALog.Log("onDestroy",this);
	}		
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btn_showview:		
				mHandler.sendEmptyMessage(MSG_SHOW_VIEW_ADD_VIEW);
			break;		
			case R.id.btn_showfixedlength:
				mHandler.sendEmptyMessage(MSG_SHOW_VIEW_FIXED_LENGTH);
			break;
			case R.id.btn_showtextsize:
				if(!mLinearLayout_TextSize.isShown()){
					mLinearLayout_TextSize.setVisibility(View.VISIBLE);
				}else{
					mLinearLayout_TextSize.setVisibility(View.GONE);
				}
			break;		
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case MSG_INIT_TEXT_VIEW_ADDED:
			setAddViewBtnClickable(false);
			initTextViewAdded();
			if(!mLayout.isShown()){
				mLayout.setVisibility(View.VISIBLE);
			}
			break;
		case MSG_INIT_TEXT_VIEW_ADDED_WIDTH:
			setAddViewBtnClickable(true);
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
			ALog.Log("MSG_SHOW_VIEW_FIXED_LENGTH");
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

	public void setAddViewBtnClickable(boolean isClickable){
		int [] btn_ids={R.id.btn_showview,R.id.btn_showfixedlength};
		Button btn=null;
		for(int i=0;i<btn_ids.length;i++){
			btn = (Button)findViewById(btn_ids[i]);
			btn.setClickable(isClickable);
		}
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
		//ALog.Log("/------------------------onWindowFocusChanged------------------------/");
		showWidthAndHeightLog();
		//ALog.Log("/************************onWindowFocusChanged************************/");
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
		ALog.Log("mTextViewAdded_getWidth:"+textViewAddedParams.widthOfTextViewAdded);
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
				 ALog.Log("post_widthOfView:"+widthOfView);	
			 }
		});
		*/
		ALog.Log("initTextViewAdded_end");
	}

	public void setOnGlobalLayoutListener(){
		mTextViewAdded.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override 
			public void onGlobalLayout() { 
				int widthOfView=0;			
				if(null!=mTextViewAdded){
					widthOfView = mTextViewAdded.getWidth();
					ALog.Log("onGlobalLayout_widthOfView:"+widthOfView);	
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
    		ALog.Log("getWidth"+betweenTitle+"getMeasuredWidth"+betweenTitle+"getHeight"+betweenTitle+"getMeasuredHeight");
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
        ALog.Log(strgetWidth+
        				 strgetMeasuredWidth+
        				 strgetHeight+
        				 strgetMeasuredHeight+
        				 str_ALog+":"+objName);
    }	
}
