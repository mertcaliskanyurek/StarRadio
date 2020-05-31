package com.mosstech.StarRadio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mosstech.StarRadio.Data.PrefenceManager;
import com.mosstech.StarRadio.Models.IChannel;
import com.mosstech.StarRadio.Service.MusicService;
import com.mosstech.StarRadio.Service.MusicServiceListener;

public abstract class BaseMusicServiceActivity extends AppCompatActivity implements MusicServiceListener {
    //service
    protected MusicService mMusicService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService();
        bindService(new Intent(this, MusicService.class), serviceConnection, 0);
    }

    private void startService()
    {
        Intent serviceIntent = new Intent(this, MusicService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(serviceIntent);
        else
            startService(serviceIntent);
    }


    protected void stopService()
    {
        mMusicService.stopService();
        unbindService(serviceConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMusicService == null)
            bindService(new Intent(this, MusicService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        else {
            mMusicService.setListener(BaseMusicServiceActivity.this);
            initViews();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mMusicService != null && (!mMusicService.isPlaying() && !mMusicService.isPreparing()))
            stopService();
    }


    @Override
    public void onPreparing() {
        initViews();
    }

    @Override
    public void onStarted() {
        initViews();
    }

    @Override
    public void onError(String error) {
        initViews();
    }


    public void handleNextClick(View v)
    {
        if (mMusicService != null) mMusicService.playNext();
    }

    public void handlePreviousClick(View v)
    {
        if(mMusicService != null) mMusicService.playPrev();
    }

    public void handlePlayClick(View v)
    {
        if(mMusicService != null){
            if(mMusicService.isPreparing() || mMusicService.isPlaying()) {
                mMusicService.stopPlayer();
                initViews();
            }
            else{
                mMusicService.playChannel();
                initViews();
            }
        }
    }

    public void handleFavoriteClick(View view) {
        if(mMusicService != null) {
            IChannel channel = mMusicService.getChannel();
            channel.toggleFavorite();
            if(channel.getIsFavorite())
            {
                new PrefenceManager(this).saveChannelToFavorite(channel);
                ((ImageView)view).setImageResource(R.drawable.button_favorite_on);
            }
            else {
                new PrefenceManager(this).removeFavorite(channel.getStationUuID());
                ((ImageView)view).setImageResource(R.drawable.button_favorite_off);
            }
        }
    }

    public void handleShuffleClick(View view) {
        if(mMusicService != null) {
            mMusicService.toggleShuffle();
            ((ImageView)view).setImageResource(mMusicService.getShuffle()?
                    R.drawable.button_random_on:R.drawable.button_random_off);
        }
    }

    protected abstract void initViews();

    public final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("Music Connection","onServiceCOnttected");
            MusicService.MusicBinder binder = (MusicService.MusicBinder)iBinder;
            //get service
            mMusicService = binder.getService();
            //pass list
            mMusicService.setListener(BaseMusicServiceActivity.this);
            initViews();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("Music Connection","onServiceDisconnected");
        }
    };

}
