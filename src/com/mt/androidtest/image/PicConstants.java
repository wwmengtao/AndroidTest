package com.mt.androidtest.image;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PicConstants {
	private String picPrefix=null;//图片地址前缀
    private String regPrefix = "[0-9]+@#@#@#";
    private Pattern mPattern = null;
    private Matcher mMatcher = null;
    
    public PicConstants(){
    	mPattern = Pattern.compile(regPrefix);
    }
    
	/**
	 * assetPicNames:assets文件中预存的高清图片
	 */
	public static String[] assetHDPicNames={
		"largepics/cat.jpg",
		"largepics/lake.jpg",		
		"largepics/grassland.jpg",
		"largepics/mountain.jpg",
		"largepics/starSky.jpg",	
		"largepics/sea.jpg"
	};
	
	public static String[] assetSwitcherPicNames={
		"SwitcherDemo/lenovo_widget_btn_bluetooth_on.png",
		"SwitcherDemo/lenovo_widget_btn_flashlight_on.png",
	};
	
	public static String[] assetPicPicNames={
		"pic_switcher/ic_notfound.png",
		"pic_switcher/ic_qs_flashlight_on.xml",
		"pic_switcher/vpn.xml",
	};
	
	/**
	 * createLargeNumPics：根据assetPicNames模拟大量图片数据
	 * @param picNum 期望生成的图片数量
	 * @return
	 */
	public ArrayList<String> createLargeNumHDPics(int picNum){
		if(picNum<=0)return null;
		ArrayList<String>largeNumPicsAL = new ArrayList<String>();
		int index=0;
		picPrefix=null;
		for(int i=0;i<picNum;i++){
			picPrefix = i+"@#@#@#";
			index = i%assetHDPicNames.length;
			largeNumPicsAL.add(picPrefix+assetHDPicNames[index]);
		}
		return largeNumPicsAL;
	}
	
	/**
	 * 解析出最终想要的图片URL地址
	 * @param picUrl
	 * @return
	 */
	public String parsePicUrl(String picUrl){
		if(null==picUrl)return null;
		String picUrlNew=null;
		mMatcher = mPattern.matcher(picUrl);
        if(mMatcher.find()){
        	picUrlNew=picUrl.replace(mMatcher.group(),"");
        }
		return picUrlNew;
	}
}
