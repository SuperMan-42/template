package com.recorder.app;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.integration.cache.BCache;
import com.core.utils.Constants;

/**
 * Created by hpw on 17-12-19.
 */
@Interceptor(priority = 7)
public class MyInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (!TextUtils.isEmpty(BCache.getInstance().getString(Constants.TOKEN))) {
            callback.onContinue(postcard);
        } else {
            callback.onInterrupt(null);
            ARouter.getInstance().build("/app/LoginActivity").withString(Constants.RETRY_WHEN_LOGIN_OR_AUTH, Constants.RETRY_MY).greenChannel().navigation();
        }
    }

    @Override
    public void init(Context context) {
    }
}
