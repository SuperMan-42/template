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
import com.recorder.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/app/ServiceCenterActivity")
public class ServiceCenterActivity extends BaseActivity {

    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    AppStartBean bean;

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
        bean = new Gson().fromJson(BCache.getInstance().getString(Constants.APPSTART), AppStartBean.class);
        if (bean != null) {
            tvPhone.setText(bean.getData().getService_tel());
            tvEmail.setText(bean.getData().getEmail());
        }
    }

    @OnClick(R.id.ll_phone)
    public void onViewClicked() {
        CommonUtils.call(bean.getData().getService_tel());
    }
}
