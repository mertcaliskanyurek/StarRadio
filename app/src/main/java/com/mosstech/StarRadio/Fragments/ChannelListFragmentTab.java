package com.mosstech.StarRadio.Fragments;

import android.content.Context;
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

    private View mRootView;
    private ChannelAdapter mChannelAdapter;
    private String mListName = "";

    private ChannelListOnItemClickListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIstanseState)
    {
        mRootView = inflater.inflate(R.layout.fragment_list,container,false);

        if(mChannelAdapter != null && mChannelAdapter.getDataList() != null)
            onPrepared(mChannelAdapter.getDataList());
        else {
            showProgress(true);
            prepareChannels();
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

    protected void setRootView(View rootView) {
        mRootView = rootView;
    }

    protected View getRootView(){
        return mRootView;
    }

    protected Context getRootViewContext() {
        return mRootView.getContext();
    }

    protected ChannelAdapter getChannelAdapter(){
        return mChannelAdapter;
    }

    @Override
    public void onItemClick(View view, int position) {
        //on list item favorite click
        if(view.getId() == R.id.imageView_listItem_favorite)
        {
            IChannel chn = mChannelAdapter.getDataList().get(position);
            chn.toggleFavorite();
            if(chn.getIsFavorite())
                new PrefenceManager(mRootView.getContext()).saveChannelToFavorite(chn);
            else
                new PrefenceManager(mRootView.getContext()).removeFavorite(chn.getStationUuID());

            mChannelAdapter.updateViewAtPosition(position, false);
            return;
        }

        if(mChannelAdapter != null && mChannelAdapter.getDataList() != null)
        {
            mChannelAdapter.updateViewAtPosition(position,true);
            if(mListener != null)
                mListener.onListFragmentItemClick(mChannelAdapter.getDataList(),mListName,position);
        }
    }

    public void onSearch(String searchQuery){
        if(searchQuery.length()>0)
            mChannelAdapter.doFilter(searchQuery);
    }

    public void onSearchFinished(){
        mChannelAdapter.endFilter();
    }

    public void setListener(ChannelListOnItemClickListener listener) {
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
