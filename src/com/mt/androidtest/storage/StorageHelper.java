package com.mt.androidtest.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.FileProvider;

import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

public class StorageHelper {
	private String ExternalStorageState = "Environment.getExternalStorageState";
	private String ExternalStorageRemovable = "Environment.isExternalStorageRemovable";
	private String ExternalStorageEmulated = "Environment.isExternalStorageEmulated";

	Context mContext = null;
	WeakReference<Context>mWeakReference=null;
	public StorageHelper(Context context){
		mWeakReference=new WeakReference<Context>(context);
		if(null==(mContext=mWeakReference.get())){
			return;
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
    	//下面所以要去掉文件(夹)开头的"/"是因为getAssets().list函数的参数是不允许开头有File.separator的，否则文件无法找到
		if(oldPath.startsWith(File.separator)){
			oldPath=oldPath.substring(1, oldPath.length());
		}
    	try {  
    		String fileNames[] = mContext.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名  
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
                InputStream is = mContext.getAssets().open(oldPath);  
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
	
	/**
	 * InstallApk：外部应用会用到本应用的apk文件，因此需要授予临时读取权限
	 * FLAG_GRANT_READ_URI_PERMISSION。
	 * 注意：函数执行前需保证执行了copyFilesInAssets函数将apk文件写入内部或者外部存储
	 */
	public void InstallApk(){
		String apkName = "/apks/AndroidTest2.apk"; 
		//String fileName = getFilesDir() + "/myAssets_FilesDir"+apkName; //获取内部存储中的AndroidTest2.apk
		String fileName = mContext.getExternalFilesDir(null) + "/myAssets_ExternalFilesDir"+apkName; //获取外部存储中的AndroidTest2.apk
		if(!new File(fileName).exists()){
			File mFile= new File(mContext.getFilesDir(),"myAssets_FilesDir");
			copyFilesInAssets("",mFile.getAbsolutePath());
		}
		ALog.Log("fileName: "+fileName);
		File file = new File(fileName);
		if (file.exists()) {
			try {
				Uri path = Uri.fromFile(file);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				if(Build.VERSION.SDK_INT >= 23){

					ALog.Log("path: "+path+"\nfile: "+file.getAbsolutePath());
					path = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
					intent.setDataAndType(path, "application/vnd.android.package-archive");
					intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Context context = mContext.getApplicationContext();
				 	List<ResolveInfo> infoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
					for (ResolveInfo resolveInfo : infoList) {
					    String packageName = resolveInfo.activityInfo.packageName;
					    context.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
					    mContext.startActivity(intent);
					}
				}
			}catch (android.content.ActivityNotFoundException e) {
				e.printStackTrace();
				ALog.Log("android.content.ActivityNotFoundException");
			}
		}
	}
	
	public void scanFilesArray(File[] files, String Tag){
		   ALog.Log("/------------"+Tag+"--------------/");
		   for(File file : files){
			   scanFile(file);
		   }
	   }
	   
	   public void scanFilesArray(String[] files, String Tag){
		   ALog.Log("/------------"+Tag+"--------------/");
		   for(String file : files){
			   scanFile(new File(file));
		   }
	   }
	   
	   /**
	    * scanFile：列写file的路径及各种属性
	    * @param Tag
	    * @param file
	    */
	   private static String formatScanFileStr="%-50s";
	   public void scanFile(File file){
		   try{
			   String info1 = String.format(formatScanFileStr,"1. \""+ExternalStorageState+"\": ")+Environment.getExternalStorageState(file);
			   String info2 = String.format(formatScanFileStr,"2. \""+ExternalStorageRemovable+"\": ")+Environment.isExternalStorageRemovable(file);
			   String info3 = String.format(formatScanFileStr,"3. \""+ExternalStorageEmulated+"\": ")+Environment.isExternalStorageEmulated(file);
			   ALog.Log("Path: "+file.getAbsolutePath()+"\n"+info1+"\n"+info2+"\n"+info3);
			   showExtStorageSize(file.getAbsolutePath());
		   }catch(IllegalArgumentException ia){
			   ALog.Log("IllegalArgumentException: " + file);
		   }
	   }
	
	private static String formatStorageSizeStr="%-40s";
	public void showExtStorageSize(String path) {
		String info = null;
		StatFs fs = new StatFs(path);
        long size = fs.getBlockSizeLong();
		long count = fs.getBlockCountLong();
		info = "StatFs.getBlockCountLong(单位:M): ";
		ALog.Log(String.format(formatStorageSizeStr,info)+count * size / 1024 / 1024);
        count = fs.getFreeBlocksLong();
		info = "StatFs.getFreeBlocksLong(单位:M): ";
		ALog.Log(String.format(formatStorageSizeStr,info)+count * size / 1024 / 1024);
		count = fs.getAvailableBlocksLong();
		info = "StatFs.getAvailableBlocksLong(单位:M): ";
		ALog.Log(String.format(formatStorageSizeStr,info)+count * size / 1024 / 1024);   
	}
}
