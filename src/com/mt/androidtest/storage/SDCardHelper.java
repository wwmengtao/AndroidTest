package com.mt.androidtest.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mt.androidtest.ALog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

/**
 * SD������������
 * @author http://www.androidchina.net/4106.html
 *
 */
public class SDCardHelper {
	 
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

   // ��ȡSD���������ռ��С������MB
   /**
    * getBlockCountLong()����ȡ�ļ�ϵͳ�У��ܿ���
    * getBlockSizeLong()����ȡ�ļ�ϵͳ�У�ÿ��Ĵ�С(�ֽ���)
    * @return
    */
   public static long getSDCardSize() {
        if (isExternalStorageReadable()) {
             StatFs fs = new StatFs(getSDCardBaseDir());
             long count = fs.getBlockCountLong();
             long size = fs.getBlockSizeLong();
             return count * size / 1024 / 1024;
        }
        return 0;
   }

   // ��ȡSD����ʣ��ռ��С������MB
   public static long getSDCardFreeSize() {
        if (isExternalStorageReadable()) {
              StatFs fs = new StatFs(getSDCardBaseDir());
              long count = fs.getFreeBlocksLong();
              long size = fs.getBlockSizeLong();
              return count * size / 1024 / 1024;
        }
        return 0;
   }

   // ��ȡSD���Ŀ��ÿռ��С������MB
   public static long getSDCardAvailableSize() {
        if (isExternalStorageReadable()) {
              StatFs fs = new StatFs(getSDCardBaseDir());
              long count = fs.getAvailableBlocksLong();
              long size = fs.getBlockSizeLong();
              return count * size / 1024 / 1024;
        }
        return 0;
   }

   // ��ȡSD���ĸ�Ŀ¼
   public static String getSDCardBaseDir() {
        if (isExternalStorageReadable()) {
              return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
   }
   
   // ��ȡSD������Ŀ¼��·��
   public static String getSDCardPublicDir(String type) {
        return Environment.getExternalStoragePublicDirectory(type).toString();
   }

   // ��ȡSD��˽��CacheĿ¼��·��
   public static String getSDCardPrivateCacheDir(Context context) {
        return context.getExternalCacheDir().getAbsolutePath();
   }

   // ��ȡSD��˽��FilesĿ¼��·��
   public static String getSDCardPrivateFilesDir(Context context, String type) {
        return context.getExternalFilesDir(type).getAbsolutePath();
   }
   
   // ��SD���Ĺ���Ŀ¼�±����ļ�
	public static File saveFileToSDCardPublicDir(byte[] data, String type, String fileName) {
		File file = Environment.getExternalStoragePublicDirectory(type);
		file = new File(file, fileName);
        return saveFile(data,file);
	}

    // ��SD�����Զ���Ŀ¼�±����ļ�
    public static File saveFileToSDCardCustomDir(byte[] data, String dir, String fileName) {
        File file = new File(getSDCardBaseDir() + File.separator + dir);
        file = new File(file, fileName);
        return saveFile(data,file);

    }

    // ��SD����˽��FilesĿ¼�±����ļ�
    public static File saveFileToSDCardPrivateFilesDir(byte[] data, String type, String fileName, Context context) {
        File file = context.getExternalFilesDir(type);
        file = new File(file, fileName);
        return saveFile(data,file);
    }

    // ��SD����˽��CacheĿ¼�±����ļ�
    public static File saveFileToSDCardPrivateCacheDir(byte[] data, String fileName, Context context) {
    	File file = context.getExternalCacheDir();
    	file = new File(file, fileName);
    	return saveFile(data,file);
    }

    public static File saveFile(byte[] data,File file){
		boolean savedSucceed=false;
    	if(null!=file){
	    	BufferedOutputStream bos = null;
	    	if (isExternalStorageWritable()) {
		        try {
		              bos = new BufferedOutputStream(new FileOutputStream(file));
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
        ALog.Log("File saved:"+file.getAbsolutePath()+" Status:"+savedSucceed);
        if(!savedSucceed){
        	return null;
        }
        return file;
	}
    
    // ����bitmapͼƬ��SDCard��˽��CacheĿ¼
    public static boolean saveBitmapToSDCardPrivateCacheDir(Bitmap bitmap, String fileName, Context context) {
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

    // ��SD����ȡ�ļ�
    public static byte[] loadFileFromSDCard(String fileDir) {
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

    // ��SDCard��Ѱ��ָ��Ŀ¼�µ��ļ�������Bitmap
    public Bitmap loadBitmapFromSDCard(String filePath) {
         byte[] data = loadFileFromSDCard(filePath);
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
    public static boolean removeFileFromSDCard(String filePath) {
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