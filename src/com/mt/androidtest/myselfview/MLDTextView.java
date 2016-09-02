package com.mt.androidtest.myselfview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mt.androidtest.ALog;
/**
 * һ��������ΪLineaLayoutʱ
 * 1)onMeasureִ�������Σ�ԭ����ViewRootImpl.performTraversals�ڲ�����measureHierarchy����������
 * 2)onLayout��onDraw��ִ����һ��
 * ����������ΪRelativeLayoutʱ
 * 1)onMeasureִ�����ĴΣ�ԭ����ViewRootImpl.performTraversals�ڲ�measureHierarchy���ε��ã�����ÿ��
 * ִ��measureHierarchyʱ��onMeasure��ִ������
 * 2)onLayout��onDraw��ִ����һ��
 * @author Mengtao1
 *
 */
public class MLDTextView  extends TextView{

	public MLDTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setText("MLDTextView");
		setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ALog.Log("MLDTextView_onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	super.onLayout(changed, l, t, r, b);
    	ALog.Log("MLDTextView_onLayout");
    }	
	
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        ALog.Log("MLDTextView_onDraw");
    }
}
