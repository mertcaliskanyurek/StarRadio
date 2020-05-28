package com.mosstech.StarRadio.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mosstech.StarRadio.Models.IChannel;
import com.mosstech.StarRadio.Models.ProxyChannel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PrefenceManager {

    private static final String PREFENCE_NAME_FAVORITES = "com.mosstech.starradio.favorites";
    private static final String TAG = PrefenceManager.class.getSimpleName();

    private Context mContext;

    public PrefenceManager(Context context) {
        this.mContext = context;
    }

    public void saveChannelToFavorite(IChannel channel)
    {
        if(channel instanceof ProxyChannel) {
            SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFENCE_NAME_FAVORITES, Context.MODE_PRIVATE).edit();
            editor.putString(channel.getStationUuID(),((ProxyChannel)channel).getJsonString());

            editor.apply();
        }
        else
            Log.w(TAG,"Save channel to favorite failed. Channel must be instance of Proxy Channel to get right json string.");
    }

    public List<IChannel> getFavorites()
    {
        List<IChannel> channels = new ArrayList<>();
        SharedPreferences preferences = mContext.getSharedPreferences(PREFENCE_NAME_FAVORITES, Context.MODE_PRIVATE);
        Map<String,?> allEntries = preferences.getAll();
        try {
            JSONObject channelObject;
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                channelObject = new JSONObject(entry.getValue().toString());
                channels.add(new ProxyChannel(channelObject));
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return channels;
    }

    public Set<String> getFavoriteChannelsUuIds()
    {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFENCE_NAME_FAVORITES, Context.MODE_PRIVATE);
        return preferences.getAll().keySet();
    }

    public void removeFavorite(String channelUuId)
    {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFENCE_NAME_FAVORITES, Context.MODE_PRIVATE).edit();
        editor.remove(channelUuId);
        editor.apply();
    }
}
