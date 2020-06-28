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
        View root = inflater.inflate(R.layout.fragment_list,container,false);
        setRootView(root);
        //do not cache favorite channel list. It might be change in other tabs
        prepareChannels();
        return root;
    }

    @Override
    protected void prepareChannels() {
        List<IChannel> channels = new PrefenceManager(getRootViewContext()).getFavorites();
        onPrepared(channels);
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        if(view.getId() == R.id.imageView_listItem_favorite)
        {
            getChannelAdapter().getDataList().remove(position);
            getChannelAdapter().notifyItemRemoved(position);
        }
    }
}
