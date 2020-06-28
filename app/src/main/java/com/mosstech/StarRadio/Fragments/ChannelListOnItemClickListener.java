package com.mosstech.StarRadio.Fragments;

import com.mosstech.StarRadio.Models.IChannel;

import java.util.List;

public interface ChannelListOnItemClickListener {
    void onListFragmentItemClick(List<IChannel> channels, String listName, int position);
}
