/**
 * Created by JL on 4/9/17.
 */

import java.util.*;

class Benchmarking implements Runnable {
    private Thread t;
    private String threadName;
    public SkipTrie skipTrie;
    public int[] randVals;
    public long startTime;
    public long timeElapsed;
    public int numThreads;

    Benchmarking(String name){
        threadName = name;
        System.out.println("Creating " + threadName);
        numThreads++;
    }

    public void run(){
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {}

        randVals = new int[numThreads * 50000];
        skipTrie = new SkipTrie();

        // initialize array
        for (int j = 0; j < numThreads * 50000; j++) {
            randVals[j] = (int) Math.ceil(Math.random() * 100);
        }

        try{
            Thread.sleep(3000);
        }catch (InterruptedException e) {}

        // NUM_TREADS * 50000 insert operations
        System.out.println("INSERTING");
        startTime = System.nanoTime();
        for (int j = 0; j < numThreads * 50000; j++) {
            skipTrie.insert(randVals[j]);
        }
        timeElapsed = System.nanoTime() - startTime;
        System.out.println("INSERTIONS COMPLETE");
        System.out.println("With " + numThreads + " thread and " + (numThreads * 50000) + " insert operations, the time elapsed is: " + timeElapsed);

        try{
            Thread.sleep(5000);
        }catch (InterruptedException e) {}

        // NUM_THREADS * 50000 predecessor queries
        System.out.println("PREDECESSOR QUERIES");
        startTime = System.nanoTime();
        for (int j = 0; j < numThreads * 50000; j++) {
            skipTrie.predecessor(randVals[j]);
        }
        timeElapsed = System.nanoTime() - startTime;
        System.out.println("PREDECESSOR QUERIES COMPLETE");
        System.out.println("With " + numThreads + " thread and " + (numThreads * 50000) + " predecessor queries, the time elapsed is: " + timeElapsed);

        try{
            Thread.sleep(5000);
        }catch (InterruptedException e) {}

        // NUM_THREADS * 50000 delete operations
        System.out.println("DELETING");
        startTime = System.nanoTime();
        for (int j = 0; j < numThreads * 50000; j++) {
            skipTrie.delete(randVals[j]);
        }
        timeElapsed = System.nanoTime() - startTime;
        System.out.println("DELETIONS COMPLETE");
        System.out.println("With " + numThreads + " thread and " + (numThreads * 50000) + " delete operations, the time elapsed is: " + timeElapsed);

    }

    public void start(){
        System.out.println("Starting " + threadName);
        if(t == null){
            t = new Thread (this, threadName);
            t.start();
        }
    }
}

class go {
    public static void main(String args[]){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter # of threads. 1-4");
        int n = in.nextInt();
        if(n==1){
            Benchmarking T1 = new Benchmarking("T1");
            T1.start();
        }
        if(n==2){
            Benchmarking T1 = new Benchmarking("T1");
            Benchmarking T2 = new Benchmarking("T2");
            T1.start();
            T2.start();
        }
        if(n==3){
            Benchmarking T1 = new Benchmarking("T1");
            Benchmarking T2 = new Benchmarking("T2");
            Benchmarking T3 = new Benchmarking("T3");
            T1.start();
            T2.start();
            T3.start();
        }
        if(n==4){
            Benchmarking T1 = new Benchmarking("T1");
            Benchmarking T2 = new Benchmarking("T2");
            Benchmarking T3 = new Benchmarking("T3");
            Benchmarking T4 = new Benchmarking("T4");
            T1.start();
            T2.start();
            T3.start();
            T4.start();
        }
    }
}


