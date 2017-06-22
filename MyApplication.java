package mvp.com.mvpdemo;

import android.app.Application;

/**
 * 作者：lenovo on 2017/6/22 17:52
 * desc
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private MyApplication(){}

    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }
}
