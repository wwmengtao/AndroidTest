package com.mt.androidtest;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * ACListView：让ListView全显示而不是仅仅显示一行
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

    //通过复写其onMeasure方法、达到对ScrollView适配的效果
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
