package cn.novate.architect_day12;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/5/13 11:43
 * Version 1.0
 * Params:
 * Description:    测试PriorityBlockingQueue ，自己定义的 Request对象，然后实现 Runnable,Comparable接口
*/

public class Request  implements Runnable,Comparable<Request>{
    @Override
    public void run() {
        System.out.println("run");
    }

    /**
     * 用来处理排序的
     * 这里只可以返回 <0、=0、>0
     */
    @Override
    public int compareTo(@NonNull Request o) {
        return 0;
    }
}
