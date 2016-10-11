package com.mt.androidtest.tool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * �Զ����̳߳�
 * @author Mengtao1
 *
 */
public class ExecutorHelper {
	
	
	/**getExecutorService����ȡ��ͬ���͵��̳߳�
	 * 1��newFixedThreadPool() �� 
	���ã��÷�������һ���̶��߳��������̳߳أ����̳߳��е��߳�����ʼ�ղ��䣬�������ٴ����µ��̣߳�Ҳ���������Ѿ������õ��̣߳�
	��ʼ���ն����Ǽ����̶����߳��ڹ��������Ը��̳߳ؿ��Կ����̵߳���󲢷�����
	2��newCachedThreadPool() �� 
	���ã��÷�������һ�����Ը���ʵ����������̳߳����̵߳��������̳߳ء������̳߳��е��߳�������ȷ�����Ǹ���ʵ�������̬�����ġ� 
	���ӣ�������̳߳��е������̶߳����ڹ���������ʱ���������ύ����ô���ᴴ���µ��߳�ȥ��������񣬶���ʱ����֮ǰ��һЩ�߳�
	������������������������ύ����ô�����ᴴ�����߳�ȥ�������Ǹ��ÿ��е��߳�ȥ������������ô��ʱ�����������ˣ�
	��������˵���̳߳ص��߳����ǻ�Խ��Խ�ࣿ��ʵ�����ᣬ��Ϊ�̳߳��е��̶߳���һ�������ֻʱ�䡱�Ĳ�����ͨ����������
	����̳߳��еĿ����̵߳Ŀ���ʱ�䳬���á�����ʱ�䡱������ֹͣ���̣߳������̳߳�Ĭ�ϵġ����ֻʱ�䡱Ϊ60s��
	3��newSingleThreadExecutor() �� 
	���ã��÷�������һ��ֻ��һ���̵߳��̳߳أ���ÿ��ֻ��ִ��һ���߳����񣬶��������ᱣ�浽һ����������У��ȴ���һ���߳̿��У�������߳̿������ٰ�FIFO��ʽ˳��ִ����������е�����
	4��newScheduledThreadPool() �� 
	���ã��÷�������һ�����Կ����̳߳����̶߳�ʱ��������ִ��ĳ������̳߳ء�
	5��newSingleThreadScheduledExecutor() �� 
	���ã��÷�������һ�����Կ����̳߳����̶߳�ʱ��������ִ��ĳ������̳߳ء�ֻ����������������Ǹ��̳߳ش�СΪ1��������Ŀ���ָ���̳߳صĴ�С��
	 * @param type
	 * @return
	 */
	public ExecutorService getExecutorService(int type, int coreThreads){
		ExecutorService mExecutorService =null;
		switch(type){
			case 1:
				mExecutorService = Executors.newSingleThreadExecutor();
				break;
			case 2:
				if(coreThreads<=0)coreThreads=1;
				mExecutorService = Executors.newFixedThreadPool(coreThreads);
				break;
			case 3:
				mExecutorService = Executors.newCachedThreadPool();
				break;
			case 4:
				mExecutorService = Executors.newSingleThreadScheduledExecutor();
				break;
			case 5:
				if(coreThreads<=0)coreThreads=1;
				mExecutorService = Executors.newScheduledThreadPool(coreThreads);
				break;				
		}
		return mExecutorService;
	}
}
