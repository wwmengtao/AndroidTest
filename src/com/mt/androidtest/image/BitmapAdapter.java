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
 * 专为加载大图片设计的高性能图片适配器
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
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。 
     */
    private LruCache<String, Bitmap> mLruCache;  
    //
	public BitmapAdapter(Context context){
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mAssetManager = mContext.getResources().getAssets();
		mPicConstants = new PicConstants();
		largeNumPicsAL=mPicConstants.createLargeNumHDPics(picNum);
		maxMemory = (int) (Runtime.getRuntime().maxMemory());//最大内存，单位bytes 
		int cacheSize = maxMemory / 8; 
		mLruCache = new LruCache<String, Bitmap>(cacheSize){
	        @Override  
	        protected int sizeOf(String key, Bitmap mBitmap) {  
	            return (null==mBitmap)?0:mBitmap.getByteCount();  
	        }  
		};
		//1、AsyncTask自带并行线程池
		mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;//并行线程池，如果改为1000等大数据，将GridView往下拉的时候会出现RejectedExecutionException
		//2、自定义线程池
		mExecutorHelper = new ExecutorHelper();
		//mExecutorService = mExecutorHelper.getExecutorService(3, -1);//如果使用newCachedThreadPool，很快会出现OOM，因为工作线程数量会持续增长。
		mExecutorService = mExecutorHelper.getExecutorService(2, 10);	//使用newFixedThreadPool，限制10个线程工作，其余等待，可以避免OOM。但是如果coreThreads数量过大的话，会影响性能，因为对内存要求更高。
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
			mViewHolder.mImageView.setScaleType(ScaleType.FIT_XY);//非等比例缩放，铺满整个ImageView
			if(0==widthOfIV && 0==heightOfIV){//获取mImageView的测量宽高
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
            //task.execute(bitmapUrl); //采用默认的串行处理方式
            //task.executeOnExecutor(mExecutor, bitmapUrl);//采用并行处理方式，过多的任务会导致RejectedExecutionException，因为等待队列长度为128
            task.executeOnExecutor(mExecutorService, bitmapUrl);//自定义线程池
        }  
		return convertView;
	}
	
	private class ViewHolder{
		ImageView mImageView = null;
	}
	
	/** 
     * addBitmapToMemoryCache：将一张图片存储到LruCache中。 
     */  
	public void addBitmapToMemoryCache(String key, Bitmap mBitmap) {  
        if (getBitmapFromMemoryCache(key) == null) {  
        	mLruCache.put(key, mBitmap);  
            ALog.Log("addBitmapToMemoryCache:"+key);
        }  
    }  
  
    /** 
     * getBitmapFromMemoryCache：从LruCache中获取一张图片，如果不存在就返回null。  
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
	 * getBitmap：获取Bitmap
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
			mInputStream = mAssetManager.open(imageUrlNew);//从Asset文件夹中读取图片
			ALog.Log("getBitmap--3");
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return BitmapProcess.decodeSampledBitmap(mInputStream, widthOfIV, heightOfIV,true);
	}
}
