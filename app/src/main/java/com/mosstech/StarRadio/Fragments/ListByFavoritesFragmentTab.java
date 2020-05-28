package com.mosstech.StarRadio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mosstech.StarRadio.Data.PrefenceManager;
import com.mosstech.StarRadio.Models.IChannel;
import com.mosstech.StarRadio.R;

import java.util.List;

public class ListByFavoritesFragmentTab extends ChannelListFragmentTab {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIstanseState)
    {
        mRootView = inflater.inflate(R.layout.fragment_channel_list,container,false);
        //do not cache favorite channel list. It might be change in other tabs
        prepareChannels();
        return mRootView;
    }

    @Override
    protected void prepareChannels() {
        List<IChannel> channels = new PrefenceManager(mRootView.getContext()).getFavorites();
        onPrepared(channels);
    }

    @Override
    public void onItemFavoriteClick(View favoriteView, int position) {
        super.onItemFavoriteClick(favoriteView, position);
        // on favorites fragment click on star means remove from favorites list
        mChannelAdapter.getChannelList().remove(position);
        mChannelAdapter.notifyItemRemoved(position);
    }
}
