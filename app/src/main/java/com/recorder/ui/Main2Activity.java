package com.recorder.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.core.base.AdapterViewPager;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.CoreUtils;
import com.recorder.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;

public class Main2Activity extends BaseActivity {

    @BindView(R.id.navigation)
    PageNavigationView navigation;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    NavigationController mNavigationController;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_main2;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setStatusBarColor(CoreUtils.getColor(this, R.color.colorPrimary), 0);
        title("昊翔");
        mNavigationController = navigation.material()
                .addItem(R.drawable.ic_nearby_teal_24dp, CoreUtils.getString(this, R.string.Home))
                .addItem(R.drawable.ic_nearby_teal_24dp, CoreUtils.getString(this, R.string.Equity))
                .addItem(R.drawable.ic_nearby_teal_24dp, CoreUtils.getString(this, R.string.Private))
                .addItem(R.drawable.ic_nearby_teal_24dp, CoreUtils.getString(this, R.string.Dynamic))
                .addItem(R.drawable.ic_nearby_teal_24dp, CoreUtils.getString(this, R.string.My))
                .build();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyFragment());
        fragments.add(new MyFragment());
        fragments.add(new MyFragment());
        fragments.add(new MyFragment());
        fragments.add(new MyFragment());
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
}
