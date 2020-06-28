package com.mosstech.StarRadio.Network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public final class HttpUtils {

    private static final String URL = "https://de1.api.radio-browser.info/json";
    private static final int REQUEST_TIMEOUT = 999000;

    public static final String SEARCH_TYPE_BY_NAME = "byname";
    public static final String SEARCH_TYPE_BY_COUNTRY = "bycountryexact";
    public static final String SEARCH_TYPE_BY_COUNTRY_CODE = "bycountrycodeexact";
    public static final String SEARCH_TYPE_BY_STATE = "bystateexact";
    public static final String SEARCH_TYPE_BY_LANGUAGE = "bylanguageexact";
    public static final String SEARCH_TYPE_BY_TAG = "bytagexact";

    public static StringRequest createCountriesRequest(Response.Listener<String> responseListener
            , Response.ErrorListener errorListener)
    {
        String url = URL+"/countries";

        final Map<String,String> params = new HashMap<>();
        params.put("order","stationcount");
        params.put("reverse",String.valueOf(true));
        params.put("hidebroken",String.valueOf(true));

        StringRequest request = new StringRequest(Request.Method.POST,url, responseListener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT
                ,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    public static StringRequest createLanguagesRequest(Response.Listener<String> responseListener
            , Response.ErrorListener errorListener)
    {
        String url = URL+"/languages";

        final Map<String,String> params = new HashMap<>();
        params.put("order","stationcount");
        params.put("reverse",String.valueOf(true));
        params.put("hidebroken",String.valueOf(true));

        StringRequest request = new StringRequest(Request.Method.POST,url, responseListener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT
                ,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    public static StringRequest createStatesRequest(String country, Response.Listener<String> responseListener
            , Response.ErrorListener errorListener)
    {
        String url = URL+"/states";
        if(country != null)
            url = url + "/" +country + "/";

        final Map<String,String> params = new HashMap<>();
        params.put("order","stationcount");
        params.put("reverse",String.valueOf(true));
        params.put("hidebroken",String.valueOf(true));

        StringRequest request = new StringRequest(Request.Method.POST,url, responseListener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT
                ,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    public static StringRequest createTagsRequest(Response.Listener<String> responseListener
            , Response.ErrorListener errorListener)
    {
        String url = URL+"/tags";

        final Map<String,String> params = new HashMap<>();
        params.put("order","stationcount");
        params.put("reverse",String.valueOf(true));
        params.put("hidebroken",String.valueOf(true));

        StringRequest request = new StringRequest(Request.Method.POST,url, responseListener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT
                ,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }


    /**
     *
     * @param searchType search type constant example {@link #SEARCH_TYPE_BY_TAG}
     * @param searchTerm any search string
     * @param order string of any { name, url, homepage, favicon, tags, country, state, language,
     *              votes, codec, bitrate, lastcheckok, lastchecktime, clicktimestamp, clickcount, clicktrend, random }
     * @param limit limit of result
     * @param responseListener listener for request response
     * @param errorListener listener for request error
     * @return Json Request
     */
    public static StringRequest createStationRequest(@Nullable String searchType,@Nullable String searchTerm,
                                                         @Nullable String order,@Nullable Integer limit,Boolean reverse,
                                                         @NonNull Response.Listener<String> responseListener,
                                                         @NonNull Response.ErrorListener errorListener)
    {
        String url = URL+"/stations";

        if(searchType != null && searchTerm != null)
            url = url + '/' + searchType + '/' + searchTerm;

        final Map<String, String> params = new HashMap<>();
        if(order != null) params.put("order", order);
        if(reverse != null) params.put("reverse",String.valueOf(reverse.booleanValue()));
        if(limit != null) params.put("limit",String.valueOf(limit));

        params.put("hidebroken",String.valueOf(true));

        StringRequest request = new StringRequest(Request.Method.POST,url, responseListener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT
                ,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return request;
    }

    public static StringRequest createStationRequestByVote(@Nullable Integer limit,
                                                              @NonNull Response.Listener<String> responseListener,
                                                              @NonNull Response.ErrorListener errorListener)
    {
        String url = URL+"/stations/topvote";
        if(limit != null) url += "/" + limit;

        StringRequest request = new StringRequest(Request.Method.POST,url, responseListener,errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT
                ,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
