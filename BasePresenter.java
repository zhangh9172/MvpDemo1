package mvp.com.mvpdemo;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by mac on 2016-04-11.
 * 基类主导器
 */
public abstract class BasePresenter<T>  implements NetMsgInterface{
    /**View接口类型的弱引用*/
    protected WeakReference<T> mViewRef; //View接口类型的弱引用
    protected WeakReference<T> mActivityRef;
    protected Context mContext ;
    protected T mView;
    public BasePresenter(Context context,T view){
        mContext = context;
        mView = view;
    }
    /**
     *建立关联
     * @param view
     */
    public void attachView(T view){
        mViewRef = new WeakReference<>(view); //建立关联
    }

    /**
     * 获取View
     * @return T
     */
    protected T getView(){
        return mViewRef.get(); //获取View
    }

    /**
     * 判断是否与View建立关联
     * @return boolean
     */
    public boolean isViewAttached(){
        return mViewRef != null && mViewRef.get() != null; //判断是否与View建立关联
    }

    /**
     * 解除关联
     */
    public void detachView(){
        if(mViewRef != null){
            mViewRef.clear(); //解除关联
            //mView = null;
            mViewRef = null;
        }
    }

    @Override
    public void handleReLogin() {
    }

    @Override
    public void handleServerException(int tag) {
        handleErrorMsg(tag);
    }

}
