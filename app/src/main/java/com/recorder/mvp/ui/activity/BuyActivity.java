package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.google.gson.Gson;
import com.recorder.R;
import com.recorder.di.component.DaggerBuyComponent;
import com.recorder.di.module.BuyModule;
import com.recorder.mvp.contract.BuyContract;
import com.recorder.mvp.model.entity.PayCheckBean;
import com.recorder.mvp.presenter.BuyPresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/BuyActivity")
public class BuyActivity extends BaseActivity<BuyPresenter> implements BuyContract.View {
    @Autowired
    String payCheck;

    @BindView(R.id.tv_deal_name)
    TextView tvDealName;
    @BindView(R.id.et_1)
    EditText et1;
    @BindView(R.id.tv_limit_price)
    TextView tvLimitPrice;
    @BindView(R.id.tv_shakes)
    TextView tvShakes;
    @BindView(R.id.ll_shakes)
    LinearLayout llShakes;
    @BindView(R.id.tv_manager_fee)
    TextView tvManagerFee;
    @BindView(R.id.ll_manager_fee)
    LinearLayout llManagerFee;
    @BindView(R.id.tv_consult_fee)
    TextView tvConsultFee;
    @BindView(R.id.ll_consult_fee)
    LinearLayout llConsultFee;
    @BindView(R.id.tv_subscription_fee)
    TextView tvSubscriptionFee;
    @BindView(R.id.ll_subscription_fee)
    LinearLayout llSubscriptionFee;
    @BindView(R.id.tv_partner_fee)
    TextView tvPartnerFee;
    @BindView(R.id.ll_partner_fee)
    LinearLayout llPartnerFee;
    @BindView(R.id.tv_other_fee)
    TextView tvOtherFee;
    @BindView(R.id.ll_plat_manage_fee)
    LinearLayout llPlatManageFee;
    @BindView(R.id.tv_plat_manage_fee)
    TextView tvPlatManageFee;
    @BindView(R.id.ll_other_fee)
    LinearLayout llOtherFee;
    @BindView(R.id.tv_custom_fee_name)
    TextView tvCustomFeeName;
    @BindView(R.id.tv_custom_fee)
    TextView tvCustomFee;
    @BindView(R.id.ll_custom_fee)
    LinearLayout llCustomFee;
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerview;

    private PayCheckBean.DataEntity dataEntity;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerBuyComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .buyModule(new BuyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_buy; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        dataEntity = new Gson().fromJson(payCheck, PayCheckBean.class).getData();
        title("我要认购");
        tvDealName.setText(dataEntity.getDeal_name());
        tvLimitPrice.setText("起投金额" + dataEntity.getLimit_price() + "万元");
        setContent(dataEntity.getShakes(), llShakes, tvShakes);
        setContent(dataEntity.getManager_fee(), llManagerFee, tvManagerFee);
        setContent(dataEntity.getConsult_fee(), llConsultFee, tvConsultFee);
        setContent(dataEntity.getSubscription_fee(), llSubscriptionFee, tvSubscriptionFee);
        setContent(dataEntity.getPartner_fee(), llPartnerFee, tvPartnerFee);
        setContent(dataEntity.getPlat_manage_fee(), llPlatManageFee, tvPlatManageFee);
        setContent(dataEntity.getOther_fee(), llOtherFee, tvOtherFee);
        setContent(dataEntity.getCustom_fee(), llCustomFee, tvCustomFee);
        List<String> list = new ArrayList<>();
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        manager.setAutoMeasureEnabled(true);
        recyclerview.init(manager, new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_buy, list) {
            @Override
            protected void convert(BaseViewHolder holder, String item) {
            }
        }, false);
    }

    private void setContent(String value, LinearLayout ll, TextView tv) {
        if (TextUtils.isEmpty(value) || Float.parseFloat(value) <= 0) {
            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
            tv.setText(value);
            if (value.equals(dataEntity.getCustom_fee())) {
                tvCustomFeeName.setText(dataEntity.getCustom_fee_name());
            }
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

    @OnClick({R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                String value = et1.getText().toString();
                if (TextUtils.isEmpty(value)) {
                    CoreUtils.snackbarText("金额不能为空");
                    return;
                }
                BigDecimal bigDecimal = new BigDecimal(value);
                ARouter.getInstance().build("/app/CashierActivity")
                        .withString(Constants.DEAL_ID, dataEntity.getDealID())
                        .withString("deal_name", dataEntity.getDeal_name())
                        .withString("amount", bigDecimal.multiply(new BigDecimal(10000)).toString()).navigation();
                killMyself();
                overridePendingTransition(R.anim.slide_in_right, R.anim.empty);
                break;
        }
    }
}
