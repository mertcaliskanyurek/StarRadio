package com.mosstech.StarRadio.Models;

import org.json.JSONObject;

public interface IChannel {

    String getName();

    String getUrl();

    String getFavicon();

    String getStationUuID();

    String getUrlResolved();

    String getHomepage();

    String[] getTags();

    String getCodec();

    String getCountry();

    String getCountryCode();

    String getState();

    String getLanguage();

    int getVotes();

    int getBitrate();

    int getClickCount();

    int getLastCheckOk();

    int getClickTrend();

    boolean getIsFavorite();
    void toggleFavorite();
}
