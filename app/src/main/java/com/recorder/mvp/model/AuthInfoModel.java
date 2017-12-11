package com.recorder.mvp.model;

import android.app.Application;

import com.core.di.scope.ActivityScope;
import com.core.integration.IRepositoryManager;
import com.core.mvp.BaseModel;
import com.core.utils.DeviceUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.recorder.mvp.contract.AuthInfoContract;
import com.recorder.mvp.model.api.cache.ApiCache;
import com.recorder.mvp.model.api.service.ApiService;
import com.recorder.mvp.model.entity.AuthGetBean;
import com.recorder.mvp.model.entity.ImageUploadBean;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;
import okhttp3.MultipartBody;

@ActivityScope
public class AuthInfoModel extends BaseModel implements AuthInfoContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public AuthInfoModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<AuthGetBean> authGet(int type) {
        return Observable.just(mRepositoryManager.obtainRetrofitService(ApiService.class).authGet("1", type))
                .flatMap(resultObservable -> mRepositoryManager.obtainCacheService(ApiCache.class)
                        .authGet(resultObservable, new EvictProvider(DeviceUtils.netIsConnected(mApplication)))
                        .map(Reply::getData));
    }

    @Override
    public Observable<Object> authPerson(int type, String true_name, String id_card, String idcard_imgf, String idcard_imgb, String check, String assets) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).authPerson("1", type, true_name, id_card, idcard_imgf, idcard_imgb, check, assets);
    }

    @Override
    public Observable<Object> authOrgan(String organ_name, String legal_person, String contact, String license, String check, String assets) {
        Logger.d("organ_name=> " + organ_name + " legal_person=> " + legal_person + " contact=> " + contact + " license=> " + license + " check=> " + check + " assets=> " + assets);
        return mRepositoryManager.obtainRetrofitService(ApiService.class).authOrgan("1", organ_name, legal_person, contact, license, check, assets);
    }

    @Override
    public Observable<ImageUploadBean> imageUpload(List<MultipartBody.Part> images) {
        return mRepositoryManager.obtainRetrofitService(ApiService.class).imageUpload("1", images);
    }
}
