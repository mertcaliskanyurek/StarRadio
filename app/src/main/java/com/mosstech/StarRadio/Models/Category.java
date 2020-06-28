package com.mosstech.StarRadio.Models;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Category implements ICategory, JsonConvertable {

    private String name;
    private int stationCount;

    public Category(String name, int stationCount) {
        this.name = name;
        this.stationCount = stationCount;
    }

    public Category(JSONObject object) {
        convertFromJsonObject(object);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getStationCount() {
        return stationCount;
    }

    @Override
    public void convertFromJsonObject(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
            this.stationCount = jsonObject.getInt("stationcount");
        }catch (JSONException e)
        {
            e.printStackTrace();
            this.name = "";
            this.stationCount = 0;
        }
    }

    @Override
    public String getJsonString() {
        return "";
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
