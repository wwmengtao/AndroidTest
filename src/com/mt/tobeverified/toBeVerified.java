package com.mt.tobeverified;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class toBeVerified {
	
	/**
	 * bitmapRecycle:Bitmap Out Of Memory问题，报错的原因：bitmap size exceeds VM budget，Android虚拟机不允许单个程序中的Bitmap占用超过8M的内存，一旦超过了就会报错，
	而报的错正是bitmap size exceeds VM budget。要想程序的bitmap小于8M，要么就在用了bitmap后立即回收这部分内存，要么就压缩图片的大小。
	 */
	public void bitmapRecycle(){
		//第一种方法--及时回收bitmap内存：		一般而言，回收bitmap内存可以用到以下代码
		Bitmap bitmap=null;
		if(bitmap != null && !bitmap.isRecycled()){    
	        bitmap.recycle();    
	        bitmap = null;    
		}    
		System.gc();    
		//第二种方法--压缩图片：
		/*可以有两种方式：
		一种是使图片质量降低（分辨率不变），
		另一种是使图片分辨率降低（分辨率改变）。
		总之，使图片大小变小就行了。
		实践证明，使图片质量降低（分辨率不变）可以大幅度地减小体积，而且质量的差异肉眼看上去并不明显。
		当我使用了上述两种方法BUG依然还没解决的时候，我开始怀疑，bitmap超过8M会报错，可现在我把前前后后的bitmap都回收了，
		不可能还有8M了，那为什么还会报错呢？压缩，用于节省BITMAP内存空间--解决BUG的关键步骤    */
		 BitmapFactory.Options opts = new BitmapFactory.Options();    
		 opts.inSampleSize = 2;    //这个的值压缩的倍数（2的整数倍），数值越小，压缩率越小，图片越清晰    
		//返回原图解码之后的bitmap对象    
		// bitmap = BitmapFactory.decodeResource(Context, ResourcesId, opts);  
	}
}
