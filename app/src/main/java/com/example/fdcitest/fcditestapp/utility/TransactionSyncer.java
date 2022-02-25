package com.example.fdcitest.fcditestapp.utility;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.fdcitest.R;
import com.example.fdcitest.fcditestapp.interfaces.HttpRequestListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TransactionSyncer {

    private Context context;
    private HttpRequestListener httpRequestListener;

    public TransactionSyncer(Context context)
    {
        this.context = context;
    }

    public void setOnRequestListener(HttpRequestListener httpRequestListener)
    {
        this.httpRequestListener = httpRequestListener;
    }

    public void downloadPlaces()throws Exception
    {
        try
        {
            HttpClientProvider.getSync(context, "https://restcountries.com/v3.1/all", new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    try
                    {
                        JSONArray response = new JSONArray(new String(responseBody));
                        httpRequestListener.onSuccess("places",response.toString());

                    } catch (Exception err)
                    {
                        httpRequestListener.onFailure("ONS places  "+err.getMessage(),"places");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {
                    try
                    {
                        String responseString = new String(responseBody);
                        if (statusCode == 404 || responseString.contains("html"))
                        {
                            httpRequestListener.onFailure(context.getString(R.string.return_404),"places");
                        }
                        else if (statusCode == 408)
                        {
                            httpRequestListener.onFailure(context.getString(R.string.return_408),"places");
                        }
                        else
                        {
                            httpRequestListener.onFailure(responseString,"places");
                        }

                    } catch (Exception err)
                    {
                        httpRequestListener.onFailure(context.getString(R.string.internet_connection_problem),"places");
                    }
                }
            });
        }
        catch (Exception err)
        {
            throw new Exception("Error on places request:" + err.toString());
        }
    }

}
