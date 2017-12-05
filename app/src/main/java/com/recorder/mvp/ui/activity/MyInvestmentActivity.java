package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.core.widget.recyclerview.SpacesItemDecoration;
import com.recorder.R;
import com.recorder.di.component.DaggerMyInvestmentComponent;
import com.recorder.di.module.MyInvestmentModule;
import com.recorder.mvp.contract.MyInvestmentContract;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.model.entity.OrderListBean;
import com.recorder.mvp.presenter.MyInvestmentPresenter;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/MyInvestmentActivity")
public class MyInvestmentActivity extends BaseActivity<MyInvestmentPresenter> implements MyInvestmentContract.View {
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyInvestmentComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myInvestmentModule(new MyInvestmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_my_investment; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("我的投资");
        mPresenter.orderList(null, null);
    }

    @Subscriber(tag = Constants.RETRY_MYINVESTMENT)
    private void retry(LoginBean loginBean) {
        findViewById(R.id.view_empty).setVisibility(View.GONE);
        mPresenter.orderList(null, null);
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

    @Override
    public void showOrderList(OrderListBean.DataEntity data) {
        recyclerView.init(new BaseQuickAdapter<OrderListBean.DataEntity.ListEntity, BaseViewHolder>(R.layout.item_my_investment, data.getList()) {
            @Override
            protected void convert(BaseViewHolder holder, OrderListBean.DataEntity.ListEntity item) {
                CoreUtils.imgLoader(holder.itemView.getContext(), item.getCover(), holder.getView(R.id.im_cover));
                holder.setText(R.id.tv_deal_name, item.getDeal_name())
                        .setText(R.id.tv_amount, item.getAmount())
                        .setText(R.id.tv_actual_amount, item.getActual_amount());
                setContent(holder, item, item.getManager_amount(), R.id.ll_manager_amount, R.id.tv_manager_amount);
                setContent(holder, item, item.getConsult_amount(), R.id.ll_consult_amount, R.id.tv_consult_amount);
                setContent(holder, item, item.getSubscription_amount(), R.id.ll_subscription_amount, R.id.tv_subscription_amount);
                setContent(holder, item, item.getPartner_amount(), R.id.ll_partner_amount, R.id.tv_partner_amount);
                setContent(holder, item, item.getPlat_manage_amount(), R.id.ll_plat_manage_amount, R.id.tv_plat_manage_amount);
                setContent(holder, item, item.getOther_amount(), R.id.ll_other_amount, R.id.tv_other_amount);
                setContent(holder, item, item.getCustom_amount(), R.id.ll_custom_amount, R.id.tv_custom_amount);
                holder.getView(R.id.tv_pay_bt).setOnClickListener(view -> {
                    //TODO
                });
            }
        }, false);
        recyclerView.getRecyclerView().addItemDecoration(new SpacesItemDecoration(0, 36));
    }

    private void setContent(BaseViewHolder holder, OrderListBean.DataEntity.ListEntity data, String value, int ll, int tv) {
        if (TextUtils.isEmpty(value) || Float.parseFloat(value) <= 0) {
            holder.setVisible(ll, false);
        } else {
            holder.setVisible(ll, true);
            holder.setText(tv, value);
            if (value.equals(data.getCustom_amount())) {
                holder.setText(R.id.tv_custom_amount_name, data.getCustom_amount_name());
            }
        }
    }
}
