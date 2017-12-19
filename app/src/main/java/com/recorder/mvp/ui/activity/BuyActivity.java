package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
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

import java.io.Serializable;
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
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

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
        setContent(dataEntity.getManager_fee(), dataEntity.getManager_fee_year(), llManagerFee, tvManagerFee);
        setContent(dataEntity.getConsult_fee(), dataEntity.getConsult_fee_year(), llConsultFee, tvConsultFee);
        setContent(dataEntity.getSubscription_fee(), dataEntity.getSubscription_fee_year(), llSubscriptionFee, tvSubscriptionFee);
        setContent(dataEntity.getPartner_fee(), dataEntity.getPartner_fee_year(), llPartnerFee, tvPartnerFee);
        setContent(dataEntity.getPlat_manage_fee(), dataEntity.getPlat_manage_fee_year(), llPlatManageFee, tvPlatManageFee);
        setContent(dataEntity.getOther_fee(), dataEntity.getOther_fee_year(), llOtherFee, tvOtherFee);
        setContent(dataEntity.getCustom_fee(), dataEntity.getCustom_fee_year(), llCustomFee, tvCustomFee);
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        manager.setAutoMeasureEnabled(true);
        List<Bean> list = new ArrayList<>();
        list.add(new Bean("协议", false));
        list.add(new Bean("协议", false));
        String content = "阅读并同意《股权投资协议》";
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#3782E9"));
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) dataEntity.getPurchse_agreement());
                ARouter.getInstance().build("/app/AgreeActivity")
                        .withBundle("data", bundle).navigation();
            }
        }, content.indexOf("阅读并同意"), content.indexOf(""), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(new Bean(spannableString, false));
        recyclerview.init(manager, new BaseQuickAdapter<Bean, BaseViewHolder>(R.layout.item_buy, list) {
            @Override
            protected void convert(BaseViewHolder holder, Bean item) {
                holder.setText(R.id.tv_agree, item.getKey())
                        .setImageResource(R.id.im_agree, item.getCheck() ? R.drawable.ic_item_buy_selector : R.drawable.ic_item_buy);
                holder.getView(R.id.im_agree).setOnClickListener(view -> {
                    item.setCheck(!item.getCheck());
                    holder.setImageResource(R.id.im_agree, item.getCheck() ? R.drawable.ic_item_buy_selector : R.drawable.ic_item_buy);
                    tvSubmit.setEnabled(true);
                    for (Bean entity : getData()) {
                        if (!entity.getCheck()) {
                            tvSubmit.setEnabled(false);
                        }
                    }
                });
            }
        }, false);

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString()) || Integer.parseInt(editable.toString()) < Integer.parseInt(dataEntity.getLimit_price())) {
                    et1.setTextColor(TextUtils.isEmpty(editable.toString()) ? Color.parseColor("#333333") : Color.RED);
                    tvSubmit.setEnabled(false);
                } else {
                    tvSubmit.setEnabled(true);
                    for (Object entity : recyclerview.getAdapter().getData()) {
                        if (!((Bean) entity).getCheck()) {
                            tvSubmit.setEnabled(false);
                        }
                    }
                }
            }
        });
    }

    private void setContent(String value, String year, LinearLayout ll, TextView tv) {
        if (TextUtils.isEmpty(value) || Float.parseFloat(value) <= 0) {
            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
            int yearInt = Integer.parseInt(year);
            int valueInt = Integer.parseInt(value);
            tv.setText((valueInt * yearInt) + "%  (" + valueInt + "% * " + yearInt + "年)");
            if (value.equals(dataEntity.getCustom_fee())) {
                tvCustomFeeName.setText(dataEntity.getCustom_fee_name());
            }
        }
    }

    private void setContent(String value, LinearLayout ll, TextView tv) {
        if (TextUtils.isEmpty(value) || Float.parseFloat(value) <= 0) {
            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
            tv.setText(value + "%");
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
                BigDecimal bigDecimal = new BigDecimal(et1.getText().toString());
                ARouter.getInstance().build("/app/CashierActivity")
                        .withString(Constants.DEAL_ID, dataEntity.getDealID())
                        .withString("deal_name", dataEntity.getDeal_name())
                        .withString("amount", bigDecimal.multiply(new BigDecimal(10000)).toString()).navigation();
                killMyself();
                overridePendingTransition(R.anim.slide_in_right, R.anim.empty);
                break;
        }
    }

    public class Bean {
        CharSequence key;
        boolean isCheck;

        public Bean(CharSequence key, boolean isCheck) {
            this.key = key;
            this.isCheck = isCheck;
        }

        public CharSequence getKey() {
            return key;
        }

        public void setKey(CharSequence key) {
            this.key = key;
        }

        public boolean getCheck() {
            return isCheck;
        }

        public void setCheck(boolean isCheck) {
            this.isCheck = isCheck;
        }

    }
}
