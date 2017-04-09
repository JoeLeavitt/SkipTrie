    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
    package concurrentskip;

    import java.util.concurrent.atomic.AtomicInteger;


    public class LockFreeHashSet<K,V> {
        protected BucketList<K,V>[] bucket;
        protected AtomicInteger bucketSize;
        protected AtomicInteger setSize;
        private static final double THRESHOLD = 4.0;


    public LockFreeHashSet(int capacity) {
        bucket = (BucketList<K,V>[]) new BucketList[capacity];
        bucket[0] = new BucketList<K,V>();
        bucketSize = new AtomicInteger(2);
        setSize = new AtomicInteger(0);
    }

    public boolean add(K p, V tn) {
        int myBucket = Math.abs(BucketList.hashCode(p) % bucketSize.get());
        BucketList<K,V> b = getBucketList(myBucket);
        if (!b.add(p,tn))
            return false;
        int setSizeNow = setSize.getAndIncrement();
        int bucketSizeNow = bucketSize.get();
        if (setSizeNow / (double)bucketSizeNow > THRESHOLD)
            bucketSize.compareAndSet(bucketSizeNow, 2 * bucketSizeNow);
        return true;
    }

    public boolean remove(K p) {
        int myBucket = Math.abs(BucketList.hashCode(p) % bucketSize.get());
        BucketList<K,V> b = getBucketList(myBucket);
        if (!b.remove(p)) {
            return false;		
        }
        return true;
    }
    
    public boolean contains(K p) {
        int myBucket = Math.abs(BucketList.hashCode(p) % bucketSize.get());
        BucketList<K,V> b = getBucketList(myBucket);
        return b.contains(p);
    }
    public BucketList.Node lookup(K p){
        int myBucket = Math.abs(BucketList.hashCode(p) % bucketSize.get());
        BucketList<K,V> b = getBucketList(myBucket);
        return b.containsN(p);
    }
    
    public void compareAndDelete(K p, SkipTrie.TrieNode n){
        int myBucket = Math.abs(BucketList.hashCode(p) % bucketSize.get());
        BucketList<K,V> b = getBucketList(myBucket);
        b.compareAndDelete(p, n);
    }
    
    
    private BucketList<K,V> getBucketList(int myBucket) {
        if (bucket[myBucket] == null)
          initializeBucket(myBucket);
        return bucket[myBucket];
    }
    
    private void initializeBucket(int myBucket) {
        int parent = getParent(myBucket);
        if (bucket[parent] == null)
          initializeBucket(parent);
        BucketList<K,V> b = bucket[parent].getSentinel(myBucket);
        if (b != null)
          bucket[myBucket] = b;
    }
    
    private int getParent(int myBucket){
        int parent = bucketSize.get();
        do {
            parent = parent >> 1;
        } while (parent > myBucket);
        parent = myBucket - parent;
        return parent;
        }
    }



