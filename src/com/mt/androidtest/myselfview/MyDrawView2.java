package com.mt.androidtest.myselfview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.DisplayMetrics;
import android.view.View;


public class MyDrawView2 extends View {  
	private int displayMetricsWidth, displayMetricsHeight;
    public MyDrawView2(Context context) {  
        super(context);  
        //获取屏幕宽高
		DisplayMetrics displayMetrics = null;
		displayMetrics = context.getResources().getDisplayMetrics();
		displayMetricsWidth = displayMetrics.widthPixels;//屏幕宽度
		displayMetricsHeight = displayMetrics.heightPixels;//屏幕高度
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  

//        //画点  
//        p.setStyle(Paint.Style.FILL);  
//        canvas.drawText("画点：", 10, 390, p);  
//        canvas.drawPoint(60, 390, p);//画一个点  
//        canvas.drawPoints(new float[]{60,400,65,400,70,400}, p);//画多个点  
//          
//        //画图片，就是贴图  
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);  
//        canvas.drawBitmap(bitmap, 250,360, p);

    }  
}  
