package com.mosstech.StarRadio.Models;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Mert on 08.04.2017.
 */

public final class Channel implements IChannel, JsonConvertable {

    private String stationUuID;
    private String name;
    private String url;
    private String urlResolved;
    private String homepage;
    private String tags;
    private String country;
    private String countryCode;
    private String state;
    private String language;
    private String codec;
    private String favicon;
    private int votes = 0;
    private int bitrate = 0;
    private int clickCount = 0;

    private boolean isFavorite = false;

    //The current online/offline state of this stream.
    // This is a value calculated from multiple measure points in the internet.
    // The test servers are located in different countries. It is a majority vote.
    private int lastCheckOk = 0;

    //The difference of the clickcounts within the last 2 days.
    // Posivite values mean an increase, negative a decrease of clicks.
    private int clickTrend = 0;

    public Channel(JSONObject jsonObject) {
        convertFromJsonObject(jsonObject);
    }

    @Override
    public String getStationUuID() {
        return stationUuID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getFavicon() {
        return favicon;
    }

    @Override
    public String getUrlResolved() {
        return urlResolved;
    }

    @Override
    public String getHomepage() {
        return homepage;
    }

    @Override
    public String[] getTags() {
        return tags.split(",");
    }

    @Override
    public String getCodec() {
        return codec;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public int getVotes() {
        return votes;
    }

    @Override
    public int getBitrate() {
        return bitrate;
    }

    @Override
    public int getClickCount() {
        return clickCount;
    }

    @Override
    public int getLastCheckOk() {
        return lastCheckOk;
    }

    @Override
    public int getClickTrend() {
        return clickTrend;
    }

    @Override
    public boolean getIsFavorite(){return isFavorite;}

    @Override
    public void toggleFavorite() { isFavorite = !isFavorite; }

    //setters

    public void setStationUuID(String stationUuID) {
        this.stationUuID = stationUuID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlResolved(String urlResolved) {
        this.urlResolved = urlResolved;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public void setLastCheckOk(int lastCheckOk) {
        this.lastCheckOk = lastCheckOk;
    }

    public void setClickTrend(int clickTrend) {
        this.clickTrend = clickTrend;
    }

    @Override
    public void convertFromJsonObject(JSONObject jsonObject) {
        try {
            setStationUuID(jsonObject.getString("stationuuid"));
            setName(jsonObject.getString("name"));
            setUrl(jsonObject.getString("url"));
            setUrlResolved(jsonObject.getString("url_resolved"));
            setHomepage(jsonObject.getString("homepage"));
            setFavicon(jsonObject.getString("favicon"));
            setTags(jsonObject.getString("tags"));
            setCountry(jsonObject.getString("country"));
            setCountryCode(jsonObject.getString("countrycode"));
            setState(jsonObject.getString("state"));
            setLanguage(jsonObject.getString("language"));
            setCodec(jsonObject.getString("codec"));

            setVotes(jsonObject.getInt("votes"));
            setBitrate(jsonObject.getInt("bitrate"));
            setLastCheckOk(jsonObject.getInt("lastcheckok"));
            setClickCount(jsonObject.getInt("clickcount"));
            setClickTrend(jsonObject.getInt("clicktrend"));

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getJsonString() {
        return "{}";
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}

