package com.mt.tobeverified;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class toBeVerified {
	
	/**
	 * bitmapRecycle:Bitmap Out Of Memory���⣬�����ԭ��bitmap size exceeds VM budget��Android������������������е�Bitmapռ�ó���8M���ڴ棬һ�������˾ͻᱨ��
	�����Ĵ�����bitmap size exceeds VM budget��Ҫ������bitmapС��8M��Ҫô��������bitmap�����������ⲿ���ڴ棬Ҫô��ѹ��ͼƬ�Ĵ�С��
	 */
	public void bitmapRecycle(){
		//��һ�ַ���--��ʱ����bitmap�ڴ棺		һ����ԣ�����bitmap�ڴ�����õ����´���
		Bitmap bitmap=null;
		if(bitmap != null && !bitmap.isRecycled()){    
	        bitmap.recycle();    
	        bitmap = null;    
		}    
		System.gc();    
		//�ڶ��ַ���--ѹ��ͼƬ��
		/*���������ַ�ʽ��
		һ����ʹͼƬ�������ͣ��ֱ��ʲ��䣩��
		��һ����ʹͼƬ�ֱ��ʽ��ͣ��ֱ��ʸı䣩��
		��֮��ʹͼƬ��С��С�����ˡ�
		ʵ��֤����ʹͼƬ�������ͣ��ֱ��ʲ��䣩���Դ���ȵؼ�С��������������Ĳ������ۿ���ȥ�������ԡ�
		����ʹ�����������ַ���BUG��Ȼ��û�����ʱ���ҿ�ʼ���ɣ�bitmap����8M�ᱨ���������Ұ�ǰǰ����bitmap�������ˣ�
		�����ܻ���8M�ˣ���Ϊʲô���ᱨ���أ�ѹ�������ڽ�ʡBITMAP�ڴ�ռ�--���BUG�Ĺؼ�����    */
		 BitmapFactory.Options opts = new BitmapFactory.Options();    
		 opts.inSampleSize = 2;    //�����ֵѹ���ı�����2��������������ֵԽС��ѹ����ԽС��ͼƬԽ����    
		//����ԭͼ����֮���bitmap����    
		// bitmap = BitmapFactory.decodeResource(Context, ResourcesId, opts);  
	}
}
