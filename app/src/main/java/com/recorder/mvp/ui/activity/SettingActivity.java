package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.integration.cache.BCache;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.utils.DeviceUtils;
import com.core.widget.CustomPopupWindow;
import com.recorder.R;
import com.recorder.di.component.DaggerSettingComponent;
import com.recorder.di.module.SettingModule;
import com.recorder.mvp.contract.SettingContract;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.presenter.SettingPresenter;
import com.recorder.utils.FileUtils;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/SettingActivity")
public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingContract.View {

    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.tv_login_out)
    TextView tvLoginOut;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSettingComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .settingModule(new SettingModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_setting; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("设置");
        tvVersion.setText("昊翔V" + DeviceUtils.getVersionName(this));
        tvCache.setText("缓存大小" + FileUtils.getFileAllSize(Constants.SDCARD_PATH) / 1048576 + "M");
        tvLoginOut.setVisibility(TextUtils.isEmpty(BCache.getInstance().getString(Constants.TOKEN)) ? View.GONE : View.VISIBLE);
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
    }

    @OnClick({R.id.rl_1, R.id.rl_2, R.id.rl_3, R.id.tv_login_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_1:
                CustomPopupWindow.builder().contentView(CoreUtils.inflate(this, R.layout.view_setting_bottom))
                        .animationStyle(R.style.popwindow_anim_buttom_style)
                        .isOutsideTouch(true)
                        .backgroundDrawable(new BitmapDrawable())
                        .customListener(contentView -> {
                            ((TextView) contentView.findViewById(R.id.tv_title)).setText("是否清除缓存");
                            TextView button = contentView.findViewById(R.id.tv_button);
                            button.setText("清除");
                            button.setOnClickListener(view12 -> {
                                FileUtils.cleanCustomCache(Constants.SDCARD_PATH);
                                FileUtils.cleanExternalCache(this);
                                FileUtils.cleanInternalDbs(this);
                                FileUtils.cleanInternalFiles(this);
                                FileUtils.cleanInternalSP(this);
                                tvCache.setText("缓存大小" + FileUtils.getFileAllSize(Constants.SDCARD_PATH) / 1048576 + "M");
                                CustomPopupWindow.killMySelf();
                            });
                            contentView.findViewById(R.id.tv_cancel).setOnClickListener(view1 -> CustomPopupWindow.killMySelf());
                        }).build().show(this);
                break;
            case R.id.rl_2:
                ARouter.getInstance().build("/app/FeedBackActivity").navigation();
                break;
            case R.id.rl_3:
                ARouter.getInstance().build("/app/ServiceCenterActivity").navigation();
                break;
            case R.id.tv_login_out:
                CustomPopupWindow.builder().contentView(CoreUtils.inflate(this, R.layout.view_setting_bottom))
                        .animationStyle(R.style.popwindow_anim_buttom_style)
                        .isOutsideTouch(true)
                        .backgroundDrawable(new BitmapDrawable())
                        .customListener(contentView -> {
                            ((TextView) contentView.findViewById(R.id.tv_title)).setText("您确定要注销吗");
                            TextView button = contentView.findViewById(R.id.tv_button);
                            button.setText("确定");
                            button.setOnClickListener(view12 -> {
                                BCache.getInstance().remove(Constants.TOKEN);
                                BCache.getInstance().remove(Constants.LOGIN_INFO);
                                BCache.getInstance().remove(Constants.USER_INFO);
                                EventBus.getDefault().post(new LoginBean(), "loginout");
                                CustomPopupWindow.killMySelf();
                                tvLoginOut.setVisibility(View.GONE);
                                killMyself();
                            });
                            contentView.findViewById(R.id.tv_cancel).setOnClickListener(view1 -> CustomPopupWindow.killMySelf());
                            contentView.findViewById(R.id.view).setOnClickListener(view1 -> CustomPopupWindow.killMySelf());
                        }).build().show(this);
                break;
        }
    }
}
