package com.example.fdcitest.fcditestapp.utility;

import android.util.Log;

import com.google.gson.Gson;

public class Debugger
{
    private static final String TAG = "FCDI";

    public static String printO(Object obj)
    {
        Gson gson = new Gson();
        System.out.println(gson.toJson(obj));
        Log.d(TAG, gson.toJson(obj));
        return gson.toJson(obj);
    }

    public static void logD(String message)
    {
        if (message != null)
        {
            Log.d(TAG, message);
        }
    }

    public static void logDouble(Double value, String name)
    {
        String valueDouble = String.valueOf(value);
        String placeHolderName = name != null ? name : "VALUE";
        Log.d(TAG, placeHolderName + " " + valueDouble);
    }

    public static void printError(Exception err)
    {
        Log.d(TAG, "Line No :" + err.getStackTrace()[0].getLineNumber());
        Log.d(TAG, err.getMessage());
        Log.d(TAG, err.toString());
        err.printStackTrace();
    }

    public static void logDint(int value) {
        Log.d(TAG, String.valueOf(value));
    }
}
