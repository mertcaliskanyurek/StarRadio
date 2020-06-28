package com.mosstech.StarRadio.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.mosstech.StarRadio.Models.IChannel;
import com.mosstech.StarRadio.Notification.AboveApi26Strategy;
import com.mosstech.StarRadio.Notification.BelowApi26Strategy;
import com.mosstech.StarRadio.Notification.NotificationStrategy;
import com.mosstech.StarRadio.R;

import java.util.List;
import java.util.Random;


public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{

    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_NEXT = "ACTION_PLAY_NEXT";
    public static final String ACTION_PREVIOUS = "ACTION_PLAY_PREVIOUS";

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
    private boolean mIsPreparing;
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
        String action = intent.getAction();
        if(action != null)
        {
            switch (action){
                case ACTION_PLAY:
                    if(isPlaying()) {
                        stopPlayer();
                        mNotificationStrategy.notify(NOTIFY_ID,buildNotification());
                    }
                    else
                        playChannel();
                    break;
                case ACTION_NEXT:
                    playNext();
                    break;
                case ACTION_PREVIOUS:
                    playPrev();
                    break;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            startForeground(NOTIFY_ID,buildNotification());

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
        mNotificationStrategy.notify(NOTIFY_ID,buildNotification());
        if(mListener != null)
            mListener.onStarted();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v(TAG, "Playback Error");
        stopPlayer();
        mNotificationStrategy.notify(NOTIFY_ID,buildNotification());
        Toast.makeText(this,R.string.err_while_playing_channel,Toast.LENGTH_SHORT).show();
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
        if(isPlaying())
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

    private Notification buildNotification()
    {
        String chnName = null;
        String chnIcon = null;

        if(mChannels != null && mChannels.size() > 0 && mChnIndex >= 0) {
            chnName = mChannels.get(mChnIndex).getName();
            chnIcon = mChannels.get(mChnIndex).getFavicon();
        }

        return  mNotificationStrategy.buildNotification(chnName,mPlayListName,chnIcon,isPlaying());
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
        if(isPlaying() || isPreparing())
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

    public int getChannelsSize() {
        if(mChannels != null)
            return mChannels.size();
        return 0;
    }

    public void setListener(MusicServiceListener listener) {
        mListener = listener;
    }

    //toggle shuffle
    public void toggleShuffle() {
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
