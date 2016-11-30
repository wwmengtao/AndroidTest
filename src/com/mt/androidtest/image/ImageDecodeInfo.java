package com.mt.androidtest.image;

import java.io.InputStream;

import com.mt.androidtest.image.ImageProcess.StreamType;

import android.content.res.AssetManager;

/**
 * 图片解析信息类
 * @author Mengtao1
 *
 */
public class ImageDecodeInfo {
	private static AssetManager mAssetManager=null;
	
	public static void setAssetManager(AssetManager assetManager){
		mAssetManager = assetManager;
	}
	
	public static InputStream getInputStream(String url, StreamType type){
		InputStream mInputStream = null;
		try {
			switch(type){
				case Asset://如果图片资源来自assets文件夹
					mInputStream = mAssetManager.open(url);//从Asset文件夹中读取高清图片
					break;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mInputStream;
	}
}
