package com.recorder.mvp.contract;

import com.core.mvp.IModel;
import com.core.mvp.IView;
import com.recorder.mvp.model.entity.ImageUploadBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

public interface PersonContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<ImageUploadBean> imageUpload(List<MultipartBody.Part> images);

        Observable<Object> userModify(String field, String user_name, String intro, String email, String weixin, String address, String avatar);
    }
}
