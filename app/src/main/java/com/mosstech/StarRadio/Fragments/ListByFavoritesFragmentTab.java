package com.mosstech.StarRadio.Fragments;

import com.mosstech.StarRadio.Data.PrefenceManager;
import com.mosstech.StarRadio.Models.IChannel;

import java.util.List;

public class ListByFavoritesFragmentTab extends ChannelListFragmentTab {

    @Override
    protected void prepareChannels() {
        List<IChannel> channels = new PrefenceManager(mRootView.getContext()).getFavorites();
        onPrepared(channels);
    }

}
