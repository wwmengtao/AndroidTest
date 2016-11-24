package com.mt.androidtest.image;

import java.io.InputStream;
import java.lang.reflect.Field;
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
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.mt.androidtest.ALog;
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
	public static final boolean IsLogRun = true;
	private final AtomicBoolean paused = new AtomicBoolean(false);
	private final Object pauseLock = new Object();
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
	private Context mContext = null;
	private AssetManager mAssetManager=null;  
	private DisplayMetrics displayMetrics = null;
	private static int displayMetricsWidth = 0;
	private static int displayMetricsHeight = 0;
    private Executor mTaskLoadImg = null;
    private Executor mTaskDistributor=null;
    private volatile static ImageLoader mInstance;
    private LruCache<String, Bitmap> mLruCache;  
    private Type mType = Type.FIFO;
    /**
     * ImageView和URL的对应关系依赖ImageView的hashCode()和URL，当然，由于不同对象可能有相同的hashCode()，稳妥的做法
     * 是直接ImageView和URL对应。
     */
	private final  Map<Integer, String> urlKeysForImageViews = Collections.synchronizedMap(new HashMap<Integer, String>());
	/**
	 * WeakHashMap：不是线程安全的，需要同步，如果不同步的话，会出现WeakHashMap内部的链表死循环。此时以三个工作线程发生
	 * 的一次死循环事故为例说明，事故发生的原因参照印象笔记的“疫苗：Java HashMap的死循环”。之所以能够形成这样的条件是因为这个
	 * 图片加载器的图片URL都是不同的，会随着线程增多而增多，WeakHashMap数组下标处<String, ReentrantLock>冲突概率大增，从而造成
	 * WeakHashMap内部的链表死循环。
	 * 从而导致产生链表死循环概率大增。
	 * 一个线程死在了 rehash()的下列循环处：
     * while (entry != null) {
            int index = entry.isNull ? 0 : (entry.hash & 0x7FFFFFFF)
                    % length;
            Entry<K, V> next = entry.next;
            entry.next = newData[index];
            newData[index] = entry;
            entry = next;
        }
        剩下的两个线程死在了get()函数的下列for循环处(因为访问的是一个无限循环链表)：
     *  while (entry != null) {
            if (key.equals(entry.get())) {
                return entry.value;
            }
            entry = entry.next;
        }
	 */
	private final Map<String, ReentrantLock> uriLocks = Collections.synchronizedMap(new WeakHashMap<String, ReentrantLock>());
	
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
		int cacheSize = maxMemory / 8;
		if(IsLogRun)ALog.Log1("ImageLoader_cacheSize:"+cacheSize);
		mLruCache = new LruCache<String, Bitmap>(cacheSize){
	        @Override  
	        protected int sizeOf(String key, Bitmap mBitmap) {  
	            return (null==mBitmap)?0:mBitmap.getByteCount();  
	        }  
		};
		//以下获取屏幕的宽高
		displayMetrics = mContext.getResources().getDisplayMetrics();
		displayMetricsWidth = displayMetrics.widthPixels;
		displayMetricsHeight = displayMetrics.heightPixels;
	}
	
	private void mesureExecutorExist(){
		if(null==mTaskLoadImg||((ExecutorService) mTaskLoadImg).isShutdown()){
			mTaskLoadImg = ExecutorHelper.createExecutor(3, Thread.NORM_PRIORITY, mType);//核心线程数并非越大越好
			if(IsLogRun)ALog.Log1("mesureExecutorExist 1");
		}
		if(null==mTaskDistributor||((ExecutorService) mTaskDistributor).isShutdown()){
			mTaskDistributor = ExecutorHelper.createTaskDistributor();
			if(IsLogRun)ALog.Log1("mesureExecutorExist 2");
		}
	}
	
	public void loadImage(ImageViewParas mImageViewParas){
		mesureExecutorExist();
		ImageView mImageView = mImageViewParas.mImageView;
		String imageUrl = mImageViewParas.url;
		if(null==imageUrl)return;
		putUrlForImageView(mImageView.hashCode(),imageUrl);//强引用记录ImageView和URL关联关系
		Bitmap mBitmap = getBitmapFromMemoryCache(imageUrl);
        if (mBitmap != null) {  
        	mImageView.setImageBitmap(mBitmap);  
        } else {
        	mImageView.setImageResource(R.drawable.loading);
    		mTaskLoadImg.execute(new loadAndDisplayTask(mImageViewParas));
        }  
	}
	
	//加载、显示任务
	private class loadAndDisplayTask implements Runnable{
		private ImageViewParas mImageViewParas;
		public loadAndDisplayTask(final ImageViewParas mImageViewParas){
			this.mImageViewParas = mImageViewParas;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(waitIfPaused())return;
			ImageViewSize mImageViewSize = getImageViewSize(mImageViewParas.mImageView); 
			int widthOfIV = mImageViewSize.getWidth();
			int heightOfIV = mImageViewSize.getHeight();
			String imageUrl = mImageViewParas.url;
			ReentrantLock mUrlLock = getLockForUrl(imageUrl);
			mUrlLock.lock();//对于多个ImageView加载同一个Url资源的情况进行限制
			try{
				checkImageViewReused(mImageViewParas);
				Bitmap bitmap = loadImage(imageUrl, widthOfIV, heightOfIV);
	            if(null!=bitmap){
	            	checkImageViewReused(mImageViewParas);
	            	addBitmapToMemoryCache(imageUrl, bitmap);
	            	mImageViewParas.mBitmap=bitmap;
	            }
			}catch(TaskCancelException e){
				if(IsLogRun)ALog.Log1("loadAndDisplayTask cancelled!");
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
            //让mImageView之前承接的所有显示任务统统取消，提升性能。比如用户没有设置pauseOnScroll属性时
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
						if(IsLogRun)ALog.Log1("waitIfPaused InterruptedException");
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
	
	//以下为ImageView和URL建立强引用关系，用于维护ImageView最新的URL
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
	
	//停止一切图片加载活动
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
	
	private class ImageViewSize{
		private int width;
		private int height;
		
		public ImageViewSize(int width, int height){
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
	
	/**
	 * getImageViewSize：获取imageView的宽高，由于GridView获取单元格宽高需要多次测量最终才能确定，因此在最终获取真实宽高
	 * 之前必须给定默认值，此时的默认值是imageView.getxxx()后的各个if判断中的数值。
	 * 注意：采用convertView.measure(0,0)后imageView.getMeasuredXXX的方法获取的尺寸是不准确的
	 * @param imageView
	 * @return
	 */
	public ImageViewSize getImageViewSize(ImageView imageView){
		if(null==imageView)return null;
		LayoutParams lp = imageView.getLayoutParams();
		int width = imageView.getWidth();// 获取imageview的实际宽度
		if (width <= 0){
			width = lp.width;// 获取imageview在layout中声明的宽度
		}
		if (width <= 0){
			width = ImageViewParas.defaultWidth;
		}
		if (width <= 0){
			width = getImageViewFieldValue(imageView, "mMaxWidth");
		}
		if (width <= 0){
			width = displayMetricsWidth;
		}

		int height = imageView.getHeight();// 获取imageview的实际高度
		if (height <= 0){
			height = lp.height;// 获取imageview在layout中声明的宽度
		}
		if (height <= 0){
			height = ImageViewParas.defaultHeight;
		}
		if (height <= 0){
			height = getImageViewFieldValue(imageView, "mMaxHeight");// 检查最大值
		}
		if (height <= 0){
			height = displayMetricsHeight;
		}
		if(IsLogRun)ALog.Log("width:"+width+" height:"+height);
		return new ImageViewSize(width, height);
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


}
