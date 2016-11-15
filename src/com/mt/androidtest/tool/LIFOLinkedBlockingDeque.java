package com.mt.androidtest.tool;

import java.util.concurrent.LinkedBlockingDeque;

import com.mt.androidtest.ALog;


public class LIFOLinkedBlockingDeque<T> extends LinkedBlockingDeque<T> {

	private static final long serialVersionUID = -4114786347960826192L;

	/**
	 * 1. add offer put��������̵߳����еķ���ֻ�ڶ�������ʱ��������addΪ���쳣��offer����booleanֵ��putֱ����ӳɹ�Ϊֹ��
	 * 2. remove poll take�����Ƴ��������̵߳ķ���ֻ�ڶ���Ϊ�յ�ʱ�������� removeΪ���쳣��pollΪ����booleanֵ�� take�ȴ�ֱ�����߳̿��Ա��Ƴ���
	 */
	
	@Override
	public boolean offer(T e) {
		ALog.Log1("LIFO: offer");
		return super.offerFirst(e);
	}
	
	@Override
	public T take() throws InterruptedException {
		ALog.Log1("LIFO: take");
		return super.take();
	}
}