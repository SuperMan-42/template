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
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
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
import com.recorder.utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/MyInvestmentActivity")
public class MyInvestmentActivity extends BaseActivity<MyInvestmentPresenter> implements MyInvestmentContract.View {
    boolean isSuccess;
    int position;

    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;
    @BindView(R.id.tv_go_authentication)
    TextView tvGoAuthentication;
    @BindView(R.id.tv_go_home)
    TextView tvGoHome;
    @BindView(R.id.ll_dialog)
    LinearLayout llDialog;
    @BindView(R.id.fl_dialog)
    FrameLayout flDialog;
    @BindView(R.id.im_cover)
    ImageView imCover;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

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
        mPresenter.orderList("1", Constants.PAGE_SIZE);
        recyclerView.getRecyclerView().addItemDecoration(new SpacesItemDecoration(0, 36));
        recyclerView.init(new BaseQuickAdapter<OrderListBean.DataEntity.ListEntity, BaseViewHolder>(R.layout.item_my_investment) {
            @Override
            protected void convert(BaseViewHolder holder, OrderListBean.DataEntity.ListEntity item) {
                CoreUtils.imgLoader(holder.itemView.getContext(), item.getCover(), holder.getView(R.id.im_cover));
                holder.setText(R.id.tv_deal_name, item.getDeal_name())
                        .setText(R.id.tv_amount, item.getAmount())
                        .setText(R.id.tv_actual_amount, item.getActual_amount() + "元")
                        .setText(R.id.tv_order_status_name, item.getOrder_status_name());
                if (item.getIs_publish()) {
                    if (item.getOrder_status() == 0 || item.getOrder_status() == 1) {
                        holder.getView(R.id.tv_pay_bt).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_pay_bt, "支付");
                        holder.getView(R.id.tv_pay_bt).setOnClickListener(view -> {
                            mPresenter.orderPay(item.getOrderID(), item, "2");
                            position = holder.getAdapterPosition();
                        });
                    } else if (item.getOrder_status() == 4) {
                        holder.getView(R.id.tv_pay_bt).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_pay_bt, "上传打款凭证");
                        holder.getView(R.id.tv_pay_bt).setOnClickListener(view -> {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.UPLOAD_ORDERID, item.getOrderID());
                            ARouter.getInstance().build("/app/UploadActivity").withBundle(Constants.UPLOAD, bundle).withBoolean(Constants.ORDER_PROOF, true).navigation();
                            position = holder.getAdapterPosition();
                        });
                    } else {
                        holder.getView(R.id.tv_pay_bt).setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.getView(R.id.tv_pay_bt).setVisibility(View.INVISIBLE);
                }
                setContent(holder, item, item.getManager_amount(), R.id.ll_manager_amount, R.id.tv_manager_amount);
                setContent(holder, item, item.getConsult_amount(), R.id.ll_consult_amount, R.id.tv_consult_amount);
                setContent(holder, item, item.getSubscription_amount(), R.id.ll_subscription_amount, R.id.tv_subscription_amount);
                setContent(holder, item, item.getPartner_amount(), R.id.ll_partner_amount, R.id.tv_partner_amount);
                setContent(holder, item, item.getPlat_manage_amount(), R.id.ll_plat_manage_amount, R.id.tv_plat_manage_amount);
                setContent(holder, item, item.getOther_amount(), R.id.ll_other_amount, R.id.tv_other_amount);
                setContent(holder, item, item.getCustom_amount(), R.id.ll_custom_amount, R.id.tv_custom_amount);
            }
        }).openRefresh(page -> mPresenter.orderList("1", Constants.PAGE_SIZE))
                .openLoadMore(Constants.PAGE_SIZE_INT, page -> mPresenter.orderList(String.valueOf(page), Constants.PAGE_SIZE)).reStart();
    }

    @Subscriber(tag = Constants.RETRY_MYINVESTMENT)
    private void retry(LoginBean loginBean) {
        findViewById(R.id.view_empty).setVisibility(View.GONE);
        mPresenter.orderList("1", Constants.PAGE_SIZE);
    }

    @Override
    public void showLoading() {
        CommonUtils.show(avi);
    }

    @Override
    public void hideLoading() {
        CommonUtils.hide(avi);
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
        recyclerView.getAdapter().addData(data.getList());
    }

    @Subscriber(tag = Constants.ORDER_PROOF)
    public void upload(Object object) {
        uploadItem(5, "确认打款中");
    }

    @Override
    public void showResult(boolean isSuccess, OrderListBean.DataEntity.ListEntity item, String msg) {
        this.isSuccess = isSuccess;
        if (isSuccess) {
            CoreUtils.imgLoader(this, R.drawable.ic_result_success, imCover);
            tvTitle.setText(CoreUtils.getString(this, R.string.text_buy_success));
            String content = CoreUtils.getString(this, R.string.text_buy_success_alter) + item.getDeal_name() + ",认购金额:" + msg + "元";
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
        tvGoAuthentication.setOnClickListener(view -> {
            if (isSuccess) {
                ARouter.getInstance().build("/app/EquityDetailsActivity").withString(Constants.DEAL_ID, item.getOrderID())
                        .withBoolean(Constants.IS_EQUITY, true).navigation();
            } else {
                mPresenter.orderPay(item.getOrderID(), item, "2");
            }
        });
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

    @OnClick({R.id.toolbar_left, R.id.tv_go_authentication, R.id.tv_go_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left:
                closeDialog();
                break;
            case R.id.tv_go_home:
                closeDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        closeDialog();
    }

    @SuppressLint("WrongConstant")
    private void closeDialog() {
        if (flDialog.getVisibility() == 0) {
            title("我的投资");
            if (isSuccess) {
                uploadItem(2, "支付成功");
            }
            flDialog.setVisibility(View.GONE);
            flDialog.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        } else {
            killMyself();
        }
    }

    private void uploadItem(int status, String status_name) {
        OrderListBean.DataEntity.ListEntity entity = (OrderListBean.DataEntity.ListEntity) recyclerView.getAdapter().getData().get(position);
        entity.setOrder_status(status);
        entity.setOrder_status_name(status_name);
        recyclerView.getAdapter().getData().set(position, entity);
        recyclerView.getAdapter().notifyItemChanged(position);
    }
}
