package com.mt.androidtest.tool;

import static com.mt.androidtest.image.ImageLoader.IsLogRun;

import java.util.concurrent.LinkedBlockingQueue;

import com.mt.androidtest.ALog;

public class FIFOLinkedBlockingQueue<T> extends LinkedBlockingQueue<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7790586349451627687L;

	/**
	 * 1. add offer put三种添加线程到队列的方法只在队列满的时候有区别，add为抛异常，offer返回boolean值，put直到添加成功为止。
	 * 2. remove poll take三种移除队列中线程的方法只在队列为空的时候有区别， remove为抛异常，poll为返回boolean值， take等待直到有线程可以被移除。
	 */
	
	@Override
	public boolean offer(T e) {
		if(IsLogRun)ALog.Log1("FIFO: offer");
		return super.offer(e);
	}
	
	@Override
	public T take() throws InterruptedException{
		if(IsLogRun)ALog.Log1("FIFO: take");
		return super.take();
	}
}
