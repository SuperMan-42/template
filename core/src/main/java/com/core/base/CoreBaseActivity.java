package com.core.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.core.AppManager;
import com.core.R;
import com.core.utils.LogUtil;
import com.core.utils.TUtil;
import com.core.utils.ToastUtils;
import com.jaeger.library.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by hpw on 16/10/12.
 */

public abstract class CoreBaseActivity<T extends CoreBasePresenter, E extends CoreBaseModel> extends AppCompatActivity implements BGASwipeBackHelper.Delegate {

    protected String TAG;

    public T mPresenter;
    public E mModel;
    protected Context mContext;
    Unbinder binder;
    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
    }

    private void init(Bundle savedInstanceState) {
        TAG = getClass().getSimpleName();

        super.setContentView(this.getLayoutId());
        binder = ButterKnife.bind(this);
        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (this instanceof CoreBaseView) mPresenter.attachVM(this, mModel);
        this.initView(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (binder != null) binder.unbind();
        if (mPresenter != null) mPresenter.detachVM();
    }

    public abstract int getLayoutId();

    public abstract void initView(Bundle savedInstanceState);

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        mSwipeBackHelper.backward();
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected void setStatusBarColor(@ColorInt int color) {
        setStatusBarColor(color, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     * @param statusBarAlpha 透明度
     */
    public void setStatusBarColor(@ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        StatusBarUtil.setColorForSwipeBack(this, color, statusBarAlpha);
    }

    protected void setToolBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    /**
     * 跳转页面,无extra简易型
     *
     * @param tarActivity 目标页面
     */
    public void startActivity(Class<? extends Activity> tarActivity, Bundle options) {
        Intent intent = new Intent(this, tarActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options);
        } else {
            startActivity(intent);
        }
    }

    public void startActivity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    public void showToast(String msg) {
        ToastUtils.showToast(this, msg, Toast.LENGTH_SHORT);
    }

    public void showLog(String msg) {
        LogUtil.i(TAG, msg);
    }
}
