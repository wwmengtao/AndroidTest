package com.mt.androidtest.myselfview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * MyDrawViewSolarSystem��ģ��̫��ϵ���е��Զ���View
 * @author mengtao1
 *
 */
public class MyDrawViewSolarSystem extends MyBaseDrawView{
	private List<Planet> mPlanets = null;
    private int numOfPlanet = 9;//�Ŵ�����
	private Random mRandom;
    public static final int FLUSH_RATE = 40;
    float centerX, centerY, innerCircleRadius;//��ǰView�����ĵ������Լ�����Բ�뾶
	public MyDrawViewSolarSystem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPlanets = new ArrayList<>();
        mPaint.setStrokeWidth(2);
        mRandom = new Random(System.currentTimeMillis());
        doInitial();
	}
	
	private void doInitial(){
        double initialAngle;
        Planet mPlanet;
        for(int i=0; i<numOfPlanet;i++){
        	mPlanet = new Planet();
        	mPlanet.setSerialNumber(i);
        	initialAngle = mRandom.nextInt(180) * Math.PI / 180;
        	mPlanet.setInitialAngle(initialAngle);
        	mPlanets.add(mPlanet);
        }
	}
	
	private boolean isDrawn = false;
	
    @Override  
    protected void onDraw(Canvas canvas) {  
    	if(0 == HeightOfView)return;
        super.onDraw(canvas);  
        if(!isDrawn){
	        setSubRectRowAndColumn(1, 1);//�����ڲ����ο�������
	        setSubRectLTRB(0, 0);//�����ڲ����ο�߽���ֵ
        }
        drawRectFrame(rectFLeft, rectFTop, rectFRight, rectFBottom);
        if(!isDrawn){
	        float rectWidth = rectFRight - rectFLeft;
	        float rectHeight = rectFBottom - rectFTop;
	        //RadiusOfView����ǰ��ͼ������Բ�뾶
	        int RadiusOfView = (int)(rectWidth < rectHeight ? rectWidth/2 : rectHeight/2);
	        centerX = rectFLeft + (rectFRight - rectFLeft) / 2;
	        centerY = rectFTop + (rectFBottom - rectFTop) / 2;
	        for(int i=0; i<numOfPlanet;i++){
	        	innerCircleRadius = RadiusOfView/(numOfPlanet + 1)*(i + 1);
	        	mPlanets.get(i).setPlanetParas(centerX, centerY, innerCircleRadius);
	        }
	        isDrawn = true;
        }
        updataPlanetStatus();
        getHandler().postDelayed(PlanetStatusRunnable, FLUSH_RATE);
    }
	
    @Override    
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if(!hasFocus){
    		getHandler().removeCallbacksAndMessages(null);
    	}else{
    		invalidate();
    	}
    }
    
    @Override  
    protected void onDetachedFromWindow() {
    	super.onDetachedFromWindow();
    	ALog.Log("onDetachedFromWindow");
    	getHandler().removeCallbacksAndMessages(null);
		mPlanets.clear();
		mPlanets = null;
    }
    
    
    /**
     * ����������Planet
     * @author mengtao1
     *
     */
    private class Planet{
    	private float trackX, trackY, trackRadius;//���ǹ�ת������ĵ������Լ��뾶
    	private float cx, cy;//�����������ĵ�����
    	private double initialAngle;//�������еĳ�ʼ�Ƕ�
    	private int SerialNumber;//�����������
    	private int drawCount = 0;
    	
    	public void setSerialNumber(int SerialNumber){
    		this.SerialNumber = SerialNumber;
    	}
    	
    	public void setInitialAngle(double initialAngle){
    		this.initialAngle = initialAngle;
    	}
    	
    	public void setPlanetParas(float trackX, float trackY, float trackRadius){
    		this.trackX = trackX;
    		this.trackY = trackY;
    		this.trackRadius = trackRadius;
    	}
    	
    	private void drawSelf(){
    		int Multiple = 30;
    		//ˮ����ת�ٶ���죬��ˮ��Ϊ��׼��������������ת�ٶȣ�Ϊ����ʾЧ���������Multiple
    		double angle = initialAngle + Multiple * drawCount++ * Math.PI/180 *
    				(PlanetsInfo.RevolutionDays[0]/PlanetsInfo.RevolutionDays[SerialNumber]);
    		cx = (float) Math.cos(angle)*trackRadius + trackX;
    		cy = (float) Math.sin(angle)*trackRadius + trackY;
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mContext.getResources().getColor(R.color.black));
            mCanvas.drawCircle(trackX, trackY, trackRadius, mPaint);//�����������й��
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mContext.getResources().getColor(PlanetsInfo.PlanetsColor[SerialNumber]));
    		mCanvas.drawCircle(cx, cy, PlanetsInfo.PlanetRadius[SerialNumber], mPaint);//��������
    	}
    }
    
    private void updataPlanetStatus(){
    	mPaint.setStrokeWidth(1);
    	for(Planet planet : mPlanets){
    		planet.drawSelf();
    	}
    }

    private Runnable PlanetStatusRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			invalidate();
		}
    };
    
}
