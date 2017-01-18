package com.mt.androidtest.image;

import static com.mt.androidtest.image.PicConstants.strangeSTR;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.listview.ViewHolder.ImageViewParas;

import static com.mt.androidtest.image.ImageLoader.IsLogRun;

public class ImageProcess {
	private static int displayMetricsWidth = 0;
	private static int displayMetricsHeight = 0;
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
    
	
	/**
	 * getViewSize����ȡmView�Ŀ�ߣ�����GridView��ȡ��Ԫ������Ҫ��β������ղ���ȷ������������ջ�ȡ��ʵ���
	 * ֮ǰ�������Ĭ��ֵ����ʱ��Ĭ��ֵ��mView.getxxx()��ĸ���if�ж��е���ֵ��
	 * ע�⣺����convertView.measure(0,0)��mView.getMeasuredXXX�ķ�����ȡ�ĳߴ��ǲ�׼ȷ��
	 * @param mView
	 * @return
	 */
	public static ViewSize getViewSize(View mView){
		if(null==mView)return null;
		LayoutParams lp = mView.getLayoutParams();
		int width = mView.getWidth();// ��ȡmView��ʵ�ʿ��
		if (width <= 0){
			width = lp.width;// ��ȡmView��layout�������Ŀ��
		}
		if (width <= 0){
			width = ImageViewParas.defaultWidth;
		}
		if (width <= 0){
			width = getImageViewFieldValue(mView, "mMaxWidth");
		}
		//�������ַ����޷���ȡ�ؼ���ߣ���ôֻ��ʹ�����Ĭ��ֵ������Ļ��ߡ�

		//���»�ȡ��Ļ�Ŀ��
		if(displayMetricsWidth <= 0){
			DisplayMetrics displayMetrics = null;
			displayMetrics = mView.getContext().getResources().getDisplayMetrics();
			displayMetricsWidth = displayMetrics.widthPixels;
			displayMetricsHeight = displayMetrics.heightPixels;
		}
		//
		if (width <= 0){
			width = displayMetricsWidth;
		}

		int height = mView.getHeight();// ��ȡimageview��ʵ�ʸ߶�
		if (height <= 0){
			height = lp.height;// ��ȡimageview��layout�������Ŀ��
		}
		if (height <= 0){
			height = ImageViewParas.defaultHeight;
		}
		if (height <= 0){
			height = getImageViewFieldValue(mView, "mMaxHeight");// ������ֵ
		}
		if (height <= 0){
			height = displayMetricsHeight;
		}
		if(IsLogRun)ALog.Log("width:"+width+" height:"+height);
		return new ViewSize(width, height);
	}
	
	private static int getImageViewFieldValue(Object object, String fieldName){
		int value = 0;
		try{
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE){
				value = fieldValue;
				if(IsLogRun)ALog.Log("value" + value);
			}
		} catch (Exception e){
		}
		return value;
	}
	
	public static class ViewSize{
		private int width;
		private int height;
		
		public ViewSize(int width, int height){
			this.width = width;
			this.height = height;
		}
		
		public int getWidth(){
			return width;
		}
		
		public int getHeight(){
			return height;
		}
	}
}
