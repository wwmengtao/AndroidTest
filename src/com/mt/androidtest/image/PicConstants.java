package com.mt.androidtest.image;

import java.util.ArrayList;

public class PicConstants {
	public static final String strangeSTR="abcdcba";
	public static enum Type{FIFO, LIFO};
	private String picPrefix=null;//ͼƬ��ַǰ׺

    
    public PicConstants(){
    }
    
	/**
	 * assetPicNames:assets�ļ���Ԥ��ĸ���ͼƬ
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
	 * createLargeNumPics������assetPicNamesģ�����ͼƬ����
	 * @param picNum �������ɵ�ͼƬ����
	 * @return
	 */
	public ArrayList<String> createLargeNumHDPics(int picNum){
		if(picNum<=0)return null;
		ArrayList<String>largeNumPicsAL = new ArrayList<String>();
		int index=0;
		picPrefix=null;
		for(int i=0;i<picNum;i++){
			picPrefix = i+strangeSTR;
			index = i%assetHDPicNames.length;
			largeNumPicsAL.add(picPrefix+assetHDPicNames[index]);
		}
		return largeNumPicsAL;
	}
}
