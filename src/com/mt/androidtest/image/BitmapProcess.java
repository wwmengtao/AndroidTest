package com.mt.androidtest.image;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mt.androidtest.ALog;
/**
 * Bitmap按照一定采用率获取图片
 * @author Mengtao1
 *
 */
public class BitmapProcess {
	
	/**
	 * 获取特定采样率后解析图片
	 * @param obj
	 * @param reqWidth
	 * @param reqHeight
	 * @param increaseSample 不对图片进行采样压缩
	 * @return
	 */
    public static Bitmap decodeSampledBitmap(Object obj, int reqWidth, int reqHeight, boolean increaseSample) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片信息  
        final BitmapFactory.Options options = new BitmapFactory.Options();  
        if(!increaseSample){//不进行采样压缩图片
        	options.inSampleSize = 1;
        	return BitmapFactory.decodeStream((InputStream)obj, null, options); 
        }
		//inJustDecodeBounds属性设置为true就可以让解析方法禁止为bitmap分配内存，返回值也不再是一个Bitmap对象，
		//而是null。虽然Bitmap是null了，但是BitmapFactory.Options的outWidth、outHeight和outMimeType属性都会被赋值。        
        options.inJustDecodeBounds = true;  
        BitmapFactory.decodeStream((InputStream)obj, null, options);  //decodeStream会尝试为已经构建的bitmap分配内存，这时就会很容易导致OOM出现
        ALog.Log("imageHeight:"+options.outHeight+" imageWidth:"+options.outWidth+" imageType:"+options.outMimeType);
        //计算sampleSize值  
        options.inSampleSize = calculatesampleSize(options, reqWidth, reqHeight);//options中宽高单位是像素  
        //第二次解析将inJustDecodeBounds设置为false，结合获取到的sampleSize值再次解析图片  
        options.inJustDecodeBounds = false;  
        return BitmapFactory.decodeStream((InputStream)obj, null, options);  
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
//        while (outHeight / inSampleSize > reqHeight || outWidth / inSampleSize > reqWidth) {  
//            //如果宽高的任意一方的缩放比例没有达到要求，都继续增大缩放比例  
//            //sampleSize应该为2的n次幂，如果给sampleSize设置的数字不是2的n次幂，那么系统会就近取值  
//        	inSampleSize *= 2;  
//        }  
        return inSampleSize;  
    }  
}
