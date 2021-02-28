package com.zfliu.cache;

import com.zfliu.computable.Computable;
import com.zfliu.computable.ConsumeCompute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cache1<A,V> implements Computable<A,V> {

    private final Map<A,V> cache = new ConcurrentHashMap<>();

    private final Computable<A,V> c;

    public Cache1(Computable<A,V> c){
        this.c = c;
    }

    @Override
    public V compute(A arg) {
        System.out.println("进入缓存功能");
        V result = cache.get(arg);
        if (result == null){
            System.out.println("other place pulling.....");
            /**
             * 若两个线程进入这里，会重复计算，违背了缓存思想。。。。
             * */
            result = c.compute(arg);
            cache.put(arg,result);
        }
        return result;
    }

    /**
     * 演示多个线程:线程安全，但是同样的数据会在缓存中存放两次，未达到缓存效果
     * */
    public static void main(String[] args) {
        ConsumeCompute consumeCompute = new ConsumeCompute();
        Cache1<String, Integer> cache = new Cache1<String, Integer>(consumeCompute);
        /*ExecutorService threadPool = Executors.newFixedThreadPool(3);
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Integer result = cache.compute("6666");
                System.out.println("第一次计算结果为:" + result);
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Integer result = cache.compute("6666");
                System.out.println("第一次计算结果为:" + result);
            }
        });
        threadPool.submit(thread1);
        threadPool.submit(thread2);*/
        System.out.println("第一次："+cache.compute("6666"));
        System.out.println("第二次："+cache.compute("6666"));
    }
}
