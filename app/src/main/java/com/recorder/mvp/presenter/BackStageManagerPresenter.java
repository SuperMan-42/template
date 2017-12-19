package com.recorder.mvp.presenter;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.http.imageloader.ImageLoader;
import com.core.integration.AppManager;
import com.core.mvp.BasePresenter;
import com.core.utils.RxLifecycleUtils;
import com.recorder.mvp.contract.BackStageManagerContract;
import com.recorder.mvp.model.entity.OrderPimanageBean;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

@ActivityScope
public class BackStageManagerPresenter extends BasePresenter<BackStageManagerContract.Model, BackStageManagerContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public BackStageManagerPresenter(BackStageManagerContract.Model model, BackStageManagerContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void orderPimanage() {
        mModel.orderPimanage()
                .compose(RxLifecycleUtils.transformer(mRootView))
                .subscribe(new ErrorHandleSubscriber<OrderPimanageBean>(mErrorHandler) {
                    @Override
                    public void onNext(OrderPimanageBean orderPimanageBean) {
//                        List<OrderPimanageBean.DataEntity.PiFilesEntity> piFiles = new ArrayList<>();
//                        List<OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity> files = new ArrayList<>();
//                        OrderPimanageBean.DataEntity.PiFilesEntity piflie = new OrderPimanageBean.DataEntity.PiFilesEntity();
//                        OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity file = new OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity();
//                        file.setCtime("2017-08-08");
//                        file.setFile("http://ustatic-test.dreamflyc.com/group1/M00/02/62/ChRYqlhY3wqATFESAERJa-O_DjA496.pdf");
//                        file.setFile_name("《第二季度财务报告》");
//                        files.add(file);
//                        OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity file11 = new OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity();
//                        file11.setCtime("2017-08-08");
//                        file11.setFile("http://ustatic-test.dreamflyc.com/group1/M00/02/62/ChRYqlhY3wqATFESAERJa-O_DjA496.pdf");
//                        file11.setFile_name("《第二季度财务报告1》");
//                        files.add(file11);
//                        OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity file12 = new OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity();
//                        file12.setCtime("2017-08-08");
//                        file12.setFile("http://ustatic-test.dreamflyc.com/group1/M00/02/62/ChRYqlhY3wqATFESAERJa-O_DjA496.pdf");
//                        file12.setFile_name("《第二季度财务报告2》");
//                        files.add(file12);
//                        OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity file13 = new OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity();
//                        file13.setCtime("2017-08-08");
//                        file13.setFile("http://ustatic-test.dreamflyc.com/group1/M00/02/62/ChRYqlhY3wqATFESAERJa-O_DjA496.pdf");
//                        file13.setFile_name("《第二季度财务报告3》");
//                        files.add(file13);
//                        piflie.setDeal_name("共享电动车项目");
//                        piflie.setDealID("123456789");
//                        piflie.setFiles(files);
//                        piFiles.add(piflie);
//                        List<OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity> files1 = new ArrayList<>();
//                        OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity file1 = new OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity();
//                        OrderPimanageBean.DataEntity.PiFilesEntity piflie1 = new OrderPimanageBean.DataEntity.PiFilesEntity();
//                        file1.setCtime("2017-02-03");
//                        file1.setFile("http://ustatic-test.dreamflyc.com/group1/M00/02/62/ChRYqlhY3wqATFESAERJa-O_DjA496.pdf");
//                        file1.setFile_name("《第一季度财务报告》");
//                        files1.add(file1);
//                        piflie1.setDeal_name("共享ofo项目");
//                        piflie1.setDealID("123456789");
//                        piflie1.setFiles(files1);
//                        piFiles.add(piflie1);
//                        List<OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity> files2 = new ArrayList<>();
//                        OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity file2 = new OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity();
//                        OrderPimanageBean.DataEntity.PiFilesEntity piflie2 = new OrderPimanageBean.DataEntity.PiFilesEntity();
//                        file2.setCtime("2017-11-03");
//                        file2.setFile("http://ustatic-test.dreamflyc.com/group1/M00/02/62/ChRYqlhY3wqATFESAERJa-O_DjA496.pdf");
//                        file2.setFile_name("《第三季度财务报告》");
//                        files2.add(file2);
//                        piflie2.setDeal_name("共享自行车项目");
//                        piflie2.setDealID("123456789");
//                        piflie2.setFiles(files2);
//                        piFiles.add(piflie2);
//
//                        List<OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity> files3 = new ArrayList<>();
//                        OrderPimanageBean.DataEntity.PiFilesEntity piflie3 = new OrderPimanageBean.DataEntity.PiFilesEntity();
//                        OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity file3 = new OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity();
//                        file3.setCtime("2017-11-04");
//                        file3.setFile("http://ustatic-test.dreamflyc.com/group1/M00/02/62/ChRYqlhY3wqATFESAERJa-O_DjA496.pdf");
//                        file3.setFile_name("《第四季度财务报告》");
//                        files3.add(file3);
//                        OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity file31 = new OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity();
//                        file31.setCtime("2017-11-04");
//                        file31.setFile("http://ustatic-test.dreamflyc.com/group1/M00/02/62/ChRYqlhY3wqATFESAERJa-O_DjA496.pdf");
//                        file31.setFile_name("《第四季度财务报告2》");
//                        files3.add(file31);
//                        OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity file32 = new OrderPimanageBean.DataEntity.PiFilesEntity.FilesEntity();
//                        file32.setCtime("2017-11-04");
//                        file32.setFile("http://ustatic-test.dreamflyc.com/group1/M00/02/62/ChRYqlhY3wqATFESAERJa-O_DjA496.pdf");
//                        file32.setFile_name("《第四季度财务报告3》");
//                        files3.add(file32);
//                        piflie3.setDeal_name("共享充电宝项目");
//                        piflie3.setDealID("123456789");
//                        piflie3.setFiles(files3);
//                        piFiles.add(piflie3);
//
//                        orderPimanageBean.getData().setPi_files(piFiles);
                        mRootView.showOrderPimanage(orderPimanageBean.getData());
                    }
                });
    }
}
