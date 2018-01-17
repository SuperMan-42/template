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
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.integration.cache.BCache;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.recorder.R;
import com.recorder.di.component.DaggerBuyIntentionComponent;
import com.recorder.di.module.BuyIntentionModule;
import com.recorder.mvp.contract.BuyIntentionContract;
import com.recorder.mvp.model.entity.AppStartBean;
import com.recorder.mvp.model.entity.DealDetailBean;
import com.recorder.mvp.presenter.BuyIntentionPresenter;
import com.recorder.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/BuyIntentionActivity")
public class BuyIntentionActivity extends BaseActivity<BuyIntentionPresenter> implements BuyIntentionContract.View {
    @Autowired
    String dealDetail;
    DealDetailBean.DataEntity dataEntity;
    @BindView(R.id.tv_deal_name)
    TextView tvDealName;
    @BindView(R.id.tv_limit_price)
    TextView tvLimitPrice;
    @BindView(R.id.et_1)
    EditText et1;
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerview;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_info)
    TextView tvInfo;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerBuyIntentionComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .buyIntentionModule(new BuyIntentionModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_buy_intention; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        dataEntity = new Gson().fromJson(dealDetail, DealDetailBean.DataEntity.class);
        title("投资意向");
        tvDealName.setText(dataEntity.getDeal_name());
        tvLimitPrice.setTextColor(Color.RED);
        tvLimitPrice.setText("起投金额" + dataEntity.getLimit_price() + "万元");
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        manager.setAutoMeasureEnabled(true);
        List<BuyActivity.Bean> list = new ArrayList<>();
        list.add(new BuyActivity.Bean(getString(R.string.text_agree_1), false));
        list.add(new BuyActivity.Bean(getString(R.string.text_agree_2), false));
        list.add(new BuyActivity.Bean(getString(R.string.text_agree_3), false));
        recyclerview.init(manager, new BaseQuickAdapter<BuyActivity.Bean, BaseViewHolder>(R.layout.item_buy, list) {
            @Override
            protected void convert(BaseViewHolder holder, BuyActivity.Bean item) {
                holder.setText(R.id.tv_agree, item.getKey())
                        .setImageResource(R.id.im_agree, item.getCheck() ? R.drawable.ic_item_buy_selector : R.drawable.ic_item_buy);
                holder.getView(R.id.im_agree).setOnClickListener(view -> {
                    item.setCheck(!item.getCheck());
                    holder.setImageResource(R.id.im_agree, item.getCheck() ? R.drawable.ic_item_buy_selector : R.drawable.ic_item_buy);
                    tvSubmit.setEnabled(true);
                    for (BuyActivity.Bean entity : getData()) {
                        if (!entity.getCheck()) {
                            tvSubmit.setEnabled(false);
                        }
                    }
                    if (TextUtils.isEmpty(et1.getText().toString()) || Float.parseFloat(et1.getText().toString()) < Float.parseFloat(dataEntity.getLimit_price())) {
                        tvSubmit.setEnabled(false);
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
                Logger.d("buy=> " + et1.getText().toString() + " " + TextUtils.isEmpty(et1.getText().toString()));
                if (TextUtils.isEmpty(et1.getText().toString()) || Float.parseFloat(et1.getText().toString()) > Float.parseFloat(dataEntity.getTarget_fund()) || Float.parseFloat(et1.getText().toString()) < Float.parseFloat(dataEntity.getLimit_price())) {
                    tvLimitPrice.setTextColor(Color.RED);
                    tvSubmit.setEnabled(false);
                } else {
                    tvLimitPrice.setTextColor(Color.parseColor("#333333"));
                    tvSubmit.setEnabled(true);
                    for (Object entity : recyclerview.getAdapter().getData()) {
                        if (!((BuyActivity.Bean) entity).getCheck()) {
                            tvSubmit.setEnabled(false);
                        }
                    }
                }
            }
        });

        AppStartBean bean = new Gson().fromJson(BCache.getInstance().getString(Constants.APPSTART), AppStartBean.class);
        String content = String.format(getString(R.string.text_intention), bean.getData().getService_tel());
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#303BD3"));
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
            }

            @Override
            public void onClick(View view) {
                CommonUtils.call(BuyIntentionActivity.this, bean.getData().getService_tel());
            }
        }, content.indexOf("拨打") + 2, content.indexOf("来电"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInfo.setMovementMethod(LinkMovementMethod.getInstance());
        tvInfo.setText(spannableString);
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

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        if (CommonUtils.isFastClick()) {
            mPresenter.orderIntention(dataEntity.getDealID(), et1.getText().toString());
        }
    }
}
