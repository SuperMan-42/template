package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;

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
import com.recorder.R;
import com.recorder.di.component.DaggerSearchComponent;
import com.recorder.di.module.SearchModule;
import com.recorder.mvp.contract.SearchContract;
import com.recorder.mvp.model.entity.EquityBean;
import com.recorder.mvp.presenter.SearchPresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/SearchActivity")
public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {
    @Autowired(name = Constants.IS_EQUITY)
    boolean isEquity;
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;
    @BindView(R.id.etInput)
    AutoCompleteTextView etInput;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSearchComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchModule(new SearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_search; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
//        setStatusBarColor(Color.parseColor("#F9F9F9"), 0);
        etInput.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mPresenter.dealList(isEquity ? "1" : "2", null, null, etInput.getText().toString(), null, null);
                CoreUtils.hideSoftInput(etInput);
                return true;
            }
            return false;
        });
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

    @OnClick(R.id.search_button)
    public void onViewClicked() {
        killMyself();
    }

    @Override
    public void showDealList(EquityBean.DataEntity data) {
        recyclerView.init(new BaseQuickAdapter<EquityBean.DataEntity.ListEntity, BaseViewHolder>(R.layout.item_equity, data.getList()) {
            @Override
            protected void convert(BaseViewHolder holder, EquityBean.DataEntity.ListEntity item) {
                CoreUtils.imgLoader(getApplication(), "http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg", holder.getView(R.id.im_cover));
                holder.setText(R.id.tv_deal_name, item.getDeal_name())
                        .setText(R.id.tv_brief, item.getBrief())
                        .setText(R.id.tv_labels, item.getLabels())
                        .setText(R.id.tv_round, item.getRound())
                        .setText(R.id.tv_online_str, item.getOnline_str())
                        .setVisible(R.id.tv_is_group, item.getIs_group().equals("1"));
                EquityBean.DataEntity.ListEntity.ViewFooterEntity viewFooterEntity = item.getView_footer();
                if (viewFooterEntity != null) {
                    holder.setText(R.id.tv_view, String.valueOf(viewFooterEntity.getView()))
                            .setText(R.id.tv_focus, String.valueOf(viewFooterEntity.getFocus()))
                            .setText(R.id.tv_consult, String.valueOf(viewFooterEntity.getConsult()))
                            .setVisible(R.id.ll_view_footer, true);
                } else {
                    holder.setVisible(R.id.ll_view_footer, false);
                }
                holder.itemView.setOnClickListener(view1 -> ARouter.getInstance().build("/app/EquityDetailsActivity")
                        .withBoolean(Constants.IS_EQUITY, isEquity).withString(Constants.DEAL_ID, item.getDealID()).navigation());
            }
        }, false);
    }
}
