package com.example.fdcitest.fcditestapp.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fdcitest.R;
import com.example.fdcitest.fcditestapp.adapter.PlaceAdapter;
import com.example.fdcitest.fcditestapp.interfaces.HttpRequestListener;
import com.example.fdcitest.fcditestapp.interfaces.IDialogInterface;
import com.example.fdcitest.fcditestapp.model.Place;
import com.example.fdcitest.fcditestapp.utility.Debugger;
import com.example.fdcitest.fcditestapp.utility.TransactionSyncer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlacesDialog extends DialogFragment implements  PlaceAdapter.ItemClickListener{

    //Views
    private Context context;
    private View view;

    //Widgets
    private LinearLayout llPlace;
    private RecyclerView rcPlace;
    private TextView tvTitle;
    private PlaceAdapter placeAdapter;
    private ImageView imgClose;

    //Variables
    private IDialogInterface.onDataStringListener onDismissListener;
    private String  regionName = "", CountryName = "", action, regionSelected;
    private Place selectedPlace;
    private boolean confirm;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.list_places_dialog, container, false);
        context = view.getContext();

        regionName      = getArguments().getString("REGION");
        CountryName     = getArguments().getString("COUNTRY");
        action          = getArguments().getString("ACTION");
        regionSelected  = getArguments().getString("REGION SELECTED");
        initializeUI();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Debugger.logD("action "+action);
        return view;

    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        confirm = false;
        dismiss();
    }

    private void initializeUI()
    {
        rcPlace   = view.findViewById(R.id.rcPlace);
        tvTitle   = view.findViewById(R.id.tvTitle);
        llPlace   = view.findViewById(R.id.llPlace);
        imgClose  = view.findViewById(R.id.imgClose);

        imgClose.setOnClickListener(v -> {
            confirm = false;
            dismiss();
        });

        tvTitle.setText(getArguments().getString("TITLE"));

        new RequestPlaces().execute();
    }

    @Override
    public void onItemClick(View view, int position) {
        selectedPlace = placeAdapter.getItem(position);
        confirm = true;
        dismiss();
    }

    private void readRecords(ArrayList<Place> placeArrayList)
    {
        try
        {
            rcPlace.setLayoutManager(new LinearLayoutManager(context));
            placeAdapter = new PlaceAdapter(context, placeArrayList);
            placeAdapter.setClickListener(this);
            rcPlace.setAdapter(placeAdapter);

        }catch (Exception err)
        {
            Debugger.logD("readRecords "+err.getMessage());
        }
    }

    public void setOnDismissListener(IDialogInterface.onDataStringListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        try {
            Bundle bundle = new Bundle();
            bundle.putString("SELECTED_PLACE", selectedPlace.getName());
            bundle.putString("SELECTED_CAPITAL", selectedPlace.getCapital());
            bundle.putBoolean("CONFIRM", confirm);
            if (onDismissListener != null) onDismissListener.onCompleteString(bundle);

        }catch (Exception err)
        {
            Debugger.logD(" onDismiss "+err.getMessage());
        }
    }

    private class RequestPlaces extends AsyncTask<String, Integer, String> {

        private String message = "", response;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            llPlace.setVisibility(View.VISIBLE);
            imgClose.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {

            try
            {
                TransactionSyncer transactionSyncer = new TransactionSyncer(context);
                transactionSyncer.setOnRequestListener(new HttpRequestListener()
                {
                    @Override
                    public void onSuccess(String fromWhere, String returnSuccess)
                    {
                        message = "success";
                        response = returnSuccess;
                    }

                    @Override
                    public void onUpdate(int max, int progress, String currentDownload)
                    {

                    }

                    @Override
                    public void onFailure(String errorMessage, String fromWhere)
                    {
                        message = errorMessage;
                    }

                    @Override
                    public void showSpinner(boolean isShow, String currentDownload)
                    {

                    }
                });

                transactionSyncer.downloadPlaces();

            }catch (Exception err)
            {
                message += " failed " +err.getMessage();
                return message;
            }

            return message;
        }

        @Override
        protected void onPostExecute(String success)
        {
            try
            {
                setCancelable(false);
                llPlace.setVisibility(View.GONE);
                imgClose.setVisibility(View.VISIBLE);

                if (success.contains("failed"))
                {
                    Debugger.logD("Failed to download ");
                }
                else
                {
                    //Filter places dynamically
                    JSONArray jsonArray = new JSONArray(response);
                    JSONArray arrayName = new JSONArray();
                    switch (action)
                    {
                        case "REGION":
                            for (int ii = 0; ii < jsonArray.length(); ii++)
                            {
                                JSONObject getName = jsonArray.getJSONObject(ii);
                                JSONObject objectName = new JSONObject();

                                objectName.put("name", getName.getString("region"));
                                arrayName.put(objectName);
                            }
                            break;

                        case "COUNTRY":
                             for (int ii = 0; ii < jsonArray.length(); ii++)
                            {
                                JSONObject getName = jsonArray.getJSONObject(ii);
                                JSONObject objectName = new JSONObject();

                                if(getName.getString("region").equals(regionSelected) && getName.has("subregion")) {
                                    objectName.put("name", getName.getString("subregion"));
                                    objectName.put("capital", getName.getJSONArray("capital").get(0));
                                    arrayName.put(objectName);
                                }
                            }
                            break;
                    }

                    ArrayList<Place> placeArrayList = new Gson().fromJson(arrayName.toString(), new TypeToken<ArrayList<Place>>() {}.getType());
                    readRecords(placeArrayList);
                }
            }catch (Exception err)
            {
                Debugger.logD("onPostExecute "+err.getMessage());
            }
        }
    }


    @Override
    public void onResume()
    {
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        super.onResume();
    }

    public int getTheme() {
        return R.style.full_screen_dialog;
    }

}
