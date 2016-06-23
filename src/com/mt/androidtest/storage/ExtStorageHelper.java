package com.mt.androidtest.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.mt.androidtest.ALog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

/**
 * External Storage����������
 * @author http://www.androidchina.net/4106.html
 *
 */
public class ExtStorageHelper {
	 
	/* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
    	return Environment.getExternalStorageState().equals(
    			Environment.MEDIA_MOUNTED);
   }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
        		Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

   // ��ȡExternal Storage�������ռ��С������MB
   /**
    * getBlockCountLong()����ȡ�ļ�ϵͳ�У��ܿ���
    * getBlockSizeLong()����ȡ�ļ�ϵͳ�У�ÿ��Ĵ�С(�ֽ���)
    * @return
    */
   public static long getExtStorageSize() {
        if (isExternalStorageReadable()) {
             StatFs fs = new StatFs(getExternalStorageDirectory().getAbsolutePath());
             long count = fs.getBlockCountLong();
             long size = fs.getBlockSizeLong();
             return count * size / 1024 / 1024;
        }
        return 0;
   }

   // ��ȡExternal Storage��ʣ��ռ��С������MB
   public static long getExtStorageFreeSize() {
        if (isExternalStorageReadable()) {
              StatFs fs = new StatFs(getExternalStorageDirectory().getAbsolutePath());
              long count = fs.getFreeBlocksLong();
              long size = fs.getBlockSizeLong();
              return count * size / 1024 / 1024;
        }
        return 0;
   }

   // ��ȡExternal Storage�Ŀ��ÿռ��С������MB
   public static long getExtStorageAvailableSize() {
        if (isExternalStorageReadable()) {
              StatFs fs = new StatFs(getExternalStorageDirectory().getAbsolutePath());
              long count = fs.getAvailableBlocksLong();
              long size = fs.getBlockSizeLong();
              return count * size / 1024 / 1024;
        }
        return 0;
   }

   // ��ȡExternal Storage�ĸ�Ŀ¼
   public static File getExternalStorageDirectory() {
        if (isExternalStorageReadable()) {
              return Environment.getExternalStorageDirectory();
        }
        return null;
   }
   
   // ��ȡExternal Storage����Ŀ¼
	public static File getExternalStoragePublicDirectory(String type) {
	   if (isExternalStorageReadable()) {
		   return Environment.getExternalStoragePublicDirectory(type);
	   }
	   return null;
	}

   // ��ȡExternal Storage˽��CacheĿ¼��·��
   public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
   }

   // ��ȡExternal Storage˽��FilesĿ¼��·��
   public static File getExternalFilesDir(Context context, String type) {
        return context.getExternalFilesDir(type);
   }
   
   /**
    *Android6.0֮��Environment.getExternalStorageDirectoryĿ¼�³���˽�����ݴ洢��(Android/data/������
    *��cache��filesĿ¼)�⣬д��������������Ҫ��̬����WRITE_EXTERNAL_STORAGEȨ��
    */
	public static File saveFileToExtStoragePublicDir(byte[] data, String type, String fileName) {
		File dir = getExternalStoragePublicDirectory(type);
    	return saveFile(data,dir,fileName);
	}

    public static File saveFileToExtStorageCustomDir(byte[] data, String dir_custome, String fileName) {
        File dir = new File(getExternalStorageDirectory(),dir_custome);
    	return saveFile(data,dir,fileName);

    }

    // ��External Storage��˽��FilesĿ¼�±����ļ�
    public static File saveFileToExtStoragePrivateFilesDir(byte[] data, String type, String fileName, Context context) {
        File dir = context.getExternalFilesDir(type);
    	return saveFile(data,dir,fileName);
    }

    // ��External Storage��˽��CacheĿ¼�±����ļ�
    public static File saveFileToExtStoragePrivateCacheDir(byte[] data, String fileName, Context context) {
    	File dir = context.getExternalCacheDir();
    	return saveFile(data,dir,fileName);
    }

    public static File saveFile(byte[] data,File dir,String fileName){
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
	    		ALog.Log("ExternalStorage is writable!");
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
    
    // ����bitmapͼƬ��External Storage��˽��CacheĿ¼
    public static boolean saveBitmapToExtStoragePrivateCacheDir(Bitmap bitmap, String fileName, Context context) {
         if (isExternalStorageWritable()) {
               BufferedOutputStream bos = null;
               // ��ȡ˽�е�Cache����Ŀ¼
               File file = context.getExternalCacheDir();
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

    // ��External Storage��ȡ�ļ�
    public static byte[] loadFileFromExtStorage(String fileDir) {
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

    // ��External Storage��Ѱ��ָ��Ŀ¼�µ��ļ�������Bitmap
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

    public static boolean isFileExist(String filePath) {
         File file = new File(filePath);
         return file.isFile();
    }

    // ��sdcard��ɾ���ļ�
    public static boolean removeFileFromExtStorage(String filePath) {
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