package com.mt.androidtest.showview;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class ShowViewActivity extends BaseActivity{
	boolean isLogRunAll=false;
	//1、addView添加View
	private LinearLayout mLayoutAddView=null;
	private TextView mTextViewAdded=null;
	//2、将不同长度的字串字体大小调整到适应控件宽度
	private View mLinearLayout_AdjustTextSize=null;
    private TextView mTV1_TextSize=null;
    private TextView mTV2_TextSize=null;
    //3、根据控件内容确定控件宽度
	private LinearLayout mLinearLayout_AdjustViewWidth=null;
	private TextView mTV_AdjustViewWidth=null;
    //4、获取手机显示信息
    private PhoneViewInfo mPhoneViewInfo=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//1、去除ActionBar
		setContentView(R.layout.activity_show_view);
		//1、addView添加View
		mLayoutAddView=(LinearLayout) findViewById(R.id.linearlayout_addview);
		initTextViewAdded();
		//2、调整内容字体大小以适应控件宽度
		mLinearLayout_AdjustTextSize = findViewById(R.id.linearlayout_adjusttextsize);
	    mTV1_TextSize = (TextView) findViewById(R.id.textview_textsize1);
	    mTV2_TextSize = (TextView) findViewById(R.id.textview_textsize2);
	    //3、调整控件宽度以全部显示内容
	    mLinearLayout_AdjustViewWidth = (LinearLayout) findViewById(R.id.linearlayout_adjustviewwidth);
		//4、以下获取手机显示信息
	    mPhoneViewInfo = new PhoneViewInfo(this);
		mPhoneViewInfo.showPhoneViewInfo();
	}
	
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
		//1、固定控件宽度下，调整字体大小以适应控件
		String time_now_str = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ987654321";
		showViewAdjustTextSize(mTV1_TextSize,time_now_str);
		time_now_str = "123456789123456789123456789";
		showViewAdjustTextSize(mTV2_TextSize,time_now_str);
		//2、根据要显示的内容控制控件宽度
		showViewAdjustViewWidth();
		//3、显示控件的宽高信息
		if(isLogRunAll)ALog.Log("/------------------------onWindowFocusChanged------------------------/");
		showWidthAndHeightLog();
		if(isLogRunAll)ALog.Log("/************************onWindowFocusChanged************************/");
		//4、以下获取手机显示信息
		mPhoneViewInfo.showPhoneViewInfo();
		ALog.Log("statusBarHeight:"+mPhoneViewInfo.StatusBarHeight);
	    View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);  
	    ALog.Log("contentTop:"+v.getTop());
	    //获取标题栏高度
	    int titleBarHeight = v.getTop() - mPhoneViewInfo.StatusBarHeight;
	    ALog.Log("titleBarHeight:"+titleBarHeight);
	}
	
    public void showWidthAndHeightLog(){
    	showWidthAndHeight(mTextViewAdded, "mTextViewAdded");	
    	showWidthAndHeight(mLinearLayout_AdjustTextSize, "mLinearLayout_AdjustTextSize");	
    	showWidthAndHeight(mLayoutAddView, "mLayoutAddView");
    	showWidthAndHeight(mTV1_TextSize, "mTV1_TextSize");
    }
    
    /**
     * initTextViewAdded：在onCreate中被调用，控件无法准确获取宽度高度，可以注册OnGlobalLayoutListener正确获取
     */
	public void initTextViewAdded(){
		//第一个参数为宽的设置，第二个参数为高的设置。   
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(      
				LinearLayout.LayoutParams.WRAP_CONTENT,      
				LinearLayout.LayoutParams.WRAP_CONTENT );      
		//调用addView()方法增加一个TextView到线性布局中   
		//往mLayout里边添加一个TextView
		mTextViewAdded = new TextView(this);  
		mTextViewAdded.setBackgroundColor(getResources().getColor(R.color.greenyellow));				
		mLayoutAddView.addView(mTextViewAdded, p); //引起onGlobalLayout函数的调用
		mTextViewAdded.setSingleLine(true);
		mTextViewAdded.setText("View to be added!");
		//mTextViewAdded设置监听器OnGlobalLayoutListener以成功获取控件宽高
		setOnGlobalLayoutListener();
	}
	
	public void setOnGlobalLayoutListener(){
		mTextViewAdded.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override 
			public void onGlobalLayout() { 
				int widthOfView=0;			
				if(null!=mTextViewAdded){
					widthOfView = mTextViewAdded.getWidth();//mTextViewAdded的宽度可以由此处获取
					if(0!=widthOfView){
						mTextViewAdded.getViewTreeObserver().removeOnGlobalLayoutListener(this);//获取非0宽度之后取消监听
					}
				}
			} 
		}); 
	}
	
	/**
	 * showViewAdjustTextSize:根据固定控件的大小调整所能显示的最大字体
	 * @param mView
	 * @param str
	 */
	public void showViewAdjustTextSize(TextView mView,String str){
		if(null==mView||null==str)return;
		mView.setSingleLine(true);
        int widthofView = mView.getWidth();
        int textSize = (int)mView.getTextSize();
        while((int)mView.getPaint().measureText(str) > widthofView){
        	mView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize--);
            if(1==textSize)break;//字体大小最小为1
        }
        mView.setText(str);
	}
	
	/**
	 * showViewAdjustViewWidth：根据要显示的内容以及间距精确控制控件的宽度
	 */	
	public void showViewAdjustViewWidth(){
		//第一个参数为宽的设置，第二个参数为高的设置。   
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(      
				LinearLayout.LayoutParams.WRAP_CONTENT,      
				LinearLayout.LayoutParams.WRAP_CONTENT );      
		//
		String str = "123456789123456789123456789123456789";
		//控件宽度由字符串test_str和dimen值联合确定
        int widthOfView = getWidthByString(str)+2*getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
		mTV_AdjustViewWidth = new TextView(this);  
		mTV_AdjustViewWidth.setWidth(widthOfView);
		mTV_AdjustViewWidth.setBackgroundColor(getResources().getColor(R.color.wheat));		
		mTV_AdjustViewWidth.setGravity(Gravity.CENTER_HORIZONTAL);
		mTV_AdjustViewWidth.setSingleLine(true);
		mTV_AdjustViewWidth.setText(str);
		mLinearLayout_AdjustViewWidth.addView(mTV_AdjustViewWidth, p); //引起onGlobalLayout函数的调用
		mLinearLayout_AdjustViewWidth.setGravity(Gravity.CENTER_HORIZONTAL);
	}

	public int getWidthByString(String str){
		TextView mTextView=new TextView(this);
		boolean method1=true;
		//widthOfView：根据字符串内容确定控件的精确宽度
		int widthOfView=0;
		if(method1){
			//方法一
	        Rect bounds = new Rect();
	        Paint mTextPaint = mTextView.getPaint();
	        mTextPaint.getTextBounds(str,0,0,bounds);
	        widthOfView = (int)mTextPaint.measureText(str);    
		}else{
	        //方法二
	        TextPaint mTextPaint = mTextView.getPaint(); 
	        widthOfView = (int)mTextPaint.measureText(str);
		}
        return widthOfView;
	}	
}
