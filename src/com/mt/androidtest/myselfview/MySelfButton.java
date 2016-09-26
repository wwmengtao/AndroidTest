package com.mt.androidtest.myselfview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mt.androidtest.ALog;

/**
 * ����ʵ��MySelfButton�ļ��ֻ�������
 * @author Mengtao1
 *
 */
public class MySelfButton extends Button implements View.OnClickListener{
    private int lastX = 0;
    private int lastY = 0;
    //
    private ViewGroup.MarginLayoutParams layoutParams = null;
	public MySelfButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	/**
	 * ����ʵ��Button���϶�
	 */
    public boolean onTouchEvent(MotionEvent event) {
        //��ȡ����ָ���ĺ������������
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                //�����ƶ��ľ���
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                //����layout���������·�������λ��
                layout(getLeft()+offsetX, getTop()+offsetY,
                        getRight()+offsetX , getBottom()+offsetY);
                break;
        }

        return super.onTouchEvent(event);
    }

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		startToMove(0);
	}

	/**
	 * Button�����ļ��ַ�ʽ
	 * @param type
	 */
	public void startToMove(int type){
		switch(type){
		case 0:
			layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
	        layoutParams.leftMargin += 10;
	        layoutParams.topMargin += 10;
	        setLayoutParams(layoutParams);
			break;
		case 1:
			offsetLeftAndRight(10);
			offsetTopAndBottom(10);
			break;
		}
	}
}
