package com.mosstech.StarRadio;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mosstech.StarRadio.Fragments.CategoryCountryListFragmentTab;
import com.mosstech.StarRadio.Fragments.CategoryLanguageListFragmentTab;
import com.mosstech.StarRadio.Fragments.CategoryListFragmentTab;
import com.mosstech.StarRadio.Fragments.CategoryTagListFragmentTab;
import com.mosstech.StarRadio.Fragments.ChannelListFragmentTab;
import com.mosstech.StarRadio.Fragments.ListByFavoritesFragmentTab;
import com.mosstech.StarRadio.Fragments.ListByCurrCountryFragmentTab;
import com.mosstech.StarRadio.Fragments.ListByVotesFragmentTab;
import com.mosstech.StarRadio.Fragments.ChannelListOnItemClickListener;
import com.mosstech.StarRadio.Models.Channel;
import com.mosstech.StarRadio.Models.IChannel;
import com.mosstech.StarRadio.Adapters.PagerAdapter;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseMusicServiceActivity
        implements ChannelListOnItemClickListener, MaterialSearchView.OnQueryTextListener,
        MaterialSearchView.SearchViewListener {

    private ViewPager mPager;
    private MaterialSearchView mSearchView;
    private Fragment mCurrFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //first start and bind service then init views
        initTabs();
    }

    private void initTabs()
    {
        List<Fragment> tabs = getTabs();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for(Fragment tab: tabs) {
            String name = ((ChannelListFragmentTab)tab).getListName();
            tabLayout.addTab(tabLayout.newTab().setText(name));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(),tabs);
        mPager.setAdapter(adapter);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mPager.setCurrentItem(1);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private List<Fragment> getTabs()
    {
        List<Fragment> tabs = new ArrayList<>();
        ChannelListFragmentTab listByCurrCountry = new ListByCurrCountryFragmentTab();
        ChannelListFragmentTab listByVotes = new ListByVotesFragmentTab();
        ChannelListFragmentTab listByFavorites = new ListByFavoritesFragmentTab();
        CategoryListFragmentTab categoryListCountries = new CategoryCountryListFragmentTab();
        CategoryListFragmentTab categoryListLanguages = new CategoryLanguageListFragmentTab();
        CategoryListFragmentTab categoryListTags = new CategoryTagListFragmentTab();

        String tabNameCurrCountry = getString(R.string.tab_name_list_your_country);
        String tabNameTopVotes = getString(R.string.tab_name_list_top_vote);
        String tabNameFavorites = getString(R.string.tab_name_favorites);
        String tabNameCountries = getString(R.string.tab_name_countries);
        String tabNameLanguages = getString(R.string.tab_name_languages);
        String tabNameTags = getString(R.string.tab_name_tags);

        listByCurrCountry.setListName(tabNameCurrCountry);
        listByCurrCountry.setListener(this);

        listByVotes.setListName(tabNameTopVotes);
        listByVotes.setListener(this);

        listByFavorites.setListName(tabNameFavorites);
        listByFavorites.setListener(this);

        categoryListCountries.setListName(tabNameCountries);
        categoryListCountries.setListener(this);

        categoryListLanguages.setListName(tabNameLanguages);
        categoryListLanguages.setListener(this);

        categoryListTags.setListName(tabNameTags);
        categoryListTags.setListener(this);

        tabs.add(listByFavorites);
        tabs.add(listByCurrCountry);
        tabs.add(listByVotes);
        tabs.add(categoryListCountries);
        tabs.add(categoryListLanguages);
        tabs.add(categoryListTags);

        return tabs;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
            return;
        }

        PagerAdapter adapter = (PagerAdapter) mPager.getAdapter();
        Fragment curr = adapter.getItem(mPager.getCurrentItem());
        if(curr instanceof CategoryListFragmentTab)
        {
            CategoryListFragmentTab tab = (CategoryListFragmentTab) curr;
            if(!tab.isShowCategories())
                tab.onBackPressed();
            else
                super.onBackPressed();
        }
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        MenuItem item = menu.findItem(R.id.menu_search);
        mSearchView.setMenuItem(item);

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchViewListener(this);
        return true;
    }


    //MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_about:
                break;
            case R.id.menu_vote:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getString(R.string.packName))));
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.err_no_store),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_search:
                break;
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(),
                        SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case R.id.menu_close:
                if(mMusicService != null)
                    stopService();
                break;
        }
        return true;
    }

    @Override
    protected void initViews()
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

    public void handleFullScreenPlayerClick(View v)
    {
        startActivity(new Intent(this,PlayerFullScreenActivity.class));
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


    @Override
    public boolean onQueryTextSubmit(String query) {
        if(mCurrFragment instanceof ChannelListFragmentTab)
            ((ChannelListFragmentTab)mCurrFragment).onSearch(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(mCurrFragment instanceof ChannelListFragmentTab)
            ((ChannelListFragmentTab)mCurrFragment).onSearch(newText);

        return false;
    }

    @Override
    public void onSearchViewShown() {
        final PagerAdapter adapter = (PagerAdapter) mPager.getAdapter();
        mCurrFragment = adapter.getItem(mPager.getCurrentItem());

        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        findViewById(R.id.tab_layout).setVisibility(View.GONE);
    }

    @Override
    public void onSearchViewClosed() {
        if(mCurrFragment instanceof ChannelListFragmentTab)
            ((ChannelListFragmentTab)mCurrFragment).onSearchFinished();
        mCurrFragment = null;
        mPager.setOnTouchListener(null);
        findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);
    }
}


