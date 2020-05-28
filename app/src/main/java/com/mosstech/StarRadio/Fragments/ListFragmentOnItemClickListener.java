package com.mosstech.StarRadio.Fragments;

import com.mosstech.StarRadio.Models.IChannel;

import java.util.List;

public interface ListFragmentOnItemClickListener {
    void onListFragmentItemClick(List<IChannel> channels, String listName, int position);
}
