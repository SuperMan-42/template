package com.recorder.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseFragment;
import com.core.di.component.AppComponent;
import com.core.http.imageloader.ImageLoader;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.cunoraz.gifview.library.GifView;
import com.recorder.R;
import com.recorder.di.component.DaggerHomeComponent;
import com.recorder.di.module.HomeModule;
import com.recorder.mvp.contract.HomeContract;
import com.recorder.mvp.model.entity.Bean;
import com.recorder.mvp.model.entity.DealFilter;
import com.recorder.mvp.model.entity.HomeRecommendBean;
import com.recorder.mvp.presenter.HomePresenter;
import com.recorder.utils.CommonUtils;
import com.recorder.widget.AutoProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/HomeFragment")
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {

    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;
    @BindView(R.id.avi)
    GifView avi;

    private BGABanner banner;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.homeRecommend();
        View view = CoreUtils.inflate(getContext(), R.layout.item_home_header);
        banner = view.findViewById(R.id.banner);
        banner.setAdapter((banner1, itemView, model, position) -> {
            CoreUtils.imgLoader(getContext(), ((Bean) model).getValue(), ((Bean) model).getKey().equals("deal") ? R.drawable.ic_deal_recommend : R.drawable.home_banner, (ImageView) itemView);
            itemView.setOnClickListener(view12 -> {
                if (TextUtils.isEmpty(((Bean) model).getOther())) {
                } else if (((Bean) model).getOther().equals("hx://deal-recommend")) {
                    ARouter.getInstance().build("/app/DealRecommendActivity").navigation();
                } else {
                    ARouter.getInstance().build("/app/WebActivity")
                            .withBoolean(Constants.IS_SHOW_RIGHT, true).withString(Constants.WEB_URL, ((Bean) model).getOther()).greenChannel().navigation();
                }
            });
        });
        banner.setAutoPlayInterval(5000);

        recyclerView.init(new BaseQuickAdapter<HomeRecommendBean.DataEntity.Entity, BaseViewHolder>(R.layout.item_home) {
            @Override
            protected void convert(BaseViewHolder holder, HomeRecommendBean.DataEntity.Entity item) {
                CoreUtils.imgLoaderRound(getContext(), TextUtils.isEmpty(item.getCover()) ? R.drawable.home_list : item.getCover(), bitmapTransform(new RoundedCornersTransformation(24, 0, RoundedCornersTransformation.CornerType.ALL)), R.drawable.home_list, holder.getView(R.id.im_pic));
                holder.setText(R.id.tv_title, item.getDeal_name())
                        .setText(R.id.tv_investment, "起投金额: " + item.getLimit_price() + "万")
                        .setText(R.id.tv_financing, "融资总额: " + item.getTarget_fund() + "万")
                        .setImageResource(R.id.tv_tag, item.getType() == 1 ? R.drawable.ic_home_equity : R.drawable.ic_home_private);
                AutoProgressBar progressBar = holder.getView(R.id.numberProgressBar);
                progressBar.setProgress(item.getProgress());
                holder.itemView.setOnClickListener(view1 -> ARouter.getInstance().build("/app/EquityDetailsActivity")
                        .withBoolean(Constants.IS_EQUITY, item.getType() == 1)
                        .withString(Constants.DEAL_ID, item.getDealID()).navigation());
            }
        }).openRefresh(page -> mPresenter.homeRecommend()).addHeaderView(view);
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

    }

    @Override
    public void showFilter(ImageLoader imageLoader, DealFilter.DataEntity dataEntity) {

    }

    @Override
    public void showHomeRecomment(HomeRecommendBean.DataEntity dataEntity) {
        List<Bean> models = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (HomeRecommendBean.DataEntity.NewsRecommendEntity entity : dataEntity.getNews_recommend()) {
            models.add(new Bean<>(entity.getNewsID(), entity.getCover(), entity.getUrl()));
            titles.add(entity.getTitle());
        }
        banner.setData(models, titles);
        List<HomeRecommendBean.DataEntity.Entity> list = new ArrayList<>();
        list.addAll(dataEntity.getZc());
        list.addAll(dataEntity.getSm());
        recyclerView.getAdapter().setNewData(list);
    }
}
