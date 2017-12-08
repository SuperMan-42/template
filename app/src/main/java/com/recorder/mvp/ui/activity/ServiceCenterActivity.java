package com.recorder.mvp.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.integration.cache.BCache;
import com.core.utils.Constants;
import com.google.gson.Gson;
import com.recorder.R;
import com.recorder.mvp.model.entity.AppStartBean;

import butterknife.BindView;

@Route(path = "/app/ServiceCenterActivity")
public class ServiceCenterActivity extends BaseActivity {

    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_email)
    TextView tvEmail;

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
        AppStartBean bean = new Gson().fromJson(BCache.getInstance().getString(Constants.APPSTART), AppStartBean.class);
        tvPhone.setText(bean.getData().getService_tel());
        tvEmail.setText(bean.getData().getEmail());
    }
}
