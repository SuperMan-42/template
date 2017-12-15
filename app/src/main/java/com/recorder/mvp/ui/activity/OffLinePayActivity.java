package com.recorder.mvp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.recorder.R;
import com.recorder.mvp.model.entity.PayPayOffLineBean;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/app/OffLinePayActivity")
public class OffLinePayActivity extends BaseActivity {
    @Autowired
    Bundle offlinedata;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_payee)
    TextView tvPayee;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_bank_memo)
    TextView tvBankMemo;
    @BindView(R.id.tv_tag)
    TextView tvTag;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_off_line_pay; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("线下支付信息");
        ARouter.getInstance().inject(this);
        PayPayOffLineBean.DataEntity dataEntity = (PayPayOffLineBean.DataEntity) offlinedata.getSerializable("offlinedata");
        String content = "为保证您的认购及时有效，请转账成功后，到我的－我的投资，点击“上传打款凭证”，上传您的转账凭证。";
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FC3F08")), content.indexOf("我的－我的投资"), content.indexOf("，点击“"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FC3F08")), content.indexOf("上传打款凭证"), content.indexOf("”，上传您的转账凭证"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTag.setText(spannableString);
        tvAmount.setText(offlinedata.getString("amount"));
        tvAccount.setText(dataEntity.getAccount());
        tvPayee.setText(dataEntity.getPayee());
        tvBankMemo.setText(dataEntity.getBank_memo());
        tvBankName.setText(dataEntity.getBank_name());
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        finish();
        ARouter.getInstance().build("/app/MyInvestmentActivity").navigation();
    }
}
