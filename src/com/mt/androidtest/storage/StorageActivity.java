package com.mt.androidtest.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.util.EncodingUtils;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import com.mt.androidtest.ALog;
import com.mt.androidtest.BaseActivity;
import com.mt.androidtest.R;

public class StorageActivity extends BaseActivity {
	private boolean isLogRun=false;
	private Context mContext = null;
	private Handler mHandler=null;
	private HandlerThread mHandlerThread=null;
	private HandlerCostTime mHandlerCostTime=null;
	private String [] mMethodNameFT={
			"readRawResources",
			"getResourcesDescription",
			"listAssets",
			"getFromAssets",
			"copyFilesInAssets"};
	private String [] mActivitiesName={"ExtStorageActivity","IntStorageActivity"};
	private static final int MSG_readRawResources=0x000;
	private static final int MSG_getResourcesDescription=0x001;	
	private static final int MSG_listAssets=0x002;
	private static final int MSG_getFromAssets=0x003;
	private static final int MSG_copyFilesInAssets=0x004;
    private static final int TIME_INTERVAL_MS = 500;
    private Message mMessage=null;	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		isLogRun=getLogRun();
		if(isLogRun)ALog.Log("StorageActivity_onCreate");
		mContext=this.getApplicationContext();
		initListFTData(mMethodNameFT);
		initListActivityData(mActivitiesName);
		initHandlerThread();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(isLogRun)ALog.Log("StorageActivity_onResume");
		mHandler = getHandler();
	}

	@Override
	public void onPause(){
		if(null!=mHandlerCostTime){
			mHandlerCostTime.removeCallbacksAndMessages(0);
		}
		super.onPause();
		if(isLogRun)ALog.Log("StorageActivity_onPause");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		destroyHandlerThread();
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		super.handleMessage(msg);
		mHandler.removeMessages(msg.what);
		switch(msg.what){			
			case MSG_readRawResources:
				readRawResources();			
				break;
			case MSG_getResourcesDescription:
				getResourcesDescription();		
				break;	
		}
		return true;
	}
	
	/**
	 * HandlerCostTime：处理耗时操作的Handler
	 * @author Mengtao1
	 *
	 */
	class HandlerCostTime extends Handler{
	     public HandlerCostTime(Looper loop) {
	            super(loop);
	     }		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mHandler.removeMessages(msg.what);
			switch(msg.what){	
			case MSG_listAssets:
				listAssets("");//列举assets根目录下的文件
				//listAssets("test");//列举assets/test目录下的文件		
				break;
			case MSG_getFromAssets:
				getFromAssets("test/test.txt");		
				break;
			case MSG_copyFilesInAssets:
				//拷贝assets目录下的内容到指定位置
				/**
				 * getFilesDir：/data/data/com.mt.androidtest/files下创建子文件夹
				 * 向上述文件夹写入数据需要WRITE_EXTERNAL_STORAGE权限
				 */
				File mFile= new File(getFilesDir(),"myAssets_FilesDir");
				copyFilesInAssets("",mFile.getAbsolutePath());
				ALog.Log("copyFilesInAssets to new location:"+mFile.getAbsolutePath());
				/**
				 * getExternalFilesDir：storage/emulated/0/Android/data/com.mt.androidtest/files
				 */
				mFile= new File(getExternalFilesDir(null),File.separator+"myAssets_ExternalFilesDir");
				copyFilesInAssets("",mFile.getAbsolutePath());
				ALog.Log("copyFilesInAssets to new location:"+mFile.getAbsolutePath());
				break;		
			}
		}
	}
	
	public void initHandlerThread(){
		mHandlerThread = new HandlerThread("StorageActivity",
                android.os.Process.THREAD_PRIORITY_FOREGROUND);
		mHandlerThread.start();
		mHandlerCostTime=new HandlerCostTime(mHandlerThread.getLooper());
	}
	
	public void destroyHandlerThread(){
		/**HandlerThread的run方法是一个死循环，它不会自己结束，线程的生命周期超过了activity生命周期。
		当横竖屏切换，HandlerThread线程的数量会随着activity重建次数的增加而增加。应该在onDestroy时将线程停止掉。
		另外，对于不是HandlerThread的线程，也应该确保activity销毁前，线程已经终止，可以这样做：在onDestroy时调用
		mThread.join();*/
		mHandlerThread.getLooper().quit();
	}
	
	@Override
	public void initListFTData(String [] mMethodNameFT){
		super.initListFTData(mMethodNameFT);
	}
	
	public void initListActivityData(String [] mActivitiesName){
		super.initListActivityData(mActivitiesName);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		//ALog.Log("position:"+position);
		mMessage=Message.obtain(mHandler, position);
		if(position<=1){//执行的是非耗时操作
			mHandler.sendMessageDelayed(mMessage, TIME_INTERVAL_MS);
		}else{//执行的是耗时操作
			mHandlerCostTime.sendMessageDelayed(mMessage, TIME_INTERVAL_MS);
		}
	}	
	
	//原生资源的读取res/raw文件夹下内容，读取asset内容需要用到AssetManager
	public void readRawResources(){
		String res = null; 
		try{
		    //得到资源中的Raw数据流
		    InputStream in = mContext.getResources().openRawResource(R.raw.taido); 
		    //得到数据的大小
		    int length = in.available();       
		    byte [] buffer = new byte[length];        
		    //读取数据
		    in.read(buffer);         
		    //按照文件编码类型选择合适的编码，如果不调整会乱码 
		    res = EncodingUtils.getString(buffer, "UTF-8"); 
		    ALog.Log(res);
		    //关闭
		    in.close();
		}catch(Exception e){ 
		    e.printStackTrace();         
		}
	}
	
	public void getResourcesDescription(){
		Resources mResources=mContext.getResources();
		Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
			    + mResources.getResourcePackageName(R.drawable.ic_launcher) + "/"
			    + mResources.getResourceTypeName(R.drawable.ic_launcher) + "/"
			    + mResources.getResourceEntryName(R.drawable.ic_launcher));
		ALog.Log("uri:"+uri.toString());
	}
	
	/**
	 * getFromAssets：从assets中文件读取数据
	 * @param fileName
	 */
    public void getFromAssets(String fileName){ 
        try { 
             InputStreamReader inputReader = new InputStreamReader(mContext.getResources().getAssets().open(fileName) ); 
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line=null;
            while((line = bufReader.readLine()) != null)
                ALog.Log("line:"+line);
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    } 
    
    /*assets目录下存放的原生资源文件。因为系统在编译的时候不会编译assets下的资源文件，所以我们
     * 不能通过R.XXX.ID的方式访问它们。那能不能通过该资源的绝对路径去访问它们呢？因为apk安装
     * 之后会放在/data/app/**.apk目录下，以apk形式存在，asset/res和被绑定在apk里，并不会解压到
     * /data/data/YourApp目录下去，所以我们无法直接获取到assets的绝对路径，因为它们根本就没有。
     */
    /**
     * listAssets：列举assets目录或者子目录下所有文件(夹)内容
     */
    public void listAssets(String fileName){
       	String fileNames[];
    		try {
    			fileNames = mContext.getAssets().list(fileName);
    	    	for(int i=0;i<fileNames.length;i++){
    	    		ALog.Log("fileAssets:"+fileNames[i]);
    	    	}
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} 
    }
    
    /**  
     *  copyFilesInAssets：从assets目录中复制整个文件夹内容  
     *  @param  context  Context 使用CopyFiles类的Activity 
     *  @param  oldPath  String  原文件路径
     *  @param  newPath  String  复制后路径
     */   
    public void copyFilesInAssets(String oldPath,String newPath) {     
    	Context context=this;
    	//下面所以要去掉文件(夹)开头的"/"是因为getAssets().list函数的参数是不允许开头有File.separator的，否则文件无法找到
		if(oldPath.startsWith(File.separator)){
			oldPath=oldPath.substring(1, oldPath.length());
		}
    	try {  
    		String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名  
            if (fileNames.length > 0) {//如果是目录  
                File file = new File(newPath);  
                if(file.exists()){
                	deleteDir(file);
                }
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                	copyFilesInAssets(oldPath+File.separator+fileName,newPath+File.separator+fileName);  
                }
            } else{//如果是文件  
            	File newFile = new File(newPath);
        		if (newFile.exists()) {
        			newFile.delete();
        		}
        		newFile.createNewFile();
                InputStream is = context.getAssets().open(oldPath);  
                //ALog.Log("oldPath:"+oldPath+" newPath:"+newPath);
                FileOutputStream fos = new FileOutputStream(newFile);  
                byte[] buffer = new byte[1024];
                int byteCount=0;
                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节       
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流  
                }
                fos.flush();//刷新缓冲区  
                is.close();  
                fos.close();  
            }  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            ALog.Log("Exception");
        }                             
    }   

    /**
     * deleteDir:删除指定文件夹下所有内容
     * @param file
     */
	public void deleteDir(File file){
		if(!file.exists()){//文件夹不存在
			ALog.Log("指定目录不存在:"+file.getName());
			return;
		}
		boolean rslt=true;//保存中间结果
		if(!(rslt=file.delete())){//先尝试直接删除
			//若文件夹非空。枚举、递归删除里面内容
			File subs[] = file.listFiles();
			for (File afile : subs) {
				if (afile.isDirectory()){
					deleteDir(afile);//递归删除子文件夹内容
				}
				rslt = afile.delete();//删除子文件夹本身
			}
			rslt = file.delete();//删除此文件夹本身
		}
		if(!rslt){
			ALog.Log("无法删除:"+file.getName());
			return;
		}
	}
	
	
}
