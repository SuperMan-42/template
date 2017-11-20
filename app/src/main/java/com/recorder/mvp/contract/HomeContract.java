package com.recorder.mvp.contract;

import com.core.http.imageloader.ImageLoader;
import com.core.mvp.IModel;
import com.core.mvp.IView;
import com.recorder.mvp.model.entity.ReferFilter;

import io.reactivex.Observable;

public interface HomeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showFilter(ImageLoader imageLoader, ReferFilter referFilter);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<ReferFilter> getFilter();
    }
}
