package com.mt.androidtest;

import android.app.Activity;
import android.os.Bundle;
/**
 * �Զ���ؼ���Ϊ���ࣺ�Լ����ơ���ϡ��̳У���activity_customed_controller�е�SelfDrawnView��CombinedView��InheritedView��
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
