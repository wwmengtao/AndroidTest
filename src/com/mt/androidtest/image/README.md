http://blog.csdn.net/guolin_blog/article/details/9316683
һ�������ܿؼ���Ҫ���ǵ����⣺
1)����豸����Ϊÿ��Ӧ�ó����������ڴ棿
2)�豸��Ļ��һ���������ʾ������ͼƬ���ж���ͼƬ��Ҫ����Ԥ���أ���Ϊ�п��ܺܿ�Ҳ����ʾ����Ļ�ϣ�
3)����豸����Ļ��С�ͷֱ��ʷֱ��Ƕ��٣�һ�����߷ֱ��ʵ��豸������ Galaxy Nexus) ����һ���ϵͷֱ��ʵ��豸
������ Nexus S�����ڳ�����ͬ����ͼƬ��ʱ����Ҫ����Ļ���ռ䡣
4)ͼƬ�ĳߴ�ʹ�С������ÿ��ͼƬ��ռ�ݶ����ڴ�ռ䡣
5)ͼƬ�����ʵ�Ƶ���ж�ߣ��᲻����һЩͼƬ�ķ���Ƶ�ʱ�����ͼƬҪ�ߣ�����еĻ�����Ҳ��Ӧ����һЩͼƬ��פ���ڴ浱�У�
����ʹ�ö��LruCache ���������ֲ�ͬ���ͼƬ��
6)����ά�ֺ�����������֮���ƽ������Щʱ�򣬴洢��������ص�ͼƬ�����ں�̨ȥ���̼߳��ظ����ص�ͼƬ����ӵ���Ч��
��û��һ��ָ���Ļ����С�����������е�Ӧ�ó���������������ġ���Ӧ��ȥ���������ڴ��ʹ�������Ȼ���ƶ���һ�����ʵ�
���������һ��̫С�Ļ���ռ䣬�п������ͼƬƵ���ر��ͷź����¼��أ��Ⲣû�кô�����һ��̫��Ļ���ռ䣬
���п��ܻ��ǻ����� java.lang.OutOfMemory ���쳣��
����ImageLoader.java�е�һЩ����˼��
2.1��private final Map<String, ReentrantLock> uriLocks = Collections.synchronizedMap(new WeakHashMap<String, ReentrantLock>());
a)uriLocksʹ��Collections.synchronizedMap��ԭ��
	uriLocks�����ݻᱻ��ͬ�߳�put�Լ�get�������ͬ����ʹ��WeakHashMap�Ļ����������ԭ�����£�
	WeakHashMap�������̰߳�ȫ�ģ���Ҫͬ���������ͬ���Ļ��������WeakHashMap�ڲ���������ѭ������ʱ�����������̷߳�����һ����ѭ���¹�Ϊ��˵����
�¹ʷ�����ԭ�����ӡ��ʼǵġ����磺Java HashMap����ѭ������֮�����ܹ��γ���������������Ϊ�ڼ��ص�ͼƬURL���ǲ�ͬ������£��������߳���������࣬
WeakHashMap�����±괦<String, ReentrantLock>��ͻ���ʴ������Ӷ����WeakHashMap�ڲ���������ѭ����
���� һ���߳������� rehash()������ѭ������
while (entry != null) {
    int index = entry.isNull ? 0 : (entry.hash & 0x7FFFFFFF) % length;
    Entry<K, V> next = entry.next;
    entry.next = newData[index];
    newData[index] = entry;
    entry = next;
}
 ʣ�µ������߳�������get()����������forѭ����(��Ϊ���ʵ���һ������ѭ������)��
while (entry != null) {
    if (key.equals(entry.get())) {
        return entry.value;
    }
    entry = entry.next;
}
b)Collections.synchronizedMap��Ǳ��Σ��
	Collections.synchronizedMap�еķ�����������ͬ���� �����Ⲣ������������һ�����̰߳�ȫ�ġ���ĳЩʱ������һЩ���벻���Ľ����
��������δ��룺
// shm��Collections.synchronizedMap��һ��ʵ��
if(shm.containsKey(key)){   
        shm.remove(key);   
}
	���������������߳�A�ж��Ƿ����key֮������߳�Bɾ����key����ô�������߳�Aɾ��key�Ļ�������������������ڵ�ǰͼƬ��������²���
���ڣ���ΪͼƬ�����ǲ��ϵ�put(key,value)����ֻ�����ͼƬ��ַ��ͣ��ִ��uriLocks.put������
c)b�е�Collections.synchronizedMap���ڴ���������
	����ͼƬ���ع����е�ͼƬ�ǲ�������ģ��������ִ��uriLocks.put��uriLocks.get��������ִ��uriLocks.remove�Ļ����ڴ���uriLocks
ռ�õĿռ����Խ��Խ��ģ���˿��Կ����ں��ʵ�ʱ��ִ��uriLocks���ض�Ԫ��ɾ����������Ȼ�ˣ�����2.2��ɾ������������Ǳ��Σ�գ���˴�ʱ���Կ����ں��ʵ�
ʱ��ʹ��ConcurrentHashMap��ConcurrentHashMap���Ա���b������ĳ��֡����Կ�����ImageLoader.java��ͬΪCollections.synchronizedMap
��urlKeysForImageViews������ʾ�������֮��ִ��urlKeysForImageViews.remove(mImageView)��������ܡ�����uriLocks��ɾ���ǳ����Կ��ƣ�
��Ϊһ��ɾ���Ļ�����ͬ�ؼ�������ͬͼƬ��Դ�������û������(��������������ֵĸ��ʺ�С)��
d)ConcurrentHashMap��HashTable�ļ�����Ϊ֪�ʼǡ�Java����---ConcurrentHashMapԭ���������WeakHashMap����Ϊ֪�ʼǡ�WeakHashMap��ʵ��ԭ����


