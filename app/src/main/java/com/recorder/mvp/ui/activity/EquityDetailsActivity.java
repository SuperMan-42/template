package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.autolayout.AutoScrollView;
import com.core.widget.autolayout.AutoToolbar;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.jaeger.library.StatusBarUtil;
import com.recorder.R;
import com.recorder.di.component.DaggerEquityDetailsComponent;
import com.recorder.di.module.EquityDetailsModule;
import com.recorder.mvp.contract.EquityDetailsContract;
import com.recorder.mvp.model.entity.DealDetailBean;
import com.recorder.mvp.presenter.EquityDetailsPresenter;
import com.recorder.widget.AutoProgressBar;

import app.dinus.com.itemdecoration.LinearDividerItemDecoration;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/EquityDetailsActivity")
public class EquityDetailsActivity extends BaseActivity<EquityDetailsPresenter> implements EquityDetailsContract.View {
    @Autowired(name = Constants.IS_EQUITY)
    boolean isEquity;
    @Autowired(name = Constants.IS_GROUP)
    boolean isGroup;
    @Autowired(name = Constants.DEAL_ID)
    String dealId;
    DealDetailBean.DataEntity dataEntity;

    @BindView(R.id.scrollview)
    AutoScrollView scrollview;
    @BindView(R.id.im_bg)
    ImageView imBg;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_right)
    RelativeLayout toolbarRight;
    @BindView(R.id.im_left)
    ImageView imLeft;
    @BindView(R.id.im_right)
    ImageView imRight;
    @BindView(R.id.toolbar)
    AutoToolbar toolbar;
    @BindView(R.id.im_cover)
    ImageView imCover;
    @BindView(R.id.tv_deal_name)
    TextView tvDealName;
    @BindView(R.id.tv_brief)
    TextView tvBrief;
    @BindView(R.id.tv_labels)
    TextView tvLabels;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.progress)
    AutoProgressBar progress;
    @BindView(R.id.tv_round)
    TextView tvRound;
    @BindView(R.id.ll_isShow)
    LinearLayout llIsShow;
    @BindView(R.id.view_isShow)
    View viewIsShow;
    @BindView(R.id.tv_target_fund)
    TextView tvTargetFund;
    @BindView(R.id.tv_limit_price)
    TextView tvLimitPrice;
    @BindView(R.id.tag_limit_price)
    TextView tagLimitPrice;
    @BindView(R.id.tv_shakes)
    TextView tvShakes;
    @BindView(R.id.tag_shakes)
    TextView tagShakes;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.rv_team)
    CoreRecyclerView rvTeam;
    @BindView(R.id.ll_isShow_growth)
    LinearLayout llIsShowGrowth;
    @BindView(R.id.tv_overview)
    TextView tvOverview;
    @BindView(R.id.ll_isShow_overview)
    LinearLayout llIsShowOverview;
    @BindView(R.id.tv_finance_history)
    TextView tvFinanceHistory;
    @BindView(R.id.ll_isShow_finance_history)
    LinearLayout llIsShowFinanceHistory;
    @BindView(R.id.tv_risk)
    TextView tvRisk;
    @BindView(R.id.ll_isShow_risk)
    LinearLayout llIsShowRisk;
    @BindView(R.id.tv_opinion)
    TextView tvOpinion;
    @BindView(R.id.ll_isShow_opinion)
    LinearLayout llIsShowOpinion;
    @BindView(R.id.tv_plan)
    TextView tvPlan;
    @BindView(R.id.ll_isShow_plan)
    LinearLayout llIsShowPlan;
    @BindView(R.id.tv_withdrawal)
    TextView tvWithdrawal;
    @BindView(R.id.ll_isShow_withdrawal)
    LinearLayout llIsShowWithdrawal;
    @BindView(R.id.rv_qa)
    CoreRecyclerView rvQa;
    @BindView(R.id.ll_isShow_qa)
    LinearLayout llIsShowQa;
    @BindView(R.id.rv_public_files)
    CoreRecyclerView rvPublicFiles;
    @BindView(R.id.ll_isShow_public_files)
    LinearLayout llIsShowPublicFiles;
    @BindView(R.id.ll_isShow_video)
    LinearLayout llIsShowVideo;
    @BindView(R.id.ll_consultation)
    LinearLayout llConsultation;
    @BindView(R.id.ll_buy)
    LinearLayout llBuy;
    private float alpha;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerEquityDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .equityDetailsModule(new EquityDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        return R.layout.activity_equity_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        toolbarRight.setVisibility(View.VISIBLE);
        imLeft.setImageResource(R.drawable.back_icon_black);
        imRight.setImageResource(R.drawable.collection_icon_black);
        toolbar.getBackground().setAlpha(0);
        ARouter.getInstance().inject(this);
        mPresenter.dealDetail(dealId);
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
        switch (message) {
            case "统计电话联系数成功":
                String phone = "15383465913";
                Intent myIntentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                startActivity(myIntentDial);
                break;
            case "项目取消关注成功":
                dataEntity.setIs_follow("0");
                if (alpha <= 0.5 && alpha >= 0) {
                    imRight.setImageResource(R.drawable.collection_icon_black);
                } else if (alpha > 0.5 && alpha <= 1) {
                    imRight.setImageResource(R.drawable.collection_icon_white);
                }
                break;
            case "项目关注成功":
                dataEntity.setIs_follow("1");
                if (alpha <= 0.5 && alpha >= 0) {
                    imRight.setImageResource(R.drawable.collection_icon_choose_black);
                } else if (alpha > 0.5 && alpha <= 1) {
                    imRight.setImageResource(R.drawable.collection_icon_choose_white);
                }
                break;
        }
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

    @OnClick({R.id.toolbar_right, R.id.ll_bp_file, R.id.ll_consultation, R.id.ll_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right:
                if (dataEntity.getIs_follow().equals("1")) {//1-关注
                    mPresenter.dealUnfollow(dataEntity.getDealID());
                } else {
                    mPresenter.dealFollow(dataEntity.getDealID());
                }
                break;
            case R.id.ll_bp_file:
                break;
            case R.id.ll_consultation:
                mPresenter.dealConsult(dataEntity.getDealID());
                break;
            case R.id.ll_buy:
                ARouter.getInstance().build("/app/BuyActivity").navigation();
                break;
        }
    }

    @Override
    public void showDealDetail(DealDetailBean.DataEntity dataEntity) {
        this.dataEntity = dataEntity;
        //顶部变色bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            translucentBar();
        }
        llBuy.setVisibility(isEquity ? View.VISIBLE : View.GONE);//私募和众筹的底部按钮不同
        tvDealName.setText(dataEntity.getDeal_name());//头部标题
        CoreUtils.imgLoader(this, dataEntity.getCover(), imCover);//头部头像
        Glide.with(this).load(dataEntity.getCover()).apply(bitmapTransform(new BlurTransformation())).into(imBg);//头部高斯模糊背景
        tvBrief.setText(dataEntity.getBrief());//头部简介
        tvLabels.setText(dataEntity.getLabels());//标签
        //进度条相关
        tvProgress.setText(dataEntity.getProgress() + "%");
        if (dataEntity.getProgress() == 0) {
            progress.reset();
        } else {
            progress.setProgress(dataEntity.getProgress());
        }
        tvRound.setText(dataEntity.getRound());//轮次
        tvTargetFund.setText(dataEntity.getTarget_fund() + "万");
        if (dataEntity.getIs_group().equals("1")) {//1-是 0-不是 众筹组合
            llIsShow.setVisibility(View.GONE);
            viewIsShow.setVisibility(View.GONE);
            tagShakes.setText("项目数");
            tvShakes.setText(dataEntity.getNumber());
        } else {
            if (isEquity) {//众筹非组合
                tvLimitPrice.setText(dataEntity.getLimit_price() + "万");
                tvShakes.setText(dataEntity.getShakes() + "%");
            } else {//私募
                llIsShow.setVisibility(View.GONE);
                viewIsShow.setVisibility(View.GONE);
                tvShakes.setText(dataEntity.getShakes() + "%");
            }
        }
        //以下应该只分组合非组合的情况 无众筹私募的区分
        tv1.setText(isGroup ? "项目亮点" : "项目介绍");
        tvIntro.setText(dataEntity.getIntro());
        LinearLayoutManager teamManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        teamManager.setAutoMeasureEnabled(true);
        LinearDividerItemDecoration dividerItemDecoration = new LinearDividerItemDecoration(this, LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL);
        dividerItemDecoration.setDivider(CoreUtils.getDrawablebyResource(this, R.drawable.bga_divider));
        //团队成员
        rvTeam.init(teamManager, new BaseQuickAdapter<DealDetailBean.DataEntity.TeamEntity, BaseViewHolder>(R.layout.item_detail_team, dataEntity.getTeam()) {
            @Override
            protected void convert(BaseViewHolder holder, DealDetailBean.DataEntity.TeamEntity item) {
                CoreUtils.imgLoader(getApplication(), "http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg", holder.getView(R.id.im_avator));
                holder.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_position, item.getPosition())
                        .setText(R.id.tv_intro, item.getIntro());
            }
        }, false);
        rvTeam.getRecyclerView().addItemDecoration(dividerItemDecoration);
        //项目动态(可隐藏)//TODO
        if (dataEntity.getGrowth() == null || dataEntity.getGrowth().size() == 0) {
            llIsShowGrowth.setVisibility(View.GONE);
        } else {
            llIsShowGrowth.setVisibility(View.VISIBLE);
        }
        //行业概况(非组合)
        if (isGroup) {
            llIsShowOverview.setVisibility(View.GONE);
        } else {
            llIsShowOverview.setVisibility(View.VISIBLE);
            tvOverview.setText(dataEntity.getOverview());
        }
        //融资历史(非组合)
        if (isGroup) {
            llIsShowFinanceHistory.setVisibility(View.GONE);
        } else {
            llIsShowFinanceHistory.setVisibility(View.VISIBLE);
            tvFinanceHistory.setText(dataEntity.getFinance_history());
        }
        //项目风险(可隐藏)
        if (TextUtils.isEmpty(dataEntity.getRisk())) {
            llIsShowRisk.setVisibility(View.GONE);
        } else {
            llIsShowRisk.setVisibility(View.VISIBLE);
            tvRisk.setText(dataEntity.getRisk());
        }
        //专家及管理团队意见
        tvOpinion.setText(dataEntity.getOpinion());
        //投资方案
        tvPlan.setText(dataEntity.getPlan());
        //退出渠道
        tvWithdrawal.setText(dataEntity.getWithdrawal());
        //QA
        LinearLayoutManager qaManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        qaManager.setAutoMeasureEnabled(true);
        rvQa.init(qaManager, new BaseQuickAdapter<DealDetailBean.DataEntity.QaEntity, BaseViewHolder>(R.layout.item_detail_qa, dataEntity.getQa()) {
            @Override
            protected void convert(BaseViewHolder holder, DealDetailBean.DataEntity.QaEntity item) {
                holder.setText(R.id.tv_question, (holder.getAdapterPosition() + 1) + "、" + item.getQuestion())
                        .setText(R.id.tv_answer, item.getAnswer());
            }
        }, false);
        rvQa.getRecyclerView().addItemDecoration(dividerItemDecoration);
        //投资文件(非组合)
        if (isGroup) {
            llIsShowPublicFiles.setVisibility(View.GONE);
        } else {
            llIsShowPublicFiles.setVisibility(View.VISIBLE);
            LinearLayoutManager gridMganager = new GridLayoutManager(this, 3) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            gridMganager.setAutoMeasureEnabled(true);
            rvPublicFiles.init(gridMganager, new BaseQuickAdapter<DealDetailBean.DataEntity.PublicFilesEntity, BaseViewHolder>(R.layout.item_detail_publicfiles, dataEntity.getPublic_files()) {
                @Override
                protected void convert(BaseViewHolder holder, DealDetailBean.DataEntity.PublicFilesEntity item) {
                    holder.setText(R.id.tv_file_name, item.getFile_name());
                    holder.itemView.setOnClickListener(view -> ARouter.getInstance().build("/app/PdfActivity")
                            .withString(Constants.PDF_HTTP, item.getFile()).withString(Constants.PDF_NAME, item.getFile_name()).navigation());
                }
            }, false);
        }
        //视频(可隐藏)//TODO
        if (dataEntity.getVideo() == null || TextUtils.isEmpty(dataEntity.getVideo().getVideo_id())) {
            llIsShowVideo.setVisibility(View.GONE);
        } else {
            llIsShowVideo.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void translucentBar() {
        scrollview.setTranslucentListener((alpha, t) -> {
            this.alpha = alpha;
            toolbar.getBackground().setAlpha((int) (alpha * 255));
            if (t >= CoreUtils.dip2px(this, 148)) {
                title(dataEntity.getDeal_name());
                tvDealName.setVisibility(View.INVISIBLE);
            } else {
                title("");
                tvDealName.setVisibility(View.VISIBLE);
            }
            if (alpha <= 0.5 && alpha >= 0) {
                if ("1".equals(dataEntity.getIs_follow())) {
                    imRight.setImageResource(R.drawable.collection_icon_choose_black);
                } else {
                    imRight.setImageResource(R.drawable.collection_icon_black);
                }
                imLeft.setImageResource(R.drawable.back_icon_black);
                imRight.setImageResource(R.drawable.collection_icon_black);
                toolbarTitle.setTextColor(Color.argb(0, 255, 255, 255));
                imLeft.setImageAlpha((int) ((1 - alpha * 2) * 255));
                imRight.setImageAlpha((int) ((1 - alpha * 2) * 255));
            } else if (alpha > 0.5 && alpha <= 1) {
                if ("1".equals(dataEntity.getIs_follow())) {
                    imRight.setImageResource(R.drawable.collection_icon_choose_white);
                } else {
                    imRight.setImageResource(R.drawable.collection_icon_white);
                }
                imLeft.setImageResource(R.drawable.back_icon_white);
                imRight.setImageResource(R.drawable.collection_icon_white);
                toolbarTitle.setTextColor(Color.argb((int) (((alpha - 0.5) * 2) * 255), 255, 255, 255));
                imLeft.setImageAlpha((int) (((alpha - 0.5) * 2) * 255));
                imRight.setImageAlpha((int) (((alpha - 0.5) * 2) * 255));
            }
        });
    }
}
