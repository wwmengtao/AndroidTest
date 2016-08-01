package com.mt.androidtest.showview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class ShowViewActivity extends BaseActivity{
	boolean isLogRunAll=false;
	private LinearLayout mLayout=null;
	private TextView mTextViewAdded=null;
	private View mLinearLayout_TextSize=null;
    private TextView mTV1_TextSize=null;
    private TextView mTV2_TextSize=null;    
    private LinearLayout mLinearLayout_Parent=null;    
    private GridLayout mGridLayout_Calculator=null;    
    private LinearLayout mLinearLayout_Child=null;
    private static Handler mHandler=null;
    private LayoutInflater mLayoutInflater=null;
	private final int MSG_INIT_TEXT_VIEW_ADDED=0x000;
	private final int MSG_INIT_TEXT_VIEW_ADDED_WIDTH=0x001;
	private final int MSG_SHOW_VIEW_ADD_VIEW=0x002;
	private final int MSG_SHOW_VIEW_FIXED_LENGTH=0x003;
	private final int MSG_SHOW_VIEW_FINALLY=0x004;	
	private String [] mMethodNameFT={"showViewAdded","showViewFixedLength","showViewFixedSize",
			"showChildView"};
	private String [] mActivitiesName={"InflateActivity"};			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_view);
		mLayoutInflater=getLayoutInflater();
		super.initListFTData(mMethodNameFT);
		super.initListActivityData(mActivitiesName);
		mLayout=(LinearLayout) findViewById(R.id.linearlayout_showview);
		mLinearLayout_TextSize = findViewById(R.id.linearlayout_textsize);
	    mTV1_TextSize = (TextView) findViewById(R.id.textview_textsize1);
	    mTV2_TextSize = (TextView) findViewById(R.id.textview_textsize2);
	    mLinearLayout_Parent= (LinearLayout) findViewById(R.id.parentView);
	    initParentView();
	}

	@Override
	public void onResume(){	
		super.onResume();
		mHandler=getHandler();
	}
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
	}
	
	public void initParentView(){
		//1����Ӽ�����GridLayout���֣�ֱ��
	    mGridLayout_Calculator=(GridLayout) mLayoutInflater.inflate(R.layout.gridlayout_calculator,mLinearLayout_Parent,false);
	    mLinearLayout_Parent.addView(mGridLayout_Calculator);
	    //2�����preference���Բ���
	    mLinearLayout_Child=(LinearLayout) mLayoutInflater.inflate(R.layout.linearlayout_preference,mLinearLayout_Parent);
	    //2.1�����ÿ�ͷ��ͼ�ꡣע�͵����д��룬�ᷢ�����ֿ�����ʾ����֮��ͼƬ������ʾ
	    View imageFrame = mLinearLayout_Child.findViewById(R.id.icon_frame);
	    ViewGroup.LayoutParams lp=null;
	    if(!imageFrame.isShown()){
	    	imageFrame.setVisibility(View.VISIBLE);
	    	ImageView mIMG = (ImageView)imageFrame.findViewById(R.id.icon_img);
		    mIMG.setBackgroundResource(R.drawable.number1_g);
		    setLayoutParams(mIMG,0.3,0.3);
	    }
	    //2.2�����ý�β�Ĳ�������
	    LinearLayout widget_frame=(LinearLayout)mLinearLayout_Child.findViewById(R.id.widget_frame);
	    setLayoutParams(widget_frame,0.3,0.3);
	    widget_frame.setBackgroundResource(R.drawable.number1_r);
	    //3�����FrameLayout
	    FrameLayout mFrameLayout = (FrameLayout) mLayoutInflater.inflate(R.layout.framelayout_lmr,mLinearLayout_Parent,false);
	    mLinearLayout_Parent.addView(mFrameLayout);
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
		case "showChildView":
			if(mLinearLayout_Parent.isShown()){
				mLinearLayout_Parent.setVisibility(View.GONE);
			}else{
				mLinearLayout_Parent.setVisibility(View.VISIBLE);
			}
			break;
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
		String time_now_str = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ987654321";
		showTextSizeView(mTV1_TextSize,time_now_str);
		time_now_str = "123456789123456789123456789";
		showTextSizeView(mTV2_TextSize,time_now_str);
		//if(isLogRunAll)ALog.Log("/------------------------onWindowFocusChanged------------------------/");
		showWidthAndHeightLog();
		//if(isLogRunAll)ALog.Log("/************************onWindowFocusChanged************************/");
	}	
	
	/**
	 * ���ݹ̶��ؼ��Ĵ�С����������ʾ���������
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
	 * showViewAddView���ؼ��������Ϊ��ʼ���
	 */
	public void showViewAddView(){
		mTextViewAdded.setWidth(mTVAddedParams.widthOfTextViewAdded);
		mTextViewAdded.setGravity(Gravity.CENTER_HORIZONTAL);
		mTextViewAdded.setText("showViewByAddView");
	}
	
	/**
	 * showViewFixedLength������Ҫ��ʾ�������Լ���ྫȷ���ƿؼ��Ŀ��
	 */
	public void showViewFixedLength(){
		String str = "123456789123456789123456789123456789";
		//�ؼ�������ַ���test_str��dimenֵ����ȷ��
        int widthOfView = getWidthByString(str)+2*getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
		mTextViewAdded.setWidth(widthOfView);
		mTextViewAdded.setGravity(Gravity.CENTER_HORIZONTAL);
		mTextViewAdded.setText(str);
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

	public void initTextViewAdded(){
		//��һ������Ϊ������ã��ڶ�������Ϊ�ߵ����á�   
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(      
				LinearLayout.LayoutParams.WRAP_CONTENT,      
				LinearLayout.LayoutParams.WRAP_CONTENT );      
		//����addView()��������һ��TextView�����Բ�����   
		//��mLayout������һ��TextView
		mTextViewAdded = new TextView(this);  
		mTextViewAdded.setBackgroundColor(getResources().getColor(R.color.wheat));				
		mLayout.addView(mTextViewAdded, p); //����onGlobalLayout�����ĵ���
		mTextViewAdded.setSingleLine(true);
		mTextViewAdded.setText("View added");
		//����ֱ�ӻ�ȡ�ؼ����Ϊ0������ʹ��ViewTreeObserver.OnGlobalLayoutListener������
		/*
		textViewAddedParams.widthOfTextViewAdded = mTextViewAdded.getMeasuredWidth();
		if(isLogRunAll)ALog.Log("mTextViewAdded_getWidth:"+textViewAddedParams.widthOfTextViewAdded);
		*/
		//����1.1��mTextViewAddedֱ�����ü�����
		setOnGlobalLayoutListener();
		//����1.2��mTextViewAdded����mOnGlobalLayoutListener������
		//setOnGlobalLayoutListener2();
		//����2����һ��runnable��ӵ�Layout�����У�View.post()��runnable�����еķ�������View��measure��layout���¼��󴥷�
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
    
	String regShowWidthAndHeight = "id\\/[a-zA-Z]+.+\\}";//������ȡ�ؼ�id���������ݲ�Ҫ
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
