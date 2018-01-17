package com.recorder.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.widget.TextView;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.CoreUtils;
import com.core.utils.RxLifecycleUtils;
import com.core.widget.CustomPopupWindow;
import com.recorder.R;
import com.recorder.mvp.contract.BuyIntentionContract;

import org.json.JSONObject;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

@ActivityScope
public class BuyIntentionPresenter extends BasePresenter<BuyIntentionContract.Model, BuyIntentionContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public BuyIntentionPresenter(BuyIntentionContract.Model model, BuyIntentionContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void orderIntention(String dealID, String amount) {
        mModel.orderIntention(dealID, amount)
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(data -> {
                    if (new JSONObject(data.toString()).optInt("errno") == 0) {
                        //处理中的情况
                        CustomPopupWindow window = CustomPopupWindow.builder().contentView(CoreUtils.inflate((Activity) mRootView, R.layout.layout_dialog_one_button)).isOutsideTouch(false)
                                .customListener(contentView -> {
                                    ((TextView) contentView.findViewById(R.id.tv_title)).setText("提示");
                                    ((TextView) contentView.findViewById(R.id.tv_content)).setText("您已提交投资意向，版若云的投资经理会尽快和您取得联系");
                                    ((TextView) contentView.findViewById(R.id.tv_sure)).setText("确定");
                                    contentView.findViewById(R.id.tv_sure).setOnClickListener(view -> {
                                        CustomPopupWindow.killMySelf();
                                    });

                                }).build();
                        window.setOnDismissListener(() -> mRootView.killMyself());
                        window.show();
                    }
                });
    }
}
