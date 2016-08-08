package com.mt.androidtest.myselfview;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.mt.androidtest.ALog;

public class ScrollerLayout extends ViewGroup {

    /**
     * ������ɹ���������ʵ��
     */
    private Scroller mScroller;

    /**
     * �ж�Ϊ�϶�����С�ƶ�������
     */
    private int mTouchSlop;

    /**
     * �ֻ�����ʱ����Ļ����
     */
    private float mXDown;

    /**
     * �ֻ���ʱ��������Ļ����
     */
    private float mXMove;

    /**
     * �ϴδ���ACTION_MOVE�¼�ʱ����Ļ����
     */
    private float mXLastMove;

    /**
     * ����ɹ�������߽�
     */
    private int leftBorder;

    /**
     * ����ɹ������ұ߽�
     */
    private int rightBorder;

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // ��һ��������Scroller��ʵ��
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // ��ȡTouchSlopֵ
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        ALog.Log("mTouchSlop:"+mTouchSlop);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // ΪScrollerLayout�е�ÿһ���ӿؼ�������С
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // ΪScrollerLayout�е�ÿһ���ӿؼ���ˮƽ�����Ͻ��в���
                childView.layout(i * childView.getMeasuredWidth(), 0, (i + 1) * childView.getMeasuredWidth(), childView.getMeasuredHeight());
            }
            // ��ʼ�����ұ߽�ֵ
            leftBorder = getChildAt(0).getLeft();
            rightBorder = getChildAt(getChildCount() - 1).getRight();
            //ALog.Log("leftBorder:"+leftBorder+" rightBorder:"+rightBorder);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:
            	//ALog.Log("onInterceptTouchEvent.ACTION_MOVE");
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                // ����ָ�϶�ֵ����TouchSlopֵʱ����ΪӦ�ý��й����������ӿؼ����¼�
                if (diff > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
               // ALog.Log("onTouchEvent_ACTION_MOVE:"+mXMove);
                int scrolledXMove = (int) (mXLastMove - mXMove);
                if (getScrollX() + scrolledXMove < leftBorder) {
                    scrollTo(leftBorder, 0);
                    //ALog.Log("#-#-#-#-1getScrollX():"+getScrollX()+" scrolledXMove:"+scrolledXMove+" getWidth():"+getWidth());
                    return true;
                } else if (getScrollX() + getWidth() + scrolledXMove > rightBorder) {
                    scrollTo(rightBorder - getWidth(), 0);
                    //ALog.Log("#-#-#-#-2getScrollX():"+getScrollX()+" scrolledXMove:"+scrolledXMove+" getWidth():"+getWidth());
                    return true;
                }
                scrollBy(scrolledXMove, 0);//scrollBy����scrollTo�ᵼ��invalidate�ĵ���
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                // ����ָ̧��ʱ�����ݵ�ǰ�Ĺ���ֵ���ж�Ӧ�ù������ĸ��ӿؼ��Ľ���
                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
                // �ڶ���������startScroll()��������ʼ���������ݲ�ˢ�½���
                //ALog.Log("dx:"+dx+" getScrollX:"+getScrollX());
                mScroller.startScroll(getScrollX(), 0, dx, 0, Math.abs(dx) * 2);
                postInvalidate();//�����draw����
                break;
        }
        return super.onTouchEvent(event);
    }
    /**invalidate�����draw����
     * View.draw����������Ƭ�Σ�
     * if (!drawingWithRenderNode) {
            computeScroll();
            sx = mScrollX;
            sy = mScrollY;
        }
     */
    @Override
    public void computeScroll() {
        // ����������дcomputeScroll()�������������ڲ����ƽ���������߼�
        if (mScroller.computeScrollOffset()) {//computeScrollOffset������Scroller�е����ݱ仯
        	scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        	ALog.fillInStackTrace("====mScroller.getCurrX():"+mScroller.getCurrX()+" mScroller.getCurrY():"+mScroller.getCurrY());
        	postInvalidate();
        }
    }
 }