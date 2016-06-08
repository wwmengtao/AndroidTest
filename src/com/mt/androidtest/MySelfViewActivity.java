package com.mt.androidtest;

import android.app.Activity;
import android.os.Bundle;
/**
 * 自定义控件分为三类：自己绘制、组合、继承，即activity_customed_controller中的SelfDrawnView、CombinedView、InheritedView。
 * @author Mengtao1
 *
 */
public class MySelfViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customed_controller);
	}
}
