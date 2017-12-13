package com.mt.androidtest.tool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.mt.androidtest.ALog;
import com.mt.androidtest.image.PicConstants.Type;
import static com.mt.androidtest.image.ImageLoader.IsLogRun;
/**
 * 自定义线程池
 * @author Mengtao1
 *
 */
public class ExecutorHelper {
	
	
	/**getExecutorService：获取不同类型的线程池
	 * 1、newFixedThreadPool() ： 
	作用：该方法返回一个固定线程数量的线程池，该线程池中的线程数量始终不变，即不会再创建新的线程，也不会销毁已经创建好的线程，
	自始自终都是那几个固定的线程在工作，所以该线程池可以控制线程的最大并发数。
	2、newCachedThreadPool() ： 
	作用：该方法返回一个可以根据实际情况调整线程池中线程的数量的线程池。即该线程池中的线程数量不确定，是根据实际情况动态调整的。 
	例子：假如该线程池中的所有线程都正在工作，而此时有新任务提交，那么将会创建新的线程去处理该任务，而此时假如之前有一些线程
	完成了任务，现在又有新任务提交，那么将不会创建新线程去处理，而是复用空闲的线程去处理新任务。那么此时有人有疑问了，
	那这样来说该线程池的线程岂不是会越集越多？其实并不会，因为线程池中的线程都有一个“保持活动时间”的参数，通过配置它，
	如果线程池中的空闲线程的空闲时间超过该“保存活动时间”则立刻停止该线程，而该线程池默认的“保持活动时间”为60s。
	3、newSingleThreadExecutor() ： 
	作用：该方法返回一个只有一个线程的线程池，即每次只能执行一个线程任务，多余的任务会保存到一个任务队列中，等待这一个线程空闲，当这个线程空闲了再按FIFO方式顺序执行任务队列中的任务。
	4、newScheduledThreadPool() ： 
	作用：该方法返回一个可以控制线程池内线程定时或周期性执行某任务的线程池。
	5、newSingleThreadScheduledExecutor() ： 
	作用：该方法返回一个可以控制线程池内线程定时或周期性执行某任务的线程池。只不过和上面的区别是该线程池大小为1，而上面的可以指定线程池的大小。
	6、4和5中ScheduledExecutorService的使用说明
	1)scheduleWithFixedDelay：受任务执行时间的影响，要等到上一个任务结束，等待固定时间间隔后下一个任务执行。
	2)scheduleAtFixedRate：不受任务执行时间的影响，只要上一个任务开始，等待固定时间间隔后下一个任务执行。
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
		return Executors.newCachedThreadPool(createThreadFactory(Thread.NORM_PRIORITY, "pool-D-"));
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
				createThreadFactory(threadPriority, "pool-L-"));
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

		/**
		 * 下列newThread生成的线程执行Thread.currentThread()会显示例如下列所示信息：
		 * Thread[uil-pool-d-2-thread-1,5,main]
		 * 解释：中括号里的第一个值(uil-pool-d-2-thread-1)为当前主线程的名字(可以自己定义)，第二个为线程级别，第三个为线程组
		 */
		
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon()) t.setDaemon(false);
			t.setPriority(threadPriority);
//			if(IsLogRun)ALog.Log1("newThread："+t.toString());
			return t;
		}
	}
}
