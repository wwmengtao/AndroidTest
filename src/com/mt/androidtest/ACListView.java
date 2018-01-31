package com.mt.androidtest;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * ACListView����ListViewȫ��ʾ�����ǽ�����ʾһ��
 * @author mengtao1
 *
 */
public class ACListView extends ListView {
    public ACListView(Context context) {
        super(context);
    }

    public ACListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ACListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //ͨ����д��onMeasure�������ﵽ��ScrollView�����Ч��
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
