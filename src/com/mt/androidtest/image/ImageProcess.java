package com.mt.androidtest.image;

import static com.mt.androidtest.image.PicConstants.strangeSTR;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mt.androidtest.ALog;

import static com.mt.androidtest.image.ImageLoader.IsLogRun;

public class ImageProcess {
    private static final String regPrefix = "[0-9]+"+strangeSTR;
    private static Pattern mPattern = Pattern.compile(regPrefix);
    
    public static enum StreamType{
    	Asset
    }
	/**
	 * 解析出最终想要的图片URL地址
	 * @param picUrl
	 * @return
	 */
	public static String parsePicUrl(String picUrl){
		if(null==picUrl)return null;
		String picUrlNew=null;
		Matcher mMatcher = mPattern.matcher(picUrl);
        if(null!=mMatcher && mMatcher.find()){
        	picUrlNew=picUrl.replace(mMatcher.group(),"");
        }
		return picUrlNew;
	}
	
	/**
	 * 获取特定采样率后解析图片
	 * @param Url 图片地址描述字符串
	 * @param mType 图片资源种类
	 * @param reqWidth 显示图片的ImageView宽度
	 * @param reqHeight 显示图片的ImageView高度
	 * @param isSample 是否对图片进行采样
	 * @return 采样(不采样)后的Bitmap
	 */
	public static Bitmap decodeSampledBitmap(String Url, StreamType mType, int reqWidth, int reqHeight, boolean isSample){
	   	if(null == Url)return null;
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片信息  
        if(!isSample){//不进行采样压缩图片
        	return BitmapFactory.decodeStream(ImageDecodeInfo.getInputStream(Url, mType)); 
        }
        InputStream mInputStream = null;
        Bitmap mBitmap = null;
        try{
	        mInputStream = ImageDecodeInfo.getInputStream(Url, mType);
	        final BitmapFactory.Options options = new BitmapFactory.Options();  
			//inJustDecodeBounds属性设置为true就可以让解析方法禁止为bitmap分配内存，返回值也不再是一个Bitmap对象，
			//而是null。虽然Bitmap是null了，但是BitmapFactory.Options的outWidth、outHeight和outMimeType属性都会被赋值。        
	        options.inJustDecodeBounds = true;  
	        BitmapFactory.decodeStream(mInputStream, null, options);  
	        if(IsLogRun)ALog.Log("imageHeight:"+options.outHeight+" imageWidth:"+options.outWidth+" imageType:"+options.outMimeType);
	        //计算sampleSize值  
	        options.inSampleSize = calculatesampleSize(options, reqWidth, reqHeight);//options中宽高单位是像素  
	        //第二次解析将inJustDecodeBounds设置为false，结合获取到的sampleSize值再次解析图片  
	        options.inJustDecodeBounds = false;  
	        mInputStream = resetStream(mInputStream,Url, mType);
	        mBitmap = BitmapFactory.decodeStream(mInputStream, null, options);  //decodeStream会尝试为已经构建的bitmap分配内存，这时就会很容易导致OOM出现
        }catch (OutOfMemoryError e) {
        	e.printStackTrace();
        	ALog.Log("OutOfMemoryError");
		} finally{
			closeSilently(mInputStream);
        }
        return mBitmap;
	}
	
	protected static InputStream resetStream(InputStream imageStream, String Url, StreamType mType){
		if (imageStream.markSupported()) {
			try {
				imageStream.reset();
				return imageStream;
			} catch (IOException e) {
			}
		}
		closeSilently(imageStream);
		return  ImageDecodeInfo.getInputStream(Url, mType);
	}
    
	public static void closeSilently(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
			}
		}
	}
	
    //根据期望控件的大小确定图片采样率，让宽高等比例压缩
    public static int calculatesampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {  
        // 源图片的高度和宽度  
        final int outHeight = options.outHeight;  
        final int outWidth = options.outWidth;  
        int inSampleSize = 1;  
        if (outHeight > reqHeight || outWidth > reqWidth) {  
            // 计算出实际宽高和目标宽高的比率  
            final int heightRatio = Math.round((float) outHeight / (float) reqHeight);  
            final int widthRatio = Math.round((float) outWidth / (float) reqWidth);  
            // 选择宽和高中最小的比率作为sampleSize的值，这样可以保证最终图片的宽和高  
            // 一定都会大于等于目标的宽和高。  
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  
        }
        if(IsLogRun)ALog.Log("inSampleSize:"+inSampleSize);
        return inSampleSize;  
    }  
}
