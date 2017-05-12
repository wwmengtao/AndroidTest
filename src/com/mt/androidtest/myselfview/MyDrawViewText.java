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
 * 绘制文本原理说明图，使用方式为在布局文件中使用类似如下方式：
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
        //1、绘制矩形
        setSubRectLTRB(0,0);//设置内部矩形框行列数，0,0表示不划分矩形框
		drawRectFrame(rectFLeft, rectFTop, rectFRight, rectFBottom);
        //2、绘制文字
        Paint textPaint = new Paint();  
        setTextSize(textPaint, "你好世界");
        textPaint.setStyle(Paint.Style.FILL);  
        //该方法即为设置基线上那个点究竟是left,center,还是right
        textPaint.setTextAlign(Paint.Align.CENTER);  
        //2.1、获取文字区域坐标，下列几个数值都是相对于baseline而言的，但是baseline需要计算获得
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();  
        float top = fontMetrics.top;//为基线到字体上边框的距离
        float acent = fontMetrics.ascent;
        float decent = fontMetrics.descent;
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离
        ALog.Log("top: "+top+" acent: "+acent+" decent: "+decent+ " bottom: "+bottom);
        ALog.Log("rect.centerX(): "+mRectF.centerX()+" rect.centerY(): "+mRectF.centerY());
        //2.2、获取文字区域的baseline起点坐标
        mRectF.set(rectFLeft, rectFTop, rectFRight, rectFBottom);
        int originTextX = (int) mRectF.centerX();
        /**
         * 基线中间点的y轴计算公式：矩形框底部坐标 - 矩形框和文字框高度差的一半 - fontMetrics.bottom
         */
        int baseLineY = (int)(rectFBottom - ((rectFBottom-rectFTop) - (bottom - top))/2 - bottom);
        textPaint.setColor(mContext.getResources().getColor(R.color.whitesmoke));
        canvas.drawText("你好世界",originTextX, baseLineY, textPaint);
        //
        int lineY = -1;
        //绘制文字区域top线
        mPaint.setColor(mContext.getResources().getColor(R.color.purple));
        lineY = baseLineY+(int)top;
        canvas.drawLine(rectFLeft, lineY, rectFRight, lineY, mPaint);// 画top线  
        //绘制文字区域asend线
        mPaint.setColor(Color.GREEN);
        lineY = baseLineY+(int)acent;
        canvas.drawLine(rectFLeft, lineY, rectFRight, lineY, mPaint);// 画acent线  
        //绘制文字区域baseline线
        mPaint.setColor(Color.RED);
        lineY = baseLineY;
        canvas.drawLine(rectFLeft, lineY, rectFRight, lineY, mPaint);// 画baseline线 
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(originTextX, baseLineY, 8, mPaint);// 文字区域起始点
        //绘制文字区域decent线
        mPaint.setColor(Color.BLUE);
        lineY = baseLineY+(int)decent;
        canvas.drawLine(rectFLeft, lineY, rectFRight, lineY, mPaint);// 画decent线  
        //绘制文字区域bottom线
        mPaint.setColor(mContext.getResources().getColor(R.color.orange));
        lineY = baseLineY+(int)bottom;
        canvas.drawLine(rectFLeft, lineY, rectFRight, lineY, mPaint);// 画bottom线  
        //
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle((int) mRectF.centerX(), (int) mRectF.centerY(), 8, mPaint);// 绘制矩形中心点
    }  
    
    /**
     * setTextSize：设置恰好能够填满矩形框的文字
     * @param paint
     * @param str
     */
	public void setTextSize(Paint paint,String str){
		if(null==paint||null==str)return;
        int widthofView = WidthOfView - 2*PADDING_RECT;
        float textSize = (int)paint.getTextSize();ALog.Log("textSize: "+textSize);
        if((int)paint.measureText(str) > widthofView){//1、如果字体过大
        	do{
            	paint.setTextSize(textSize--);
                if(1==textSize)break;//字体大小最小为1
        	}while((int)paint.measureText(str) > widthofView);
        }else{//2、如果字体不够大
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
