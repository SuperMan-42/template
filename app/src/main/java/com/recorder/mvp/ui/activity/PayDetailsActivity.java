package com.recorder.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.google.gson.Gson;
import com.recorder.R;
import com.recorder.di.component.DaggerPayDetailsComponent;
import com.recorder.di.module.PayDetailsModule;
import com.recorder.mvp.contract.PayDetailsContract;
import com.recorder.mvp.model.entity.OrderListBean;
import com.recorder.mvp.presenter.PayDetailsPresenter;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/PayDetailsActivity")
public class PayDetailsActivity extends BaseActivity<PayDetailsPresenter> implements PayDetailsContract.View {
    @Autowired
    String data;
    @Autowired
    int position;

    private boolean isSuccess;

    @BindView(R.id.tv_order_status_name)
    TextView tvOrderStatusName;
    @BindView(R.id.tv_actual_amount)
    TextView tvActualAmount;
    @BindView(R.id.tv_deal_name)
    TextView tvDealName;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_manager_amount)
    TextView tvManagerAmount;
    @BindView(R.id.ll_manager_amount)
    RelativeLayout llManagerAmount;
    @BindView(R.id.tv_subscription_amount)
    TextView tvSubscriptionAmount;
    @BindView(R.id.ll_subscription_amount)
    RelativeLayout llSubscriptionAmount;
    @BindView(R.id.tv_consult_amount)
    TextView tvConsultAmount;
    @BindView(R.id.ll_consult_amount)
    RelativeLayout llConsultAmount;
    @BindView(R.id.tv_partner_amount)
    TextView tvPartnerAmount;
    @BindView(R.id.ll_partner_amount)
    RelativeLayout llPartnerAmount;
    @BindView(R.id.tv_plat_manage_amount)
    TextView tvPlatManageAmount;
    @BindView(R.id.ll_plat_manage_amount)
    RelativeLayout llPlatManageAmount;
    @BindView(R.id.tv_other_amount)
    TextView tvOtherAmount;
    @BindView(R.id.ll_other_amount)
    RelativeLayout llOtherAmount;
    @BindView(R.id.tv_custom_amount_name)
    TextView tvCustomAmountName;
    @BindView(R.id.tv_custom_amount)
    TextView tvCustomAmount;
    @BindView(R.id.ll_custom_amount)
    RelativeLayout llCustomAmount;
    @BindView(R.id.tv_order_sn)
    TextView tvOrderSn;
    @BindView(R.id.tv_subscribe_time)
    TextView tvSubscribeTime;
    @BindView(R.id.tv_payment_way)
    TextView tvPaymentWay;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.im_cover)
    ImageView imCover;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_go_authentication)
    TextView tvGoAuthentication;
    @BindView(R.id.tv_go_home)
    TextView tvGoHome;
    @BindView(R.id.ll_dialog)
    LinearLayout llDialog;
    @BindView(R.id.fl_dialog)
    FrameLayout flDialog;

    private OrderListBean.DataEntity.ListEntity listEntity;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerPayDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .payDetailsModule(new PayDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_pay_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView(Bundle savedInstanceState) {
        title("订单详情");
        ARouter.getInstance().inject(this);
        listEntity = new Gson().fromJson(data, OrderListBean.DataEntity.ListEntity.class);
        //一定显示的项目
        tvOrderStatusName.setText(listEntity.getOrder_status_name());
        tvActualAmount.setText(listEntity.getActual_amount() + "元");
        tvDealName.setText(listEntity.getDeal_name());
        tvAmount.setText(listEntity.getAmount() + "元");
        tvOrderSn.setText(listEntity.getOrder_sn());
        tvSubscribeTime.setText(listEntity.getSubscribe_time());
        tvPaymentWay.setText(listEntity.getPayment_way());
        //可能不显示的项目
        setContent(listEntity, listEntity.getManager_amount(), llManagerAmount, tvManagerAmount);
        setContent(listEntity, listEntity.getConsult_amount(), llConsultAmount, tvConsultAmount);
        setContent(listEntity, listEntity.getSubscription_amount(), llSubscriptionAmount, tvSubscriptionAmount);
        setContent(listEntity, listEntity.getPartner_amount(), llPartnerAmount, tvPartnerAmount);
        setContent(listEntity, listEntity.getPlat_manage_amount(), llPlatManageAmount, tvPlatManageAmount);
        setContent(listEntity, listEntity.getOther_amount(), llOtherAmount, tvOtherAmount);
        setContent(listEntity, listEntity.getCustom_amount(), llCustomAmount, tvCustomAmount);

        if (listEntity.getIs_publish()) {
            if (listEntity.getOrder_status() == 0 || listEntity.getOrder_status() == 1) {
                tvSubmit.setVisibility(View.VISIBLE);
                tvSubmit.setText("支付");
                tvSubmit.setOnClickListener(view -> mPresenter.orderPay(listEntity.getOrderID(), listEntity, "2"));
            } else if (listEntity.getOrder_status() == 4) {
                tvSubmit.setVisibility(View.VISIBLE);
                tvSubmit.setText("上传打款凭证");
                tvSubmit.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.UPLOAD_ORDERID, listEntity.getOrderID());
                    ARouter.getInstance().build("/app/UploadActivity").withBundle(Constants.UPLOAD, bundle).withInt("position", position).withBoolean(Constants.ORDER_PROOF, true).navigation();
                });
            } else {
                tvSubmit.setVisibility(View.GONE);
            }
        } else {
            tvSubmit.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setContent(OrderListBean.DataEntity.ListEntity data, String value, View ll, TextView tv) {
        if (TextUtils.isEmpty(value) || Float.parseFloat(value) <= 0) {
            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
            tv.setText(value + "元");
            if (value.equals(data.getCustom_amount())) {
                tvCustomAmountName.setText(data.getCustom_amount_name());
            }
        }
    }

    @Subscriber(tag = Constants.ORDER_PROOF)
    private void paySuccess(int position) {
        tvSubmit.setVisibility(View.GONE);
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

    @OnClick({R.id.ll_deal_name, R.id.tv_go_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_deal_name:
                ARouter.getInstance().build("/app/EquityDetailsActivity")
                        .withString(Constants.DEAL_ID, listEntity.getDealID()).navigation();
                break;
            case R.id.tv_go_home:
                killMyself();
//                ARouter.getInstance().build("/app/MyInvestmentActivity").navigation();
//                overridePendingTransition(R.anim.slide_in_right, R.anim.empty);
                break;
        }
    }

    @Override
    public void showResult(boolean isSuccess, OrderListBean.DataEntity.ListEntity item, String msg) {
        this.isSuccess = isSuccess;
        if (isSuccess) {
            CoreUtils.imgLoader(this, R.drawable.ic_result_success, imCover);
            tvTitle.setText(CoreUtils.getString(this, R.string.text_buy_success));
            String content = CoreUtils.getString(this, R.string.text_buy_success_alter) + item.getDeal_name() + ",支付金额:" + msg + "元";
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FC3F08")), content.indexOf(msg), content.indexOf("元"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvContent.setText(spannableString);
            tvContent.setTextColor(Color.parseColor("#333333"));
            tvGoAuthentication.setText("项目详情");
            title("支付成功");
            EventBus.getDefault().post(new Object(), Constants.PAY_SUCCESS);
        } else {
            CoreUtils.imgLoader(this, R.drawable.ic_result_fail, imCover);
            tvTitle.setText(CoreUtils.getString(this, R.string.text_buy_fail));
            tvContent.setText(CoreUtils.getString(this, R.string.text_buy_fail_alter) + msg);
            tvContent.setTextColor(Color.parseColor("#FF5701"));
            tvGoAuthentication.setText("重新支付");
            title("支付失败");
        }
        tvGoHome.setText("我的订单");
        flDialog.setVisibility(View.VISIBLE);
        flDialog.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        tvGoAuthentication.setOnClickListener(view -> {
            if (isSuccess) {
                ARouter.getInstance().build("/app/EquityDetailsActivity").withString(Constants.DEAL_ID, item.getDealID()).navigation();
            } else {
                mPresenter.orderPay(item.getOrderID(), item, "2");
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void closeDialog() {
        if (flDialog.getVisibility() == 0) {
            title("我的投资");
            if (isSuccess) {
                tvSubmit.setVisibility(View.GONE);
                EventBus.getDefault().post(position, Constants.PAY_SUCCESS);
            }
            flDialog.setVisibility(View.GONE);
            flDialog.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        } else {
            killMyself();
        }
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void onBackPressed() {
        closeDialog();
    }
}
