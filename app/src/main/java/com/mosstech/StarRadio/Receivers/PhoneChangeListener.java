package com.mosstech.StarRadio.Receivers;

public interface PhoneChangeListener {
    void onNetworkChange(boolean disconnected);
    void onHeadsetPlugChange(boolean unPlugged);
    void onCall();
}
