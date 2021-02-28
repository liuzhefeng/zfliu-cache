package com.zfliu.future;

import java.util.Random;
import java.util.concurrent.*;

public class FutureTask {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10));
        Future<Integer> future = executor.submit(new CallableTask());
        System.out.println(future.get());
        executor.shutdown();
    }

    static class CallableTask implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            Thread.sleep(3000L);
            /**
             * 0-10随机数
             * */
            return new Random().nextInt(11) - 1;
        }
    }
}
