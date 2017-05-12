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
	protected float rectFLeft, rectFTop, rectFRight, rectFBottom;//������ƾ��ε�����������ֵ��������setSubRectLTRB(int, int)�ĵ���ʵʱ�ı�
	protected int WidthOfView;
	protected int HeightOfView;
	protected int PADDING_RECT = 10;//�����ıߺ��ڲ����ݼ�ļ��
	//���¶���MyBaseDrawView�ڲ����ֳ�rowNumRect��columnNumRect�и��Ӿ��ο�
	protected int rowNumRect = 1;
	protected int columnNumRect = 1;
	
	public MyBaseDrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		/***********���û���*************/
		mPaint=new Paint();    //����Ĭ�����ô���һ������
		mPaint.setStrokeWidth(3);
		mPaint.setAntiAlias(true);//ʹ�ÿ���ݹ���
		//
		mRectF=new RectF();
	}

    @Override  
    protected void onAttachedToWindow(){
//    	super.onAttachedToWindow();	
    	ALog.Log("MyBaseDrawView_onAttachedToWindow");
		post(new Runnable(){//ֻ��Attach��Window�Ŀؼ�ִ��post�̻߳�ȡ�ؼ���߲���Ч
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
	 * ����MyBaseDrawView�ڲ����ֳɵľ��ο�������
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
	 * setSubRectLTRB�������ڲ��Ӿ��������������ֵ
	 * @param row
	 * @param column
	 */
	protected void setSubRectLTRB(int row, int column){
		if(row >= rowNumRect || row < 0 || column>= columnNumRect || column < 0){
			throw new IllegalStateException("MyBaseDrawView.getSubRectLTRB error, row or column wrong!");
		}
		//1��ȷ���Ӿ��εĿ��
		int widthOfSubRect = (WidthOfView - PADDING_RECT*(columnNumRect+1))/columnNumRect;
		int heightOfSubRect = (HeightOfView - PADDING_RECT*(rowNumRect+1))/rowNumRect;
		//2��ȷ����ǰ�Ӿ��ε�����������ֵ
		rectFLeft = column*widthOfSubRect + (column+1)*PADDING_RECT;
		rectFRight = rectFLeft + widthOfSubRect;
		rectFTop = row*heightOfSubRect + (row+1)*PADDING_RECT;
		rectFBottom = rectFTop + heightOfSubRect;
	}
	
    /**
     * ���ƾ��������һ��ƾ������ĵ�
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    protected void drawRectFrame(float left, float top, float right, float bottom){
		mPaint.setStyle(Paint.Style.STROKE);//���û�������ΪSTROKE
		mPaint.setColor(Color.BLACK);
		mCanvas.drawRect(left, top, right, bottom, mPaint);// 1�����ƾ�������
		mPaint.setStyle(Paint.Style.FILL);//���û�������ΪFILL
		mCanvas.drawCircle(left+(right-left)/2, top+(bottom-top)/2, 8, mPaint);//2�����ƾ����������ĵ�
		mPaint.setStyle(Paint.Style.STROKE);//���û�������ΪSTROKE
    }
}
