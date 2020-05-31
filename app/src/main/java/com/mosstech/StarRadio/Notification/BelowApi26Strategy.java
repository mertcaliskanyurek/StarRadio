package com.mosstech.StarRadio.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.mosstech.StarRadio.R;

public final class BelowApi26Strategy implements NotificationStrategy {
    private Context mContext;

    public BelowApi26Strategy(Context context){
        this.mContext = context;
    }

    @Override
    public Notification buildNotification(String stitle, String text, String icon, boolean isPlaying) {
        return new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.default_simge)
                .setContentTitle(stitle)
                .setContentText(text)
                .build();
    }

    @Override
    public void notify(int id,Notification notification) {
        ((NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE)).notify(id, notification);
    }
}
