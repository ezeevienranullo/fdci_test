package com.example.fdcitest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fdcitest.fcditestapp.dialog.PlacesDialog;
import com.example.fdcitest.fcditestapp.interfaces.IDialogInterface;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private EditText etRegion, etCountry, etName;
    private Button btnSubmit;
    private TextView tvDisplay;

    private String capital;
    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        initializeUI();
    }

    private void initializeUI() {

        etRegion  = findViewById(R.id.etRegion);
        etCountry = findViewById(R.id.etCountry);
        etName    = findViewById(R.id.etName);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvDisplay = findViewById(R.id.tvDisplay);

        etRegion.setOnClickListener(view -> selectPlaceDialog("REGION"));

        etCountry.setOnClickListener(view -> selectPlaceDialog("COUNTRY"));

        btnSubmit.setOnClickListener(view -> {
            if(validate()) {
                submit();
            }
        });
    }

    private void selectPlaceDialog(String type){
        Bundle bundle = new Bundle();
        bundle.putString("TITLE", "SELECT "+type);
        bundle.putString("ACTION", type);
        bundle.putString("REGION SELECTED", etRegion.getText().toString());
        PlacesDialog placesDialog = new PlacesDialog();

        placesDialog.setOnDismissListener(new IDialogInterface.onDataStringListener() {
            @Override
            public void onCompleteString(Bundle bundle)
            {
                etRegion.setEnabled(true);
                if(bundle.getBoolean("CONFIRM"))
                {
                    switch(type) {
                        case "REGION":
                            etRegion.setText(bundle.getString("SELECTED_PLACE"));
                            break;
                        case "COUNTRY":
                            etCountry.setText(bundle.getString("SELECTED_PLACE"));
                            capital = bundle.getString("SELECTED_CAPITAL");
                            break;
                    }
                }
            }
        });

        placesDialog.setArguments(bundle);
        placesDialog.show(getSupportFragmentManager(), "SELECTED_PLACE");
    }

    private void submit(){

        String displayMsg = "Hi "+etName.getText().toString() +"! Welcome. \n Your currently in "+ etRegion.getText().toString() +", "+etCountry.getText().toString()
                +" Your capital city is : " +capital ;

        tvDisplay.setText(displayMsg);
    }

    private boolean validate(){

        if (TextUtils.isEmpty(etName.getText().toString()))
        {
            Toast.makeText(context, getString(R.string.name_field), Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!etName.getText().toString().matches("[a-zA-Z_]+"))
        {
            Toast.makeText(context, getString(R.string.error_name), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(TextUtils.isEmpty(etRegion.getText().toString())){

            Toast.makeText(context, getString(R.string.region_field), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(TextUtils.isEmpty(etCountry.getText().toString())){

            Toast.makeText(context, getString(R.string.country_field), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}