package com.mt.androidtest.image;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import libcore.io.DiskLruCache;
import libcore.io.DiskLruCache.Snapshot;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;
import com.mt.androidtest.image.ImageProcess.ViewSize;
import com.mt.androidtest.image.ImageProcess.StreamType;
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

    private Executor mTaskLoadImg = null;
    private Executor mTaskDistributor=null;
    private volatile static ImageLoader mInstance;
    private LruCache<String, Bitmap> mLruCache;  //线程安全类，put、get等方法都有 synchronized (this) 限制
	/**
	 * 图片硬盘缓存核心类。
	 */
	private DiskLruCache mDiskLruCache;    //线程安全类，put，Editor.commit有关方法前有synchronized限制
    private Type mType = Type.FIFO;
    /**
     * ImageView和URL的对应关系依赖ImageView的hashCode()和URL，在不自定义而直接使用java实现的hashCode()可以保证ImageView
     * 的hashCode和对应的URL一一对应。
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
		//以下创建硬盘缓存目录及初始化mDiskLruCache
		try {
			// 获取图片缓存路径
			File cacheDir = getDiskCacheDir(mContext, "PicsCacheOfImageLoader");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			// 创建DiskLruCache实例，初始化缓存数据
			mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(mContext), 1, 10 * 1024 * 1024);//10 * 1024 * 1024：10M
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 获取当前应用程序的版本号。
	 */
	public int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
					0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	/**
	 * 根据传入的uniqueName获取硬盘缓存的路径地址。
	 */
	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		//如果外置SD卡装载并且不可移除
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
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
        if (mBitmap != null && !mBitmap.isRecycled()) {  
        	mImageView.setImageBitmap(mBitmap);  ALog.Log("imageUrl:"+imageUrl);
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
			ViewSize mImageViewSize = ImageProcess.getInstance(mContext).getViewSize(mImageViewParas.mImageView); 
			int widthOfIV = mImageViewSize.getWidth();
			int heightOfIV = mImageViewSize.getHeight();
			String imageUrl = mImageViewParas.url;
			ReentrantLock mUrlLock = getLockForUrl(imageUrl);
			mUrlLock.lock();//对于多个ImageView加载同一个Url资源的情况进行限制
			try{
				checkTaskInterrupted();
				checkImageViewReused(mImageViewParas);
				Bitmap bitmap = loadImage(imageUrl, widthOfIV, heightOfIV, mImageViewParas);
	            if(null!=bitmap){
	            	checkTaskInterrupted();
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
		if(null!=mTaskLoadImg)((ExecutorService) mTaskLoadImg).shutdownNow();
		if(null!=mTaskDistributor)((ExecutorService) mTaskDistributor).shutdownNow();
		if(null!=mDisplayHandler)mDisplayHandler.removeCallbacksAndMessages(null);
		if(null!=urlKeysForImageViews)urlKeysForImageViews.clear();
		uriLocks.clear();
		fluchCache();
		ALog.Log("mDiskLruCache.size:"+mDiskLruCache.size()/1024+"KB");//用于统计应用的缓存大小
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
	
	private void checkTaskInterrupted() throws TaskCancelException {
		if (isTaskInterrupted()) {
			throw new TaskCancelException();
		}
	}

	private boolean isTaskInterrupted() {
		if (Thread.interrupted()) {
			return true;
		}
		return false;
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap mBitmap) {  
        if (getBitmapFromMemoryCache(key) == null) {  
        	mLruCache.put(key, mBitmap);  
        }  
    }  

	public Bitmap getBitmapFromMemoryCache(String key) {  
	    return mLruCache.get(key);  
	}  
	
	public Bitmap loadImage(String imageUrl,int widthOfImageView, int heightOfImageView, ImageViewParas mImageViewParas)
		throws TaskCancelException{
		if(null==imageUrl)return null;
		if(imageUrl.startsWith("http")){//表示需要从网络下载图片
			return tryToDownloadBitmap(imageUrl, mImageViewParas);
		}
		String imageUrlNew = ImageProcess.getInstance(mContext).parsePicUrl(imageUrl);
		return ImageProcess.getInstance(mContext).decodeSampledBitmap(imageUrlNew, StreamType.Asset, widthOfImageView, heightOfImageView,true);
	}

	/**
	 * 从硬盘缓存中读取数据，如果不存在，从网络下载
	 * @param imageUrl
	 * @return
	 */
	protected Bitmap tryToDownloadBitmap(String imageUrl, ImageViewParas mImageViewParas) throws TaskCancelException {
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		Snapshot snapShot = null;
		try {
			// 生成图片URL对应的key
			final String key = hashKeyForDisk(imageUrl);
			// 查找key对应的缓存
			snapShot = mDiskLruCache.get(key);
			if (snapShot == null) {
				checkTaskInterrupted();
				checkImageViewReused(mImageViewParas);
				// 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
				DiskLruCache.Editor editor = mDiskLruCache.edit(key);
				if (editor != null) {
					checkTaskInterrupted();
					checkImageViewReused(mImageViewParas);
					OutputStream outputStream = editor.newOutputStream(0);
					if (downloadUrlToStream(imageUrl, outputStream, mImageViewParas)) {
						checkTaskInterrupted();
						checkImageViewReused(mImageViewParas);
						editor.commit();
					} else {
						editor.abort();
					}
				}
				// 缓存被写入后，再次查找key对应的缓存
				snapShot = mDiskLruCache.get(key);
			}
			if (snapShot != null) {
				fileInputStream = (FileInputStream) snapShot.getInputStream(0);
				fileDescriptor = fileInputStream.getFD();
			}
			// 将缓存数据解析成Bitmap对象
			Bitmap bitmap = null;
			if (fileDescriptor != null) {
				bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
			}
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileDescriptor == null && fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	
	/**
	 * 使用MD5算法对传入的key进行加密并返回。
	 */
	public String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			//首先进行实例化和初始化
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			//得到一个操作系统默认的字节编码格式的字节数组
			final byte[] btInput = key.getBytes();
			//对得到的字节数组进行处理
			mDigest.update(btInput);
			//进行哈希计算并返回结果
			byte[] btOutput = mDigest.digest();
			cacheKey = bytesToHexString(btOutput);
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	
	/**
	 * 建立HTTP请求，并获取Bitmap对象。
	 * 
	 * @param imageUrl
	 *            图片的URL地址
	 * @return 解析后的Bitmap对象
	 */
	private boolean downloadUrlToStream(String urlString, OutputStream outputStream, ImageViewParas mImageViewParas) {
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
			out = new BufferedOutputStream(outputStream, 8 * 1024);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
		}finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 将缓存记录同步到journal文件中，一般在Activity即将退出时候执行
	 */
	public void fluchCache() {
		if (mDiskLruCache != null) {
			try {
				mDiskLruCache.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
