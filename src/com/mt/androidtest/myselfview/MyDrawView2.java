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
        //��ȡ��Ļ���
		DisplayMetrics displayMetrics = null;
		displayMetrics = context.getResources().getDisplayMetrics();
		displayMetricsWidth = displayMetrics.widthPixels;//��Ļ���
		displayMetricsHeight = displayMetrics.heightPixels;//��Ļ�߶�
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  

//        //����  
//        p.setStyle(Paint.Style.FILL);  
//        canvas.drawText("���㣺", 10, 390, p);  
//        canvas.drawPoint(60, 390, p);//��һ����  
//        canvas.drawPoints(new float[]{60,400,65,400,70,400}, p);//�������  
//          
//        //��ͼƬ��������ͼ  
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);  
//        canvas.drawBitmap(bitmap, 250,360, p);

    }  
}  
