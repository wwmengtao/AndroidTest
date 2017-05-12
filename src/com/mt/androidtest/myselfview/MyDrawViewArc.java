package com.mt.androidtest.myselfview;

import com.mt.androidtest.ALog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 绘制圆弧原理说明图，使用方式为在布局文件中使用类似如下方式：
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
        setSubRectRowAndColumn(2,2);//设置内部矩形框行列数
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
		//绘制MyDrawViewArc中心点
		mPaint.setStyle(Paint.Style.FILL);//设置画笔类型为FILL
		mCanvas.drawCircle((float)(WidthOfView*0.5), (float)(HeightOfView*0.5), 8, mPaint);
    }  
    
    /**
     * drawModule：绘制圆弧所在矩形区域、矩形区域中线点、圆弧
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
    	mPaint.setStyle(paintSty);//设置画笔类型为STROKE
		/**
		 * startAngle -    开始角度（以时钟3点的方向为0°，顺时针为正方向）
		 * sweepAngle - 扫过角度（以时钟3点的方向为0°，顺时针为正方向）
		 */
		mCanvas.drawArc(mRectF, startAngle, sweepAngle, useCenter, mPaint);//1、绘制圆弧(是否填充是否含圆心？)
		drawRectFrame(rectFLeft, rectFTop, rectFRight, rectFBottom);
    }
    
	/**
	 * startAngle -    开始角度（以时钟3点的方向为0°，顺时针为正方向）
	 * sweepAngle - 扫过角度（以时钟3点的方向为0°，顺时针为正方向）
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
