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
 * MyDrawViewSolarSystem：模拟太阳系运行的自定义View
 * @author mengtao1
 *
 */
public class MyDrawViewSolarSystem extends MyBaseDrawView{
	private List<Planet> mPlanets = null;
    private int numOfPlanet = 9;//九大行星
	private Random mRandom;
    public static final int FLUSH_RATE = 40;
    float centerX, centerY, innerCircleRadius;//当前View的中心点坐标以及内切圆半径
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
	        setSubRectRowAndColumn(1, 1);//设置内部矩形框行列数
	        setSubRectLTRB(0, 0);//设置内部矩形框边界数值
        }
        drawRectFrame(rectFLeft, rectFTop, rectFRight, rectFBottom);
        if(!isDrawn){
	        float rectWidth = rectFRight - rectFLeft;
	        float rectHeight = rectFBottom - rectFTop;
	        //RadiusOfView：当前视图的内切圆半径
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
     * 定义行星类Planet
     * @author mengtao1
     *
     */
    private class Planet{
    	private float trackX, trackY, trackRadius;//行星公转轨道中心点坐标以及半径
    	private float cx, cy;//行星自身中心点坐标
    	private double initialAngle;//行星运行的初始角度
    	private int SerialNumber;//行星自身序号
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
    		//水星运转速度最快，以水星为基准计算其他行星运转速度，为了显示效果引入乘数Multiple
    		double angle = initialAngle + Multiple * drawCount++ * Math.PI/180 *
    				(PlanetsInfo.RevolutionDays[0]/PlanetsInfo.RevolutionDays[SerialNumber]);
    		cx = (float) Math.cos(angle)*trackRadius + trackX;
    		cy = (float) Math.sin(angle)*trackRadius + trackY;
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mContext.getResources().getColor(R.color.black));
            mCanvas.drawCircle(trackX, trackY, trackRadius, mPaint);//绘制行星运行轨道
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mContext.getResources().getColor(PlanetsInfo.PlanetsColor[SerialNumber]));
    		mCanvas.drawCircle(cx, cy, PlanetsInfo.PlanetRadius[SerialNumber], mPaint);//绘制行星
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
