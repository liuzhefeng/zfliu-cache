package com.zfliu.cache;

import com.zfliu.computable.Computable;
import com.zfliu.computable.ConsumeCompute;

import java.util.Map;
import java.util.concurrent.*;


public class Cache2<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> map = new ConcurrentHashMap<>();

    private final Computable<A, V> c;

    public Cache2(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) {
        /**
         * 避免重复计算
         * */
        Future<V> future = map.get(arg);
        if (future == null) {
            Callable<V> callable = new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return c.compute(arg);
                }
            };
            FutureTask task = new FutureTask(callable);
            future = map.putIfAbsent(arg, task);
            if (future == null) {
                System.out.println("第一次存入，存入数据");
                future = task;
                task.run();
            }
        }
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("运行时抛出异常。。。。。。");
        }
    }

    public static void main(String[] args) {
        Cache2<String, Integer> cache2 = new Cache2(new ConsumeCompute());
        ExecutorService pool = Executors.newFixedThreadPool(10);
        pool.submit((() -> {
            try {
                Integer result = cache2.compute("666");
                System.out.println("第一次的计算结果：" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        pool.submit((new Runnable() {
            @Override
            public void run() {
                try {
                    Integer result = cache2.compute("666");
                    System.out.println("第二次的计算结果：" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        pool.submit((new Runnable() {
            @Override
            public void run() {
                try {
                    Integer result = cache2.compute("777");
                    System.out.println("第三次的计算结果：" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        pool.shutdown();
    }

}

class Task implements Runnable {
    @Override
    public void run() {

    }
}