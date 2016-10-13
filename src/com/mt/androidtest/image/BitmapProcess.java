package com.mt.androidtest.image;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static com.mt.androidtest.image.PicConstants.strangeSTR;
import com.mt.androidtest.ALog;
/**
 * Bitmap����һ�������ʻ�ȡͼƬ
 * @author Mengtao1
 *
 */
public class BitmapProcess {
	private AssetManager mAssetManager=null;  
    private String regPrefix = "[0-9]+"+strangeSTR;
    private Pattern mPattern = null;
	
	public BitmapProcess(Context mContext){
		mAssetManager = mContext.getResources().getAssets();
    	mPattern = Pattern.compile(regPrefix);
	}
	/**
	 * ��ȡ�ض������ʺ����ͼƬ
	 * @param obj
	 * @param reqWidth
	 * @param reqHeight
	 * @param increaseSample ����ͼƬ���в���ѹ��
	 * @return
	 */
    public static Bitmap decodeSampledBitmap(Object obj, int reqWidth, int reqHeight, boolean increaseSample) {
        // ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��Ϣ  
        final BitmapFactory.Options options = new BitmapFactory.Options();  
        if(!increaseSample){//�����в���ѹ��ͼƬ
        	options.inSampleSize = 1;
        	return BitmapFactory.decodeStream((InputStream)obj, null, options); 
        }
		//inJustDecodeBounds��������Ϊtrue�Ϳ����ý���������ֹΪbitmap�����ڴ棬����ֵҲ������һ��Bitmap����
		//����null����ȻBitmap��null�ˣ�����BitmapFactory.Options��outWidth��outHeight��outMimeType���Զ��ᱻ��ֵ��        
        options.inJustDecodeBounds = true;  
        BitmapFactory.decodeStream((InputStream)obj, null, options);  //decodeStream�᳢��Ϊ�Ѿ�������bitmap�����ڴ棬��ʱ�ͻ�����׵���OOM����
        ALog.Log("imageHeight:"+options.outHeight+" imageWidth:"+options.outWidth+" imageType:"+options.outMimeType);
        //����sampleSizeֵ  
        options.inSampleSize = calculatesampleSize(options, reqWidth, reqHeight);//options�п�ߵ�λ������  
        //�ڶ��ν�����inJustDecodeBounds����Ϊfalse����ϻ�ȡ����sampleSizeֵ�ٴν���ͼƬ  
        options.inJustDecodeBounds = false;  
        return BitmapFactory.decodeStream((InputStream)obj, null, options);  
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
//        while (outHeight / inSampleSize > reqHeight || outWidth / inSampleSize > reqWidth) {  
//            //�����ߵ�����һ�������ű���û�дﵽҪ�󣬶������������ű���  
//            //sampleSizeӦ��Ϊ2��n���ݣ������sampleSize���õ����ֲ���2��n���ݣ���ôϵͳ��ͽ�ȡֵ  
//        	inSampleSize *= 2;  
//        }  
        return inSampleSize;  
    }  
    
	/**
	 * ������������Ҫ��ͼƬURL��ַ
	 * @param picUrl
	 * @return
	 */
	public String parsePicUrl(String picUrl){
		if(null==picUrl)return null;
		String picUrlNew=null;
		Matcher mMatcher = mPattern.matcher(picUrl);
        if(null!=mMatcher && mMatcher.find()){
        	picUrlNew=picUrl.replace(mMatcher.group(),"");
        }
		return picUrlNew;
	}
    
	/**
	 * getBitmap����ȡBitmap
	 * @param imageUrl
	 * @return
	 */
	public Bitmap getBitmap(String imageUrl,int widthOfImageView, int heightOfImageView) {
		String imageUrlNew=parsePicUrl(imageUrl);
		if(null==imageUrlNew)return null;
		InputStream mInputStream=null;
		try {
			mInputStream = mAssetManager.open(imageUrlNew);//��Asset�ļ����ж�ȡͼƬ
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return decodeSampledBitmap(mInputStream, widthOfImageView, heightOfImageView,true);
	}
}
