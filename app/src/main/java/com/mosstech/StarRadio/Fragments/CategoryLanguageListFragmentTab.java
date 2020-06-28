package com.mosstech.StarRadio.Fragments;

import com.mosstech.StarRadio.Network.HttpUtils;
import com.mosstech.StarRadio.Network.VolleySingleton;

public final class CategoryLanguageListFragmentTab extends CategoryListFragmentTab {

    @Override
    protected void prepareCategories() {
        VolleySingleton volley = VolleySingleton.getInstance(getRootViewContext());
        volley.addToRequestQueue(HttpUtils.createLanguagesRequest(this,this));
    }

    @Override
    protected void makeRequest() {
        VolleySingleton volley = VolleySingleton.getInstance(getRootViewContext());
        volley.addToRequestQueue(HttpUtils.createStationRequest(HttpUtils.SEARCH_TYPE_BY_LANGUAGE,getSelectedCategoryName(),
                "votes", null,true, this,this));
    }
}
