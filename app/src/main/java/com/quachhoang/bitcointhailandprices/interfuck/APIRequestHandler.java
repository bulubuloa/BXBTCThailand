package com.quachhoang.bitcointhailandprices.interfuck;

import org.json.JSONObject;

/**
 * Created by QuachHoang on 1/13/2016.
 */
public interface APIRequestHandler {
    void onComplete(boolean success, JSONObject result);
}