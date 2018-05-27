package cn.novate.architect_day12;

import android.support.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/5/13 10:47
 * Version 1.0
 * Params:
 * Description:
*/

public class ThreadPoolTestException {

    static ThreadPoolExecutor threadPoolExecutor;

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(4);


    // Queue的参数：
    // BlockingQueue：先进先出的队列，FIFO，先进去的先执行（RxJava所使用的）
    // SynchronousQueue：线程安全的队列，它里边没有固定的缓存的（OkHttp所用的）
    // PriorityBlockingQueue：无序的、可以根据优先级进行排序，指定的对象要实现 Comparable 作比较  ，下边使用testRequest()方法来测试
    // 自己写一个 Request来实现 Runnable和Comparable，



    // sPoolWorkQueue 修改为4 以后就报错，原因就是：
    // RejectedExecutionException
    // 线程队列（缓存队列）是4，核心线程数是4，最大线程数是10，目前加入的 runnable 数量有20个
    // 20个runnable 放到缓存队列中，但是如果设置sPoolWorkQueue=4，表示缓存队列中只能放4个runnable，剩余的16个不能放下，
    // 这个时候最大线程数是10，非核心线程数 是6（最大线程数 - 核心线程数），这个时候会拿6个 runnable出来执行，此时就会在线程池中
    // 重新创建6个线程，线程池中的线程就达到10个，但是还有10个runnable不能放到 缓存队列中，就意味着 剩余的10个 runnable没有办法执行
    // 这个时候就会报异常
    static {
        threadPoolExecutor = new ThreadPoolExecutor(
                4,  // 核心线程数：就是线程池中的线程数量，自己图中画了 4个线程，这里就是4
                10, // 最大线程数：就是线程池中最大线程数量，随便给个10
                60, // 线程存活时间：比如线程1执行完下载任务，然后缓存队列中没有任务了，这个时候线程1的等待时间，如果等了60秒还没有下载任务，就销毁线程
                TimeUnit.SECONDS, // 线程存活时间的单位：秒
                sPoolWorkQueue,   // 线程的队列，就是图中的缓存队列，给队列中放 下载任务的个数，比如给缓存队列中放 4个下载任务
                new ThreadFactory() { // 线程的创建工厂，如果线程池需要创建线程，就调用这个new Thread()来创建
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        Thread thread = new Thread(r,"自己线程的名字");
                        thread.setDaemon(false); // 不是守护线程
                        return new Thread(r);
                    }
                });
    }



    public static void main(String[] args){


        /*// 原来写法：直接new Thread
        testThread() ;*/

//        testThreadPool();

        testRequest() ;

    }


    /**
     * 测试 Request
     */
    private static void testRequest() {
        for (int i = 0; i < 20; i++) {
            Request request = new Request() ;
            threadPoolExecutor.execute(request);
        }
    }

    private static void testThreadPool() {
        // 下边代码意思就是：
        //      一次性给 缓存队列中加入20个下载任务；
        //      一次性只会执行4个下载任务；执行5次，然后就等待60秒，如果60秒之内继续给 缓存队列中放下载任务，线程池的线程就继续下载
        //      如果60秒内没有下载任务，就会销毁这4个线程；
        //      如果过了一会，缓存队列中又有下载任务了，这个时候线程池又会重新创建线程，然后从缓存队列中取任务，然后下载
        for (int i = 0; i < 20; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("图片下载完毕" + Thread.currentThread().getName());

                }
            } ;

            // 这个方法不会立马执行，会先把runnable任务加入缓存队列，寻找合适的时机去执行
            threadPoolExecutor.execute(runnable);

        }
    }


    /**
     * 原来写法：直接new Thread
     */
    private static void testThread() {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
