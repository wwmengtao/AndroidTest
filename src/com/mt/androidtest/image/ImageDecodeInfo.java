package com.mt.androidtest.image;

import java.io.IOException;
import java.io.InputStream;

import com.mt.androidtest.ALog;
import com.mt.androidtest.image.ImageProcess.StreamType;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * ͼƬ������Ϣ��
 * @author Mengtao1
 *
 */
public class ImageDecodeInfo {
	
	private volatile static ImageDecodeInfo mInstance = null;
	private Context mContext = null;
	private AssetManager mAssetManager=null;

	public static ImageDecodeInfo getInstance(Context context)	{
		if (mInstance == null){
			synchronized (ImageDecodeInfo.class){
				if (mInstance == null){
					mInstance = new ImageDecodeInfo(context);
				}
			}
		}
		return mInstance;
	}
	
	public ImageDecodeInfo(Context context){
		mContext = context.getApplicationContext();
		mAssetManager = mContext.getAssets();
	}
	
	
	
	public int getFilesNum(String dir){
        try {
            String []files = mAssetManager.list(dir);
            return files.length;
        } catch (IOException ioe) {
            ALog.Log("Could not list assets"+dir);
            return 0;
        }
	}
	
	public InputStream getInputStream(String url, StreamType type){
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
