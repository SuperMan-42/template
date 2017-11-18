package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.core.base.AdapterViewPager;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.recorder.R;
import com.recorder.di.component.DaggerHomeComponent;
import com.recorder.di.module.HomeModule;
import com.recorder.mvp.contract.HomeContract;
import com.recorder.mvp.presenter.HomePresenter;
import com.recorder.mvp.ui.fragment.HomeFragment;
import com.recorder.widget.AutoPageNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.majiajie.pagerbottomtabstrip.NavigationController;

import static com.core.utils.Preconditions.checkNotNull;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {
    @BindView(R.id.navigation)
    AutoPageNavigationView navigation;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar_left)
    View back;
    @BindView(R.id.ll_filter)
    View llFilter;

    private static boolean isFilterOpen = false;
    NavigationController mNavigationController;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_home; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title("首页");
        back.setVisibility(View.INVISIBLE);
        mNavigationController = navigation.material()
                .addItem(R.mipmap.ic_nav_theme, CoreUtils.getString(this, R.string.Home))
                .addItem(R.mipmap.ic_nav_theme, CoreUtils.getString(this, R.string.Equity))
                .addItem(R.mipmap.ic_nav_theme, CoreUtils.getString(this, R.string.Private))
                .addItem(R.mipmap.ic_nav_theme, CoreUtils.getString(this, R.string.Dynamic))
                .addItem(R.mipmap.ic_nav_theme, CoreUtils.getString(this, R.string.My))
                .build();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        CharSequence[] strings = new CharSequence[]{CoreUtils.getString(this, R.string.Home), CoreUtils.getString(this, R.string.Equity),
                CoreUtils.getString(this, R.string.Private), CoreUtils.getString(this, R.string.Dynamic), CoreUtils.getString(this, R.string.My)};
        AdapterViewPager adapterViewPager = new AdapterViewPager(getSupportFragmentManager(), fragments, strings);
        viewPager.setAdapter(adapterViewPager);
        mNavigationController.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title(viewPager.getAdapter().getPageTitle(viewPager.getCurrentItem()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
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

    @OnClick({R.id.toolbar_left, R.id.toolbar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left:
                llFilter.setVisibility(isFilterOpen ? View.VISIBLE : View.GONE);
                isFilterOpen = !isFilterOpen;
                break;
            case R.id.toolbar_right:
                break;
        }
    }
}
