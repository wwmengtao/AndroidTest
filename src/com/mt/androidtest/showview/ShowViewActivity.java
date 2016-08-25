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
	//1��addView���View
	private LinearLayout mLayoutAddView=null;
	private TextView mTextViewAdded=null;
	//2������ͬ���ȵ��ִ������С��������Ӧ�ؼ����
	private View mLinearLayout_AdjustTextSize=null;
    private TextView mTV1_TextSize=null;
    private TextView mTV2_TextSize=null;
    //3�����ݿؼ�����ȷ���ؼ����
	private LinearLayout mLinearLayout_AdjustViewWidth=null;
	private TextView mTV_AdjustViewWidth=null;
    //4����ȡ�ֻ���ʾ��Ϣ
    private PhoneViewInfo mPhoneViewInfo=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//1��ȥ��ActionBar
		setContentView(R.layout.activity_show_view);
		//1��addView���View
		mLayoutAddView=(LinearLayout) findViewById(R.id.linearlayout_addview);
		initTextViewAdded();
		//2���������������С����Ӧ�ؼ����
		mLinearLayout_AdjustTextSize = findViewById(R.id.linearlayout_adjusttextsize);
	    mTV1_TextSize = (TextView) findViewById(R.id.textview_textsize1);
	    mTV2_TextSize = (TextView) findViewById(R.id.textview_textsize2);
	    //3�������ؼ������ȫ����ʾ����
	    mLinearLayout_AdjustViewWidth = (LinearLayout) findViewById(R.id.linearlayout_adjustviewwidth);
		//4�����»�ȡ�ֻ���ʾ��Ϣ
	    mPhoneViewInfo = new PhoneViewInfo(this);
		mPhoneViewInfo.showPhoneViewInfo();
	}
	
	/**
	 * onWindowFocusChanged��
		1���������ʱִ��˳�����£�
		onStart--->onResume--->onAttachedToWindow--->onWindowVisibilityChanged--visibility=0--->onWindowFocusChanged(true)--->
		ִ�е�onWindowFocusChanged�����Ѿ���ȡ���㣬��ʱView�Ļ��ƹ����Ѿ���ɣ����Ի�ȡView�ؼ��Ŀ�ȡ��߶ȡ�
		2���뿪���ʱ
		2.1)onPause---->onStop---->onWindowFocusChanged(false)  --- (lockscreen)
		2.2)onPause--->onWindowFocusChanged(false)--->onWindowVisibilityChanged--visibility=8--->onStop(to another activity)
	 * @param hasFocus
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		//1���̶��ؼ�����£����������С����Ӧ�ؼ�
		String time_now_str = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ987654321";
		showViewAdjustTextSize(mTV1_TextSize,time_now_str);
		time_now_str = "123456789123456789123456789";
		showViewAdjustTextSize(mTV2_TextSize,time_now_str);
		//2������Ҫ��ʾ�����ݿ��ƿؼ����
		showViewAdjustViewWidth();
		//3����ʾ�ؼ��Ŀ����Ϣ
		if(isLogRunAll)ALog.Log("/------------------------onWindowFocusChanged------------------------/");
		showWidthAndHeightLog();
		if(isLogRunAll)ALog.Log("/************************onWindowFocusChanged************************/");
		//4�����»�ȡ�ֻ���ʾ��Ϣ
		mPhoneViewInfo.showPhoneViewInfo();
		ALog.Log("statusBarHeight:"+mPhoneViewInfo.StatusBarHeight);
	    View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);  
	    ALog.Log("contentTop:"+v.getTop());
	    //��ȡ�������߶�
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
     * initTextViewAdded����onCreate�б����ã��ؼ��޷�׼ȷ��ȡ��ȸ߶ȣ�����ע��OnGlobalLayoutListener��ȷ��ȡ
     */
	public void initTextViewAdded(){
		//��һ������Ϊ������ã��ڶ�������Ϊ�ߵ����á�   
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(      
				LinearLayout.LayoutParams.WRAP_CONTENT,      
				LinearLayout.LayoutParams.WRAP_CONTENT );      
		//����addView()��������һ��TextView�����Բ�����   
		//��mLayout������һ��TextView
		mTextViewAdded = new TextView(this);  
		mTextViewAdded.setBackgroundColor(getResources().getColor(R.color.greenyellow));				
		mLayoutAddView.addView(mTextViewAdded, p); //����onGlobalLayout�����ĵ���
		mTextViewAdded.setSingleLine(true);
		mTextViewAdded.setText("View to be added!");
		//mTextViewAdded���ü�����OnGlobalLayoutListener�Գɹ���ȡ�ؼ����
		setOnGlobalLayoutListener();
	}
	
	public void setOnGlobalLayoutListener(){
		mTextViewAdded.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override 
			public void onGlobalLayout() { 
				int widthOfView=0;			
				if(null!=mTextViewAdded){
					widthOfView = mTextViewAdded.getWidth();//mTextViewAdded�Ŀ�ȿ����ɴ˴���ȡ
					if(0!=widthOfView){
						mTextViewAdded.getViewTreeObserver().removeOnGlobalLayoutListener(this);//��ȡ��0���֮��ȡ������
					}
				}
			} 
		}); 
	}
	
	/**
	 * showViewAdjustTextSize:���ݹ̶��ؼ��Ĵ�С����������ʾ���������
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
            if(1==textSize)break;//�����С��СΪ1
        }
        mView.setText(str);
	}
	
	/**
	 * showViewAdjustViewWidth������Ҫ��ʾ�������Լ���ྫȷ���ƿؼ��Ŀ��
	 */	
	public void showViewAdjustViewWidth(){
		//��һ������Ϊ������ã��ڶ�������Ϊ�ߵ����á�   
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(      
				LinearLayout.LayoutParams.WRAP_CONTENT,      
				LinearLayout.LayoutParams.WRAP_CONTENT );      
		//
		String str = "123456789123456789123456789123456789";
		//�ؼ�������ַ���test_str��dimenֵ����ȷ��
        int widthOfView = getWidthByString(str)+2*getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
		mTV_AdjustViewWidth = new TextView(this);  
		mTV_AdjustViewWidth.setWidth(widthOfView);
		mTV_AdjustViewWidth.setBackgroundColor(getResources().getColor(R.color.wheat));		
		mTV_AdjustViewWidth.setGravity(Gravity.CENTER_HORIZONTAL);
		mTV_AdjustViewWidth.setSingleLine(true);
		mTV_AdjustViewWidth.setText(str);
		mLinearLayout_AdjustViewWidth.addView(mTV_AdjustViewWidth, p); //����onGlobalLayout�����ĵ���
		mLinearLayout_AdjustViewWidth.setGravity(Gravity.CENTER_HORIZONTAL);
	}

	public int getWidthByString(String str){
		TextView mTextView=new TextView(this);
		boolean method1=true;
		//widthOfView�������ַ�������ȷ���ؼ��ľ�ȷ���
		int widthOfView=0;
		if(method1){
			//����һ
	        Rect bounds = new Rect();
	        Paint mTextPaint = mTextView.getPaint();
	        mTextPaint.getTextBounds(str,0,0,bounds);
	        widthOfView = (int)mTextPaint.measureText(str);    
		}else{
	        //������
	        TextPaint mTextPaint = mTextView.getPaint(); 
	        widthOfView = (int)mTextPaint.measureText(str);
		}
        return widthOfView;
	}	
}
