package com.recorder.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.recorder.R;
import com.recorder.di.component.DaggerCashierComponent;
import com.recorder.di.module.CashierModule;
import com.recorder.mvp.contract.CashierContract;
import com.recorder.mvp.model.entity.PayPayOffLineBean;
import com.recorder.mvp.presenter.CashierPresenter;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/CashierActivity")
public class CashierActivity extends BaseActivity<CashierPresenter> implements CashierContract.View {
    @Autowired(name = Constants.DEAL_ID)
    String dealID;
    @Autowired
    String amount;
    @Autowired
    String deal_name;

    boolean isSuccess;

    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_go_authentication)
    TextView tvGoAuthentication;
    @BindView(R.id.tv_go_home)
    TextView tvGoHome;
    @BindView(R.id.fl_dialog)
    FrameLayout flDialog;
    @BindView(R.id.im_cover)
    ImageView imCover;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void initView(Bundle savedInstanceState) {
        title("收银台");
        ARouter.getInstance().inject(this);
        tvAmount.setText(new BigDecimal(amount).divide(new BigDecimal(10000)) + "万元");
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.empty);
    }

    @OnClick({R.id.toolbar_left, R.id.rl_onLine, R.id.rl_offLine, R.id.tv_go_authentication, R.id.tv_go_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left:
                ARouter.getInstance().build("/app/EquityDetailsActivity").withString(Constants.DEAL_ID, dealID).navigation();
                break;
            case R.id.rl_onLine:
                mPresenter.payPay(dealID, amount, "2");
                break;
            case R.id.rl_offLine:
                mPresenter.payPayOffLine(dealID, amount, "1");
                break;
            case R.id.tv_go_authentication:
                if (isSuccess) {
                    ARouter.getInstance().build("/app/EquityDetailsActivity").withString(Constants.DEAL_ID, dealID).navigation();
                } else {
                    mPresenter.payPay(dealID, amount, "2");
                }
                break;
            case R.id.tv_go_home:
                ARouter.getInstance().build("/app/MyInvestmentActivity").navigation();
                killMyself();
                overridePendingTransition(R.anim.slide_in_right, R.anim.empty);
                break;
        }
    }

    @Override
    public void showResult(boolean isSuccess, String msg) {
        this.isSuccess = isSuccess;
        if (isSuccess) {
            CoreUtils.imgLoader(this, R.drawable.ic_result_success, imCover);
            tvTitle.setText(CoreUtils.getString(this, R.string.text_buy_success));
            String content = CoreUtils.getString(this, R.string.text_buy_success_alter) + deal_name + ",认购金额:" + msg + "元";
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FC3F08")), content.indexOf(msg), content.indexOf("元"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvContent.setText(spannableString);
            tvContent.setTextColor(Color.parseColor("#333333"));
            tvGoAuthentication.setText("项目详情");
            title("支付成功");
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
    }

    @Override
    public void showPayOffLine(PayPayOffLineBean.DataEntity data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("offlinedata", data);
        bundle.putString("amount", new BigDecimal(amount).divide(new BigDecimal(10000)) + "万元");
        ARouter.getInstance().build("/app/OffLinePayActivity").withBundle("offlinedata", bundle).navigation();
        killMyself();
    }

    @Override
    public void onBackPressed() {
        ARouter.getInstance().build("/app/EquityDetailsActivity").withString(Constants.DEAL_ID, dealID).navigation();
    }
}
