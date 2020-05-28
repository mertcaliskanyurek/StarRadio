package com.mosstech.StarRadio.Models;

import org.json.JSONObject;

public interface JsonConvertable {

    void convertFromJsonObject(JSONObject jsonObject);
    String getJsonString();
}
