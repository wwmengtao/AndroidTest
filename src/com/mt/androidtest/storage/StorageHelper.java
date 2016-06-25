package com.mt.androidtest.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

import org.apache.http.util.EncodingUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import com.mt.androidtest.ALog;
import com.mt.androidtest.R;

public class StorageHelper {
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
    	//��������Ҫȥ���ļ�(��)��ͷ��"/"����ΪgetAssets().list�����Ĳ����ǲ�������ͷ��File.separator�ģ������ļ��޷��ҵ�
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
}