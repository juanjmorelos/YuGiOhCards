package com.siicolombia.yugiohcards.service;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface onServiceResponseListener {
    default void onSuccessResponse(JSONObject response) throws JSONException {};
    default void onSuccessResponse(JSONArray response) throws JSONException {};
    void onErrorResponse(VolleyError error);
}
