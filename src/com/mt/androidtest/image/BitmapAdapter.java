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
 * רΪ���ش�ͼƬ��Ƶĸ�����ͼƬ������
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
	private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory());//����ڴ棬��λbytes 

    private Executor mExecutor=null;
    private ExecutorHelper mExecutorHelper =null;
    private ExecutorService mExecutorService = null;
    
    /** 
     * ͼƬ���漼���ĺ����࣬���ڻ����������غõ�ͼƬ���ڳ����ڴ�ﵽ�趨ֵʱ�Ὣ�������ʹ�õ�ͼƬ�Ƴ����� 
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
		//1��AsyncTask�Դ������̳߳�
		mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;//�����̳߳أ��ȴ����г���Ϊ128�������Ϊ1000�ȴ����ݣ���GridView��������ʱ������RejectedExecutionException
		//2���Զ����̳߳�
		mExecutorHelper = new ExecutorHelper();
		//mExecutorService = mExecutorHelper.getExecutorService(3, -1);//���ʹ��newCachedThreadPool���ܿ�����OOM����Ϊ�����߳����������������
		mExecutorService = mExecutorHelper.getExecutorService(2, 2*CPU_COUNT+1);	//ʹ��newFixedThreadPool�������̹߳������������Ա���OOM���������coreThreads��������Ļ�����Ӱ�����ܣ���Ϊ���ڴ�Ҫ����ߡ�
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
            //task.execute(bitmapUrl); //����Ĭ�ϵĴ��д���ʽ
            //task.executeOnExecutor(mExecutor, bitmapUrl);//���ò��д���ʽ�����������ᵼ��RejectedExecutionException����Ϊ�ȴ����г���Ϊ128
            task.executeOnExecutor(mExecutorService, bitmapUrl);//�Զ����̳߳�
        }  
		return mViewHolder.getConvertView();
	}
	
	public void doAdditionalWork(){
			View convertView = mViewHolder.getConvertView();
			ImageView mImageView =mViewHolder.getView(R.id.myimageview);
			mImageView.setScaleType(ScaleType.FIT_XY);//�ǵȱ������ţ���������ImageView
			if(0==widthOfIV && 0==heightOfIV){//��ȡmImageView�Ĳ������
				convertView.measure(0, 0);
				widthOfIV = mImageView.getMeasuredWidth();
				heightOfIV = mImageView.getMeasuredHeight();
			}
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
            Bitmap bitmap = mBitmapProcess.getBitmap(imageUrl, widthOfIV, heightOfIV);  
            if(null!=bitmap)addBitmapToMemoryCache(imageUrl, bitmap);
            return bitmap;  
        }  
  
        @Override  
        protected void onPostExecute(Bitmap mBitmap) {  
            ImageView mImageView = (ImageView) mViewGroup.findViewWithTag(imageUrl); //ListView����GridView��Ҫ������ң�Ч�ʵ�  
            if (mImageView != null && mBitmap != null) {
        		mImageView.setImageBitmap(mBitmap);
        		ALog.Log("imageUrl:"+imageUrl+" mImageView:"+mImageView);
            }
        }
	}
	
}
