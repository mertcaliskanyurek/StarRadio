package com.mosstech.StarRadio;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mosstech.StarRadio.Adapters.ChannelAdapter;
import com.mosstech.StarRadio.Fragments.ChannelListFragmentTab;
import com.mosstech.StarRadio.Fragments.ListByFavoritesFragmentTab;
import com.mosstech.StarRadio.Fragments.ListByCurrCountryFragmentTab;
import com.mosstech.StarRadio.Fragments.ListByVotesFragmentTab;
import com.mosstech.StarRadio.Fragments.ListFragmentOnItemClickListener;
import com.mosstech.StarRadio.Models.IChannel;
import com.mosstech.StarRadio.Service.MusicService;
import com.mosstech.StarRadio.Adapters.PagerAdapter;
import com.mosstech.StarRadio.Service.MusicServiceListener;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MusicServiceListener, ListFragmentOnItemClickListener {

    //service
    private MusicService mMusicService = null;
    private ChannelAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startService();
        bindService(new Intent(this, MusicService.class), serviceConnection, 0);

        //then init views
        initTabs();

    }//end onCreate

    private void initTabs()
    {
        Map<String, Fragment> tabs = getTabs();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for(String name: tabs.keySet())
            tabLayout.addTab(tabLayout.newTab().setText(name));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(),new ArrayList<Fragment>(tabs.values()));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //viewPager.setCurrentItem(1);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     *  init tabs here
     * @return hashtable with tab name and tab fragment
     */
    private Map<String,Fragment> getTabs()
    {
        Map<String,Fragment> tabs = new HashMap<>();
        ChannelListFragmentTab listByCurrCountry = new ListByCurrCountryFragmentTab();
        ChannelListFragmentTab listByVotes = new ListByVotesFragmentTab();
        ChannelListFragmentTab listByFavorites = new ListByFavoritesFragmentTab();

        String tabNameCurrCountry = getString(R.string.tab_name_list_your_country);
        String tabNameTopVotes = getString(R.string.tab_name_list_top_vote);
        String tabNameFavorites = getString(R.string.tab_name_favorites);

        listByCurrCountry.setListName(tabNameCurrCountry);
        listByCurrCountry.setListener(this);

        listByVotes.setListName(tabNameTopVotes);
        listByVotes.setListener(this);

        listByFavorites.setListName(tabNameFavorites);
        listByFavorites.setListener(this);

        tabs.put(tabNameCurrCountry, listByCurrCountry);
        tabs.put(tabNameTopVotes, listByVotes);
        tabs.put(tabNameFavorites, listByFavorites);

        return tabs;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMusicService == null)
            bindService(new Intent(this, MusicService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mMusicService != null && (!mMusicService.isPlaying() && !mMusicService.isPreparing()))
            stopService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void handleNextClick(View v)
    {
        if (mMusicService != null) {
            mMusicService.playNext();
        }
    }

    public void handlePreviousClick(View v)
    {
        if(mMusicService != null)
        {
            mMusicService.playPrev();
        }
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

    //MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_about:
                break;
            case R.id.menu_search:
                //startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                break;
            case R.id.menu_vote:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getString(R.string.packName))));
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(),getString(R.string.err_no_store),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case R.id.menu_close:
                if(mMusicService != null)
                    stopService();
                break;
        }
        return true;
    }

    private void startService()
    {
        Intent serviceIntent = new Intent(this, MusicService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(serviceIntent);
        else
            startService(serviceIntent);
    }

    private void stopService()
    {
        mMusicService.stopService();
        unbindService(serviceConnection);
    }

    public final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("Music Connection","onServiceCOnttected");
            MusicService.MusicBinder binder = (MusicService.MusicBinder)iBinder;
            //get service
            mMusicService = binder.getService();
            //pass list
            mMusicService.setListener(MainActivity.this);
            initViews();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("Music Connection","onServiceDisconnected");
        }
    };

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

    private void initViews()
    {
        if(mMusicService != null)
        {
            ImageView iv_Play = findViewById(R.id.button_play_minimize);
            TextView tv_channelName = findViewById(R.id.textView_channel_name_minimize);
            TextView tv_playlistName = findViewById(R.id.textView_playlist_name_minimize);

            if(mMusicService.isPreparing() || mMusicService.isPlaying()) {
                iv_Play.setImageResource(R.drawable.button_stop);
            }
            else {
                iv_Play.setImageResource(R.drawable.button_play);
            }

            tv_channelName.setText(mMusicService.getChannelName());
            tv_playlistName.setText(mMusicService.getPlayListName());

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_music_control_minimize);
            progressBar.setVisibility(mMusicService.isPreparing()?View.VISIBLE:View.INVISIBLE);
        }

    }

    @Override
    public void onListFragmentItemClick(List<IChannel> channels, String listName, int position) {
        if(mMusicService != null)
        {
            if(mMusicService.getPlayListName().equals(listName))
                mMusicService.playChannel(position);
            else
                mMusicService.playChannel(channels, listName, position);
        }
    }


}


