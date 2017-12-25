package com.recorder.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.core.base.BaseActivity;
import com.core.di.component.AppComponent;
import com.core.utils.Constants;
import com.core.utils.CoreUtils;
import com.cunoraz.gifview.library.GifView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.link.LinkHandler;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.model.LinkTapEvent;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.recorder.R;
import com.recorder.di.component.DaggerPdfComponent;
import com.recorder.di.module.PdfModule;
import com.recorder.mvp.contract.PdfContract;
import com.recorder.mvp.presenter.PdfPresenter;
import com.recorder.utils.CommonUtils;

import java.io.File;

import butterknife.BindView;

import static com.core.utils.Preconditions.checkNotNull;

@Route(path = "/app/PdfActivity")
public class PdfActivity extends BaseActivity<PdfPresenter> implements PdfContract.View, OnDrawListener, OnPageChangeListener, OnErrorListener,
        OnPageScrollListener, OnLoadCompleteListener, OnPageErrorListener, OnRenderListener, OnTapListener, LinkHandler {

    @Autowired(name = Constants.PDF_HTTP)
    String pdf_http;
    @Autowired(name = Constants.PDF_NAME)
    String pdf_name;
    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.avi)
    GifView avi;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerPdfComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .pdfModule(new PdfModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutResID(Bundle savedInstanceState) {
        return R.layout.activity_pdf; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mPresenter.download(pdf_http, Constants.SDCARD_PATH + pdf_name + ".pdf");
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
//        CoreUtils.snackbarText(CoreUtils.getString(this, R.string.text_download));
        pdfView.fromFile(new File(message))
                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen
                .onDraw(this)
                // allows to draw something on all pages, separately for every page. Called only for visible pages
                .onDrawAll(this)
                .onLoad(this) // called after document is loaded and starts to be rendered
                .onPageChange(this)
                .onPageScroll(this)
                .onError(this)
                .onPageError(this)
                .onRender(this) // called after document is rendered for the first time
                // called on single tap, return true if handled, false to toggle scroll handle visibility
                .onTap(this)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .linkHandler(this)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();
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

    @Override
    public void handleLinkEvent(LinkTapEvent event) {

    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

    @Override
    public void onPageScrolled(int page, float positionOffset) {

    }

    @Override
    public void onInitiallyRendered(int nbPages) {

    }

    @Override
    public boolean onTap(MotionEvent e) {
        return false;
    }
}