package com.mosstech.StarRadio.Notification;

import android.app.Notification;

public interface NotificationStrategy {

    Notification buildNotification(String stitle, String text, int smallIcon);
    void notify(int id,Notification notification);
}
