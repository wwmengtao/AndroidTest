package com.mt.androidtest.myselfview;

import com.mt.androidtest.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

/**
 * 绘制多边形原理说明图，使用方式为在布局文件中使用类似如下方式：
 */
public class MyDrawViewPolygon extends MyBaseDrawView{

	public MyDrawViewPolygon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        if(0 == HeightOfView)return;
        setSubRectRowAndColumn(3,2);//设置内部矩形框行列数
        String[][] shapes = {{"oval","triangle",},{"polygon","filletRect"},{"bessel","drawable"}};
        Path path = new Path();
        for(int i=0; i<rowNumRect; i++){
        	for(int j=0; j<columnNumRect; j++){
        		setSubRectLTRB(i,j);
        		mPaint.setStyle(Paint.Style.STROKE);//设置画笔类型为STROKE
        		switch(shapes[i][j]){
	        		case "oval"://绘制椭圆
	        			mRectF.set(rectFLeft, rectFTop, rectFRight, rectFBottom);
	        			mCanvas.drawOval(mRectF, mPaint);  
	        			break;
	        		case "triangle"://绘制三角形
	        	        float pathX, pathY;
	        			rectFLeft = PADDING_RECT+(float)(WidthOfView*0.5); 
	        			rectFRight = WidthOfView-PADDING_RECT;
	        	        pathX = rectFLeft+(rectFRight-rectFLeft)/2;
	        	        pathY = rectFTop+PADDING_RECT;
	        	        path.moveTo(pathX, pathY);//path.moveTo:设置Path的起点  
	        	        pathY = rectFBottom - PADDING_RECT;
	        	        path.lineTo(pathX, pathY);  
	        	        pathX = rectFRight - PADDING_RECT;
	        	        path.lineTo(pathX, pathY);  
	        	        path.close(); // 使这些点构成封闭的多边形  
	        	        mCanvas.drawPath(path, mPaint); 
	        			break;
	        		case "polygon"://绘制多边形
	        	        path.moveTo(rectFLeft + PADDING_RECT, rectFTop + (rectFBottom - rectFTop)/2);  //path.moveTo:设置Path的起点  
	        	        path.lineTo(rectFLeft + (rectFRight-rectFLeft)/3, rectFTop + PADDING_RECT);  
	        	        path.lineTo(rectFLeft + 2*(rectFRight-rectFLeft)/3, rectFTop + PADDING_RECT);  
	        	        path.lineTo(rectFRight-PADDING_RECT, rectFTop + (rectFBottom - rectFTop)/2);  
	        	        path.lineTo(rectFLeft + 2*(rectFRight-rectFLeft)/3, rectFBottom - PADDING_RECT);  
	        	        path.lineTo(rectFLeft + (rectFRight-rectFLeft)/3, rectFBottom - PADDING_RECT);  
	        	        path.close();//封闭  
	        	        mCanvas.drawPath(path, mPaint);  
	        			break;
	        		case "filletRect"://绘制圆角矩形  
	        	        mRectF.set(rectFLeft + PADDING_RECT, rectFTop+ PADDING_RECT, rectFRight - PADDING_RECT, rectFBottom - PADDING_RECT);
	        	        mCanvas.drawRoundRect(mRectF, 4*PADDING_RECT, 8*PADDING_RECT, mPaint);//第二个参数是x半径，第三个参数是y半径 
	        			break;
	        		case "bessel":
	        	        //画贝塞尔曲线  
	        			/**
	        			 * 贝赛尔曲线原理图参照为知笔记：Android 绘图贝塞尔曲线简单使用
	        			 * Path.moveTo(float x, float y) // Path的初始点
	        			 * Path.lineTo(float x, float y) // 线性公式的贝赛尔曲线, 其实就是直线
	        			 * Path.quadTo(float x1, float y1, float x2, float y2) // 二次方公式的贝赛尔曲线，想象成拉扯线性贝塞尔橡皮筋的感觉
	        			 * Path.cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) // 三次方公式的贝赛尔曲线，想象成拉扯线性贝塞尔橡皮筋的感觉
	        			 */
	        			path.moveTo(rectFLeft + PADDING_RECT, rectFTop + PADDING_RECT);//path.moveTo:设置Path的起点 
	        			path.lineTo(rectFRight - PADDING_RECT, rectFBottom - PADDING_RECT);//线性贝塞尔曲线
	        			mPaint.setColor(Color.BLACK);
	        			canvas.drawPath(path, mPaint);//画出贝塞尔曲线
	        			path.reset();
	        			path.moveTo(rectFLeft + PADDING_RECT, rectFTop + PADDING_RECT);//path.moveTo:设置Path的起点 
	        			path.quadTo(rectFRight - PADDING_RECT, rectFTop + PADDING_RECT, 
	        					rectFRight - PADDING_RECT, rectFBottom - PADDING_RECT); //二次方贝塞尔曲线
	        			mPaint.setColor(Color.BLUE);
	        	        canvas.drawPath(path, mPaint);//画出贝塞尔曲线  
	        	        path.reset();
	        			path.moveTo(rectFLeft + PADDING_RECT, rectFTop + PADDING_RECT);//path.moveTo:设置Path的起点 
	        			path.cubicTo(rectFLeft + PADDING_RECT, (rectFBottom - rectFTop)/2 + rectFTop,
	        									rectFRight - PADDING_RECT, (rectFBottom - rectFTop)/2 + rectFTop,
	        									rectFRight - PADDING_RECT, rectFBottom - PADDING_RECT); //三次方贝塞尔曲线
	        			mPaint.setColor(Color.RED);
	        	        canvas.drawPath(path, mPaint);//画出贝塞尔曲线 
	        			break;
	        		case "drawable":
	        	        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);  
	        	        canvas.drawBitmap(bitmap, rectFLeft + PADDING_RECT, rectFTop + PADDING_RECT, mPaint);
	        			break;
        		}
        		drawRectFrame(rectFLeft, rectFTop, rectFRight, rectFBottom);
        	}
        } 
    }
}
