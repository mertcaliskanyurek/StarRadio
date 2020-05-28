package com.mosstech.StarRadio.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.mosstech.StarRadio.Models.IChannel;
import com.mosstech.StarRadio.Notification.AboveApi26Strategy;
import com.mosstech.StarRadio.Notification.BelowApi26Strategy;
import com.mosstech.StarRadio.Notification.NotificationStrategy;
import com.mosstech.StarRadio.R;

import java.io.IOException;
import java.util.List;
import java.util.Random;


public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{

    private static final String TAG = MusicService.class.getSimpleName();

    //notification id
    private static final int NOTIFY_ID=1;

    //media player
    private MediaPlayer mPlayer;
    //song list
    private List<IChannel> mChannels;
    //current position
    private int mChnIndex;
    //shuffle flag and random
    private boolean mShuffle;
    //preparing
    private boolean mIsPreparing = false;
    private String mPlayListName;

    //notification
    private NotificationStrategy mNotificationStrategy;

    //listener for this service
    MusicServiceListener mListener = null;
    //binder
    private final IBinder musicBind = new MusicBinder();

    public void onCreate(){
        //create the service
        super.onCreate();
        //initialize position
        mChnIndex = 0;
        //create player
        mPlayer = new MediaPlayer();
        //set default suffle
        mShuffle = false;
        //set preparing
        mIsPreparing = false;
        //init notification strategy
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            mNotificationStrategy = new AboveApi26Strategy(this);
        else
            mNotificationStrategy = new BelowApi26Strategy(this);
        //initialize
        initMusicPlayer();
    }

    //binder
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String text = "";
            if(mChannels != null && mChannels.size() > 0)
                text = mChannels.get(mChnIndex).getName();

            Notification notification = mNotificationStrategy.
                    buildNotification(getString(R.string.text_noti_title),text,R.drawable.default_info);

            startForeground(NOTIFY_ID,notification);

            Log.i(TAG,"Service Started with START_NOT_STICKY! Receiver registered");
            return START_NOT_STICKY;
        }

        Log.i(TAG,"Service Started with START_STICKY! Receiver registered");
        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mIsPreparing = false;
        mp.start();
        String text = mChannels.get(mChnIndex).getName();
        Notification noti = mNotificationStrategy
                .buildNotification(getString(R.string.text_noti_title),text,R.drawable.default_simge);
        mNotificationStrategy.notify(NOTIFY_ID,noti);
        if(mListener != null)
            mListener.onStarted();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v(TAG, "Playback Error");
        stopPlayer();
        if(mListener != null)
            mListener.onError("Playback Error");
        return false;
    }

    public void initMusicPlayer(){

        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setVolume(100,100);
        //set listeners
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnErrorListener(this);
    }

    //play current channel
    public void playChannel(){
        if(mChannels == null || mChannels.size() == 0)
        {
            Log.w(TAG,"Set channels before call play channel");
            return;
        }

        stopPlayer();
        //get song
        //set the data source
        try{
            String url = mChannels.get(mChnIndex).getUrl();
            mPlayer.setDataSource(url);
            mPlayer.prepareAsync();

            mIsPreparing = true;
            if(mListener != null)
                mListener.onPreparing();
        }
        catch(Exception e){
            Log.e(TAG, "Error setting data source", e);
        }
    }

    public void stopPlayer() {
        if(mPlayer.isPlaying())
            mPlayer.stop();
        mPlayer.reset();
        mIsPreparing = false;
    }

    //skip to previous track
    public void playPrev(){
        if(mChannels == null || mChannels.size() == 0)
        {
            Log.w(TAG,"Set channels before call play prev");
            return;
        }

        mChnIndex--;
        if(mChnIndex <0) mChnIndex = mChannels.size()-1;
        playChannel();
    }

    //skip to next
    public void playNext(){
        if(mChannels == null || mChannels.size() == 0)
        {
            Log.w(TAG,"Set channels before call play next");
            return;
        }

        if(mShuffle){
            int newChn = mChnIndex;
            Random rand = new Random();
            while(newChn == mChnIndex){
                newChn = rand.nextInt(mChannels.size());
            }
            mChnIndex = newChn;
        }
        else{
            mChnIndex++;
            if(mChnIndex >= mChannels.size()) mChnIndex =0;
        }
        playChannel();
    }

    @Override
    public void onDestroy() {
        stopPlayer();
        stopService();
    }

    public void stopService()
    {
        stopPlayer();
        mPlayer.release();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            stopForeground(true);
        stopSelf();
    }

    //play channel
    public void playChannel(int indexOfList){
        this.mChnIndex = indexOfList;
        playChannel();
    }

    public boolean isPlaying(){
        if(mPlayer == null)
            return false;

        return mPlayer.isPlaying();
    }

    public void playChannel(List<IChannel> list, String playList, int chnIndex)
    {
        if(mPlayer.isPlaying() || mIsPreparing)
            stopPlayer();
        mChannels = list;
        mChnIndex = chnIndex;
        mPlayListName = playList;

        playChannel();
    }

    public int getChnIndex() {return mChnIndex; }

    public IChannel getChannel(){
        if(mChannels == null || mChannels.size() == 0)
        {
            Log.w(TAG,"Set channels before call get channel");
            return null;
        }

        return mChannels.get(mChnIndex);
    }

    public void setListener(MusicServiceListener listener) {
        mListener = listener;
    }

    //toggle shuffle
    public void changeShuffle() {
        mShuffle = !mShuffle;}

    public boolean getShuffle() { return mShuffle; }

    public boolean isPreparing() {
        return mIsPreparing;
    }

    public String getPlayListName() {
        if(mPlayListName == null)
            return getString(R.string.text_playlist_name);

        return mPlayListName;
    }
    public String getChannelName() {
        if(mChannels == null || mChannels.size() == 0)
            return getString(R.string.text_channel_name);

        return mChannels.get(mChnIndex).getName();
    }

}
