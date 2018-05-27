package cn.novate.architect_day12;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/5/13 9:46
 * Version 1.0
 * Params:
 * Description:    模板设计模式
*/

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 设置布局
        setContentView() ;


        /**
         * 这个叫做：钩子
         *      可以达到子类控制父类，在父类中返回true，子某一个子类中返回false
         */
        if (flagTest()) {
            // 2. 初始化头部Title
            initTitle();
        }
        // 3. 初始化View
        initView() ;
        // 4. 访问接口数据
        initData(savedInstanceState) ;

    }

    protected boolean flagTest() {
        return true;
    }

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initView();

    protected abstract void initTitle();

    protected abstract void setContentView() ;

    public void startActivity(Class<? extends BaseActivity> clazz) {
        Intent intent =  new Intent(this,clazz);
        startActivity(intent);
    }
}
