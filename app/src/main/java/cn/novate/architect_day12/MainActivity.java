package cn.novate.architect_day12;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends BaseActivity {


    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void setContentView() {

    }


    /**
     * 钩子：达到子类可以控制父类，子类返回false，父类返回true
     */
    @Override
    protected boolean flagTest() {
        return false;
    }
}
