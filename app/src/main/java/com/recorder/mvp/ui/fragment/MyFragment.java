package com.recorder.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseFragment;
import com.core.di.component.AppComponent;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.cache.BCache;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.core.widget.recyclerview.BaseQuickAdapter;
import com.core.widget.recyclerview.BaseViewHolder;
import com.core.widget.recyclerview.CoreRecyclerView;
import com.google.gson.Gson;
import com.recorder.R;
import com.recorder.di.component.DaggerMyComponent;
import com.recorder.di.module.MyModule;
import com.recorder.mvp.contract.MyContract;
import com.recorder.mvp.model.entity.Bean;
import com.recorder.mvp.model.entity.LoginBean;
import com.recorder.mvp.model.entity.UserInfoBean;
import com.recorder.mvp.presenter.MyPresenter;
import com.recorder.utils.CommonUtils;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/MyFragment")
public class MyFragment extends BaseFragment<MyPresenter> implements MyContract.View {
    @BindView(R.id.recyclerview)
    CoreRecyclerView recyclerView;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_my_investment)
    TextView tvMyInvestment;
    @BindView(R.id.tv_follow_count)
    TextView tvFollowCount;
    @BindView(R.id.tv_post_investment)
    TextView tvPostInvestment;
    @BindView(R.id.tv_auth_type)
    TextView tvAuthType;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMyComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myModule(new MyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (TextUtils.isEmpty(BCache.getInstance().getString(Constants.TOKEN))) {
            tvUserName.setText("您还没登录哦");
            CoreUtils.imgLoaderCircle(getContext(), R.drawable.ic_person, profileImage, R.drawable.ic_person);
            tvLogin.setVisibility(View.VISIBLE);
            tvMyInvestment.setText("0");
            tvFollowCount.setText("0");
            tvPostInvestment.setText("0");
        } else {
            mPresenter.userInfo();
        }
        List<Bean> list = new ArrayList<>();
        list.add(new Bean<>(R.drawable.ic_news, "我的消息", "/app/MyMessageActivity"));
        list.add(new Bean<>(R.drawable.ic_help, "投资帮助", "/app/HelpListActivity"));
        list.add(new Bean<>(R.drawable.ic_setting, "设置", "/app/SettingActivity"));
        list.add(new Bean<>(R.drawable.ic_project, "推荐项目", "/app/DealRecommendActivity"));
        list.add(new Bean<>(R.drawable.ic_recommend, "推荐给朋友", "recommend"));
        list.add(new Bean<>(R.drawable.ic_auth, "投资人认证", "/app/AuthActivity"));
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.init(layoutManager, new BaseQuickAdapter<Bean, BaseViewHolder>(R.layout.item_my, list) {
            @Override
            protected void convert(BaseViewHolder holder, Bean item) {
                holder.setImageResource(R.id.im_pic, (Integer) item.getKey());
                holder.setText(R.id.tv_content, item.getValue());
                holder.itemView.setOnClickListener(view1 -> {
                    switch (item.getOther()) {
                        case "recommend":
                            CommonUtils.share(getActivity(), "http://baidu.com", "昊翔分享测试", "昊翔分享测试", "http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f19cc0079.jpg");
                            break;
                        default:
                            ARouter.getInstance().build(item.getOther()).navigation();
                            break;
                    }
                });
            }
        }, false);
        recyclerView.getRecyclerView().addItemDecoration(CommonUtils.girdDivider(getContext()));
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

    @Subscriber(tag = Constants.RETRY_MY)
    private void retry(LoginBean loginBean) {
        mPresenter.userInfo();
    }

    @Subscriber(tag = "loginout")
    private void loginout(LoginBean loginBean) {
        tvUserName.setText("您还没登录哦");
        CoreUtils.imgLoaderCircle(getContext(), R.drawable.ic_person, profileImage, R.drawable.ic_person);
        tvLogin.setVisibility(View.VISIBLE);
        tvAuthType.setVisibility(View.GONE);
        tvMyInvestment.setText("0");
        tvFollowCount.setText("0");
        tvPostInvestment.setText("0");
    }

    @Subscriber(tag = "person_avatar")
    private void avatar(String avatar) {
        CoreUtils.imgLoaderCircle(getContext(), avatar, profileImage, R.drawable.ic_person);
    }

    @Subscriber(tag = "person_name")
    private void name(String name) {
        tvUserName.setText(name);
    }

    @OnClick({R.id.tv_login, R.id.profile_image, R.id.ll_investment, R.id.ll_attention, R.id.ll_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                ARouter.getInstance().build("/app/LoginActivity").withString(Constants.RETRY_WHEN_LOGIN_OR_AUTH, Constants.RETRY_MY).navigation();
                break;
            case R.id.profile_image:
                ARouter.getInstance().build("/app/PersonActivity").navigation();
                break;
            case R.id.ll_investment:
                ARouter.getInstance().build("/app/MyInvestmentActivity").navigation();
                break;
            case R.id.ll_attention:
                ARouter.getInstance().build("/app/MyAttentionActivity").navigation();
                break;
            case R.id.ll_manager:
                ARouter.getInstance().build("/app/BackStageManagerActivity").navigation();
                break;
        }
    }

    @Override
    public void showUserInfo(ImageLoader imageLoader, UserInfoBean userInfoBean) {
        BCache.getInstance().put(Constants.USER_INFO, new Gson().toJson(userInfoBean));
        tvLogin.setVisibility(View.GONE);
        CoreUtils.imgLoaderCircle(getContext(), userInfoBean.getData().getAvatar(), profileImage, R.drawable.ic_person);
        tvUserName.setText(userInfoBean.getData().getUser_name());
        switch (userInfoBean.getData().getAuth_type()) {
            case 0:
                tvAuthType.setText("未认证");
                break;
            case 1:
                tvAuthType.setText("众筹认证");
                break;
            case 2:
                tvAuthType.setText("合格认证");
                break;
            case 3:
                tvAuthType.setText("机构认证");
                break;
        }
//        tvAuthType.setVisibility(View.VISIBLE);
        tvMyInvestment.setText(userInfoBean.getData().getMy_investment());
        tvFollowCount.setText(userInfoBean.getData().getMy_follow_count());
        tvPostInvestment.setText(userInfoBean.getData().getPost_investment());
    }

}
