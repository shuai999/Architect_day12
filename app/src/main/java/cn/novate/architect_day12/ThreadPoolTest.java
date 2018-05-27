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
 * Description:    线程池示例代码
*/

public class ThreadPoolTest {

    static ThreadPoolExecutor threadPoolExecutor;

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

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

        testThreadPool();

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
