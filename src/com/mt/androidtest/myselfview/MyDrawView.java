package com.mt.androidtest.myselfview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;


public class MyDrawView extends View {  
	  
    public MyDrawView(Context context) {  
        super(context);  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        //��ȡ��Ļ���
		DisplayMetrics displayMetrics = null;
		displayMetrics = mContext.getResources().getDisplayMetrics();
		int displayMetricsWidth = displayMetrics.widthPixels;//��Ļ���
		int displayMetricsHeight = displayMetrics.heightPixels;//��Ļ�߶�
        // ��������  
        Paint p = new Paint();  
        //1�����ƾ���
        int rectLeft = 50, rectTop = 50, rectRight = displayMetricsWidth-rectLeft, rectBottom = displayMetricsHeight-4*rectTop;
        Rect rect = new Rect(rectLeft, rectTop, rectRight, rectBottom);//��һ������  
        p.setColor(mContext.getResources().getColor(R.color.white));  
        p.setStyle(Paint.Style.FILL);  
        canvas.drawRect(rect, p);  
        p.setColor(Color.BLACK);
        canvas.drawCircle((int) rect.centerX(), (int) rect.centerY(), 8, p);// �������ĵ�
        //2����������
        Paint textPaint = new Paint();  
        textPaint.setTextSize(260);  
        textPaint.setStyle(Paint.Style.FILL);  
        //�÷�����Ϊ���û������Ǹ��㾿����left,center,����right  ����������Ϊcenter  
        textPaint.setTextAlign(Paint.Align.CENTER);  
        //2.1����ȡ�����������꣬���м�����ֵ���������baseline���Եģ�����baseline��Ҫ������
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();  
        float top = fontMetrics.top;//Ϊ���ߵ������ϱ߿�ľ���
        float acent = fontMetrics.ascent;
        float decent = fontMetrics.descent;
        float bottom = fontMetrics.bottom;//Ϊ���ߵ������±߿�ľ���
        ALog.Log("top: "+top+" acent: "+acent+" decent: "+decent+ " bottom: "+bottom);
        ALog.Log("rect.centerX(): "+rect.centerX()+" rect.centerY(): "+rect.centerY());
        //2.2����ȡ���������baseline�������
        int originTextX = (int) rect.centerX();
        /**
         * �����м���y����㹫ʽ�����ο�ײ����� - ���ο�����ֿ�߶Ȳ��һ�� - fontMetrics.bottom
         */
        int baseLineY = rectBottom - ((rectBottom-rectTop) - (int)(bottom - top))/2 -  (int)bottom;
        textPaint.setColor(mContext.getResources().getColor(R.color.whitesmoke));
        canvas.drawText("�������",originTextX, baseLineY, textPaint);
        //
        int lineY = -1;
        p.setStrokeWidth(4);//����������ֱ�߿��
        //������������top��
        p.setColor(mContext.getResources().getColor(R.color.purple));
        lineY = baseLineY+(int)top;
        canvas.drawLine(rectLeft, lineY, rectRight, lineY, p);// ��top��  
        //������������asend��
        p.setColor(Color.GREEN);
        lineY = baseLineY+(int)acent;
        canvas.drawLine(rectLeft, lineY, rectRight, lineY, p);// ��acent��  
        //������������baseline��
        p.setColor(Color.RED);
        lineY = baseLineY;
        canvas.drawLine(rectLeft, lineY, rectRight, lineY, p);// ��baseline��  
        canvas.drawCircle(originTextX, baseLineY, 8, p);// ����������ʼ��
        //������������decent��
        p.setColor(Color.BLUE);
        lineY = baseLineY+(int)decent;
        canvas.drawLine(rectLeft, lineY, rectRight, lineY, p);// ��decent��  
        //������������bottom��
        p.setColor(mContext.getResources().getColor(R.color.orange));
        lineY = baseLineY+(int)bottom;
        canvas.drawLine(rectLeft, lineY, rectRight, lineY, p);// ��bottom��  
//        textPaint.setColor(Color.GREEN);
//        canvas.drawText("�������",originTextX, rect.centerY(), textPaint);
//        textPaint.setColor(Color.RED);
//        baseLineY = (int) (rect.centerY() - top/2 + bottom/2);
//        canvas.drawText("�������",originTextX, baseLineY, textPaint);

    }  
}  
