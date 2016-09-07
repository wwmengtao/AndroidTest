package com.mt.androidtest.measurelayoutdraw;

import static com.mt.androidtest.measurelayoutdraw.MeasureLayoutDrawActivity.layoutDes;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.showview.PhoneViewInfo;

public class MLDTextViewModel  extends TextView implements View.OnTouchListener{
	Context mContext = null;
	//
	private boolean isClickChanged = true;
	private String desStr = null;
	private String strSpecMode = null;
	private String strLayoutPara = null;
	//
	private boolean useDefaultWidthAndHeight = false;
	private int defaultWidth = 0;
	private int defaultHeight = 0;
	//
	private static String formatStr="%-25s";
	
	public MLDTextViewModel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setOnTouchListener(this);
		defaultWidth = (int)(PhoneViewInfo.getPhoneWidth(context)*0.7);
		defaultHeight = (int)(PhoneViewInfo.getPhoneHeight(context)*0.05);
	}
	
	protected void setLayoutDes(String newDesStr){
		desStr = newDesStr;
		setText(desStr+layoutDes);
	}
	
	protected void setDefaultWidthAndHeight(boolean setDefault){
		useDefaultWidthAndHeight = setDefault;
	}
	
    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	boolean callSuper = true;
    	int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);  
		switch(heightSpecMode){
		    	case MeasureSpec.UNSPECIFIED:
		    		strSpecMode = "UNSPECIFIED";
		    		break;
		    	case MeasureSpec.AT_MOST:
		    		strSpecMode = "AT_MOST";		
		    		break;
		    	case MeasureSpec.EXACTLY:
		    		strSpecMode = "EXACTLY";
		    		break;	 	
	    		default:
	    			strSpecMode = "UnKnown";
		}
    	LayoutParams lp = getLayoutParams();
		switch(lp.height){
	    	case ViewGroup.LayoutParams.MATCH_PARENT:
	    		strLayoutPara = "LayoutParams.MATCH_PARENT";
	    		break;
	    	case ViewGroup.LayoutParams.WRAP_CONTENT://如果heightSpecMode为MeasureSpec.AT_MOST，说明需自己制定child的宽高
	    		strLayoutPara = "LayoutParams.WRAP_CONTENT";
	    		if(strSpecMode.equals("AT_MOST") && useDefaultWidthAndHeight){//为特殊情况设置默认尺寸
	    			setMeasuredDimension(defaultWidth,defaultHeight);
	    			callSuper = false;
	    		}
	    		break;
    		default:
    			strLayoutPara = ""+lp.height;
		}
    	if(callSuper)super.onMeasure(widthMeasureSpec, heightMeasureSpec); 
		/**
		 * 由下列log信息可知，控件宽高不是一次测量就能完全确定的，可能要经历多次测量
		 */
		ALog.Log("/**********/");
		ALog.Log(desStr+"onMeasure"+" lp.height:"+strLayoutPara+" heightSpecMode:"+strSpecMode);
		ALog.Log(String.format(formatStr,"getMeasuredHeight:"+getMeasuredHeight())+String.format(formatStr," getMeasuredWidth:"+getMeasuredWidth()));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	super.onLayout(changed, l, t, r, b);
    	ALog.Log(desStr+"onLayout");
    }	
	
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        ALog.Log(desStr+"onDraw");  
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean gestureHandled = gestureDetector.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN){//保证后续touch事件继续到来
        	return true;
        }
        return gestureHandled;
    }
    
    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {//单击事件
        	ALog.Log("\n"+desStr+"requestLayout");
        	requestLayout();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {//双击事件
        	ALog.Log("\n"+desStr+"invalidate");
        	invalidate();
            return false;
        }
        
        @Override
        public void onLongPress(MotionEvent e) {//长按事件
        	ALog.Log("\n"+desStr+"TextChange");
    		if(isClickChanged){
    			setText(desStr+layoutDes+"\nchangeHeight");
    			isClickChanged=false;
    		}else{
    			setText(desStr+layoutDes);
    			isClickChanged=true;
    		}
        }
    });
    
}
