package com.mt.androidtest.measurelayoutdraw;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest.ALog;

/**
 * һ��getMeasuredxxx()��getxxx()��ͬ�����
 * ����TextView���ڲ��ֵ�ԭ�򣬵����е�����ͼgetMeasuredxxx()��getxxx()��ͬ���е���ͬ����˵���������ݣ�
 * getMeasuredxxx()�Ľ�������ǿؼ�����ֵ����onLayoutʱ��ο�ʹ�ã��ؼ�ʵ��ռ��Ļ��СҪ��getxxx()�����
 * ����ViewRootImpl.requestLayoutDuringLayout�ĵ��ó���������ͼ��MLDRootViewGroup2.onLayout�е���requestLayout()��
 * @author Mengtao1
 *
 */
public class MLDRootViewGroup2 extends ViewGroup {  
	private View mView, mViewPre, mViewNext;
	private int preViewsHeight=0;
	private int defMeasuredWidthSize = 600;
	private int defMeasuredHeightSize = 600;
	private int defLayoutWidthSize = 800;
	private int defLayoutHeightSize = 300;	
	private int extraSize = 200;
	private int requestLayoutDuringLayoutCount = 0;
	//
	private static int offsetTop = 0;
	private static int offsetRight = 0;
	
    public MLDRootViewGroup2(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        offsetTop = 0;
        offsetRight = 0;
    }  
    
    public static void setOffsetTop(int value){
    	offsetTop = value;
    }
    
    public static void setOffsetRight(int value){
    	offsetRight = value;
    }
    
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
        	mView = getChildAt(i);
            measureChild(mView, MeasureSpec.makeMeasureSpec(defMeasuredWidthSize, MeasureSpec.EXACTLY), 
            										MeasureSpec.makeMeasureSpec(defMeasuredHeightSize, MeasureSpec.EXACTLY));  
        }
    }  
  
    @Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b) {  
    	ALog.Log("MLDRootViewGroup2_onLayout");
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
        	if(0==i){
        		mViewPre=getChildAt(i);
        		mViewPre.layout(0, offsetTop, defLayoutWidthSize+offsetRight, defLayoutHeightSize+offsetTop); //��һ��View����Ĭ�Ͽ�߲��֣�������getMeasuredxxx���
        		if(requestLayoutDuringLayoutCount++<6){
        			mViewPre.requestLayout();//�ᵼ�µ���ViewRootImpl.requestLayoutDuringLayout
        			ALog.Log("mViewPre.requestLayout()");
        		}
        	}else{
        		preViewsHeight += mViewPre.getHeight();
        		mViewNext=getChildAt(i);
        		if(childCount-1==i){//���һ���ӿؼ�����ʱ�򣬿�ȱ�getMeasuredWidth��extraSize
        			mViewNext.layout(0, preViewsHeight+offsetTop, mViewNext.getMeasuredWidth()+extraSize, preViewsHeight+mViewNext.getMeasuredHeight()+offsetTop);
        			preViewsHeight = 0;
        		}else{
            		mViewNext.layout(0, preViewsHeight+offsetTop, mViewNext.getMeasuredWidth(), preViewsHeight+mViewNext.getMeasuredHeight()+offsetTop);
        		}
        		mViewPre = mViewNext;
        	}
        }
    }
    
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) {  
        super.onWindowFocusChanged(hasFocus);  
        ALog.Log("MLDRootViewGroup2_onWindowFocusChanged");
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
        	mView = getChildAt(i);
            ALog.Log("mView"+i+".W:"+mView.getWidth()+" MW:"+mView.getMeasuredWidth()+" H:"+mView.getHeight()+" MH:"+mView.getMeasuredHeight());         	
        }
     }  

}  
