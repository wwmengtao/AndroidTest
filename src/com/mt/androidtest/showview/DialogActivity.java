package com.mt.androidtest.showview;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class DialogActivity extends BaseActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setFinishOnTouchOutside(false);
		initDialogWindow();
	}

	private void initDialogWindow(){
		Window window = this.getWindow();
		  //ȥ��dialog��title��Ҫ��setContentView()ǰ
		  window.requestFeature(Window.FEATURE_NO_TITLE);
		  setContentView(R.layout.activity_dialog);
		  //ȥ��dialogĬ�ϵ�padding
		  window.getDecorView().setPadding(0, 0, 0, 0);
		  window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		  WindowManager.LayoutParams lp = window.getAttributes();
		  lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		  lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		  //����dialog��λ���ڵײ�
		  lp.gravity = Gravity.TOP;
		  window.setAttributes(lp);
	}
	
}
