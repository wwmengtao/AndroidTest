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
	 * ������������Ҫ��ͼƬURL��ַ
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
	 * ��ȡ�ض������ʺ����ͼƬ
	 * @param Url ͼƬ��ַ�����ַ���
	 * @param mType ͼƬ��Դ����
	 * @param reqWidth ��ʾͼƬ��ImageView���
	 * @param reqHeight ��ʾͼƬ��ImageView�߶�
	 * @param isSample �Ƿ��ͼƬ���в���
	 * @return ����(������)���Bitmap
	 */
	public static Bitmap decodeSampledBitmap(String Url, StreamType mType, int reqWidth, int reqHeight, boolean isSample){
	   	if(null == Url)return null;
        // ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��Ϣ  
        if(!isSample){//�����в���ѹ��ͼƬ
        	return BitmapFactory.decodeStream(ImageDecodeInfo.getInputStream(Url, mType)); 
        }
        InputStream mInputStream = null;
        Bitmap mBitmap = null;
        try{
	        mInputStream = ImageDecodeInfo.getInputStream(Url, mType);
	        final BitmapFactory.Options options = new BitmapFactory.Options();  
			//inJustDecodeBounds��������Ϊtrue�Ϳ����ý���������ֹΪbitmap�����ڴ棬����ֵҲ������һ��Bitmap����
			//����null����ȻBitmap��null�ˣ�����BitmapFactory.Options��outWidth��outHeight��outMimeType���Զ��ᱻ��ֵ��        
	        options.inJustDecodeBounds = true;  
	        BitmapFactory.decodeStream(mInputStream, null, options);  
	        if(IsLogRun)ALog.Log("imageHeight:"+options.outHeight+" imageWidth:"+options.outWidth+" imageType:"+options.outMimeType);
	        //����sampleSizeֵ  
	        options.inSampleSize = calculatesampleSize(options, reqWidth, reqHeight);//options�п�ߵ�λ������  
	        //�ڶ��ν�����inJustDecodeBounds����Ϊfalse����ϻ�ȡ����sampleSizeֵ�ٴν���ͼƬ  
	        options.inJustDecodeBounds = false;  
	        mInputStream = resetStream(mInputStream,Url, mType);
	        mBitmap = BitmapFactory.decodeStream(mInputStream, null, options);  //decodeStream�᳢��Ϊ�Ѿ�������bitmap�����ڴ棬��ʱ�ͻ�����׵���OOM����
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
	
    //���������ؼ��Ĵ�Сȷ��ͼƬ�����ʣ��ÿ�ߵȱ���ѹ��
    public static int calculatesampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {  
        // ԴͼƬ�ĸ߶ȺͿ��  
        final int outHeight = options.outHeight;  
        final int outWidth = options.outWidth;  
        int inSampleSize = 1;  
        if (outHeight > reqHeight || outWidth > reqWidth) {  
            // �����ʵ�ʿ�ߺ�Ŀ���ߵı���  
            final int heightRatio = Math.round((float) outHeight / (float) reqHeight);  
            final int widthRatio = Math.round((float) outWidth / (float) reqWidth);  
            // ѡ���͸�����С�ı�����ΪsampleSize��ֵ���������Ա�֤����ͼƬ�Ŀ�͸�  
            // һ��������ڵ���Ŀ��Ŀ�͸ߡ�  
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  
        }
        if(IsLogRun)ALog.Log("inSampleSize:"+inSampleSize);
        return inSampleSize;  
    }  
}
