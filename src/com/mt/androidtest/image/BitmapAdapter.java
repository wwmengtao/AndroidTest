package com.mt.androidtest.image;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;
import com.mt.androidtest.listview.CommonBaseAdapter;
import com.mt.androidtest.listview.ViewHolder;
import com.mt.androidtest.tool.ExecutorHelper;

/**
 * 专为加载大图片设计的高性能图片适配器
 * @author Mengtao1
 *
 */
public class BitmapAdapter extends CommonBaseAdapter<String>{
	private Context mContext = null;
	private ViewGroup mViewGroup; 
    private BitmapProcess mBitmapProcess=null;
    //
    private int widthOfIV = 0;
    private int heightOfIV = 0;
    //
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory());//最大内存，单位bytes 

    private Executor mExecutor=null;
    private ExecutorHelper mExecutorHelper =null;
    private ExecutorService mExecutorService = null;
    
    /** 
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。 
     */
    private LruCache<String, Bitmap> mLruCache;  
    //
	public BitmapAdapter(Context context, List<String> mDatas){
		super(context, mDatas);
		mContext = context.getApplicationContext();
		mBitmapProcess = new BitmapProcess(mContext);
		int cacheSize = maxMemory / 8;
		mLruCache = new LruCache<String, Bitmap>(cacheSize){
	        @Override  
	        protected int sizeOf(String key, Bitmap mBitmap) {  
	            return (null==mBitmap)?0:mBitmap.getByteCount();  
	        }  
		};
		//1、AsyncTask自带并行线程池
		mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;//并行线程池，等待队列长度为128。如果改为1000等大数据，将GridView往下拉的时候会出现RejectedExecutionException
		//2、自定义线程池
		mExecutorHelper = new ExecutorHelper();
		//mExecutorService = mExecutorHelper.getExecutorService(3, -1);//如果使用newCachedThreadPool，很快会出现OOM，因为工作线程数量会持续增长。
		mExecutorService = mExecutorHelper.getExecutorService(2, 2*CPU_COUNT+1);	//使用newFixedThreadPool，限制线程工作个数，可以避免OOM。但是如果coreThreads数量过大的话，会影响性能，因为对内存要求更高。
	}

	private ViewHolder mViewHolder=null;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
        if (mViewGroup == null) {    
        	mViewGroup = (ViewGroup) parent;    
        }
        boolean needDoAdditionalWork = (null==convertView)?true:false;
        mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_getview_bitmap, position);
        if(needDoAdditionalWork)doAdditionalWork();
		String bitmapUrl = getItem(position);
		if(null==bitmapUrl)return mViewHolder.getConvertView();
		ImageView mImageView = mViewHolder.getView(R.id.myimageview);;
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
		return mViewHolder.getConvertView();
	}
	
	public void doAdditionalWork(){
			View convertView = mViewHolder.getConvertView();
			ImageView mImageView =mViewHolder.getView(R.id.myimageview);
			mImageView.setScaleType(ScaleType.FIT_XY);//非等比例缩放，铺满整个ImageView
			if(0==widthOfIV && 0==heightOfIV){//获取mImageView的测量宽高
				convertView.measure(0, 0);
				widthOfIV = mImageView.getMeasuredWidth();
				heightOfIV = mImageView.getMeasuredHeight();
			}
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
            Bitmap bitmap = mBitmapProcess.getBitmap(imageUrl, widthOfIV, heightOfIV);  
            if(null!=bitmap)addBitmapToMemoryCache(imageUrl, bitmap);
            return bitmap;  
        }  
  
        @Override  
        protected void onPostExecute(Bitmap mBitmap) {  
            ImageView mImageView = (ImageView) mViewGroup.findViewWithTag(imageUrl); //ListView或者GridView需要整体查找，效率低  
            if (mImageView != null && mBitmap != null) {
        		mImageView.setImageBitmap(mBitmap);
        		ALog.Log("imageUrl:"+imageUrl+" mImageView:"+mImageView);
            }
        }
	}
	
}
