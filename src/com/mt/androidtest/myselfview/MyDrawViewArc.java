package com.mt.androidtest.myselfview;

import com.mt.androidtest.ALog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * ����Բ��ԭ��˵��ͼ��ʹ�÷�ʽΪ�ڲ����ļ���ʹ���������·�ʽ��
 */
public class MyDrawViewArc extends MyBaseDrawView implements View.OnClickListener{  

	public MyDrawViewArc(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}
	
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        if(0 == HeightOfView)return;
        setSubRectRowAndColumn(2,2);//�����ڲ����ο�������
        Paint.Style[][] paintStyles = {{Paint.Style.FILL, Paint.Style.FILL},
        												{Paint.Style.STROKE, Paint.Style.STROKE}};
        int[][] colors = {{Color.RED, Color.BLUE},
        								{Color.RED, Color.BLUE}};
        boolean[][] useCenter = {{false, true},
        												{false, true}};
        for(int i=0; i<rowNumRect; i++){
        	for(int j=0; j<columnNumRect; j++){
        		setSubRectLTRB(i,j);
        		drawModule(rectFLeft, rectFTop, rectFRight, rectFBottom, paintStyles[i][j], colors[i][j], useCenter[i][j]);
        	}
        }
		//����MyDrawViewArc���ĵ�
		mPaint.setStyle(Paint.Style.FILL);//���û�������ΪFILL
		mCanvas.drawCircle((float)(WidthOfView*0.5), (float)(HeightOfView*0.5), 8, mPaint);
    }  
    
    /**
     * drawModule������Բ�����ھ������򡢾����������ߵ㡢Բ��
     * @param rectFLeft
     * @param rectFTop
     * @param rectFRight
     * @param rectFBottom
     * @param paintSty
     * @param paintColor
     * @param useCenter
     */
    private void drawModule(float rectFLeft, float rectFTop, float rectFRight, float rectFBottom, 
    		Paint.Style paintSty, int paintColor, boolean useCenter){
    	mPaint.setColor(paintColor);
    	mRectF.set(rectFLeft, rectFTop, rectFRight, rectFBottom);
    	mPaint.setStyle(paintSty);//���û�������ΪSTROKE
		/**
		 * startAngle -    ��ʼ�Ƕȣ���ʱ��3��ķ���Ϊ0�㣬˳ʱ��Ϊ������
		 * sweepAngle - ɨ���Ƕȣ���ʱ��3��ķ���Ϊ0�㣬˳ʱ��Ϊ������
		 */
		mCanvas.drawArc(mRectF, startAngle, sweepAngle, useCenter, mPaint);//1������Բ��(�Ƿ�����Ƿ�Բ�ģ�)
		drawRectFrame(rectFLeft, rectFTop, rectFRight, rectFBottom);
    }
    
	/**
	 * startAngle -    ��ʼ�Ƕȣ���ʱ��3��ķ���Ϊ0�㣬˳ʱ��Ϊ������
	 * sweepAngle - ɨ���Ƕȣ���ʱ��3��ķ���Ϊ0�㣬˳ʱ��Ϊ������
	 */
    private static final float ANGLE_SA= 30;
    private static final float ANGLE_SW = 60;
    private float startAngle = -ANGLE_SA; 
    private float sweepAngle = ANGLE_SW;
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		startAngle+=sweepAngle;
		if(startAngle >= 360){
			startAngle = ANGLE_SA;
			sweepAngle = -ANGLE_SW;
		}else if(startAngle <= -360){
			startAngle = -ANGLE_SA;
			sweepAngle = ANGLE_SW;
		}
		ALog.Log("startAngle: "+startAngle+" sweepAngle: "+sweepAngle);
		invalidate();
	}
}  
