package com.mt.androidtest.myselfview;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
/**
 * �����ı�ԭ��˵��ͼ��ʹ�÷�ʽΪ�ڲ����ļ���ʹ���������·�ʽ��
 */
public class MyDrawViewText extends MyBaseDrawView {  

    public MyDrawViewText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setOnClickListener(this);
	}
	
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        if(0 == HeightOfView)return;
        //1�����ƾ���
        setSubRectLTRB(0,0);//�����ڲ����ο���������0,0��ʾ�����־��ο�
		drawRectFrame(rectFLeft, rectFTop, rectFRight, rectFBottom);
        //2����������
        Paint textPaint = new Paint();  
        setTextSize(textPaint, "�������");
        textPaint.setStyle(Paint.Style.FILL);  
        //�÷�����Ϊ���û������Ǹ��㾿����left,center,����right
        textPaint.setTextAlign(Paint.Align.CENTER);  
        //2.1����ȡ�����������꣬���м�����ֵ���������baseline���Եģ�����baseline��Ҫ������
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();  
        float top = fontMetrics.top;//Ϊ���ߵ������ϱ߿�ľ���
        float acent = fontMetrics.ascent;
        float decent = fontMetrics.descent;
        float bottom = fontMetrics.bottom;//Ϊ���ߵ������±߿�ľ���
        ALog.Log("top: "+top+" acent: "+acent+" decent: "+decent+ " bottom: "+bottom);
        ALog.Log("rect.centerX(): "+mRectF.centerX()+" rect.centerY(): "+mRectF.centerY());
        //2.2����ȡ���������baseline�������
        mRectF.set(rectFLeft, rectFTop, rectFRight, rectFBottom);
        int originTextX = (int) mRectF.centerX();
        /**
         * �����м���y����㹫ʽ�����ο�ײ����� - ���ο�����ֿ�߶Ȳ��һ�� - fontMetrics.bottom
         */
        int baseLineY = (int)(rectFBottom - ((rectFBottom-rectFTop) - (bottom - top))/2 - bottom);
        textPaint.setColor(mContext.getResources().getColor(R.color.whitesmoke));
        canvas.drawText("�������",originTextX, baseLineY, textPaint);
        //
        int lineY = -1;
        //������������top��
        mPaint.setColor(mContext.getResources().getColor(R.color.purple));
        lineY = baseLineY+(int)top;
        canvas.drawLine(rectFLeft, lineY, rectFRight, lineY, mPaint);// ��top��  
        //������������asend��
        mPaint.setColor(Color.GREEN);
        lineY = baseLineY+(int)acent;
        canvas.drawLine(rectFLeft, lineY, rectFRight, lineY, mPaint);// ��acent��  
        //������������baseline��
        mPaint.setColor(Color.RED);
        lineY = baseLineY;
        canvas.drawLine(rectFLeft, lineY, rectFRight, lineY, mPaint);// ��baseline�� 
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(originTextX, baseLineY, 8, mPaint);// ����������ʼ��
        //������������decent��
        mPaint.setColor(Color.BLUE);
        lineY = baseLineY+(int)decent;
        canvas.drawLine(rectFLeft, lineY, rectFRight, lineY, mPaint);// ��decent��  
        //������������bottom��
        mPaint.setColor(mContext.getResources().getColor(R.color.orange));
        lineY = baseLineY+(int)bottom;
        canvas.drawLine(rectFLeft, lineY, rectFRight, lineY, mPaint);// ��bottom��  
        //
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle((int) mRectF.centerX(), (int) mRectF.centerY(), 8, mPaint);// ���ƾ������ĵ�
    }  
    
    /**
     * setTextSize������ǡ���ܹ��������ο������
     * @param paint
     * @param str
     */
	public void setTextSize(Paint paint,String str){
		if(null==paint||null==str)return;
        int widthofView = WidthOfView - 2*PADDING_RECT;
        float textSize = (int)paint.getTextSize();ALog.Log("textSize: "+textSize);
        if((int)paint.measureText(str) > widthofView){//1������������
        	do{
            	paint.setTextSize(textSize--);
                if(1==textSize)break;//�����С��СΪ1
        	}while((int)paint.measureText(str) > widthofView);
        }else{//2��������岻����
        	do{
            	paint.setTextSize(textSize++);
        	}while((int)paint.measureText(str) <= widthofView);
        }
        paint.setTextSize(textSize);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		invalidate();
	}
}  
