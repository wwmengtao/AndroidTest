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
	//ԭ����Դ�Ķ�ȡres/raw�ļ��������ݣ���ȡasset������Ҫ�õ�AssetManager
	public void readRawResources(){
		String res = null; 
		try{
		    //�õ���Դ�е�Raw������
		    InputStream in = mContext.getResources().openRawResource(R.raw.taido); 
		    //�õ����ݵĴ�С
		    int length = in.available();       
		    byte [] buffer = new byte[length];        
		    //��ȡ����
		    in.read(buffer);         
		    //�����ļ���������ѡ����ʵı��룬��������������� 
		    res = EncodingUtils.getString(buffer, "UTF-8"); 
		    ALog.Log(res);
		    //�ر�
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
	 * getFromAssets����assets���ļ���ȡ����
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
    
    /*assetsĿ¼�´�ŵ�ԭ����Դ�ļ�����Ϊϵͳ�ڱ����ʱ�򲻻����assets�µ���Դ�ļ�����������
     * ����ͨ��R.XXX.ID�ķ�ʽ�������ǡ����ܲ���ͨ������Դ�ľ���·��ȥ���������أ���Ϊapk��װ
     * ֮������/data/app/**.apkĿ¼�£���apk��ʽ���ڣ�asset/res�ͱ�����apk��������ѹ��
     * /data/data/YourAppĿ¼��ȥ�����������޷�ֱ�ӻ�ȡ��assets�ľ���·������Ϊ���Ǹ�����û�С�
     */
    /**
     * listAssets���о�assetsĿ¼������Ŀ¼�������ļ�(��)����
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
     *  copyFilesInAssets����assetsĿ¼�и��������ļ�������  
     *  @param  context  Context ʹ��CopyFiles���Activity 
     *  @param  oldPath  String  ԭ�ļ�·��
     *  @param  newPath  String  ���ƺ�·��
     */   
    public void copyFilesInAssets(String oldPath,String newPath) {     
    	//��������Ҫȥ���ļ�(��)��ͷ��"/"����ΪgetAssets().list�����Ĳ����ǲ�����ͷ��File.separator�ģ������ļ��޷��ҵ�
		if(oldPath.startsWith(File.separator)){
			oldPath=oldPath.substring(1, oldPath.length());
		}
    	try {  
    		String fileNames[] = mContext.getAssets().list(oldPath);//��ȡassetsĿ¼�µ������ļ���Ŀ¼��  
            if (fileNames.length > 0) {//�����Ŀ¼  
                File file = new File(newPath);  
                if(file.exists()){
                	deleteDir(file);
                }
                file.mkdirs();//����ļ��в����ڣ���ݹ�
                for (String fileName : fileNames) {
                	copyFilesInAssets(oldPath+File.separator+fileName,newPath+File.separator+fileName);  
                }
            } else{//������ļ�  
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
                while((byteCount=is.read(buffer))!=-1) {//ѭ������������ȡ buffer�ֽ�       
                    fos.write(buffer, 0, byteCount);//����ȡ��������д�뵽�����  
                }
                fos.flush();//ˢ�»�����  
                is.close();  
                fos.close();  
            }  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            ALog.Log("Exception");
        }                             
    }   

    /**
     * deleteDir:ɾ��ָ���ļ�������������
     * @param file
     */
	public void deleteDir(File file){
		if(!file.exists()){//�ļ��в�����
			ALog.Log("ָ��Ŀ¼������:"+file.getName());
			return;
		}
		boolean rslt=true;//�����м���
		if(!(rslt=file.delete())){//�ȳ���ֱ��ɾ��
			//���ļ��зǿա�ö�١��ݹ�ɾ����������
			File subs[] = file.listFiles();
			for (File afile : subs) {
				if (afile.isDirectory()){
					deleteDir(afile);//�ݹ�ɾ�����ļ�������
				}
				rslt = afile.delete();//ɾ�����ļ��б���
			}
			rslt = file.delete();//ɾ�����ļ��б���
		}
		if(!rslt){
			ALog.Log("�޷�ɾ��:"+file.getName());
			return;
		}
	}
	
	/**
	 * InstallApk���ⲿӦ�û��õ���Ӧ�õ�apk�ļ��������Ҫ������ʱ��ȡȨ��
	 * FLAG_GRANT_READ_URI_PERMISSION��
	 * ע�⣺����ִ��ǰ�豣ִ֤����copyFilesInAssets������apk�ļ�д���ڲ������ⲿ�洢
	 */
	public void InstallApk(){
		String apkName = "/apks/AndroidTest2.apk"; 
		//String fileName = getFilesDir() + "/myAssets_FilesDir"+apkName; //��ȡ�ڲ��洢�е�AndroidTest2.apk
		String fileName = mContext.getExternalFilesDir(null) + "/myAssets_ExternalFilesDir"+apkName; //��ȡ�ⲿ�洢�е�AndroidTest2.apk
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
	    * scanFile����дfile��·������������
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
		info = "StatFs.getBlockCountLong(��λ:M): ";
		ALog.Log(String.format(formatStorageSizeStr,info)+count * size / 1024 / 1024);
        count = fs.getFreeBlocksLong();
		info = "StatFs.getFreeBlocksLong(��λ:M): ";
		ALog.Log(String.format(formatStorageSizeStr,info)+count * size / 1024 / 1024);
		count = fs.getAvailableBlocksLong();
		info = "StatFs.getAvailableBlocksLong(��λ:M): ";
		ALog.Log(String.format(formatStorageSizeStr,info)+count * size / 1024 / 1024);   
	}
}
