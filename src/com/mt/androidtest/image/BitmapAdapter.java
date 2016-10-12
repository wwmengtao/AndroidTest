package com.mt.androidtest.image;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;
import com.mt.androidtest.tool.ExecutorHelper;

/**
 * רΪ���ش�ͼƬ��Ƶĸ�����ͼƬ������
 * @author Mengtao1
 *
 */
public class BitmapAdapter extends BaseAdapter{
	private Context mContext = null;
	private ViewGroup mViewGroup; 
	private ArrayList<String>largeNumPicsAL = null;
	private PicConstants mPicConstants = null;
	private int picNum = 1000;
	private int maxMemory = 0;
    private LayoutInflater mLayoutInflater= null;
	private AssetManager mAssetManager=null;    
    //
    private int widthOfIV = 0;
    private int heightOfIV = 0;
    //
    private Executor mExecutor=null;
    private ExecutorHelper mExecutorHelper =null;
    private ExecutorService mExecutorService = null;
    
    /** 
     * ͼƬ���漼���ĺ����࣬���ڻ����������غõ�ͼƬ���ڳ����ڴ�ﵽ�趨ֵʱ�Ὣ�������ʹ�õ�ͼƬ�Ƴ����� 
     */
    private LruCache<String, Bitmap> mLruCache;  
    //
	public BitmapAdapter(Context context){
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mAssetManager = mContext.getResources().getAssets();
		mPicConstants = new PicConstants();
		largeNumPicsAL=mPicConstants.createLargeNumHDPics(picNum);
		maxMemory = (int) (Runtime.getRuntime().maxMemory());//����ڴ棬��λbytes 
		int cacheSize = maxMemory / 8; 
		mLruCache = new LruCache<String, Bitmap>(cacheSize){
	        @Override  
	        protected int sizeOf(String key, Bitmap mBitmap) {  
	            return (null==mBitmap)?0:mBitmap.getByteCount();  
	        }  
		};
		//1��AsyncTask�Դ������̳߳�
		mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;//�����̳߳أ������Ϊ1000�ȴ����ݣ���GridView��������ʱ������RejectedExecutionException
		//2���Զ����̳߳�
		mExecutorHelper = new ExecutorHelper();
		//mExecutorService = mExecutorHelper.getExecutorService(3, -1);//���ʹ��newCachedThreadPool���ܿ�����OOM����Ϊ�����߳����������������
		mExecutorService = mExecutorHelper.getExecutorService(2, 10);	//ʹ��newFixedThreadPool������10���̹߳���������ȴ������Ա���OOM���������coreThreads��������Ļ�����Ӱ�����ܣ���Ϊ���ڴ�Ҫ����ߡ�
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return largeNumPicsAL.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return largeNumPicsAL.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
        if (mViewGroup == null) {    
        	mViewGroup = (ViewGroup) parent;    
        }
    	ViewHolder mViewHolder=null;
		if(null==convertView){
			convertView = mLayoutInflater.inflate(R.layout.item_getview_bitmap, parent,false);
			mViewHolder=new ViewHolder();
			mViewHolder.mImageView = (ImageView)convertView.findViewById(R.id.myimageview);
			mViewHolder.mImageView.setScaleType(ScaleType.FIT_XY);//�ǵȱ������ţ���������ImageView
			if(0==widthOfIV && 0==heightOfIV){//��ȡmImageView�Ĳ������
				convertView.measure(0, 0);
				widthOfIV = mViewHolder.mImageView.getMeasuredWidth();
				heightOfIV = mViewHolder.mImageView.getMeasuredHeight();
			}
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder = (ViewHolder)convertView.getTag();
		}
		String bitmapUrl = largeNumPicsAL.get(position);
		if(null==bitmapUrl)return convertView;
		ImageView mImageView = mViewHolder.mImageView;
		mImageView.setTag(bitmapUrl); 
		Bitmap mBitmap = getBitmapFromMemoryCache(bitmapUrl);
        if (mBitmap != null) {  
        	mImageView.setImageBitmap(mBitmap);  
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask(); 
            //task.execute(bitmapUrl); //����Ĭ�ϵĴ��д���ʽ
            //task.executeOnExecutor(mExecutor, bitmapUrl);//���ò��д���ʽ�����������ᵼ��RejectedExecutionException����Ϊ�ȴ����г���Ϊ128
            task.executeOnExecutor(mExecutorService, bitmapUrl);//�Զ����̳߳�
        }  
		return convertView;
	}
	
	private class ViewHolder{
		ImageView mImageView = null;
	}
	
	/** 
     * addBitmapToMemoryCache����һ��ͼƬ�洢��LruCache�С� 
     */  
	public void addBitmapToMemoryCache(String key, Bitmap mBitmap) {  
        if (getBitmapFromMemoryCache(key) == null) {  
        	mLruCache.put(key, mBitmap);  
            ALog.Log("addBitmapToMemoryCache:"+key);
        }  
    }  
  
    /** 
     * getBitmapFromMemoryCache����LruCache�л�ȡһ��ͼƬ����������ھͷ���null��  
     */  
	public Bitmap getBitmapFromMemoryCache(String key) {  
	    return mLruCache.get(key);  
	}  
    
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {  
		String imageUrl;   
  
        @Override  
        protected Bitmap doInBackground(String... params) {  
            imageUrl = params[0];
            ALog.Log("doInBackground--1");
            Bitmap bitmap = getBitmap(imageUrl);  
            ALog.Log("doInBackground--2");
            if(null!=bitmap)addBitmapToMemoryCache(imageUrl, bitmap);
            ALog.Log("doInBackground--3");
            return bitmap;  
        }  
  
        @Override  
        protected void onPostExecute(Bitmap mBitmap) {  
            ImageView mImageView = (ImageView) mViewGroup.findViewWithTag(imageUrl);    
            if (mImageView != null && mBitmap != null) {    
        		mImageView.setImageBitmap(mBitmap);
        		ALog.Log("imageUrl:"+imageUrl+" mImageView:"+mImageView);
            }   
        }   
	}
	
	/**
	 * getBitmap����ȡBitmap
	 * @param imageUrl
	 * @return
	 */
	private Bitmap getBitmap(String imageUrl) {
        ALog.Log("getBitmap--1");
		String imageUrlNew=mPicConstants.parsePicUrl(imageUrl);
		if(null==imageUrlNew)return null;
		InputStream mInputStream=null;
		try {
			ALog.Log("getBitmap--2");
			mInputStream = mAssetManager.open(imageUrlNew);//��Asset�ļ����ж�ȡͼƬ
			ALog.Log("getBitmap--3");
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return BitmapProcess.decodeSampledBitmap(mInputStream, widthOfIV, heightOfIV,true);
	}
}
