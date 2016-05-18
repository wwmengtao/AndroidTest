package com.mt.androidtest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResourceActivity extends Activity  implements View.OnClickListener{
	private LinearLayout mLayout_linear_switchbar=null;
    private RelativeLayout mRelativeLayout=null;
	String mText=null;
	private TextView mTextView=null;
    private EditText mEditText=null;
	private TextView mTextView_Switchbar=null;	
    private ImageView mImageView=null;	
    private int mDensityDpi = 0;
    private DisplayMetrics metric=null;
    private Resources mResource=null;
	int [] buttonID = {R.id.btn_showresource};    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resource);
		mResource=this.getResources();
		mTextView_Switchbar=(TextView) findViewById(R.id.txStatus);
		mLayout_linear_switchbar=(LinearLayout) findViewById(R.id.switch_bar);	
		mLayout_linear_switchbar.setOnClickListener(this);
		mRelativeLayout=(RelativeLayout) findViewById(R.id.relativelayout_resource);  
        metric = getResources().getDisplayMetrics();
        mDensityDpi = metric.densityDpi;
		for(int i=0;i<buttonID.length;i++){
			((Button)findViewById(buttonID[i])).setOnClickListener(this);
		}
	}

	@Override
	protected void onResume(){	
		super.onResume();
		ALog.Log("====onResume");
		testFunctions();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case	R.id.btn_showresource:
			showResourceView();
			break;
		case R.id.switch_bar:
			updateSwitchBarStatus();
			break;
		}
	}
	
	public void testFunctions(){
		setSwitchBarBackground();
		setEditText();
	}
	
	boolean isSwitchBarClicked=false;
	public void updateSwitchBarStatus(){
		//直接构建颜色：mTextView_Switchbar.setTextColor(Color.argb(255,255,255,255))，对应的颜色值为#ffffffff
		if(!isSwitchBarClicked){
			mTextView_Switchbar.setTextColor(mResource.getColor(R.color.k5m_textcolor));
			isSwitchBarClicked=true;
		}else{
			mTextView_Switchbar.setTextColor(mResource.getColor(R.color.sisley2m_color));
			isSwitchBarClicked=false;
		}
	}
	
	public void setEditText(){
		mTextView = (TextView) findViewById(R.id.textview);  
		mText = mTextView.getText().toString();
		mEditText = (EditText) findViewById(R.id.editText);  
		mEditText.setText(mText);  
		mEditText.setSelection(mText.length()); //光标一直位于内容后面，方便输入
	}
	
    public void setLayoutParams(View mView){
    	ViewGroup.LayoutParams lp = mView.getLayoutParams();
    	lp.width= (int)(mDensityDpi*0.3);//144;
    	lp.height = (int)(mDensityDpi*0.3);//144;
    	mView.setLayoutParams(lp);
    }
	
    /**
     * 从系统设置中获取参数设置SwitchBar的背景参数
     */
	public void setSwitchBarBackground(){
		//以下为系统设置WiFi的SwitchBar的背景设置
    	//K5M：android:background="@color/switchbar_background_color"
    	//Sisley2M：android:background="@drawable/bg_switchbar"
		String packageName="com.android.settings";
		String [][]type_name = {	{"color","switchbar_background_color"},
												{"drawable","bg_switchbar"}};
		Object obj=null;
		for(int i=0;i<type_name.length;i++){
			if(null!=(obj=getResourceType0(this,type_name[i][1],type_name[i][0],packageName))){
				switch(type_name[i][0]){
				case "color":
					mLayout_linear_switchbar.setBackgroundColor((Integer)obj);
					break;
				case "drawable":
					mLayout_linear_switchbar.setBackground((Drawable)obj);
					break;
				}
				break;
			}
		}
	}	
	
	   
    /**
     * 获取指定packageName中的资源
     * @param context
     * @param name
     * @param type
     * @param packageName
     * @return
     */
	private Object getResourceType0(Context context,String name,String type,String packageName) {
		Object obj=null;
		int resID = 0;
		Context mContext = null;
		try {
			mContext = context.createPackageContext(packageName,
					Context.CONTEXT_IGNORE_SECURITY);
			if (mContext != null) {
				resID = getResourceID1(mContext.getResources(), name, type ,packageName);
			}
		} catch (NameNotFoundException e) {
			ALog.Log(packageName + " not found！-->" + e.getMessage());
			return null;
		}
		if(0==resID)return null;
    	switch(type){
	    	case "drawable":
	    		obj = mContext.getResources().getDrawable(resID);
	    	break;
	    	case "string":
	    		obj = mContext.getResources().getString(resID);
	    	break;
	    	case "color":
	    		obj = mContext.getResources().getColor(resID);	    		
	    	break;    		
    	}
		return obj;
    	
	}
    
	
	
    public Object getResourceType1(Context context,String name,String type,String packageName){
		Object obj=null;
    	int resID = 0;
        Resources mResources=null;
        PackageManager pm=context.getPackageManager();
        try {
        	mResources=pm.getResourcesForApplication(packageName);
        	if (mResources != null) {
        		resID = getResourceID0(mResources, name, type ,packageName);
        	}
        } catch(NameNotFoundException e) {
        	 e.printStackTrace();
        	 return null;
         }
		if(0==resID)return null;
    	switch(type){
	    	case "drawable":
	    		obj = mResources.getDrawable(resID);
	    	break;
	    	case "string":
	    		obj = mResources.getString(resID);
    	break;    		
    	}
		return obj;
	}
    
    public int getResourceID0(Resources mResources, String name, String type, String packageName){
    	return mResources.getIdentifier(name, type ,packageName);
    }
    
    public int getResourceID1(Resources mResources, String name, String type, String packageName){
    	return mResources.getIdentifier(packageName+":"+type+"/"+name,null,null);

    }
    
    public void getSystemResource(){
    	//无需任何权限可以直接获取
        boolean isCellBroadcastAppLinkEnabled = false;
        int resId = getResources().getIdentifier("config_cellBroadcastAppLinks", "bool", "android");
        if (resId > 0) {
            isCellBroadcastAppLinkEnabled = this.getResources().getBoolean(resId);
        }
        ALog.Log("android_isCellBroadcastAppLinkEnabled:"+isCellBroadcastAppLinkEnabled);
    }	
    
    
	public void showResourceView(){
		if(!mRelativeLayout.isShown()){
			mRelativeLayout.setVisibility(View.VISIBLE);
			mTextView = (TextView) findViewById(R.id.tv_relative);
			mImageView = (ImageView) findViewById(R.id.img_relative);
    		setLayoutParams(mImageView);
    		setTextView();
    		setImageView();
    		getViewColors();
		}else{
			mRelativeLayout.setVisibility(View.GONE);
		}
	}
	
	
	/**
	 * 获取指定控件字体颜色、背景。只适用于本应用中已经绘制完成的控件，无法跨应用
	 */
	public void getViewColors(){
		TextView mTvTemp =(TextView)this.findViewById(R.id.tv_relative_color);
		String packageName=null;
		packageName=this.getPackageName();
		int textViewId =0;
		textViewId=this.getResources().getIdentifier("tv_relative", "id", packageName);
		ALog.Log("textViewId:"+ALog.toHexString(textViewId));		
		mTextView= (TextView)findViewById(textViewId);
		mTvTemp.setTextColor(mTextView.getCurrentTextColor());
		mTvTemp.setBackground(mTextView.getBackground());
		//获取其他package中控件，但是只能获取ID，无法加载得到控件，因为其他应用还没有show Activity
		packageName="com.android.settings";
		Context mContext;
		try {
			mContext = this.createPackageContext(packageName,Context.CONTEXT_IGNORE_SECURITY);
			if (mContext != null) {
				textViewId = mContext.getResources().getIdentifier("switch_text", "id", packageName);
				ALog.Log("textViewId:"+ALog.toHexString(textViewId));		
				//mTvTemp =(TextView)mContext.findViewById(textViewId);//语法错误，findViewById只能是Activity使用，而不是Context
				//mTvTemp= (TextView) LayoutInflater.from(mContext).inflate(textViewId, null);//错误，LayoutInflater只能解析布局文件
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//ALog.Log("mTextView.getText:"+mTextView.getText());
	}
	
	
	public void setTextView(){
		String packageName = "com.lenovo.powersetting";//其他应用的包名
    	String name="app_name";
    	String type = "string";
    	Object obj1 = getResourceType0(this, name, type ,packageName);
    	if(null!=obj1){
    		mTextView.setText((String)obj1);
    	}
	}
	
	public void setImageView(){
		Drawable mDrawable=null;
		//
		String packageName = "com.lenovo.powersetting";//其他应用的包名
    	//packageName = getPackageName();//本应用的包名
    	String name = "ic_launcher";
    	String type = "drawable";
    	Object obj0 = getResourceType1(this, name, type ,packageName);
    	if(null!=obj0){
    		mDrawable = (Drawable)obj0;//getIdentifier获取图片资源
    	}
		//mDrawable = getDrawbleFromSrc();//从src中获取图片资源
		//mDrawable = getDrawbleFromAsset();//从assets中获取图片资源
		//mDrawable = getDrawableFromResourcesXml();//从系统xml资源获取图片
    	//mDrawable = getDrawbleFromAssetXml();//方法暂时FC！！！！！
		mImageView.setBackground(mDrawable);
	}    
    
    /**
     * getDrawbleFromSrc：从src文件夹中获取图片
     * @return
     */
    public Drawable getDrawbleFromSrc(){
    	String path = "com/drawable/resource/test.png"; 
    	InputStream is = getClassLoader().getResourceAsStream(path); 
    	return Drawable.createFromStream(is, "src"); 
    }
    /**
     * getDrawbleFromAsset：从assets目录中获取png类型图片
     * @return
     */
    public Drawable getDrawbleFromAsset(){
    	InputStream is=null;
    	AssetManager asm=this.getAssets();
    	Drawable mDrawable=null;
    	try {
			is=asm.open("pic_switcher/ic_notfound.png");
			mDrawable=Drawable.createFromStream(is, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return mDrawable;
    }
    
    public Drawable getDrawbleFromAssetXml(){
    	Drawable mDrawable = null;
    	XmlPullParser mXmlPullParser = null;
        InputStream mInputStream = null;
        Resources mResources = getResources();
        AssetManager mAM=null;
        try {
        	mAM = mResources.getAssets();
        	mInputStream = mAM.open("pic_switcher/vpn.xml"); 
        	mXmlPullParser = Xml.newPullParser();
        	mXmlPullParser.setInput(mInputStream, StandardCharsets.UTF_8.name());
            mDrawable = Drawable.createFromXml(mResources,  mXmlPullParser);
        } catch (XmlPullParserException e) {
        	ALog.fillInStackTrace("getDrawbleFromAssetXml.XmlPullParserException");
        } catch (IOException e) {
        	ALog.fillInStackTrace("getDrawbleFromAssetXml.Exception");
        }finally {
            try {
            	if(null!=mInputStream)mInputStream.close();
            } catch (IOException e) {
            	ALog.fillInStackTrace("getDrawbleFromAssetXml.IOException");
            }
        }
        return mDrawable;
    }
    
    public Drawable getDrawableFromResourcesXml(){
    	Resources mResources = this.getResources();
    	XmlPullParser parser = mResources.getXml(R.drawable.ic_qs_bluetooth_on);
        Drawable mDrawable = null;
		try {
			mDrawable = Drawable.createFromXml(mResources, parser);
		} catch (XmlPullParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return mDrawable;
    }
    	
}
