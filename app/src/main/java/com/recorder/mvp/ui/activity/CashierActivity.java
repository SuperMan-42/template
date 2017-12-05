package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.recorder.R;
import com.recorder.di.component.DaggerCashierComponent;
import com.recorder.di.module.CashierModule;
import com.recorder.mvp.contract.CashierContract;
import com.recorder.mvp.presenter.CashierPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/CashierActivity")
public class CashierActivity extends BaseActivity<CashierPresenter> implements CashierContract.View {

    @BindView(R.id.tv_amount)
    TextView tvAmount;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerCashierComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cashierModule(new CashierModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_cashier; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {

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

    @OnClick({R.id.rl_onLine, R.id.rl_offLine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_onLine:
                break;
            case R.id.rl_offLine:
                break;
        }
    }
}
