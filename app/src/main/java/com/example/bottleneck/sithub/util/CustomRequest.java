package com.example.bottleneck.sithub.util;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;

/**
 * Created by priyankpatel on 10/7/16.
 */
public class CustomRequest extends StringRequest {
    private HashMap<String, String> mParams;

    public CustomRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public CustomRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, HashMap<String, String> parameters) {
        super(method, url, listener, errorListener);
        mParams = parameters;
    }

    @Override
    public HashMap<String, String> getParams() {
        return mParams;
    }

    @Override
    public HashMap<String, String> getHeaders() {
        return mParams;
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }
}

