package com.zfliu.computable;
/**
 * description: 缓存里面功能（将字符串转换成数字）
 * */
public class ConsumeCompute implements Computable<String,Integer>{
    @Override
    public Integer compute(String arg) {
        /**
         * 模拟缓存时消耗时间
         * */
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return new Integer(arg);
    }
}
