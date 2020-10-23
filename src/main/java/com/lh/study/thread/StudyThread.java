package com.lh.study.thread;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.locks.*;
/**
 * @program: liu_hui
 * @description: 多线程方面的学习，关键概论
 * @author: Mr.Lhui
 * @create: 2020-07-22 16:39
 **/
public class StudyThread {
    /**
     * 1，说一说对于synchronized 关键字的了解？
         * synchronized 关键字解决的使多个线程之间访问资源的同步性，synchronized 关键字可以保证被他修饰的方法或代码块在任意时刻只能有一个线程执行
         * jdk 6 之后对锁的实现引入了大量的优化，如：自旋锁、适应性自旋锁、锁消除、锁粗化、偏向锁、轻量级锁等技术来减少锁操作的开销
         * synchronized 修时实例方法、修时静态方法、修时代码块
         * 总结：synchronized 关键字加到static 和synchronized(class)代码块上都是给class类上锁
         * 加到实例方法上是给对象实例上错。尽量不要使用synchronized（String a） 因为 JVM 中，字符串常量池具有缓存功能
     * <p>
     * 2，谈谈synchronized 和 ReentrantLock的区别？
         * 1，两者都是可重入锁，获取了锁之后，当前线程可以再次进入锁内部
         * 2，synchronized一来于JVM 而ReentrantLock依赖于API
         * 3，ReentrantLock 比 synchronized 增加了一些高级功能 ： 等待可终端、可实现公平锁、可实现选择性通知（锁可以绑定多个条件）、性能已不是选择标准
     * <p>
     * 3，volatile关键字
         * 指示JVM，这个变量不稳定的，每次使用它都到主存中进行读取，说白了，volatile关键字的主要作用就是保证变量的可见性然后还有一个作用是防止指令重排序
         * 并发编程的三个重要特性： 原子性、可见性、有序性
         * 说说synchronized 关键字和volatile关键字的区别
         * 两个是互补的存在，而不是对立的存在
         * volatile关键字是线程同步的轻量级实现，性能好点。但是volatile关键字只能用于变量而synchronized关键字可以修饰方法以及代码块
         * 多线程访问volatile关键字不会发生阻塞，而synchronized关键字可能会发生阻塞
         * volatile关键字能保证数据的可见性，但不能保证数据的原子性。synchronized 关键字两者都能保证
         * volatile关键字主要用于解决变量再多个线程之间的可见性，而synchronized关键字解决的是多个线程之间访问资源的同步性
     * <p>
     * 4，线程池
         * 4.1 为什么要用线程池
         * 降低资源消耗、提高响应速度、提高线程得可管理性。
         * 4.2实现Runnable接口和Callable接口得区别
         * Runnable接口不会返回结果或抛出检查异常，callable接口可以
         * 4.3 执行execute()方法和submit()方法得区别是什么？
         * execute()方法用于提交不需要返回值得任务，所以无法判断任务是否被线程池执行成功与否；
         * submit()方法用于提交需要返回值得任务，线程池会返回一个future类型得对象，通过这个future对象可以判断任务是否执行成功；并且可以通过future得get()方法来获取返回值；
         * 4.4如何创建线程池
         * 具体看本包下面的 ThreadPoolExecutorDemo
         *  ThreadPoolExecutor threadPoolExecutor =
         *             new ThreadPoolExecutor
         *                     (1,1,1, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
     *
     * 5，AQS介绍
     *      全称为（AbstractQueuedSynchronizer），这个类在java.util.concurrent.locks包下
     *      5.1,AQS原理概览
     *      如果被请求得共享资源空闲，则将当前请求资源得线程设置为悠闲得工作线程，并且将共享资源设置为锁定状态。
     *      如果被请求得共享资源被占用，那么就需要一套线程阻塞等待以及被唤醒时锁分配得机制，这个机制AQS是用CHL队列锁实现得，即将暂时获取不到锁得线程加入到队列中。
     *      5.2。AQS定义两种资源共享方式
     *          *Exclusive(独占)：只有一个线程能执行，如ReentrantLock。又可分为公平锁和非公平锁
     *          *Share（共享）：多个线程可同事执行，如Semaphore/CountDownLatch。Semaphore、CountDownLatch、 CyclicBarrier、ReadWriteLock
     *6，多线程带来得问题？
     *      内存泄漏、死锁、线程不安全等
     *
     * 7，线程得生命周期和状态
     *      1，(NEW)初始状态，线程被构建，还没有调用start（）方法
     *      2，(RUNNABLE)运行状态，
     *      3，(BLOCKED)阻塞状态，
     *      4，(WAITING)等待状态
     *      5，(TIME_WAITING)超时等待状态
     *      6，(TERMINATED)终止状态
     * 8,什么是上下文切换？
     *      当前任务在执行完CPU时间片切换到另一个任务之前会先保存自己得状态，以便下次再却换回这个任务时，可以再加载这个任务得状态。
     *      任务从保存到再加载得过程就是一次上下文切换。
     * 9，什么是线程死锁，如何避免线程死锁
     *      产生死锁四个必要条件：互斥条件，请求与保持条件，不可剥夺条件，循环与等待条件
     *      避免是破环产生条件中得其中一个
     *
     *10 Executor 框架结构（ 主要由三大部分组成）
     *      1），任务（Runnable/Callable）
     *          执行任务需要实现的 Runnable 接口 或 Callable接口。Runnable 接口或 Callable 接口 实现类都可以被 ThreadPoolExecutor 或 ScheduledThreadPoolExecutor 执行。
     *      2)，任务得执行（Executor）
     *          包括任务执行机制的核心接口 Executor ，以及继承自 Executor 接口的 ExecutorService 接口。ThreadPoolExecutor 和 ScheduledThreadPoolExecutor 这两个关键类实现了 ExecutorService 接口
     *      3），异步计算得结果（Future）
     *          Future 接口以及 Future 接口的实现类 FutureTask 类都可以代表异步计算的结果。
     *          当我们把 Runnable接口 或 Callable 接口 的实现类提交给 ThreadPoolExecutor 或 ScheduledThreadPoolExecutor 执行。（调用 submit() 方法时会返回一个 FutureTask 对象）
     *
     * 11何为乐观锁和悲观锁
     *      悲观锁：共享资源每次只给一个线程使用，其它线程阻塞，用完后再把资源转让给其它线程
     *      乐观锁：总是假设最好的情况，每次去拿数据的时候都认为别人不会修改，所以不会上锁，
         *      但是在更新的时候会判断一下在此期间别人有没有去更新这个数据，可以使用版本号机制和CAS算法实现。
         *      乐观锁适用于多读的应用类型，这样可以提高吞吐量，像数据库提供的类似于write_condition机制
         *      ，其实都是提供的乐观锁。在Java中java.util.concurrent.atomic包下面的原子变量类就是使用了乐观锁的一种实现方式CAS实现的。
     *      乐观锁中得CAS算法：
     *             即compare and swap（比较与交换），是一种有名的无锁算法。无锁编程，即不使用锁的情况下实现多线程之间的变量同步，
     *             也就是在没有线程被阻塞的情况下实现变量的同步，所以也叫非阻塞同步（Non-blocking Synchronization）。CAS算法涉及到三个操作数
                     * 需要读写的内存值 V
                     * 进行比较的值 A
                     * 拟写入的新值 B
     *          当且仅当 V 的值等于 A时，CAS通过原子方式用新值B来更新V的值，否则不会执行任何操作（比较和替换是一个原子操作）。
     *          一般情况下是一个自旋操作，即不断的重试。
     *
     * 12、导致线程阻塞的原因有哪些？
     *      1、调用join
     *      2、调用sleep
     *      3、调用yield
     *      4、改变线程的优先级
     *      5、将线程设置成守护线程（jvm中的垃圾回收线程）
     *
     * 13、线程池是什么时候创建线程的？
     *      任务提交的时候
     * 14、任务runnable task 是先放到core到maxthread之间的线程，还是先放到队列？
     *      先放到队列
     * 15、队列中的任务是什么时候取出来的？
     *      worker中runWorker（）一个任务完成后，会取下一个任务
     * 16、什么时候会触发reject策略？
     *      队列满并且maxthread也满了，还有新任务，默认策略是reject
     *
     */


    public static void main(String[] args) {
       //跳表是一种利用空间换时间的算法。
        //        使用跳表实现 Map 和使用哈希算法实现 Map 的另外一个不同之处是：哈希并不会保存元素的顺序，而跳表内所有的元素都是排序的。
        //        因此在对跳表进行遍历时，你会得到一个有序的结果。所以，如果你的应用需要有序性，那么跳表就是你不二的选择。
        //        JDK 中实现这一数据结构的类是 ConcurrentSkipListMap
        ConcurrentSkipListMap<String,String> concurrentSkipListMap = new ConcurrentSkipListMap();
        concurrentSkipListMap.put("1", "刘辉");
        concurrentSkipListMap.put("2", "傻逼钊");
        concurrentSkipListMap.forEach((key,value)->{
            System.out.println(key+value);
        });



//        在 ConcurrentHashMap 中，无论是读操作还是写操作都能保证很高的性能：在进行读操作时(几乎)不需要加锁，
//        而在写操作时通过锁分段技术只对所操作的段加锁而不影响客户端对其它段的访问。
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("1", "刘辉");
        map.put("2", "傻逼钊");
        map.forEach((key,value)->{
            System.out.println(key+value);
        });
        char a='我';
        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.add("hahah");
        copyOnWriteArrayList.add("1111");
        copyOnWriteArrayList.stream().forEach(s -> {
            System.out.println(s);
        });







    }



}

