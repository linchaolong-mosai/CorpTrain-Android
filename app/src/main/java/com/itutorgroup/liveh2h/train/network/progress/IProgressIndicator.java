package com.itutorgroup.liveh2h.train.network.progress;

/**
 * Created by Rays on 16/5/12.
 */
public interface IProgressIndicator {
    void showErrorInfo(String message);
    void showProgress();
    void dismissProgress();
}
