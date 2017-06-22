package mvp.com.mvpdemo;

import android.os.Bundle;

/**
 * Created by mac on 2016-04-11.
 */
public interface NetMsgInterface  {
    void  handleSuccessMsg(int tag, Bundle data);
    void  handleErrorMsg(int tag);
     void handleReLogin();
    void handleServerException(int tag);
}
