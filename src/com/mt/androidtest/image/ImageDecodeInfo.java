package com.mt.androidtest.image;

import java.io.IOException;
import java.io.InputStream;

import com.mt.androidtest.ALog;
import com.mt.androidtest.image.ImageProcess.StreamType;

import android.content.res.AssetManager;

/**
 * ͼƬ������Ϣ��
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
				case Asset://���ͼƬ��Դ����assets�ļ���
					mInputStream = mAssetManager.open(url);//��Asset�ļ����ж�ȡ����ͼƬ
					break;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mInputStream;
	}
}
