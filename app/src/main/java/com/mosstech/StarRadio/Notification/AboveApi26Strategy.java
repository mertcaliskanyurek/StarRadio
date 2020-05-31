package com.mosstech.StarRadio.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.mosstech.StarRadio.MainActivity;
import com.mosstech.StarRadio.PlayerFullScreenActivity;
import com.mosstech.StarRadio.R;
import com.mosstech.StarRadio.Service.MusicService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static android.content.Context.NOTIFICATION_SERVICE;

public final class AboveApi26Strategy implements NotificationStrategy {

    public static final String CHANNEL_NAME = "NotificationChannel";

    private Context mContext;
    private NotificationManager mNotiManager;

    public AboveApi26Strategy(Context context){
        this.mContext = context;
        mNotiManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            initChannel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initChannel()
    {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_NAME,
                "Star Radio Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        mNotiManager = mContext.getSystemService(NotificationManager.class);
        mNotiManager.createNotificationChannel(serviceChannel);
    }


    @Override
    public Notification buildNotification(String channelName, String playListName, String channelIcon, boolean isPlaying) {
        final RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.notification_layout);

        Intent notificationIntent = new Intent(mContext, PlayerFullScreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                0, notificationIntent, 0);

        Intent previousIntent = new Intent(mContext, MusicService.class);
        previousIntent.setAction(MusicService.ACTION_PREVIOUS);
        PendingIntent ppreviousIntent = PendingIntent.getService(mContext, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(mContext, MusicService.class);
        playIntent.setAction(MusicService.ACTION_PLAY);
        PendingIntent pplayIntent = PendingIntent.getService(mContext, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(mContext, MusicService.class);
        nextIntent.setAction(MusicService.ACTION_NEXT);
        PendingIntent pnextIntent = PendingIntent.getService(mContext, 0,
                nextIntent, 0);

        /*Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);*/

        views.setOnClickPendingIntent(R.id.imageView_notification_play, pplayIntent);
        views.setOnClickPendingIntent(R.id.imageView_notification_next, pnextIntent);
        views.setOnClickPendingIntent(R.id.imageView_notification_previous, ppreviousIntent);

        if(isPlaying)
            views.setImageViewResource(R.id.imageView_notification_play, R.drawable.button_stop);
        else
            views.setImageViewResource(R.id.imageView_notification_play, R.drawable.button_play);

        if(channelIcon != null && !channelIcon.equals(""))
            Picasso.get()
                    .load(channelIcon)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            views.setImageViewBitmap(R.id.imageView_notification_chnlogo, bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            views.setImageViewResource(R.id.imageView_notification_chnlogo, R.drawable.default_simge);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

        if(channelName != null && !channelName.equals(""))
        {
            views.setTextViewText(R.id.imageView_notification_chnName, channelName);
            views.setTextViewText(R.id.imageView_notification_playlistName, playListName);
        }

        return new NotificationCompat.Builder(mContext,CHANNEL_NAME)
                .setCustomContentView(views)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.default_simge)
                .setOnlyAlertOnce(true)
                .build();
    }

    @Override
    public void notify(int id,Notification notification) {
        mNotiManager.notify(id,notification);
    }

}
