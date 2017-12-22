package com.recorder.mvp.contract;

import com.core.mvp.IModel;
import com.core.mvp.IView;
import com.recorder.mvp.model.entity.AuthBean;
import com.recorder.mvp.model.entity.AuthGetBean;
import com.recorder.mvp.model.entity.ImageUploadBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

public interface AuthInfoContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void showAuthGet(AuthGetBean.DataEntity data);

        void showSuccess(int type, String url);

        void showFail(String msg);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<AuthGetBean> authGet(int tpye);

        Observable<AuthBean> authPerson(int type, String true_name, String id_card, String idcard_imgf, String idcard_imgb, String check, String assets);

        Observable<AuthBean> authOrgan(String organ_name, String legal_person, String contact, String license, String check, String assets);

        Observable<ImageUploadBean> imageUpload(List<MultipartBody.Part> images);
    }
}
