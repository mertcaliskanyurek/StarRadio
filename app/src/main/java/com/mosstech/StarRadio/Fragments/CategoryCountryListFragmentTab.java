package com.mosstech.StarRadio.Fragments;

import com.mosstech.StarRadio.Network.HttpUtils;
import com.mosstech.StarRadio.Network.VolleySingleton;

public final class CategoryCountryListFragmentTab extends CategoryListFragmentTab {

    @Override
    protected void prepareCategories() {
        VolleySingleton volley = VolleySingleton.getInstance(getRootViewContext());
        volley.addToRequestQueue(HttpUtils.createCountriesRequest(this,this));
    }

    @Override
    protected void makeRequest() {
        VolleySingleton volley = VolleySingleton.getInstance(getRootViewContext());
        volley.addToRequestQueue(HttpUtils.createStationRequest(HttpUtils.SEARCH_TYPE_BY_COUNTRY,getSelectedCategoryName(),
                "votes", null,true, this,this));
    }
}
