package com.mosstech.StarRadio.Fragments;

import com.mosstech.StarRadio.Network.HttpUtils;
import com.mosstech.StarRadio.Network.VolleySingleton;

public final class ListByVotesFragmentTab extends ChannelListFromWebFragmentTab {
    @Override
    protected void makeRequest() {
        VolleySingleton volley = VolleySingleton.getInstance(getRootViewContext());
        volley.addToRequestQueue(HttpUtils.createStationRequestByVote( 100,this,this));
    }
}
