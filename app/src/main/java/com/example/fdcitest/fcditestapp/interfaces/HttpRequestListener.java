package com.example.fdcitest.fcditestapp.interfaces;

public interface HttpRequestListener {

    void onSuccess(String fromWhere,String returnSuccess );
    void onUpdate(int max, int progress, String currentDownload);
    void onFailure(String errorMessage, String fromWhere);
    void showSpinner(boolean isShow, String currentDownload);
}
