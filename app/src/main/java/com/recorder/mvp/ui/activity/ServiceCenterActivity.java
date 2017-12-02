package com.recorder.mvp.ui.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.recorder.R;

@Route(path = "/app/ServiceCenterActivity")
public class ServiceCenterActivity extends BaseActivity {

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_service_center;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("客服中心");
    }
}
