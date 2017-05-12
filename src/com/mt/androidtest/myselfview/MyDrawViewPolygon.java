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
 * ���ƶ����ԭ��˵��ͼ��ʹ�÷�ʽΪ�ڲ����ļ���ʹ���������·�ʽ��
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
        setSubRectRowAndColumn(3,2);//�����ڲ����ο�������
        String[][] shapes = {{"oval","triangle",},{"polygon","filletRect"},{"bessel","drawable"}};
        Path path = new Path();
        for(int i=0; i<rowNumRect; i++){
        	for(int j=0; j<columnNumRect; j++){
        		setSubRectLTRB(i,j);
        		mPaint.setStyle(Paint.Style.STROKE);//���û�������ΪSTROKE
        		switch(shapes[i][j]){
	        		case "oval"://������Բ
	        			mRectF.set(rectFLeft, rectFTop, rectFRight, rectFBottom);
	        			mCanvas.drawOval(mRectF, mPaint);  
	        			break;
	        		case "triangle"://����������
	        	        float pathX, pathY;
	        			rectFLeft = PADDING_RECT+(float)(WidthOfView*0.5); 
	        			rectFRight = WidthOfView-PADDING_RECT;
	        	        pathX = rectFLeft+(rectFRight-rectFLeft)/2;
	        	        pathY = rectFTop+PADDING_RECT;
	        	        path.moveTo(pathX, pathY);//path.moveTo:����Path�����  
	        	        pathY = rectFBottom - PADDING_RECT;
	        	        path.lineTo(pathX, pathY);  
	        	        pathX = rectFRight - PADDING_RECT;
	        	        path.lineTo(pathX, pathY);  
	        	        path.close(); // ʹ��Щ�㹹�ɷ�յĶ����  
	        	        mCanvas.drawPath(path, mPaint); 
	        			break;
	        		case "polygon"://���ƶ����
	        	        path.moveTo(rectFLeft + PADDING_RECT, rectFTop + (rectFBottom - rectFTop)/2);  //path.moveTo:����Path�����  
	        	        path.lineTo(rectFLeft + (rectFRight-rectFLeft)/3, rectFTop + PADDING_RECT);  
	        	        path.lineTo(rectFLeft + 2*(rectFRight-rectFLeft)/3, rectFTop + PADDING_RECT);  
	        	        path.lineTo(rectFRight-PADDING_RECT, rectFTop + (rectFBottom - rectFTop)/2);  
	        	        path.lineTo(rectFLeft + 2*(rectFRight-rectFLeft)/3, rectFBottom - PADDING_RECT);  
	        	        path.lineTo(rectFLeft + (rectFRight-rectFLeft)/3, rectFBottom - PADDING_RECT);  
	        	        path.close();//���  
	        	        mCanvas.drawPath(path, mPaint);  
	        			break;
	        		case "filletRect"://����Բ�Ǿ���  
	        	        mRectF.set(rectFLeft + PADDING_RECT, rectFTop+ PADDING_RECT, rectFRight - PADDING_RECT, rectFBottom - PADDING_RECT);
	        	        mCanvas.drawRoundRect(mRectF, 4*PADDING_RECT, 8*PADDING_RECT, mPaint);//�ڶ���������x�뾶��������������y�뾶 
	        			break;
	        		case "bessel":
	        	        //������������  
	        			/**
	        			 * ����������ԭ��ͼ����Ϊ֪�ʼǣ�Android ��ͼ���������߼�ʹ��
	        			 * Path.moveTo(float x, float y) // Path�ĳ�ʼ��
	        			 * Path.lineTo(float x, float y) // ���Թ�ʽ�ı���������, ��ʵ����ֱ��
	        			 * Path.quadTo(float x1, float y1, float x2, float y2) // ���η���ʽ�ı��������ߣ�������������Ա�������Ƥ��ĸо�
	        			 * Path.cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) // ���η���ʽ�ı��������ߣ�������������Ա�������Ƥ��ĸо�
	        			 */
	        			path.moveTo(rectFLeft + PADDING_RECT, rectFTop + PADDING_RECT);//path.moveTo:����Path����� 
	        			path.lineTo(rectFRight - PADDING_RECT, rectFBottom - PADDING_RECT);//���Ա���������
	        			mPaint.setColor(Color.BLACK);
	        			canvas.drawPath(path, mPaint);//��������������
	        			path.reset();
	        			path.moveTo(rectFLeft + PADDING_RECT, rectFTop + PADDING_RECT);//path.moveTo:����Path����� 
	        			path.quadTo(rectFRight - PADDING_RECT, rectFTop + PADDING_RECT, 
	        					rectFRight - PADDING_RECT, rectFBottom - PADDING_RECT); //���η�����������
	        			mPaint.setColor(Color.BLUE);
	        	        canvas.drawPath(path, mPaint);//��������������  
	        	        path.reset();
	        			path.moveTo(rectFLeft + PADDING_RECT, rectFTop + PADDING_RECT);//path.moveTo:����Path����� 
	        			path.cubicTo(rectFLeft + PADDING_RECT, (rectFBottom - rectFTop)/2 + rectFTop,
	        									rectFRight - PADDING_RECT, (rectFBottom - rectFTop)/2 + rectFTop,
	        									rectFRight - PADDING_RECT, rectFBottom - PADDING_RECT); //���η�����������
	        			mPaint.setColor(Color.RED);
	        	        canvas.drawPath(path, mPaint);//�������������� 
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
