package com.mt.androidtest.image;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.mt.androidtest.ALog;
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
    private Executor mTaskLoadImg = null;
    private Executor mTaskDistributor=null;
    private volatile static ImageLoader mInstance;
    private LruCache<String, Bitmap> mLruCache;  
    private Type mType = Type.FIFO;
	private final  Map<Integer, String> urlKeysForImageViews = Collections.synchronizedMap(new HashMap<Integer, String>());
	private final Map<String, ReentrantLock> uriLocks = new WeakHashMap<String, ReentrantLock>();
	
	private static boolean  IsImageLoaderInit = false;
	
	public static ImageLoader getInstance(Context context)	{
		if (mInstance == null){
			synchronized (ImageLoader.class){
				if (mInstance == null){
					mInstance = new ImageLoader(context);
					IsImageLoaderInit = true;
				}
			}
		}
		return mInstance;
	}
	
	public ImageLoader(Context context){
		mContext = context.getApplicationContext();
		mAssetManager = mContext.getResources().getAssets();
    	init();
	}
	
	public void setQueueType(Type type){
		mType = type;
	}
	
	public void init(){
		ALog.Log1("ImageLoader_init");
		int cacheSize = maxMemory / 8;
		mLruCache = new LruCache<String, Bitmap>(cacheSize){
	        @Override  
	        protected int sizeOf(String key, Bitmap mBitmap) {  
	            return (null==mBitmap)?0:mBitmap.getByteCount();  
	        }  
		};
	}
	
	private void mesureExecutorExist(){
		if(null==mTaskLoadImg||((ExecutorService) mTaskLoadImg).isShutdown()){
			mTaskLoadImg = ExecutorHelper.createExecutor(1, Thread.NORM_PRIORITY, mType);//�����߳������ܹ��󣬷���Ӱ�����ܲ���LIFO����Ч������������
		}
		if(null==mTaskDistributor||((ExecutorService) mTaskDistributor).isShutdown()){
			mTaskDistributor = ExecutorHelper.createTaskDistributor();
//			mTaskDistributor = ExecutorHelper.createTaskDistributor2(CPU_COUNT);
		}
	}
	
	public void loadImage(ImageViewParas mImageViewParas){
		mesureExecutorExist();
		ImageView mImageView = mImageViewParas.mImageView;
		String imageUrl = mImageViewParas.url;
		if(null==imageUrl)return;
		putUrlForImageView(mImageView.hashCode(),imageUrl);//ǿ���ü�¼ImageView��URL������ϵ
		Bitmap mBitmap = getBitmapFromMemoryCache(imageUrl);
        if (mBitmap != null) {  
        	mImageView.setImageBitmap(mBitmap);  
        } else {
        	mImageView.setImageResource(R.drawable.loading);
    		mTaskLoadImg.execute(new loadAndDisplayTask(mImageViewParas));
        }  
	}
	
	//���ء���ʾ����
	private class loadAndDisplayTask implements Runnable{
		private ImageViewParas mImageViewParas;
		public loadAndDisplayTask(final ImageViewParas mImageViewParas){
			this.mImageViewParas = mImageViewParas;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(waitIfPaused())return;
			int widthOfIV = ImageViewParas.width;
			int heightOfIV = ImageViewParas.height;
			String imageUrl = mImageViewParas.url;
			ReentrantLock mUrlLock = getLockForUrl(imageUrl);
			mUrlLock.lock();//���ڶ��ImageView����ͬһ��Url��Դ�������������
			try{
				checkImageViewReused(mImageViewParas);
				Bitmap bitmap = loadImage(imageUrl, widthOfIV, heightOfIV);
	            if(null!=bitmap){
	            	checkImageViewReused(mImageViewParas);
	            	addBitmapToMemoryCache(imageUrl, bitmap);
	            	mImageViewParas.mBitmap=bitmap;
	            }
			}catch(TaskCancelException e){
				ALog.Log1("loadAndDisplayTask cancelled!");
				return;
			}finally{
				mUrlLock.unlock();
			}
			mTaskDistributor.execute(new DisplayTask(mImageViewParas));
		}
	}

	public class DisplayTask implements Runnable{
		private ImageViewParas mImageViewParas=null;
		public DisplayTask(ImageViewParas mImageViewParas){
			this.mImageViewParas = mImageViewParas;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(null==mImageViewParas)return;
            ImageView mImageView = mImageViewParas.mImageView; 
            Bitmap mBitmap = mImageViewParas.mBitmap;
            if (null == mImageView||null == mBitmap)return;
            if (isViewReused(mImageViewParas))return;
			Message mMessage = Message.obtain();
			mMessage.obj=mImageViewParas;
			mDisplayHandler.sendMessage(mMessage);
		}
	}
	
	private Handler mDisplayHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			ImageViewParas mImageViewParas = (ImageViewParas)msg.obj;
			ImageView mImageView = mImageViewParas.mImageView;
			Bitmap mBitmap = mImageViewParas.mBitmap;
			if (isViewReused(mImageViewParas))return;
            mImageView.setImageBitmap(mBitmap);
            //��mImageView֮ǰ�нӵ�������ʾ����ͳͳȡ�����������ܡ������û�û������pauseOnScroll����ʱ
            removeDisplayTaskFor(mImageView.hashCode());
		}
	};
	
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
	
	public void resume(){
		paused.set(false);
		synchronized (getPauseLock()) {
			getPauseLock().notifyAll();
		}
	}
	
	public void pause(){
		paused.set(true);
	}
	
	Object getPauseLock() {
		return pauseLock;
	}
	
	//����ΪImageView��URL����ǿ���ù�ϵ������ά��ImageView���µ�URL
	public void putUrlForImageView(Integer mImageView, String url){
		urlKeysForImageViews.put(mImageView, url);
	}
	
	public String getUrlForImageView(Integer mImageView){
		return urlKeysForImageViews.get(mImageView);
	}
	
	public void removeDisplayTaskFor(Integer mImageView){
		urlKeysForImageViews.remove(mImageView);
	}
	
	public ReentrantLock getLockForUrl(String url){
		ReentrantLock lock = uriLocks.get(url);
		if (lock == null) {
			lock = new ReentrantLock();
			uriLocks.put(url, lock);
		}
		return lock;
	}
	
	//ֹͣһ��ͼƬ���ػ
	public void stop() {
		if(!IsImageLoaderInit)return;
		((ExecutorService) mTaskLoadImg).shutdownNow();
		((ExecutorService) mTaskDistributor).shutdownNow();
		mDisplayHandler.removeCallbacksAndMessages(null);
		urlKeysForImageViews.clear();
		uriLocks.clear();
	}
	
	@SuppressWarnings("serial")
	private class TaskCancelException extends Exception{
	}
	
	public void checkImageViewReused(ImageViewParas mImageViewParas) throws TaskCancelException{
		if (isViewReused(mImageViewParas)) {
			throw new TaskCancelException();
		}
	}
	
	public boolean isViewReused(ImageViewParas mImageViewParas){
		String url = mImageViewParas.url;
		ImageView mImageView = mImageViewParas.mImageView;
//		ALog.Log1("hashCode:"+mImageView.hashCode());
		return !url.equals(getUrlForImageView(mImageView.hashCode()));
	}
	
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


}
