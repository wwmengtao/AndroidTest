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
 * Bitmap����һ�������ʻ�ȡͼƬ
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
    private LruCache<String, Bitmap> mLruCache;  //�̰߳�ȫ�࣬put��get�ȷ������� synchronized (this) ����
	/**
	 * ͼƬӲ�̻�������ࡣ
	 */
	private DiskLruCache mDiskLruCache;    //�̰߳�ȫ�࣬put��Editor.commit�йط���ǰ��synchronized����
    private Type mType = Type.FIFO;
    /**
     * ImageView��URL�Ķ�Ӧ��ϵ����ImageView��hashCode()��URL���ڲ��Զ����ֱ��ʹ��javaʵ�ֵ�hashCode()���Ա�֤ImageView
     * ��hashCode�Ͷ�Ӧ��URLһһ��Ӧ��
     */
	private final  Map<Integer, String> urlKeysForImageViews = Collections.synchronizedMap(new HashMap<Integer, String>());
	/**
	 * WeakHashMap�������̰߳�ȫ�ģ���Ҫͬ���������ͬ���Ļ��������WeakHashMap�ڲ���������ѭ������ʱ�����������̷߳���
	 * ��һ����ѭ���¹�Ϊ��˵�����¹ʷ�����ԭ�����ӡ��ʼǵġ����磺Java HashMap����ѭ������֮�����ܹ��γ���������������Ϊ���
	 * ͼƬ��������ͼƬURL���ǲ�ͬ�ģ��������߳���������࣬WeakHashMap�����±괦<String, ReentrantLock>��ͻ���ʴ������Ӷ����
	 * WeakHashMap�ڲ���������ѭ����
	 * �Ӷ����²���������ѭ�����ʴ�����
	 * һ���߳������� rehash()������ѭ������
     * while (entry != null) {
            int index = entry.isNull ? 0 : (entry.hash & 0x7FFFFFFF)
                    % length;
            Entry<K, V> next = entry.next;
            entry.next = newData[index];
            newData[index] = entry;
            entry = next;
        }
        ʣ�µ������߳�������get()����������forѭ����(��Ϊ���ʵ���һ������ѭ������)��
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
		//���´���Ӳ�̻���Ŀ¼����ʼ��mDiskLruCache
		try {
			// ��ȡͼƬ����·��
			File cacheDir = getDiskCacheDir(mContext, "PicsCacheOfImageLoader");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			// ����DiskLruCacheʵ������ʼ����������
			mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(mContext), 1, 10 * 1024 * 1024);//10 * 1024 * 1024��10M
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * ��ȡ��ǰӦ�ó���İ汾�š�
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
	 * ���ݴ����uniqueName��ȡӲ�̻����·����ַ��
	 */
	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		//�������SD��װ�ز��Ҳ����Ƴ�
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
			mTaskLoadImg = ExecutorHelper.createExecutor(3, Thread.NORM_PRIORITY, mType);//�����߳�������Խ��Խ��
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
		putUrlForImageView(mImageView.hashCode(),imageUrl);//ǿ���ü�¼ImageView��URL������ϵ
		Bitmap mBitmap = getBitmapFromMemoryCache(imageUrl);
        if (mBitmap != null && !mBitmap.isRecycled()) {  
        	mImageView.setImageBitmap(mBitmap);  ALog.Log("imageUrl:"+imageUrl);
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
			ViewSize mImageViewSize = ImageProcess.getInstance(mContext).getViewSize(mImageViewParas.mImageView); 
			int widthOfIV = mImageViewSize.getWidth();
			int heightOfIV = mImageViewSize.getHeight();
			String imageUrl = mImageViewParas.url;
			ReentrantLock mUrlLock = getLockForUrl(imageUrl);
			mUrlLock.lock();//���ڶ��ImageView����ͬһ��Url��Դ�������������
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
		if(null!=mTaskLoadImg)((ExecutorService) mTaskLoadImg).shutdownNow();
		if(null!=mTaskDistributor)((ExecutorService) mTaskDistributor).shutdownNow();
		if(null!=mDisplayHandler)mDisplayHandler.removeCallbacksAndMessages(null);
		if(null!=urlKeysForImageViews)urlKeysForImageViews.clear();
		uriLocks.clear();
		fluchCache();
		ALog.Log("mDiskLruCache.size:"+mDiskLruCache.size()/1024+"KB");//����ͳ��Ӧ�õĻ����С
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
		if(imageUrl.startsWith("http")){//��ʾ��Ҫ����������ͼƬ
			return tryToDownloadBitmap(imageUrl, mImageViewParas);
		}
		String imageUrlNew = ImageProcess.getInstance(mContext).parsePicUrl(imageUrl);
		return ImageProcess.getInstance(mContext).decodeSampledBitmap(imageUrlNew, StreamType.Asset, widthOfImageView, heightOfImageView,true);
	}

	/**
	 * ��Ӳ�̻����ж�ȡ���ݣ���������ڣ�����������
	 * @param imageUrl
	 * @return
	 */
	protected Bitmap tryToDownloadBitmap(String imageUrl, ImageViewParas mImageViewParas) throws TaskCancelException {
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		Snapshot snapShot = null;
		try {
			// ����ͼƬURL��Ӧ��key
			final String key = hashKeyForDisk(imageUrl);
			// ����key��Ӧ�Ļ���
			snapShot = mDiskLruCache.get(key);
			if (snapShot == null) {
				checkTaskInterrupted();
				checkImageViewReused(mImageViewParas);
				// ���û���ҵ���Ӧ�Ļ��棬��׼�����������������ݣ���д�뻺��
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
				// ���汻д����ٴβ���key��Ӧ�Ļ���
				snapShot = mDiskLruCache.get(key);
			}
			if (snapShot != null) {
				fileInputStream = (FileInputStream) snapShot.getInputStream(0);
				fileDescriptor = fileInputStream.getFD();
			}
			// ���������ݽ�����Bitmap����
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
	 * ʹ��MD5�㷨�Դ����key���м��ܲ����ء�
	 */
	public String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			//���Ƚ���ʵ�����ͳ�ʼ��
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			//�õ�һ������ϵͳĬ�ϵ��ֽڱ����ʽ���ֽ�����
			final byte[] btInput = key.getBytes();
			//�Եõ����ֽ�������д���
			mDigest.update(btInput);
			//���й�ϣ���㲢���ؽ��
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
	 * ����HTTP���󣬲���ȡBitmap����
	 * 
	 * @param imageUrl
	 *            ͼƬ��URL��ַ
	 * @return �������Bitmap����
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
	 * �������¼ͬ����journal�ļ��У�һ����Activity�����˳�ʱ��ִ��
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
