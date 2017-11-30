package com.recorder.mvp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.jaeger.library.StatusBarUtil;
import com.recorder.R;

import butterknife.BindView;

/**
 * Created by hpw on 17-11-30.
 */

@Route(path = "/app/VideoActivity")
public class VideoActivity extends BaseActivity {
    @Autowired
    String vid;
    @Autowired
    String auth;
    @BindView(R.id.video)
    AliyunVodPlayerView mAliyunVodPlayerView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        return R.layout.activity_video;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        AliyunPlayAuth.AliyunPlayAuthBuilder aliyunPlayAuthBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
        aliyunPlayAuthBuilder.setVid(vid);
        aliyunPlayAuthBuilder.setPlayAuth(auth);
        aliyunPlayAuthBuilder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_LOW);
        AliyunPlayAuth mPlayAuth = aliyunPlayAuthBuilder.build();
        mAliyunVodPlayerView.setAuthInfo(mPlayAuth);
        mAliyunVodPlayerView.setAutoPlay(true);
        mAliyunVodPlayerView.setOnPreparedListener(() -> mAliyunVodPlayerView.setVisibility(View.VISIBLE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAliyunVodPlayerView != null)
            mAliyunVodPlayerView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAliyunVodPlayerView != null)
            mAliyunVodPlayerView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAliyunVodPlayerView != null)
            mAliyunVodPlayerView.onDestroy();
    }
}
