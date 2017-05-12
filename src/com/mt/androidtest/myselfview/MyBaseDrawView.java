package com.mt.androidtest.myselfview;

import com.mt.androidtest.ALog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MyBaseDrawView extends View implements View.OnClickListener{
	protected Context mContext;
	protected Paint mPaint=null;
	protected Canvas mCanvas=null;
	protected RectF mRectF = null;
	protected float rectFLeft, rectFTop, rectFRight, rectFBottom;//所需绘制矩形的左上右下数值，会随着setSubRectLTRB(int, int)的调用实时改变
	protected int WidthOfView;
	protected int HeightOfView;
	protected int PADDING_RECT = 10;//矩形四边和内部内容间的间隔
	//以下定义MyBaseDrawView内部划分成rowNumRect行columnNumRect列个子矩形框
	protected int rowNumRect = 1;
	protected int columnNumRect = 1;
	
	public MyBaseDrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		/***********配置画笔*************/
		mPaint=new Paint();    //采用默认设置创建一个画笔
		mPaint.setStrokeWidth(3);
		mPaint.setAntiAlias(true);//使用抗锯齿功能
		//
		mRectF=new RectF();
	}

    @Override  
    protected void onAttachedToWindow(){
//    	super.onAttachedToWindow();	
    	ALog.Log("MyBaseDrawView_onAttachedToWindow");
		post(new Runnable(){//只有Attach到Window的控件执行post线程获取控件宽高才有效
			@Override
			public void run() {
				// TODO Auto-generated method stub
				WidthOfView = getWidth();
				HeightOfView = getHeight();
				ALog.Log("MyBaseDrawView.onAttachedToWindow_post");
				postInvalidate();
			}
		});
    }
	
    @Override  
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	mCanvas = canvas;
    	ALog.Log("MyBaseDrawView_onDraw");
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 设置MyBaseDrawView内部划分成的矩形框行列数
	 * @param rowNumRect
	 * @param columnNumRect
	 */
	protected void setSubRectRowAndColumn(int rowNumRect, int columnNumRect){
		if(rowNumRect <=0 || columnNumRect <=0){
			throw new IllegalStateException("MyBaseDrawView.setSubRectRowAndColumn error, rowNumRect or columnNumRect wrong!");
		}
		this.rowNumRect = rowNumRect;
		this.columnNumRect = columnNumRect;
	}
	
	/**
	 * setSubRectLTRB：设置内部子矩阵的左上右下数值
	 * @param row
	 * @param column
	 */
	protected void setSubRectLTRB(int row, int column){
		if(row >= rowNumRect || row < 0 || column>= columnNumRect || column < 0){
			throw new IllegalStateException("MyBaseDrawView.getSubRectLTRB error, row or column wrong!");
		}
		//1、确定子矩形的宽高
		int widthOfSubRect = (WidthOfView - PADDING_RECT*(columnNumRect+1))/columnNumRect;
		int heightOfSubRect = (HeightOfView - PADDING_RECT*(rowNumRect+1))/rowNumRect;
		//2、确定当前子矩形的左上右下数值
		rectFLeft = column*widthOfSubRect + (column+1)*PADDING_RECT;
		rectFRight = rectFLeft + widthOfSubRect;
		rectFTop = row*heightOfSubRect + (row+1)*PADDING_RECT;
		rectFBottom = rectFTop + heightOfSubRect;
	}
	
    /**
     * 绘制矩形区域并且绘制矩形中心点
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    protected void drawRectFrame(float left, float top, float right, float bottom){
		mPaint.setStyle(Paint.Style.STROKE);//设置画笔类型为STROKE
		mPaint.setColor(Color.BLACK);
		mCanvas.drawRect(left, top, right, bottom, mPaint);// 1、绘制矩形区域
		mPaint.setStyle(Paint.Style.FILL);//设置画笔类型为FILL
		mCanvas.drawCircle(left+(right-left)/2, top+(bottom-top)/2, 8, mPaint);//2、绘制矩形区域中心点
		mPaint.setStyle(Paint.Style.STROKE);//设置画笔类型为STROKE
    }
}
