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
		//ֱ�ӹ�����ɫ��mTextView_Switchbar.setTextColor(Color.argb(255,255,255,255))����Ӧ����ɫֵΪ#ffffffff
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
		mEditText.setSelection(mText.length()); //���һֱλ�����ݺ��棬��������
	}
	
    public void setLayoutParams(View mView){
    	ViewGroup.LayoutParams lp = mView.getLayoutParams();
    	lp.width= (int)(mDensityDpi*0.3);//144;
    	lp.height = (int)(mDensityDpi*0.3);//144;
    	mView.setLayoutParams(lp);
    }
	
    /**
     * ��ϵͳ�����л�ȡ��������SwitchBar�ı�������
     */
	public void setSwitchBarBackground(){
		//����Ϊϵͳ����WiFi��SwitchBar�ı�������
    	//K5M��android:background="@color/switchbar_background_color"
    	//Sisley2M��android:background="@drawable/bg_switchbar"
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
     * ��ȡָ��packageName�е���Դ
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
			ALog.Log(packageName + " not found��-->" + e.getMessage());
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
    	//�����κ�Ȩ�޿���ֱ�ӻ�ȡ
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
	 * ��ȡָ���ؼ�������ɫ��������ֻ�����ڱ�Ӧ�����Ѿ�������ɵĿؼ����޷���Ӧ��
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
		//��ȡ����package�пؼ�������ֻ�ܻ�ȡID���޷����صõ��ؼ�����Ϊ����Ӧ�û�û��show Activity
		packageName="com.android.settings";
		Context mContext;
		try {
			mContext = this.createPackageContext(packageName,Context.CONTEXT_IGNORE_SECURITY);
			if (mContext != null) {
				textViewId = mContext.getResources().getIdentifier("switch_text", "id", packageName);
				ALog.Log("textViewId:"+ALog.toHexString(textViewId));		
				//mTvTemp =(TextView)mContext.findViewById(textViewId);//�﷨����findViewByIdֻ����Activityʹ�ã�������Context
				//mTvTemp= (TextView) LayoutInflater.from(mContext).inflate(textViewId, null);//����LayoutInflaterֻ�ܽ��������ļ�
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//ALog.Log("mTextView.getText:"+mTextView.getText());
	}
	
	
	public void setTextView(){
		String packageName = "com.lenovo.powersetting";//����Ӧ�õİ���
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
		String packageName = "com.lenovo.powersetting";//����Ӧ�õİ���
    	//packageName = getPackageName();//��Ӧ�õİ���
    	String name = "ic_launcher";
    	String type = "drawable";
    	Object obj0 = getResourceType1(this, name, type ,packageName);
    	if(null!=obj0){
    		mDrawable = (Drawable)obj0;//getIdentifier��ȡͼƬ��Դ
    	}
		//mDrawable = getDrawbleFromSrc();//��src�л�ȡͼƬ��Դ
		//mDrawable = getDrawbleFromAsset();//��assets�л�ȡͼƬ��Դ
		//mDrawable = getDrawableFromResourcesXml();//��ϵͳxml��Դ��ȡͼƬ
    	//mDrawable = getDrawbleFromAssetXml();//������ʱFC����������
		mImageView.setBackground(mDrawable);
	}    
    
    /**
     * getDrawbleFromSrc����src�ļ����л�ȡͼƬ
     * @return
     */
    public Drawable getDrawbleFromSrc(){
    	String path = "com/drawable/resource/test.png"; 
    	InputStream is = getClassLoader().getResourceAsStream(path); 
    	return Drawable.createFromStream(is, "src"); 
    }
    /**
     * getDrawbleFromAsset����assetsĿ¼�л�ȡpng����ͼƬ
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
