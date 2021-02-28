package com.zfliu.threadlocal;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadLocal:打印时间保证线程安全
 */
public class ThreadLoaclUsage {
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int fianlI = i;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    String date = new ThreadLoaclUsage().date(fianlI);
                    System.out.println(date);
                }
            });
        }
        threadPool.shutdown();
    }
/**
 * 方法一
 * */
   /* private String date(int i) {
        LocalDateTime time = LocalDateTime.of(2021, 12, 12, 12, i / 60, i % 60);
        */

    /**
     * 并发下会出现timeString被覆盖
     * *//*
        String timeString = null;
        synchronized (this) {
            timeString = time.format(formatter);
        }
        return timeString;
    }*/
    public String date(int second) {
        Date date = new Date(1000 * second);
        return ThreadLocalFormatter.dateTimeThreadLocal.get().format(date);

    }
}

class ThreadLocalFormatter {
    public static ThreadLocal<SimpleDateFormat> dateTimeThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
}