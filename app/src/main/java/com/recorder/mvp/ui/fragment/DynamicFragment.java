package com.recorder.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseFragment;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.core.widget.recyclerview.SpacesItemDecoration;
import com.recorder.R;
import com.recorder.di.component.DaggerDynamicComponent;
import com.recorder.di.module.DynamicModule;
import com.recorder.mvp.contract.DynamicContract;
import com.recorder.mvp.model.entity.NewsListBean;
import com.recorder.mvp.presenter.DynamicPresenter;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/DynamicFragment")
public class DynamicFragment extends BaseFragment<DynamicPresenter> implements DynamicContract.View {
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;

    public static DynamicFragment newInstance() {
        DynamicFragment fragment = new DynamicFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerDynamicComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .dynamicModule(new DynamicModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dynamic, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.newsList("1", Constants.PAGE_SIZE);
        recyclerView.getRecyclerView().addItemDecoration(new SpacesItemDecoration(30, 45));
        recyclerView.init(new BaseQuickAdapter<NewsListBean.DataEntity.ListEntity, BaseViewHolder>(R.layout.item_dynamic) {
            @Override
            protected void convert(BaseViewHolder holder, NewsListBean.DataEntity.ListEntity item) {
                CoreUtils.imgLoader(getContext(), item.getCover(), R.drawable.ic_dynamic, holder.getView(R.id.im_cover));
                holder.setText(R.id.tv_title, item.getTitle());
                holder.itemView.setOnClickListener(view1 -> ARouter.getInstance().build("/app/WebActivity")
                        .withBoolean(Constants.IS_SHOW_RIGHT, true)
                        .withString(Constants.WEB_URL, item.getUrl()).navigation());
            }
        }).openRefresh(page -> mPresenter.newsList("1", Constants.PAGE_SIZE))
                .openLoadMore(Constants.PAGE_SIZE_INT, page -> mPresenter.newsList(String.valueOf(page), Constants.PAGE_SIZE)).reStart();
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行
     * setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
     * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }

    @Override
    public void showNewsList(NewsListBean.DataEntity data) {
        recyclerView.getAdapter().addData(data.getList());
    }
}
