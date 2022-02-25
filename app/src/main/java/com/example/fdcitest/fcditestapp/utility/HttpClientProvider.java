package com.example.fdcitest.fcditestapp.utility;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.entity.StringEntity;

public class HttpClientProvider {

    private static final String BASE_URL = "https://restcountries.com/v3.1/all";


    public static void post(Context context, String url, StringEntity entity, AsyncHttpResponseHandler responseHandler)
    {
        AsyncHttpClient client = new AsyncHttpClient();
//        client.addHeader("Authorization", "Token " + Session.getToken(context));
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; .NET CLR 1.0.3705;)");
        client.post(context, BASE_URL, entity, null, responseHandler);
    }

    public static void getSync(Context context, String url, AsyncHttpResponseHandler responseHandler)
    {
        SyncHttpClient synClient = new SyncHttpClient();

        synClient.setConnectTimeout(1000000);
        synClient.setTimeout(1000000);
        synClient.setResponseTimeout(1000000);
        synClient.get(context, url, responseHandler);
    }

}
