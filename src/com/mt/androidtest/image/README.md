http://blog.csdn.net/guolin_blog/article/details/9316683
一、高性能控件需要考虑的问题：
1)你的设备可以为每个应用程序分配多大的内存？
2)设备屏幕上一次最多能显示多少张图片？有多少图片需要进行预加载，因为有可能很快也会显示在屏幕上？
3)你的设备的屏幕大小和分辨率分别是多少？一个超高分辨率的设备（例如 Galaxy Nexus) 比起一个较低分辨率的设备
（例如 Nexus S），在持有相同数量图片的时候，需要更大的缓存空间。
4)图片的尺寸和大小，还有每张图片会占据多少内存空间。
5)图片被访问的频率有多高？会不会有一些图片的访问频率比其它图片要高？如果有的话，你也许应该让一些图片常驻在内存当中，
或者使用多个LruCache 对象来区分不同组的图片。
6)你能维持好数量和质量之间的平衡吗？有些时候，存储多个低像素的图片，而在后台去开线程加载高像素的图片会更加的有效。
并没有一个指定的缓存大小可以满足所有的应用程序，这是由你决定的。你应该去分析程序内存的使用情况，然后制定出一个合适的
解决方案。一个太小的缓存空间，有可能造成图片频繁地被释放和重新加载，这并没有好处。而一个太大的缓存空间，
则有可能还是会引起 java.lang.OutOfMemory 的异常。
二、ImageLoader.java中的一些问题思考
2.1、private final Map<String, ReentrantLock> uriLocks = Collections.synchronizedMap(new WeakHashMap<String, ReentrantLock>());
a)uriLocks使用Collections.synchronizedMap的原因
	uriLocks的数据会被不同线程put以及get，如果不同步而使用WeakHashMap的话会出错，错误原因如下：
	WeakHashMap：不是线程安全的，需要同步，如果不同步的话，会出现WeakHashMap内部的链表死循环。此时以三个工作线程发生的一次死循环事故为例说明，
事故发生的原因参照印象笔记的“疫苗：Java HashMap的死循环”。之所以能够形成这样的条件是因为在加载的图片URL都是不同的情况下，会随着线程增多而增多，
WeakHashMap数组下标处<String, ReentrantLock>冲突概率大增，从而造成WeakHashMap内部的链表死循环。
其中 一个线程死在了 rehash()的下列循环处：
while (entry != null) {
    int index = entry.isNull ? 0 : (entry.hash & 0x7FFFFFFF) % length;
    Entry<K, V> next = entry.next;
    entry.next = newData[index];
    newData[index] = entry;
    entry = next;
}
 剩下的两个线程死在了get()函数的下列for循环处(因为访问的是一个无限循环链表)：
while (entry != null) {
    if (key.equals(entry.get())) {
        return entry.value;
    }
    entry = entry.next;
}
b)Collections.synchronizedMap的潜在危险
	Collections.synchronizedMap中的方法都进行了同步， 但是这并不等于这个类就一定是线程安全的。在某些时候会出现一些意想不到的结果。
如下面这段代码：
// shm是Collections.synchronizedMap的一个实例
if(shm.containsKey(key)){   
        shm.remove(key);   
}
	上述代码描述了线程A判断是否包含key之后，如果线程B删除了key，那么接下来线程A删除key的话会出错。但是这种情形在当前图片加载情况下并不
存在，因为图片加载是不断的put(key,value)，即只是针对图片地址不停的执行uriLocks.put操作。
c)b中的Collections.synchronizedMap的内存性能问题
	由于图片加载过程中的图片是不断增多的，如果仅仅执行uriLocks.put和uriLocks.get操作而不执行uriLocks.remove的话，内存中uriLocks
占用的空间会是越来越大的，因此可以考虑在合适的时机执行uriLocks中特定元素删除动作。当然了，根据2.2，删除动作会引发潜在危险，因此此时可以考虑在合适的
时候使用ConcurrentHashMap。ConcurrentHashMap可以避免b中问题的出现。可以看到，ImageLoader.java中同为Collections.synchronizedMap
的urlKeysForImageViews就在显示任务完毕之后执行urlKeysForImageViews.remove(mImageView)以提高性能。但是uriLocks的删除非常难以控制，
因为一旦删除的话，不同控件下载相同图片资源的情况就没法控制(尽管这种情况出现的概率很小)。
d)ConcurrentHashMap及HashTable的简介参照为知笔记“Java集合---ConcurrentHashMap原理分析”；WeakHashMap简介见为知笔记“WeakHashMap的实现原理”。


