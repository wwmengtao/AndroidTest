package com.mt.androidtest.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

import com.mt.androidtest.ALog;

/**
 * External Storage操作工具类
 * @author http://www.androidchina.net/4106.html
 *
 */
public class ExtStorageHelper {
	Context mContext=null;
	public ExtStorageHelper(Context Context){
		mContext = Context;
	}
	
	/* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
    	return Environment.getExternalStorageState().equals(
    			Environment.MEDIA_MOUNTED);
   }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
        		Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

   // 获取External Storage的完整空间大小，返回MB
   /**
    * getBlockCountLong()：获取文件系统中，总块数
    * getBlockSizeLong()：获取文件系统中，每块的大小(字节数)
    * @return
    */
   public long getExtStorageSize() {
        if (isExternalStorageReadable()) {
             StatFs fs = new StatFs(getExternalStorageDirectory().getAbsolutePath());
             long count = fs.getBlockCountLong();
             long size = fs.getBlockSizeLong();
             return count * size / 1024 / 1024;
        }
        return 0;
   }

   // 获取External Storage的剩余空间大小，返回MB
   public long getExtStorageFreeSize() {
        if (isExternalStorageReadable()) {
              StatFs fs = new StatFs(getExternalStorageDirectory().getAbsolutePath());
              long count = fs.getFreeBlocksLong();
              long size = fs.getBlockSizeLong();
              return count * size / 1024 / 1024;
        }
        return 0;
   }

   // 获取External Storage的可用空间大小，返回MB
   public long getExtStorageAvailableSize() {
        if (isExternalStorageReadable()) {
              StatFs fs = new StatFs(getExternalStorageDirectory().getAbsolutePath());
              long count = fs.getAvailableBlocksLong();
              long size = fs.getBlockSizeLong();
              return count * size / 1024 / 1024;
        }
        return 0;
   }

   // 获取External Storage的根目录
   public File getExternalStorageDirectory() {
        if (isExternalStorageReadable()) {
              return Environment.getExternalStorageDirectory();
        }
        return null;
   }
   
   // 获取External Storage公有目录
	public File getExternalStoragePublicDirectory(String type) {
	   if (isExternalStorageReadable()) {
		   return Environment.getExternalStoragePublicDirectory(type);
	   }
	   return null;
	}

   // 获取External Storage私有Cache目录的路径
   public File getExternalCacheDir() {
        return mContext.getExternalCacheDir();
   }

   // 获取External Storage私有Files目录的路径
   public File getExternalFilesDir(String type) {
        return mContext.getExternalFilesDir(type);
   }
   
   /**
    *Android6.0之后，Environment.getExternalStorageDirectory目录下除了私有数据存储区(Android/data/包名下
    *的cache和files目录)外，写其他公共区域需要动态申请WRITE_EXTERNAL_STORAGE权限
    */
	public File saveFileToExtStoragePublicDir(byte[] data, String type, String fileName) {
		File dir = getExternalStoragePublicDirectory(type);
    	return saveFile(data,dir,fileName);
	}

    public File saveFileToExtStorageCustomDir(byte[] data, String dir_custome, String fileName) {
        File dir = new File(getExternalStorageDirectory(),dir_custome);
    	return saveFile(data,dir,fileName);

    }

    // 往External Storage的私有Files目录下保存文件
    public File saveFileToExtStoragePrivateFilesDir(byte[] data, String type, String fileName) {
        File dir = mContext.getExternalFilesDir(type);
    	return saveFile(data,dir,fileName);
    }

    // 往External Storage的私有Cache目录下保存文件
    public File saveFileToExtStoragePrivateCacheDir(byte[] data, String fileName) {
    	File dir = mContext.getExternalCacheDir();
    	return saveFile(data,dir,fileName);
    }

    public File saveFile(byte[] data,File dir,String fileName){
		boolean savedSucceed=false;
		File mFile=null;
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
	    	if (isExternalStorageWritable()) {
		        try {
		              bos = new BufferedOutputStream(new FileOutputStream(mFile));
		              bos.write(data);
		              bos.flush();
		              savedSucceed = true;
		        } catch (Exception e) {
		              e.printStackTrace();
		              savedSucceed = false;
		        } finally {
	                if (bos != null) {
	                    try {
	                         bos.close();
	                    } catch (IOException e) {
	                         e.printStackTrace();
	                    }//catch
	               }//if
		        }//finally
	    	}//if
    	}//if
        ALog.Log("File saved:"+mFile.getAbsolutePath()+"\nSaved status:"+savedSucceed);
        if(!savedSucceed){
        	return null;
        }
        return mFile;
	}
    
    // 保存bitmap图片到External Storage的私有Cache目录
    public boolean saveBitmapToExtStoragePrivateCacheDir(Bitmap bitmap, String fileName) {
         if (isExternalStorageWritable()) {
               BufferedOutputStream bos = null;
               // 获取私有的Cache缓存目录
               File file = mContext.getExternalCacheDir();
               try {
                      bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                      if (fileName != null && (fileName.contains(".png") || fileName.contains(".PNG"))) {
                             bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                      } else {
                             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                      }
                      bos.flush();
               } catch (Exception e) {
                      e.printStackTrace();
               } finally {
                      if (bos != null) {
                           try {
                                bos.close();
                           } catch (IOException e) {
                                e.printStackTrace();
                           }
                      }
                }
                return true;
         } else {
               return false;
         }
    }

    // 从External Storage获取文件
    public byte[] loadFileFromExtStorage(String fileDir) {
         BufferedInputStream bis = null;
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         try {
               bis = new BufferedInputStream(new FileInputStream(new File(fileDir)));
               byte[] buffer = new byte[8 * 1024];
               int c = 0;
               while ((c = bis.read(buffer)) != -1) {
                    baos.write(buffer, 0, c);
                    baos.flush();
               }
               return baos.toByteArray();
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
         return null;
    }

    // 从External Storage中寻找指定目录下的文件，返回Bitmap
    public Bitmap loadBitmapFromExtStorage(String filePath) {
         byte[] data = loadFileFromExtStorage(filePath);
         if (data != null) {
              Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
              if (bm != null) {
                    return bm;
              }
         }
         return null;
    }

    public boolean isFileExist(String filePath) {
         File file = new File(filePath);
         return file.isFile();
    }

    // 从sdcard中删除文件
    public boolean removeFileFromExtStorage(String filePath) {
         File file = new File(filePath);
         if (file.exists()) {
              try {
                    file.delete();
                    return true;
              } catch (Exception e) {
                    return false;
              }
         } else {
              return false;
         }
    }

}