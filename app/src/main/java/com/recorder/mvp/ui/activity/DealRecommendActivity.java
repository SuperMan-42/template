package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.integration.cache.BCache;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.AndroidBug5497Workaround;
import com.google.gson.Gson;
import com.recorder.R;
import com.recorder.di.component.DaggerDealRecommendComponent;
import com.recorder.di.module.DealRecommendModule;
import com.recorder.mvp.contract.DealRecommendContract;
import com.recorder.mvp.model.entity.AppStartBean;
import com.recorder.mvp.presenter.DealRecommendPresenter;
import com.recorder.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/DealRecommendActivity")
public class DealRecommendActivity extends BaseActivity<DealRecommendPresenter> implements DealRecommendContract.View {

    @BindView(R.id.et_deal_name)
    EditText etDealName;
    @BindView(R.id.tv_deal_name)
    TextView tvDealName;
    @BindView(R.id.et_industry)
    EditText etIndustry;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.et_requirement)
    EditText etRequirement;
    @BindView(R.id.tv_requirement)
    TextView tvRequirement;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_phone1)
    TextView tvPhone1;
    @BindView(R.id.et_business)
    EditText etBusiness;
    @BindView(R.id.et_team)
    EditText etTeam;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_email)
    TextView tvEmail;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerDealRecommendComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .dealRecommendModule(new DealRecommendModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_deal_recommend; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        AndroidBug5497Workaround.assistActivity(this);
        title("推荐项目");
        AppStartBean bean = new Gson().fromJson(BCache.getInstance().getString(Constants.APPSTART), AppStartBean.class);
        if (bean != null) {
            tvPhone.setText(bean.getData().getService_tel());
            tvPhone.setOnClickListener(view -> CommonUtils.call(this, bean.getData().getService_tel()));
            tvEmail.setText(bean.getData().getEmail());
        }
        etDealName.addTextChangedListener(new MyWatcher(tvDealName));
        etIndustry.addTextChangedListener(new MyWatcher(tvIndustry));
        etRequirement.addTextChangedListener(new MyWatcher(tvRequirement));
        etContact.addTextChangedListener(new MyWatcher(tvContact));
        etPhone.addTextChangedListener(new MyWatcher(tvPhone1));
    }

    private static class MyWatcher implements TextWatcher {
        TextView textView;

        MyWatcher(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                this.textView.setVisibility(View.VISIBLE);
            } else {
                this.textView.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
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

    @OnClick(R.id.tv_do)
    public void onViewClicked() {
        dealRecommend();
    }

    private void dealRecommend() {
        if (TextUtils.isEmpty(etDealName.getText().toString())) {
            CoreUtils.snackbarText(getString(R.string.deal_name));
            return;
        }
        if (TextUtils.isEmpty(etContact.getText().toString())) {
            CoreUtils.snackbarText(getString(R.string.et_contact));
            return;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            CoreUtils.snackbarText(getString(R.string.et_phone));
            return;
        }
        mPresenter.dealRecommend(etDealName.getText().toString(), etIndustry.getText().toString(), etRequirement.getText().toString(),
                etContact.getText().toString(), etPhone.getText().toString(), etBusiness.getText().toString(), etTeam.getText().toString());
    }
}
