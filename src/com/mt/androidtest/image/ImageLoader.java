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
 * Bitmap����һ�������ʻ�ȡͼƬ
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
    //ThreadPoolHandler��ʼ���ź�������֤ThreadPoolHandler��ʵ����ʼ�����֮�����ʹ��
	private volatile Semaphore mSemaphoreThreadPoolHandlerInit = new Semaphore(0);
	//ThreadPool�̳߳�����ִ���ź�������֤�������mTasksΪLIFOʱ�̳߳�����ִ�к���ӵ���������������������
	private volatile Semaphore mSemaphoreThreadPoolExecute = null;
    
	/**
	 * �̳߳ص���Hander
	 */
	private Handler mThreadPoolHandler = null;
	
	/**
	 * �̳߳��¼������߳�
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
		mExecutorService = mExecutorHelper.getExecutorService(2, CPU_COUNT);	//ʹ��newFixedThreadPool�������̹߳������������Ա���OOM���������coreThreads��������Ļ�����Ӱ�����ܣ���Ϊ���ڴ�Ҫ����ߡ�
		mSemaphoreThreadPoolExecute = new Semaphore(2*CPU_COUNT+1);
		mTasks = new LinkedList<Runnable>();//��ʼ���������
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
	
	//��ȡͼƬ��ȡ�߳�
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
			mInputStream = mAssetManager.open(imageUrlNew);//��Asset�ļ����ж�ȡͼƬ
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
