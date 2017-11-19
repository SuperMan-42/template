package com.core.utils;

/**
 * Created by jess on 11/10/2016 16:39
 * Contact with jess.yan.effort@gmail.com
 */

public class RxUtils {

//    public static <T> ObservableTransformer<T, T> applySchedulers(final IView view) {
//        return new ObservableTransformer<T, T>() {
//            @Override
//            public Observable<T> apply(Observable<T> observable) {
//                return observable.subscribeOn(Schedulers.io())
//                        .doOnSubscribe(disposable -> {
//                            //显示进度条
//                            view.showLoading();
//                        })
//                        .subscribeOn(AndroidSchedulers.mainThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        //隐藏进度条
//                        .doAfterTerminate(view::hideLoading)
//                        .compose(RxUtils.bindToLifecycle(view));
//            }
//        };
//    }


//    public static <T> LifecycleTransformer<T> bindToLifecycle(IView view) {
//        if (view instanceof AppCompatActivity) {
//            return ((BaseActivity) view);
//        } else if (view instanceof BaseFragment) {
//            return ((BaseFragment) view).bindToLifecycle();
//        } else {
//            throw new IllegalArgumentException("view isn't activity or fragment");
//        }
//    }
}