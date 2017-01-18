package com.mt.androidtest.image;

import java.io.IOException;
import java.io.InputStream;

import com.mt.androidtest.ALog;
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
		if(null == mAssetManager)mAssetManager = assetManager;
	}
	
	public static int getFilesNum(String dir){
        try {
            String []files = mAssetManager.list(dir);
            return files.length;
        } catch (IOException ioe) {
            ALog.Log("Could not list assets"+dir);
            return 0;
        }
	}
	
	public static InputStream getInputStream(String url, StreamType type){
		if(null == mAssetManager)return null;
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
