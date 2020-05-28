package com.mosstech.StarRadio.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public final class ProxyChannel implements IChannel, JsonConvertable, Parcelable {

    private JSONObject jsonObject;
    private Channel realChannel = null;

    public ProxyChannel(JSONObject object) {
        jsonObject = object;
    }

    @Override
    public String getName() {
        try {
            return jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String getUrl() {
        try {
            return jsonObject.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String getFavicon() {
        try {
            return jsonObject.getString("favicon");
        } catch (JSONException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String getStationUuID() {
        try {
            return jsonObject.getString("stationuuid");
        } catch (JSONException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String getUrlResolved() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);
        return realChannel.getUrlResolved();
    }

    @Override
    public String getHomepage() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getHomepage();
    }

    @Override
    public String[] getTags() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getTags();
    }

    @Override
    public String getCodec() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getCodec();
    }

    @Override
    public String getCountry() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getCountry();
    }

    @Override
    public String getCountryCode() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getCountryCode();
    }

    @Override
    public String getState() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getState();
    }

    @Override
    public String getLanguage() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getLanguage();
    }

    @Override
    public int getVotes() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getVotes();
    }

    @Override
    public int getBitrate() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getBitrate();
    }

    @Override
    public int getClickCount() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getClickCount();
    }

    @Override
    public int getLastCheckOk() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getLastCheckOk();
    }

    @Override
    public int getClickTrend() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getClickTrend();
    }

    @Override
    public boolean getIsFavorite() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        return realChannel.getIsFavorite();
    }

    @Override
    public void toggleFavorite() {
        if(realChannel == null)
            realChannel = new Channel(jsonObject);

        realChannel.toggleFavorite();
    }

    // JsonConvertable Interface
    @Override
    public void convertFromJsonObject(JSONObject object){}

    @Override
    public String getJsonString() {
        return jsonObject.toString();
    }

    // Parcelable Interface
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jsonObject.toString());
    }

    public static final Parcelable.Creator<ProxyChannel> CREATOR
            = new Parcelable.Creator<ProxyChannel>() {
        public ProxyChannel createFromParcel(Parcel in) {
            return new ProxyChannel(in);
        }

        public ProxyChannel[] newArray(int size) {
            return new ProxyChannel[size];
        }
    };

    private ProxyChannel(Parcel in) {
        String jsonString = in.readString();
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
