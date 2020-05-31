package com.mosstech.StarRadio.Notification;

import android.app.Notification;
import android.graphics.Bitmap;

public interface NotificationStrategy {

    Notification buildNotification(String channelName, String playListName, String channelIcon, boolean isPlaying);
    void notify(int id,Notification notification);
}
