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
        //获取屏幕宽高
		DisplayMetrics displayMetrics = null;
		displayMetrics = mContext.getResources().getDisplayMetrics();
		int displayMetricsWidth = displayMetrics.widthPixels;//屏幕宽度
		int displayMetricsHeight = displayMetrics.heightPixels;//屏幕高度
        // 创建画笔  
        Paint p = new Paint();  
        //1、绘制矩形
        int rectLeft = 50, rectTop = 50, rectRight = displayMetricsWidth-rectLeft, rectBottom = displayMetricsHeight-4*rectTop;
        Rect rect = new Rect(rectLeft, rectTop, rectRight, rectBottom);//画一个矩形  
        p.setColor(mContext.getResources().getColor(R.color.white));  
        p.setStyle(Paint.Style.FILL);  
        canvas.drawRect(rect, p);  
        p.setColor(Color.BLACK);
        canvas.drawCircle((int) rect.centerX(), (int) rect.centerY(), 8, p);// 矩形中心点
        //2、绘制文字
        Paint textPaint = new Paint();  
        textPaint.setTextSize(260);  
        textPaint.setStyle(Paint.Style.FILL);  
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center  
        textPaint.setTextAlign(Paint.Align.CENTER);  
        //2.1、获取文字区域坐标，下列几个数值都是相对于baseline而言的，但是baseline需要计算获得
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();  
        float top = fontMetrics.top;//为基线到字体上边框的距离
        float acent = fontMetrics.ascent;
        float decent = fontMetrics.descent;
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离
        ALog.Log("top: "+top+" acent: "+acent+" decent: "+decent+ " bottom: "+bottom);
        ALog.Log("rect.centerX(): "+rect.centerX()+" rect.centerY(): "+rect.centerY());
        //2.2、获取文字区域的baseline起点坐标
        int originTextX = (int) rect.centerX();
        /**
         * 基线中间点的y轴计算公式：矩形框底部坐标 - 矩形框和文字框高度差的一半 - fontMetrics.bottom
         */
        int baseLineY = rectBottom - ((rectBottom-rectTop) - (int)(bottom - top))/2 -  (int)bottom;
        textPaint.setColor(mContext.getResources().getColor(R.color.whitesmoke));
        canvas.drawText("你好世界",originTextX, baseLineY, textPaint);
        //
        int lineY = -1;
        p.setStrokeWidth(4);//设置所绘制直线宽度
        //绘制文字区域top线
        p.setColor(mContext.getResources().getColor(R.color.purple));
        lineY = baseLineY+(int)top;
        canvas.drawLine(rectLeft, lineY, rectRight, lineY, p);// 画top线  
        //绘制文字区域asend线
        p.setColor(Color.GREEN);
        lineY = baseLineY+(int)acent;
        canvas.drawLine(rectLeft, lineY, rectRight, lineY, p);// 画acent线  
        //绘制文字区域baseline线
        p.setColor(Color.RED);
        lineY = baseLineY;
        canvas.drawLine(rectLeft, lineY, rectRight, lineY, p);// 画baseline线  
        canvas.drawCircle(originTextX, baseLineY, 8, p);// 文字区域起始点
        //绘制文字区域decent线
        p.setColor(Color.BLUE);
        lineY = baseLineY+(int)decent;
        canvas.drawLine(rectLeft, lineY, rectRight, lineY, p);// 画decent线  
        //绘制文字区域bottom线
        p.setColor(mContext.getResources().getColor(R.color.orange));
        lineY = baseLineY+(int)bottom;
        canvas.drawLine(rectLeft, lineY, rectRight, lineY, p);// 画bottom线  
//        textPaint.setColor(Color.GREEN);
//        canvas.drawText("你好世界",originTextX, rect.centerY(), textPaint);
//        textPaint.setColor(Color.RED);
//        baseLineY = (int) (rect.centerY() - top/2 + bottom/2);
//        canvas.drawText("你好世界",originTextX, baseLineY, textPaint);

    }  
}  
