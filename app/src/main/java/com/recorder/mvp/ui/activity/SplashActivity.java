package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.jaeger.library.StatusBarUtil;
import com.recorder.R;
import com.recorder.di.component.DaggerSplashComponent;
import com.recorder.di.module.SplashModule;
import com.recorder.mvp.contract.SplashContract;
import com.recorder.mvp.model.entity.AppStartBean;
import com.recorder.mvp.presenter.SplashPresenter;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {

    @BindView(R.id.im_splash)
    ImageView imSplash;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSplashComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        return R.layout.activity_splash; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mPresenter.appStart();
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        CoreUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        CoreUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
        overridePendingTransition(R.anim.empty, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void showAppStart(AppStartBean.DataEntity data) {
        CoreUtils.imgLoader(this, data.getSp_img(), imSplash);
    }
}
