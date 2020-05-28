package com.mosstech.StarRadio.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mosstech.StarRadio.Adapters.ChannelAdapter;
import com.mosstech.StarRadio.Adapters.RecyclerViewOnItemClickListener;
import com.mosstech.StarRadio.Data.PrefenceManager;
import com.mosstech.StarRadio.Models.IChannel;
import com.mosstech.StarRadio.R;

import java.util.List;

public abstract class ChannelListFragmentTab extends Fragment implements RecyclerViewOnItemClickListener {

    protected View mRootView;
    private ChannelAdapter mChannelAdapter;
    private String mListName = "";
    private ListFragmentOnItemClickListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIstanseState)
    {
        mRootView = inflater.inflate(R.layout.fragment_channel_list,container,false);

        if(mChannelAdapter != null && mChannelAdapter.getChannelList() != null)
            onPrepared(mChannelAdapter.getChannelList());
        else {
            prepareChannels();
            showProgress(true);
        }
        return mRootView;
    }

    protected abstract void prepareChannels();

    protected void onPrepared(List<IChannel> channels)
    {
        showProgress(false);
        //init list
        if(channels != null)
        {
            final RecyclerView recyclerView = mRootView.findViewById(R.id.recyclerView_channel_list);
            mChannelAdapter = new ChannelAdapter(mRootView.getContext(), channels,this);
            recyclerView.setAdapter(mChannelAdapter);
            final LinearLayoutManager manager = new LinearLayoutManager(mRootView.getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mChannelAdapter != null && mChannelAdapter.getChannelList() != null)
        {
            mChannelAdapter.updateViewAtPosition(position);
            if(mListener != null)
                mListener.onListFragmentItemClick(mChannelAdapter.getChannelList(),mListName,position);
        }
    }

    @Override
    public void onItemFavoriteClick(View favoriteView, int position) {
        IChannel chn = mChannelAdapter.getChannelList().get(position);
        chn.toggleFavorite();
        if(chn.getIsFavorite())
            new PrefenceManager(mRootView.getContext()).saveChannelToFavorite(chn);
        else
            new PrefenceManager(mRootView.getContext()).removeFavorite(chn.getStationUuID());

        mChannelAdapter.updateViewAtPosition(position);
    }

    public void setListener(ListFragmentOnItemClickListener listener) {
        mListener = listener;
    }

    public void setListName(@NonNull String listName) {
        mListName = listName;
    }

    public String getListName() {
        return mListName;
    }

    private void showProgress(boolean show)
    {
        final ProgressBar bar = mRootView.findViewById(R.id.progressBar_requestChannels);
        bar.setVisibility(show?View.VISIBLE:View.GONE);
    }
}
