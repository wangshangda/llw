package com.qlm.llw.base;

import io.reactivex.rxjava3.functions.Consumer;

/**
 * Created by Administrator on 2020/5/20.
 */

public interface OnError extends Consumer<Throwable> {
    @Override
    default void accept(Throwable throwable) throws Exception {
        onError(new ErrorInfo(throwable));
    }

    void onError(ErrorInfo error) throws Exception;
}
