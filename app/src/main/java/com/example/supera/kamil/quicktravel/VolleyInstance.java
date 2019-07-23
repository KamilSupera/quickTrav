package com.example.supera.kamil.quicktravel;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*
 * Class responsible for creating
 * singleton instance
 * and queued request from the server
 */
public class VolleyInstance {
    private RequestQueue mRequestQueue;
    private static VolleyInstance mAppSingletonInstance;
    private static Context mContext;

    public static synchronized VolleyInstance getInstance(Context context) {
        if (mAppSingletonInstance == null) {
            mAppSingletonInstance = new VolleyInstance(context);
        }

        return mAppSingletonInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    private VolleyInstance(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }
}