package com.mosstech.StarRadio.Fragments;

import com.mosstech.StarRadio.Network.HttpUtils;
import com.mosstech.StarRadio.Network.VolleySingleton;

public final class ListByCurrCountryFragmentTab extends ChannelListFromWebFragmentTab {
    @Override
    protected void makeRequest() {
        String countryCode = getRootViewContext().getResources().getConfiguration().locale.getCountry();
        VolleySingleton volley = VolleySingleton.getInstance(getRootViewContext());
        volley.addToRequestQueue(HttpUtils.createStationRequest(HttpUtils.SEARCH_TYPE_BY_COUNTRY_CODE,countryCode,
                "votes", 100,true, this,this));
    }
}
