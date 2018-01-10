package com.recorder.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.orhanobut.logger.Logger;
import com.recorder.R;
import com.recorder.utils.BitmapUtil;
import com.recorder.utils.CommonUtils;
import com.umeng.socialize.media.UMImage;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/app/WebActivity")
public class WebActivity extends BaseActivity {
    @Autowired(name = Constants.WEB_URL)
    String url;
    @Autowired(name = Constants.TITLE)
    String title;
    @Autowired(name = Constants.IS_SHOW_RIGHT)
    boolean right;
    protected AgentWeb mAgentWeb;
    @BindView(R.id.im_right)
    ImageView imRight;
    @BindView(R.id.toolbar_right)
    RelativeLayout toolbarRight;
    private LinearLayout mLinearLayout;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_web;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if (right) {
            imRight.setImageResource(R.drawable.ic_share);
            toolbarRight.setVisibility(View.VISIBLE);
        }
        mLinearLayout = this.findViewById(R.id.container);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .defaultProgressBarColor()
                .setReceivedTitleCallback(mCallback)
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setSecutityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()
                .ready()
                .go(getUrl());
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(mAgentWeb, this));
        if (!getUrl().startsWith("http")) {
            mAgentWeb.getWebCreator().get().loadData(getUrl(), "text/html; charset=UTF-8", null);
        }
        if (!TextUtils.isEmpty(title)) {
            title(title);
        }
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.d("js=> shouldOverrideUrlLoading url " + url);
            switch (url) {
                case "hx://outh-commit":
                    new Handler().post(() -> {
                        finish();
                        EventBus.getDefault().post("hx://outh-commit", Constants.AUTH_INFO_SUCCESS);
                    });
                    break;
                case "hx://outh-list":
                    new Handler().post(() -> ARouter.getInstance().build("/app/AuthActivity").navigation());
                    break;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
        }
    };

    public String getUrl() {
        return url;
    }

    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = (view, title) -> {
        if (findViewById(R.id.toolbar_title) != null && getUrl().startsWith("http"))
            title(title);
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb.handleKeyEvent(keyCode, event) && getUrl().startsWith("http") || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    @OnClick(R.id.toolbar_right)
    public void onViewClicked() {
        CommonUtils.share(this, url, "新闻分享", title, new UMImage(this, BitmapUtil.getBitmapFromDrawable(CoreUtils.getDrawablebyResource(this, R.drawable.ic_wx_share))));
    }

    public static class AndroidInterface {

        private Handler deliver = new Handler(Looper.getMainLooper());
        private AgentWeb agent;
        private Context context;

        public AndroidInterface(AgentWeb agent, Context context) {
            this.agent = agent;
            this.context = context;
        }

        @JavascriptInterface
        public void callAndroid(final String msg) {
            deliver.post(() -> {
                Logger.d("js=> " + Thread.currentThread());
                Toast.makeText(context.getApplicationContext(), "" + msg, Toast.LENGTH_LONG).show();
            });
        }
    }
}
