package com.recorder.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.core.base.BaseFragment;
import com.core.di.component.AppComponent;
import com.recorder.R;

/**
 * Created by hpw on 17-11-16.
 */

public class MyFragment extends BaseFragment {

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void setData(Object data) {

    }
}
