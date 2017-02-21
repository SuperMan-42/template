package com.recorder.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.core.base.CoreBaseActivity;
import com.recorder.R;

import butterknife.BindView;

public class Main2Activity extends CoreBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setToolBar(toolbar,"杭鹏伟");
    }
}
