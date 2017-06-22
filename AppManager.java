package mvp.com.mvpdemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * 
 * @author WuWenQiang
 */
public class AppManager {
	private static Stack<Activity> activityStack; // 保存activity的栈
	private static AppManager instance; // 当前类的实例

	private AppManager() {
	}

	/**
	 * 返回当前类的实例（单例模式）
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/***
	 * 获取指定类名的Activity实例
	 * @param cls
	 * @return
	 */
	public Activity getByActivity(Class<?> cls) {
		Activity activity = null;
		for (Activity a : activityStack) {
			if (a.getClass().equals(cls)) {
				activity = a;
			}
		}
		return activity;
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void appExit(Context context) {
		try {
			MyApplication appContext = (MyApplication) context
					.getApplicationContext();
			//appContext.Logout();
			finishAllActivity();
//	        Intent startIntent = new Intent(context, MYGpsService.class);
//	        context.stopService(startIntent);
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}
}