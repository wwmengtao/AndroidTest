package com.mt.androidtest.image;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.mt.androidtest.R;
import com.mt.androidtest.image.PicConstants.Type;
import com.mt.androidtest.listview.ViewHolder.ImageViewParas;
import com.mt.androidtest.tool.ExecutorHelper;

/**
 * Bitmap按照一定采用率获取图片
 * @author Mengtao1
 *
 */
public class ImageLoader {
	private final AtomicBoolean paused = new AtomicBoolean(false);
	private final Object pauseLock = new Object();
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
	private Context mContext = null;
	private AssetManager mAssetManager=null;  
	private ExecutorHelper mExecutorHelper =null;
    private ExecutorService mExecutorService = null;
    private LinkedList<Runnable> mTasks;
    private volatile static ImageLoader mInstance;
    private LruCache<String, Bitmap> mLruCache;  
    private Type mType;
    //ThreadPoolHandler初始化信号量，保证ThreadPoolHandler的实例初始化完毕之后才能使用
	private volatile Semaphore mSemaphoreThreadPoolHandlerInit = new Semaphore(0);
	//ThreadPool线程池任务执行信号量，保证任务队列mTasks为LIFO时线程池优先执行后添加的任务，显著提升加载体验
	private volatile Semaphore mSemaphoreThreadPoolExecute = null;
    
	/**
	 * 线程池调度Hander
	 */
	private Handler mThreadPoolHandler = null;
	
	/**
	 * 线程池事件处理线程
	 */
	private Thread mThreadPoolThread = new Thread(){
		@Override
		public void run(){
			Looper.prepare();
			mThreadPoolHandler =new Handler(){
				@Override
				public void handleMessage(Message msg){
					try {
						mSemaphoreThreadPoolExecute.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Runnable mRunnable = fetchTask();
					if(null!=mRunnable)mExecutorService.execute(mRunnable);
				}
			};
			mSemaphoreThreadPoolHandlerInit.release();
			Looper.loop();
		}
	};
	
	private Runnable fetchTask(){
		Runnable mRunnable=null; 
		if(Type.FIFO==mType){
			mRunnable = mTasks.removeFirst();
		}else if(Type.LIFO==mType){
			mRunnable = mTasks.removeLast();
		}
		return mRunnable;
	}
	
	public static ImageLoader getInstance(Context context,int threadCount, Type type)	{
		if (mInstance == null){
			synchronized (ImageLoader.class){
				if (mInstance == null){
					mInstance = new ImageLoader(context, threadCount, type);
				}
			}
		}
		return mInstance;
	}
	
	public ImageLoader(Context context, int threadCount, Type type){
		mContext = context.getApplicationContext();
		mType = type;
		mAssetManager = mContext.getResources().getAssets();
    	init();
	}
	
	public void init(){
		int cacheSize = maxMemory / 8;
		mLruCache = new LruCache<String, Bitmap>(cacheSize){
	        @Override  
	        protected int sizeOf(String key, Bitmap mBitmap) {  
	            return (null==mBitmap)?0:mBitmap.getByteCount();  
	        }  
		};
		mExecutorHelper = new ExecutorHelper();
		mExecutorService = mExecutorHelper.getExecutorService(2, CPU_COUNT);	//使用newFixedThreadPool，限制线程工作个数，可以避免OOM。但是如果coreThreads数量过大的话，会影响性能，因为对内存要求更高。
		mSemaphoreThreadPoolExecute = new Semaphore(2*CPU_COUNT+1);
		mTasks = new LinkedList<Runnable>();//初始化任务队列
		mThreadPoolThread.start();
	}
	
	public void loadImage(ImageViewParas mImageViewParas){
		ImageView mImageView = mImageViewParas.mImageView;
		String imageUrl = mImageViewParas.url;
		if(null==imageUrl)return;
		Bitmap mBitmap = getBitmapFromMemoryCache(imageUrl);
        if (mBitmap != null) {  
        	mImageView.setImageBitmap(mBitmap);  
        } else {
        	mImageView.setImageResource(R.drawable.loading);
    		mImageView.setTag(imageUrl);
        	mTasks.add(fetchGetImageRunnable(mImageViewParas));
    		try{
    			if (mThreadPoolHandler == null){
    				mSemaphoreThreadPoolHandlerInit.acquire();
    			}
    		} catch (InterruptedException e){
    		}
        	mThreadPoolHandler.sendEmptyMessage(0x00);
        }  
	}
	
	//获取图片获取线程
	private Runnable fetchGetImageRunnable(final ImageViewParas mImageViewParas){
		return new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(waitIfPaused())return;
				int widthOfIV = ImageViewParas.width;
				int heightOfIV = ImageViewParas.height;
				String imageUrl = mImageViewParas.url;
				Bitmap bitmap = loadImage(imageUrl, widthOfIV, heightOfIV);  
	            if(null!=bitmap){
	            	addBitmapToMemoryCache(imageUrl, bitmap);
	            	mImageViewParas.mBitmap=bitmap;
	            }
	            Message mMessage=Message.obtain();
	            mMessage.obj=mImageViewParas;
	            mShowImageHandler.sendMessage(mMessage);
			}
		};
	}
	
	private boolean waitIfPaused() {
		if (paused.get()) {
			synchronized (getPauseLock()) {
				if (paused.get()) {
					try {
						getPauseLock().wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private Handler mShowImageHandler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			ImageViewParas mImageViewParas = (ImageViewParas)msg.obj;
            ImageView mImageView = mImageViewParas.mImageView; 
            Bitmap mBitmap = mImageViewParas.mBitmap;
            if (null == mImageView||null == mBitmap)return;
            String url = mImageViewParas.url;
            String imageUrl = (String)mImageView.getTag();
            if(null == url||null == imageUrl)return;
            if (url.equals(imageUrl)) {
        		mImageView.setImageBitmap(mBitmap);
            }
            mSemaphoreThreadPoolExecute.release();
		}
	};
	
	public void addBitmapToMemoryCache(String key, Bitmap mBitmap) {  
        if (getBitmapFromMemoryCache(key) == null) {  
        	mLruCache.put(key, mBitmap);  
        }  
    }  

	public Bitmap getBitmapFromMemoryCache(String key) {  
	    return mLruCache.get(key);  
	}  
	
	public Bitmap loadImage(String imageUrl,int widthOfImageView, int heightOfImageView) {
		String imageUrlNew=new ImageProcess().parsePicUrl(imageUrl);
		if(null==imageUrlNew)return null;
		InputStream mInputStream=null;
		try {
			mInputStream = mAssetManager.open(imageUrlNew);//从Asset文件夹中读取图片
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ImageProcess.decodeSampledBitmap(mInputStream, widthOfImageView, heightOfImageView,true);
	}

	public void resume(){
		paused.set(false);
		synchronized (pauseLock) {
			pauseLock.notifyAll();
		}
	}
	
	public void pause(){
		paused.set(true);
	}
	
	Object getPauseLock() {
		return pauseLock;
	}
}
