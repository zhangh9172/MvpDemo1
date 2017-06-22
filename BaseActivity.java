package mvp.com.mvpdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.io.UnsupportedEncodingException;


@SuppressLint("DefaultLocale")
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends FragmentActivity implements OnClickListener {

    /**
     * Presenter
     */
    protected T mPresenter;
    /**
     * activity管理
     */
    protected AppManager appManager;

    protected Context mContext;
    protected boolean mEnableStatusColor = true;//是否启用状态栏
    protected int mStatusColor = 0xffffbe00;//状态栏颜色
    protected Activity mActivity;
    protected  MyApplication appLication;

    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
        mContext = MyApplication.getInstance();
        mActivity = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(arg0);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 将当前Activity加入栈管理
        appManager = AppManager.getAppManager();
        appManager.addActivity(this);

        appLication = (MyApplication) getApplicationContext();
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                appManager.finishActivity(this);
                return;
            }
        }
         //获取页面数据
        getIntentData();
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        //判断网络是否连接
        if (NetWorkUtil.isNetworkConnected(this)) {
            int normalView = getNormalLayout();
            if (normalView!=0)
                setContentView(getNormalLayout());
//            ButterKnife.bind(this);//绑定注解
            initView();
            setUpViewData();
        } else {
            setContentView(getErrLayout());
            setUpNoNetView();
        }

        setColor();
//        MiboLog.d("other:3",this.toString());
//        owner = (MBCharacterBean) arg0.getSerializable("owner");

    }

    /**
     * 子类得到布局文件，base类加载
     *
     * @return 布局id
     */
    public abstract int getNormalLayout();

    public abstract int getErrLayout();

    /**
     * 一开始没有网络
     */
    public  void setUpNoNetView(){

    }

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 设置初始/更新的数据
     */
    public abstract void setUpViewData();

    /**
     * 获取上个页面的数据
     */
    public abstract void getIntentData();


    @Override
    protected void onResume() {
        super.onResume();

        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */

    }


    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
        //if (mPresenter.isViewAttached())
//        ButterKnife.unbind(this);//解绑注解
//        setContentView(R.layout.view_null);
        appManager.finishActivity(this);
    }

    /**
     * 相应监听的方法
     *
     * @param view
     */
    public void setOnClick(View... view) {
        for (View v : view) {
            v.setOnClickListener(this);
        }
    }

    public boolean showEditToast(EditText... view) {
        boolean res = false;
        for (EditText v : view) {
            String strContent = v.getText().toString();
            if (strContent == null || strContent.equals("")) {
                String tag = (String) v.getTag();
                res = true;
                break;
            }
        }

        return res;
    }

    /**
     * 把unicode转换成GBK
     *
     * @param unicodeStr
     * @return
     */
    public String toGBK(String unicodeStr) {
        try {
            String gbkStr = new String(unicodeStr.getBytes("ISO8859-1"), "GBK");
            return gbkStr;
        } catch (UnsupportedEncodingException e) {
            return unicodeStr;
        }
    }

    /**
     * 初始化Presenter
     *
     * @return T
     */
    protected abstract T createPresenter();



    protected <T> T bindView(int id) {
        View view = findViewById(id);
        return (T) view;
    }

    protected <T> T bindView(View rootView, int id) {
        View view = rootView.findViewById(id);
        return (T) view;
    }

    private PopupWindow popupWindow;

    /**
     * 显示头像弹出框,工厂模式
     *
     * @param viewtemp  弹出布局
     * @param parent    父类view
     * @param animation 弹出动画
     * @param isDismiss 点击其他是否消失
     */
    public void ShowPopuptWindow(View viewtemp, View parent, int animation, boolean isDismiss) {

        if (null != popupWindow) {
            popupWindow.dismiss();
        }
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindow = new PopupWindow(viewtemp, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置动画效果
        popupWindow.setAnimationStyle(animation);
        // 点击其他地方消失
        if (isDismiss) {
            viewtemp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    return false;
                }
            });
        }
        //设置背景为透明，如果不设置在返回键里无法处理dismiss()方法
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    /**
     * popwindows退出
     */
    public void dissPopwindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * 设置状态栏颜色
     */
    public void setColor() {
        if (!mEnableStatusColor) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {
            // 设置状态栏透明
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(this, mStatusColor);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
            if (rootView==null)return;
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }
    /**
     * 生成一个和状态栏大小相同的矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }

    /**
     * @param activity
     * @return 判断当前手机是否是全屏
     */
    public static boolean isFullScreen(Activity activity) {
        int flag = activity.getWindow().getAttributes().flags;
        if((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return true;
        }else {
            return false;
        }
    }
}
