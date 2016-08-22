package com.mt.androidtest.myselfview;

import android.app.Activity;
import android.os.Bundle;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;
/**
 * 自定义控件分为三类：自己绘制、组合、继承，即activity_customed_controller中的SelfDrawnView、CombinedView、InheritedView。
 * @author Mengtao1
 *
 */
public class MySelfViewActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myselfview);
	}
}
