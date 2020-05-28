package com.mosstech.StarRadio.Service;

public interface MusicServiceListener {
    void onPreparing();
    void onStarted();
    void onError(String error);
}
