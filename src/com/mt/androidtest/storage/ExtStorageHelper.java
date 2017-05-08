package com.mt.androidtest.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.mt.androidtest.ALog;
import com.mt.androidtest.image.ImageProcess;
import com.mt.androidtest.image.ImageProcess.StreamType;
import com.mt.androidtest.image.PicConstants;

/**
 * External Storage操作工具类
 * @author http://www.androidchina.net/4106.html
 *
 */
public class ExtStorageHelper {
	private Context mContext=null;
	private StorageHelper mStorageHelper = null;
	public ExtStorageHelper(Context Context){
		mContext = Context;
		mStorageHelper = new StorageHelper(mContext);
	}
	
	/**
	 * initMountPoint：获取当前设备当前状态下的所有存储设备信息。例如内部存储、SD卡、OTG、外置TF卡。
	 * @param context
	 */
	public void getMountPointByStorageManager(Context context) {
		final String OTGTAG = "public:8,";//"public:8,"对应OTG存储设备
		boolean sBFUSEPROJECT = false;
		String sROOTFOLDER = null;
		String sROOTFOLDER2 = null;
		String sROOTOTGPATH = null;
		String sROOTOTGPATH2 = null;
		StorageVolume[] volumes = null;
		ALog.Log("initMountPoint");
		try {
			StorageManager mStorageManager = (StorageManager) context
					.getSystemService(Context.STORAGE_SERVICE);
			volumes = mStorageManager.getVolumeList();
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (volumes == null)return;

		int itemsSize = volumes.length;
		for (int i = 0; i < itemsSize; i++) {
			if (volumes[i].isEmulated()) {//说明是内部存储设备
				sBFUSEPROJECT = true;
				break;
			}
		}

		if (sBFUSEPROJECT) {
			sROOTFOLDER = null;
			sROOTFOLDER2 = null;
			sROOTOTGPATH = null;
			sROOTOTGPATH2 = null;
			for (int i = 0; i < itemsSize; i++) {
				ALog.Log("volumes["+i+"] is: \"" + volumes[i]+"\" getId: "+volumes[i].getId()+" getPath: "+volumes[i].getPath()
						+" isEmulated: "+volumes[i].isEmulated()+" isRemovable: "+volumes[i].isRemovable());
				if (volumes[i].isEmulated()
						&& (!volumes[i].getId().contains(OTGTAG))) {//内部存储设备
					sROOTFOLDER = volumes[i].getPath();
				} else if (volumes[i].isRemovable()
						&& (!volumes[i].getId().contains(OTGTAG))) {//SD卡
					sROOTFOLDER2 = volumes[i].getPath();
				} else if (!TextUtils.isEmpty(volumes[i].getPath()) && volumes[i].getState().equalsIgnoreCase("MOUNTED")
						&& (volumes[i].getId().contains(OTGTAG))){//OTG设备
					if(TextUtils.isEmpty(sROOTOTGPATH)){
						sROOTOTGPATH = volumes[i].getPath();
						continue;
					}
					if(TextUtils.isEmpty(sROOTOTGPATH2)){
						sROOTOTGPATH2 = volumes[i].getPath();
						continue;
					}
				}
			}
			ALog.Log("111 sROOTOTGPATH is " + sROOTOTGPATH);
			ALog.Log("111 sROOTOTGPATH2 is " + sROOTOTGPATH2);
		} else {
			sROOTFOLDER = Environment
					.getExternalStorageDirectory().getPath();

			for (int i = 0; i < itemsSize; i++) {
				if (!volumes[i].getPath().equals(sROOTFOLDER)
						&& (!volumes[i].getId().contains(OTGTAG))) {
					sROOTFOLDER2 = volumes[i].getPath();
					break;
				}
			}

			boolean otgPath1 = false;
			for (int i = 0; i < itemsSize; i++) {
				if (!TextUtils.isEmpty(volumes[i].getPath())
						&& (volumes[i].getId().contains(OTGTAG))) {
					if (!otgPath1) {
						sROOTOTGPATH = volumes[i].getPath();
						otgPath1 = true;
					} else {
						sROOTOTGPATH2 = volumes[i].getPath();
						break;
					}
				}
			}
			ALog.Log("222 sROOTOTGPATH is " + sROOTOTGPATH);
			ALog.Log("222 sROOTOTGPATH2 is " + sROOTOTGPATH2);
		}
		/*
		 * 例如手机插入SD卡以及OTG U盘时，显示如下信息：
		 	05-05 17:21:34.853 32232 32232 E M_T_AT  : initFirmware sROOTFOLDER     = /storage/emulated/0
			05-05 17:21:34.853 32232 32232 E M_T_AT  : initFirmware sROOTFOLDER2   = /storage/4172-1013
			05-05 17:21:34.853 32232 32232 E M_T_AT  : initFirmware sROOTOTGPATH   = /storage/1C63-6D37
			05-05 17:21:34.854 32232 32232 E M_T_AT  : initFirmware sROOTOTGPATH2 = null
		 */
		ALog.Log("initFirmware sROOTFOLDER     = " + sROOTFOLDER);
		ALog.Log("initFirmware sROOTFOLDER2   = " + sROOTFOLDER2);
		ALog.Log("initFirmware sROOTOTGPATH   = " + sROOTOTGPATH);
		ALog.Log("initFirmware sROOTOTGPATH2 = " + sROOTOTGPATH2);
	}
    
	/** 
     * showAllExterSdcardPath：获取手机系统中所有被挂载的设备，我们可以通过查看mount命令的输出来获取。
     *  
     * @return 
     */  
    public void getMountPointByMountProc(){  
    	List<String> SdList = new ArrayList<String>();  
        String firstPath = Environment.getExternalStorageDirectory().getPath(); //一般为"/storage/emulated/0"
        SdList.add(firstPath);
        try{  
        	Runtime runtime = Runtime.getRuntime();  
            // 运行mount命令，获取命令的输出，得到系统中挂载的所有目录  
            Process proc = runtime.exec("mount");  
            InputStream is = proc.getInputStream();  
            InputStreamReader isr = new InputStreamReader(is);  
            BufferedReader br = new BufferedReader(isr);  
            String line;
            while ((line = br.readLine()) != null){  
                ALog.Log("showAllExterSdcardPath: "+line);  
                // 将常见的linux分区过滤掉  
                if (line.contains("proc") || line.contains("tmpfs") || line.contains("media") || line.contains("asec") || line.contains("secure") || line.contains("system") || line.contains("cache")  
                        || line.contains("sys") || line.contains("data") || line.contains("shell") || line.contains("root") || line.contains("acct") || line.contains("misc") || line.contains("obb")){  
                    continue;  
                }  
  
                // 下面这些分区是我们需要的  
                if (line.contains("fat") || line.contains("fuse") || (line.contains("ntfs"))){  
                    // 将mount命令获取的列表分割，items[0]为设备名，items[1]为挂载路径  
                    String items[] = line.split(" ");  
                    if (items == null && items.length <1)continue;
                    for(String item : items){
                        String path = item.toLowerCase(Locale.getDefault());  
                        // 添加一些判断，确保是内部存储，sd卡，以及OTG等设备  
                        if (path != null && !isListContains(SdList, item) && (path.contains("sd")||path.contains("/storage"))){ 
                            ALog.Log("showAllExterSdcardPath2_line: "+line);  
                        	ALog.Log("showAllExterSdcardPath2_item: "+item);                              
                            SdList.add(item);  
                        }
                    }
                }  
            }  
        } catch (Exception e){  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
  
        if(SdList.size()>0){
        	ALog.Log("/---------------showAllExterSdcardPath-----------------/");
        	/*
        	 * 例如手机插入SD卡以及OTG U盘时，显示如下信息：
	        	05-05 17:19:23.610 31715 31715 E M_T_AT  : AllExterSdcardPath: /storage/emulated/0
	        	05-05 17:19:23.610 31715 31715 E M_T_AT  : AllExterSdcardPath: /storage/4172-1013
	        	05-05 17:19:23.610 31715 31715 E M_T_AT  : AllExterSdcardPath: /storage/1C63-6D37
        	*/
        	for(String str : SdList ){
        		ALog.Log("AllExterSdcardPath: "+str);
        	}
        }
    }  
   
    private boolean isListContains(List<String> list, String item){
    	if(null == list || list.size() == 0 || null == item)return false;
    	for(String str : list){
    		if(str.equals(item) || str.startsWith(item)){
    			return true;
    		}
    	}
    	return false;
    }
    
   /**
    * listDirs:罗列Environment以及Context所能获取的所有外部存储位置及其状态
    */
   public void listDirs(){
	   //一、罗列Environment内部标准文件路径
	   String[] STANDARD_DIRECTORIES = Environment.STANDARD_DIRECTORIES;
	   mStorageHelper.scanFilesArray(STANDARD_DIRECTORIES,"Environment.STANDARD_DIRECTORIES");
	   //二、罗列Environment获取外部存储文件路径以及状态
	   String Tag = "Environment.getExternalStorageDirectory()";
	   File file = Environment.getExternalStorageDirectory(); //一般为"/storage/emulated/0"
	   ALog.Log("/------------"+Tag+"--------------/");
	   mStorageHelper.scanFile(file);
	   //
	   String Type = STANDARD_DIRECTORIES[7];
	   Tag = "Environment.getExternalStoragePublicDirectory() Type: "+Type;
	   file = Environment.getExternalStoragePublicDirectory(Type);
	   ALog.Log("/------------"+Tag+"--------------/");
	   mStorageHelper.scanFile(file);
	   //三、罗列Context能获取的外部存储路径
	   File[] files = mContext.getExternalFilesDirs(Type);
	   mStorageHelper.scanFilesArray(files,"Context.getExternalFilesDirs Type: "+Type);
	   //
	   files = mContext.getExternalCacheDirs();
	   mStorageHelper.scanFilesArray(files,"Context.getExternalCacheDirs");
	   //
	   files = mContext.getExternalMediaDirs();
	   mStorageHelper.scanFilesArray(files,"Context.getExternalMediaDirs");
   }
   
   /**
    * saveFileToExtStorage：将文件写入内部存储或者SD卡
    */
	public void fileSaveAndLoadProStorage() {
		String[] STANDARD_DIRECTORIES = Environment.STANDARD_DIRECTORIES;
		byte[] bytesToSave=new byte[]{'s','u','c','c','e','s','s'};
		String fileName="success.txt";
		String type = STANDARD_DIRECTORIES[7];
		File[] dirs = mContext.getExternalFilesDirs(type);
		for(File dir : dirs){
			saveFile(bytesToSave, dir, fileName);
			byte[] data = loadFile(dir+File.separator+fileName);
		}
	}

    public File saveFile(Object data, File dir, String fileName){
		boolean savedSucceed=false;
		File mFile = null;
    	if(null!=dir&&null!=fileName){
            if (dir.exists()) {
                if (!dir.isDirectory()) {
                    throw new IllegalStateException(dir.getAbsolutePath() + " already exists and is not a directory");
                }
            } else {
                if (!dir.mkdirs()) {
                    throw new IllegalStateException("Unable to create directory: "+	dir.getAbsolutePath());
                }
            }
    		mFile=new File(dir,fileName);
	    	BufferedOutputStream bos = null;
	    	if (isExternalStorageWritable(dir)) {
		        try {
		              bos = new BufferedOutputStream(new FileOutputStream(mFile));
		              writeData(bos, data, fileName);
		              bos.flush();
		              savedSucceed = true;
		        } catch (Exception e) {
		              e.printStackTrace();
		              savedSucceed = false;
		        } finally {
	                if (bos != null) {
	                    try {
	                         bos.close();
	                         bos=null;
	                    } catch (IOException e) {
	                         e.printStackTrace();
	                    }//catch
	               }//if
		        }//finally
		        ALog.Log("File saved:"+mFile.getAbsolutePath()+" Saved status:"+savedSucceed);
	    	}//if
    	}//if
        if(!savedSucceed){
        	return null;
        }
        return mFile;
	}
    
    private void writeData(BufferedOutputStream bos, Object data, String fileName){
    	if(data instanceof byte[]){//一般性的文件，比如txt,xml文件等为byte[]类型
    		try {
				bos.write((byte[])data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else if(data instanceof Bitmap){//图片文件
            if (fileName != null && fileName.toLowerCase().contains(".png")) {
                ((Bitmap)data).compress(Bitmap.CompressFormat.PNG, 100, bos);
            } else {
            	((Bitmap)data).compress(Bitmap.CompressFormat.JPEG, 100, bos);
            }
    	}
    }
    
    public byte[] loadFile(String fileName) {
    	if(!isExternalStorageReadable(new File(fileName))){
    		ALog.Log("File not readable! "+fileName);
    		return null;
    	}
    	 byte[] dataLoaded=null;
         BufferedInputStream bis = null;
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         try {
               bis = new BufferedInputStream(new FileInputStream(new File(fileName)));
               byte[] buffer = new byte[8 * 1024];
               int c = 0;
               while ((c = bis.read(buffer)) != -1) {
                    baos.write(buffer, 0, c);
                    baos.flush();
               }
               dataLoaded = baos.toByteArray();
               if(null != dataLoaded){
            	   String content = new String(dataLoaded);
            	   if(content.length()<=100){//防止图片加载过大显示过量内容
            		   ALog.Log("File loaded: "+fileName+" Content: "+content);
            	   }else{
            		   ALog.Log("File loaded: "+fileName);
            	   }
               }
               return dataLoaded;
         } catch (Exception e) {
               e.printStackTrace();
         } finally {
               try {
                    baos.close();
                    bis.close();
               } catch (IOException e) {
                    e.printStackTrace();
               }
         }
         return dataLoaded;
    }
    
    public void bitmapSaveAndLoadProStorage(){
		//以下获取屏幕的宽高
    	DisplayMetrics displayMetrics = null;
    	displayMetrics = mContext.getResources().getDisplayMetrics();
    	int displayMetricsWidth = displayMetrics.widthPixels;
    	int displayMetricsHeight = displayMetrics.heightPixels;
		Bitmap bm = ImageProcess.getInstance(mContext).decodeSampledBitmap(
				PicConstants.assetHDPicNames[0], StreamType.Asset, displayMetricsWidth,displayMetricsHeight,true);
		String[] STANDARD_DIRECTORIES = Environment.STANDARD_DIRECTORIES;
		String fileName="btmap.png";
		String type = STANDARD_DIRECTORIES[7];
		File[] dirs = mContext.getExternalFilesDirs(type);
		for(File dir : dirs){
			saveFile(bm, dir, fileName);
			Bitmap bm2 = loadBitmap(dir+File.separator+fileName);
		}
    }

    // 从External Storage中寻找指定目录下的文件，返回Bitmap
    public Bitmap loadBitmap(String fileName) {
    	if(!isExternalStorageReadable(new File(fileName))){
    		ALog.Log("File not readable! "+fileName);
    		return null;
    	}
    	Bitmap bm = null;
         byte[] data = loadFile(fileName);
         if (data != null) {
              bm = BitmapFactory.decodeByteArray(data, 0, data.length);
              if (bm != null) {
            	    ALog.Log("loadBitmap: "+fileName);
                    return bm;
              }
         }
         return null;
    }
    
	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable(File file) {
	    String state = (file != null) ? Environment.getExternalStorageState(file) : Environment.getExternalStorageState();
		boolean isReadable = state.equals(Environment.MEDIA_MOUNTED)
										   || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
	    return isReadable;
	}
    
	private boolean isExternalStorageWritable(File file){
	    String state = (file != null) ? Environment.getExternalStorageState(file) : Environment.getExternalStorageState();
	    boolean isWritable = state.equals(Environment.MEDIA_MOUNTED);
	    return isWritable;
	}

}