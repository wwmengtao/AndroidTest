package com.mt.androidtest.tool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.mt.androidtest.image.PicConstants.Type;

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
	public static ExecutorService getExecutorService(int type, int coreThreads){
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
	
	public static Executor createTaskDistributor() {
		return Executors.newCachedThreadPool(createThreadFactory(Thread.NORM_PRIORITY, "uil-pool-d-"));
	}
	
	public static Executor createTaskDistributor2(int coreThreads) {
		return Executors.newFixedThreadPool(coreThreads);
	}
	
	/** Creates default implementation of task executor */
	public static Executor createExecutor(int threadPoolSize, int threadPriority,
			Type tasksProcessingType) {
		boolean lifo = tasksProcessingType == Type.LIFO;
		BlockingQueue<Runnable> taskQueue =
				lifo ? new LIFOLinkedBlockingDeque<Runnable>() : new FIFOLinkedBlockingQueue<Runnable>();
		return new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, taskQueue,
				createThreadFactory(threadPriority, "uil-pool-"));
	}
	
	/** Creates default implementation of {@linkplain ThreadFactory thread factory} for task executor */
	private static ThreadFactory createThreadFactory(int threadPriority, String threadNamePrefix) {
		return new DefaultThreadFactory(threadPriority, threadNamePrefix);
	}

	private static class DefaultThreadFactory implements ThreadFactory {

		private static final AtomicInteger poolNumber = new AtomicInteger(1);

		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;
		private final int threadPriority;

		DefaultThreadFactory(int threadPriority, String threadNamePrefix) {
			this.threadPriority = threadPriority;
			group = Thread.currentThread().getThreadGroup();
			namePrefix = threadNamePrefix + poolNumber.getAndIncrement() + "-thread-";
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon()) t.setDaemon(false);
			t.setPriority(threadPriority);
			return t;
		}
	}
}
