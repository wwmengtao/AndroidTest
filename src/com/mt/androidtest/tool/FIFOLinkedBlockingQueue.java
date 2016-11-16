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
	 * 1. add offer put��������̵߳����еķ���ֻ�ڶ�������ʱ��������addΪ���쳣��offer����booleanֵ��putֱ����ӳɹ�Ϊֹ��
	 * 2. remove poll take�����Ƴ��������̵߳ķ���ֻ�ڶ���Ϊ�յ�ʱ�������� removeΪ���쳣��pollΪ����booleanֵ�� take�ȴ�ֱ�����߳̿��Ա��Ƴ���
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
