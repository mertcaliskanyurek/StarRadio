package com.mosstech.StarRadio.Fragments;

import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mosstech.StarRadio.Data.PrefenceManager;
import com.mosstech.StarRadio.Models.IChannel;
import com.mosstech.StarRadio.Models.ProxyChannel;
import com.mosstech.StarRadio.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public abstract class ChannelListFromWebFragmentTab extends ChannelListFragmentTab implements
    Response.Listener<String>, Response.ErrorListener{

    @Override
    public void onResponse(String responseString) {
        JSONArray response = null;
        try {
            response = new JSONArray(responseString);

            Set<String> userFavoriteChannelsUuIds = new PrefenceManager(getRootViewContext()).getFavoriteChannelsUuIds();
            List<IChannel> channels = new ArrayList<>(response.length());
            for (int i=0; i < response.length(); i++) {
                IChannel channel = new ProxyChannel(response.getJSONObject(i));

                if(userFavoriteChannelsUuIds.contains(channel.getStationUuID()))
                    channel.toggleFavorite();

                channels.add(channel);
            }

            onPrepared(channels);
        } catch (JSONException e) {
            e.printStackTrace();
            onPrepared(null);
        }
    }

    @Override
    protected void prepareChannels() {
        makeRequest();
    }

    /**
     * Make a Volley request about which list you want. Set listeners with this keyword
     */
    protected abstract void makeRequest();


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getRootViewContext(),R.string.err_white_downloading_channels,Toast.LENGTH_SHORT).show();
        onPrepared(null);
    }

}
