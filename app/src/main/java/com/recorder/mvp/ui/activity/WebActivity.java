package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.orhanobut.logger.Logger;
import com.recorder.R;
import com.recorder.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/app/WebActivity")
public class WebActivity extends BaseActivity {
    @Autowired(name = Constants.WEB_URL)
    String url;
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
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()//
                .defaultProgressBarColor()
                .setReceivedTitleCallback(mCallback)
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setSecutityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()
                .ready()
                .go(getUrl());
        if (!getUrl().startsWith("http")) {
            mAgentWeb.getWebCreator().get().loadData(getUrl(), "text/html; charset=UTF-8", null);
        }
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
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

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            Logger.d("js=> onJsAlert");
            AlertDialog.Builder b = new AlertDialog.Builder(WebActivity.this);
            b.setTitle("Alert");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm());
            b.setCancelable(false);
            b.create().show();
            return true;
        }

        //设置响应js 的Confirm()函数
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            Logger.d("js=> onJsConfirm");
            AlertDialog.Builder b = new AlertDialog.Builder(WebActivity.this);
            b.setTitle("Confirm");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm());
            b.setNegativeButton(android.R.string.cancel, (dialog, which) -> result.cancel());
            b.create().show();
            return true;
        }

        //设置响应js 的Prompt()函数
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
//            final View v = View.inflate(WebActivity.this, R.layout.prompt_dialog, null);
//            ((TextView) v.findViewById(R.id.prompt_message_text)).setText(message);
//            ((EditText) v.findViewById(R.id.prompt_input_field)).setText(defaultValue);
//            AlertDialog.Builder b = new AlertDialog.Builder(WebActivity.this);
//            b.setTitle("Prompt");
//            b.setView(v);
//            b.setPositiveButton(android.R.string.ok, (dialog, which) -> {
//                String value = ((EditText) v.findViewById(R.id.prompt_input_field)).getText().toString();
//                result.confirm(value);
//            });
//            b.setNegativeButton(android.R.string.cancel, (dialog, which) -> result.cancel());
//            b.create().show();
            Logger.d("js=> onJsPrompt");
            return true;
        }
    };

    public String getUrl() {
        return url;
    }

    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = (view, title) -> {
        if (findViewById(R.id.toolbar_title) != null)
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
        CommonUtils.share(this, "", "昊翔分享测试", "昊翔分享测试", "http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f19cc0079.jpg");
    }
}
