package com.lh.study.thread;

import java.util.concurrent.*;

/**
 * @program: liu_hui
 * @description:
 * @author: Mr.Lhui
 * @create: 2020-07-22 20:55
 **/
public class ThreadPoolExecutorDemo {

    /**
     * 可以看到我们上面的代码指定了：
         * corePoolSize: 核心线程数为 5。
         * maximumPoolSize ：最大线程数 10
         * keepAliveTime : 等待时间为 1L。
         * unit: 等待时间的单位为 TimeUnit.SECONDS。
         * workQueue：任务队列为 ArrayBlockingQueue，并且容量为 100;
         * handler:饱和策略为 CallerRunsPolicy。
     */
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 5L;
    public static void main(String[] args) {

        //使用阿里巴巴推荐的创建线程池的方式
        //通过ThreadPoolExecutor构造函数自定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 10; i++) {
            //创建WorkerThread对象（WorkerThread类实现了Runnable 接口）
            Runnable worker = new MyRunnable("" + i);
            //执行Runnable
            executor.execute(worker);
        }
        //终止线程池
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(7);
        //-------------------------------第二种方式  --开始---------------------------
        //创建线程池大小
//        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
//        for(int i=0;i<5;i++){
//            scheduledThreadPool.scheduleAtFixedRate(new QueueThread(i+1), //Thread子类方法
//                    10, //启动延迟时间
//                    10, //定时时间
//                    TimeUnit.SECONDS);
//        }

        //-------------------------------第二种方式  --结束---------------------------



    }
}

