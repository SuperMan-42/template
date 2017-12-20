package com.recorder.mvp.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.recorder.R;
import com.recorder.mvp.model.entity.PayCheckBean;
import com.recorder.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created by hpw on 17-12-19.
 */

@Route(path = "/app/AgreeActivity")
public class AgreeActivity extends BaseActivity {
    @Autowired
    Bundle data;

    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerview;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_help_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("股权协议");
        ARouter.getInstance().inject(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setAutoMeasureEnabled(true);
        recyclerview.init(manager, new BaseQuickAdapter<PayCheckBean.DataEntity.PurchseAgreementEntity, BaseViewHolder>(R.layout.item_help_list, (List<PayCheckBean.DataEntity.PurchseAgreementEntity>) data.getSerializable("data")) {
            @Override
            protected void convert(BaseViewHolder holder, PayCheckBean.DataEntity.PurchseAgreementEntity item) {
                holder.setText(R.id.tv_title, item.getFile_name());
                holder.itemView.setOnClickListener(view -> ARouter.getInstance().build("/app/PdfActivity")
                        .withString(Constants.PDF_HTTP, item.getFile()).withString(Constants.PDF_NAME, item.getFile_name()).navigation());
            }
        }, false);
        recyclerview.getRecyclerView().addItemDecoration(CommonUtils.linearDivider(this, 45));
    }
}
