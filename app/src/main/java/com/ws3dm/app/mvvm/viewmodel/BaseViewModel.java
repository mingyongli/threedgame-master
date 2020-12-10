package com.ws3dm.app.mvvm.viewmodel;

import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {
    public enum State {
        ERR, EMPTY, SUCCESS, LOADING
    }
}
