package com.mt.androidtest.myselfview;

import android.app.Activity;
import android.os.Bundle;

import com.mt.androidtest.R;
/**
 * �Զ���ؼ���Ϊ���ࣺ�Լ����ơ���ϡ��̳У���activity_customed_controller�е�SelfDrawnView��CombinedView��InheritedView��
 * @author Mengtao1
 *
 */
public class MySelfViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myselfview);
	}
}
