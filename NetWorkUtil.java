package mvp.com.mvpdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @ClassName: NetWorkUtil
 * @Description: 网络判断帮助类
 * 
 */
public class NetWorkUtil {
	/**
	 * @Title: isNetworkConnected
	 * @Description: 判断WiFi网络
	 * @param @param context
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: isWifiNetWork
	 * @Description: 判断WiFi网络
	 * @param @param context
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isWifiNetWork(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		if (info.getState() == NetworkInfo.State.CONNECTED) { // 判断是不是在连接。。。
			if (info.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: isMobileNetWork
	 * @Description: 判断是否有网络
	 * @param @param context
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isMobileNetWork(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		if (info.getState() == NetworkInfo.State.CONNECTED) { // 判断是不是在连接。。。
			if (info.getType() == ConnectivityManager.TYPE_MOBILE)
				return true;
		}
		return false;
	}
}
