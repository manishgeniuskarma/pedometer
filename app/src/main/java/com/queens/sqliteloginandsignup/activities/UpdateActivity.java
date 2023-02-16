package com.queens.sqliteloginandsignup.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.queens.sqliteloginandsignup.R;
import com.queens.sqliteloginandsignup.helpers.InputValidation;
import com.queens.sqliteloginandsignup.model.User;
import com.queens.sqliteloginandsignup.sql.DBHelper;
import com.queens.sqliteloginandsignup.sql.DatabaseHelper;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = UpdateActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutHeight;
    private TextInputLayout textInputLayoutWeight;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextHeight;
    private TextInputEditText textInputEditTextWeight;
    private TextInputEditText textInputEditTextConfirmPassword;
    int j = 0;
    private AppCompatButton appCompatButtonUpdate, appCompatButtonLogout;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;
    private SensorManager sensorManager;
    private Sensor sensor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getSupportActionBar().hide();


        initViews();
        initListeners();
        initObjects();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences user_email = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String user_mail = user_email.getString("user_email", "");
        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        for (int i = 0; i < databaseHelper.getAllUser().size(); i++) {

            if (databaseHelper.getAllUser().get(i).getEmail().equalsIgnoreCase(emailFromIntent)) {

                j = i;
                break;
            }

        }
        emptyInputEditText();
    }


    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutHeight = (TextInputLayout) findViewById(R.id.textInputLayoutHeight);
        textInputLayoutWeight = (TextInputLayout) findViewById(R.id.textInputLayoutWeight);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextHeight = (TextInputEditText) findViewById(R.id.textInputEditTextHeight);
        textInputEditTextWeight = (TextInputEditText) findViewById(R.id.textInputEditTextWeight);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        appCompatButtonUpdate = (AppCompatButton) findViewById(R.id.appCompatButtonUpdate);
        appCompatButtonLogout = (AppCompatButton) findViewById(R.id.appCompatButtonlogout);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonUpdate.setOnClickListener(this);
        appCompatButtonLogout.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();

    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonUpdate:
                postDataToSQLite();
                break;

            case R.id.appCompatButtonlogout:
                logout();
                break;
        }
    }

    private void logout() {

        SharedPreferences preferences = getSharedPreferences("MyLoginPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();



        Intent login = new Intent(activity, LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finishAffinity();
    }


    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {

        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_blanck))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextHeight, textInputLayoutHeight, getString(R.string.error_blanck))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextWeight, textInputLayoutWeight, getString(R.string.error_blanck))) {
            return;
        }

        user.setId(databaseHelper.getAllUser().get(j).getId());
        user.setName(textInputEditTextName.getText().toString().trim());
        user.setheight(textInputEditTextHeight.getText().toString().trim());
        user.setweight(textInputEditTextWeight.getText().toString().trim());

        databaseHelper.updateUser(user);


        // Snack Bar to show success message that record saved successfully
        Toast.makeText(activity, getString(R.string.success_update), Toast.LENGTH_SHORT).show();
        emptyInputEditText();

        onBackPressed();


    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(databaseHelper.getAllUser().get(j).getName());
        textInputEditTextHeight.setText(databaseHelper.getAllUser().get(j).getheight());
        textInputEditTextWeight.setText(databaseHelper.getAllUser().get(j).getweight());
        //  textInputEditTextConfirmPassword.setText(null);
    }

}