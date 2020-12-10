package com.ws3dm.app.mvp.view;

public interface IBaseView {

    void onLoadStart();

    void onLoadSuccess();

    void onLoadFailure(int code, String errorMsg);

}
