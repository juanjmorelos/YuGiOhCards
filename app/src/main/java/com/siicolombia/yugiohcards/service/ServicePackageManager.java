package com.siicolombia.yugiohcards.service;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.siicolombia.yugiohcards.R;

import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;

public class ServicePackageManager {

    private String baseUrl;
    private Context context;

    public ServicePackageManager(Context context, String baseUrl) {
        this.baseUrl = baseUrl;
        this.context = context;
    }

    public void sendGetRequestToService(String operation, onServiceResponseListener listenerRequest) {
        String url = baseUrl + operation;
        RequestQueue rq = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                listenerRequest.onSuccessResponse(response);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            if (error instanceof TimeoutError) {
                showToast(context.getString(R.string.timeout_text_message), true, context);
                return;
            }
            listenerRequest.onErrorResponse(error);
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }
        };
        rq.add(request);
    }

    public void sendArrayGetRequestToService(String operation, onServiceResponseListener listenerRequest) {
        String url = baseUrl + operation;
        RequestQueue rq = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                listenerRequest.onSuccessResponse(response);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            if (error instanceof TimeoutError) {
                showToast(context.getString(R.string.timeout_text_message), true, context);
                return;
            }
            listenerRequest.onErrorResponse(error);
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }
        };
        rq.add(request);
    }

    private void showToast(String text, boolean longDuration, Context context) {
        int duration = longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();
    }
}
