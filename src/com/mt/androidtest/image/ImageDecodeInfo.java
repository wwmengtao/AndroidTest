package com.mt.androidtest.image;

import java.io.InputStream;

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
		mAssetManager = assetManager;
	}
	
	public static InputStream getInputStream(String url, StreamType type){
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
