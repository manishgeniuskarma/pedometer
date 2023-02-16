package com.queens.sqliteloginandsignup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.data.BarEntry;
import com.queens.sqliteloginandsignup.activities.ChallangesActivity;
import com.queens.sqliteloginandsignup.activities.GraphbarActivity;
import com.queens.sqliteloginandsignup.activities.UpdateActivity;
import com.queens.sqliteloginandsignup.model.User;
import com.queens.sqliteloginandsignup.sql.DBHelper;
import com.queens.sqliteloginandsignup.sql.DatabaseHelper;
import com.queens.sqliteloginandsignup.sql.StepDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private AppCompatButton detailsteps;
    private SensorManager sensorManager;
    private Sensor sensor;

    String name;
    DBHelper mydb;

    StepDBHelper stepdb;
    int j = 0;
    DatabaseHelper databaseHelper;
    private ListView obj;
    User user;

    private TextView steps, calorie, distance, time;
    private TextView dashboard;
    private AppCompatImageView edit;
    String emailFromIntent = "";
    String laststepData = "0";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        databaseHelper = new DatabaseHelper(this);

        stepdb = new StepDBHelper(this);
        mydb = new DBHelper(this);
        steps = (TextView) findViewById(R.id.display_steps);
        edit = (AppCompatImageView) findViewById(R.id.edit);
        calorie = (TextView) findViewById(R.id.calorie_view);
        distance = (TextView) findViewById(R.id.distance_text_view);
        time = (TextView) findViewById(R.id.time_textview);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        emailFromIntent = getIntent().getStringExtra("EMAIL");

        SharedPreferences user = getSharedPreferences("MyLoginPref", MODE_PRIVATE);
        String email_user = user.getString("user_email", "");

        for (int i = 0; i < databaseHelper.getAllUser().size(); i++) {
            if (databaseHelper.getAllUser().get(i).getEmail().equalsIgnoreCase(email_user)) {
                j = i;
                break;
            }
        }

        if (databaseHelper.getAllUser().get(j).getheight() == null || databaseHelper.getAllUser().get(j).getweight() == null) {

            Toast.makeText(this, "PLEASE UPDATE HEIGHT AND  WEIGHT", Toast.LENGTH_SHORT).show();

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (databaseHelper.getAllUser().get(j).getheight() == null || databaseHelper.getAllUser().get(j).getweight() == null) {

                    Intent intentRegister = new Intent(getApplicationContext(), UpdateActivity.class);
                    intentRegister.putExtra("EMAIL", databaseHelper.getAllUser().get(j).getEmail());
                    startActivity(intentRegister);
                }
            }
        }, 2000);


    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onResume() {
        super.onResume();
        checkPermissionRequest();


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        String[] cal = formattedDate.split("/");

        SharedPreferences user_name = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String user = user_name.getString("user_name", "");
        ArrayList<BarEntry> values = new ArrayList<>();
        ArrayList<String> array_list_step = mydb.getAllCotacts();

        for (int i = 0; i < array_list_step.size(); i++) {

            String[] steps = array_list_step.get(i).split("-");
            if (steps[1].equalsIgnoreCase(user)) {

                int step = Integer.parseInt(steps[2]);

                String[] day = steps[0].split("/");

                int days = Integer.parseInt(day[0]);

                int cals = Integer.parseInt(cal[0]);

                if (days == cals) {


                    if (!TextUtils.isEmpty(emailFromIntent)) {
                        emailFromIntent = "";
                        SharedPreferences sp = getSharedPreferences("LastStep", MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putString("steps", String.valueOf(step).trim());
                        ed.clear();
                        ed.apply();
                    }

                }

            }
        }



        dashboard = (AppCompatTextView) findViewById(R.id.dashboard);
        detailsteps = (AppCompatButton) findViewById(R.id.detail_steps);


        dashboard.setText("Hi, " + String.valueOf(databaseHelper.getAllUser().get(j).getName()));

        //Toast.makeText(this,"WEIGHT"+databaseHelper.getAllUser().get(j).getweight() , Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("user_name", String.valueOf(databaseHelper.getAllUser().get(j).getName()));
        //   myEdit.putString("user_email", String.valueOf(databaseHelper.getAllUser().get(j).getEmail()));

        myEdit.apply();


        detailsteps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intentRegister = new Intent(getApplicationContext(), GraphbarActivity.class);
                intentRegister.putExtra("EMAIL", databaseHelper.getAllUser().get(j).getEmail());
                startActivity(intentRegister);
            }
        });
        steps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intentRegister = new Intent(getApplicationContext(), ChallangesActivity.class);
                intentRegister.putExtra("EMAIL", databaseHelper.getAllUser().get(j).getEmail());
                startActivity(intentRegister);
            }
        });
        dashboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intentRegister = new Intent(getApplicationContext(), UpdateActivity.class);
                intentRegister.putExtra("EMAIL", databaseHelper.getAllUser().get(j).getEmail());
                startActivity(intentRegister);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intentRegister = new Intent(getApplicationContext(), UpdateActivity.class);
                intentRegister.putExtra("EMAIL", databaseHelper.getAllUser().get(j).getEmail());
                startActivity(intentRegister);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermissionRequest() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_GRANTED) {

            if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION)) {
            showAlertDialog();
        } else {
            makePermissionRequest();
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This app needs you to allow this permission in order to use this application");
        alertDialogBuilder.setPositiveButton("Allow",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(
                                new Intent(
                                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null)
                                )
                        );
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void makePermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                    if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
                        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enable permission manually to use this app", Toast.LENGTH_SHORT).show();
                    startActivity(
                            new Intent(
                                    android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null)
                            )
                    );
                }

            });

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            sensorManager.unregisterListener(this, sensor);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == sensor) {

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
            String formattedDate = df.format(c);


            Log.e("DSSSDSDS", String.valueOf(sensorEvent.values[0]));


            name = databaseHelper.getAllUser().get(j).getName();

            Boolean isDataAvailalbe = mydb.isTodayDataAvailable(formattedDate);
            Boolean isNameAvailalbe = mydb.isnameavailable(name);


            if (isNameAvailalbe) {
                if (isNameAvailalbe && isDataAvailalbe) {
                    mydb.deleteCourse(formattedDate);
                    mydb.insertContact(formattedDate, String.valueOf(sensorEvent.values[0]), name);
                } else {

                    mydb.insertContact(formattedDate, String.valueOf(sensorEvent.values[0]), name);
                }
            } else {

                mydb.insertContact(formattedDate, String.valueOf(sensorEvent.values[0]), name);
                // Snack Bar to show error message that record already exists

            }


            Boolean isDataAvailalbe_step = stepdb.isTodayDataAvailable(formattedDate);
            Boolean isNameAvailalbe_step = stepdb.isnameavailable(name);

            if (Math.round(Float.parseFloat(laststepData)) != 0) {
                if (isNameAvailalbe_step) {
                    if (isNameAvailalbe_step && isDataAvailalbe_step) {
                        stepdb.deleteCourse(formattedDate);
                        stepdb.insertContact(formattedDate, String.valueOf(Math.round(sensorEvent.values[0]) - Math.round(Float.parseFloat(laststepData))).trim(), name);
                    } else {

                        stepdb.insertContact(formattedDate, String.valueOf(Math.round(sensorEvent.values[0]) - Math.round(Float.parseFloat(laststepData))).trim(), name);
                    }
                } else {

                    stepdb.insertContact(formattedDate, String.valueOf(Math.round(sensorEvent.values[0]) - Math.round(Float.parseFloat(laststepData))).trim(), name);
                    // Snack Bar to show error message that record already exists

                }

            } else {

                stepdb.insertContact(formattedDate, "0", name);

            }

            SharedPreferences laststeps = getSharedPreferences("LastStep", MODE_PRIVATE);
            laststepData = laststeps.getString("steps", "0");

            runOnUiThread(new Runnable() {
                public void run() {
                    if (Math.round(Float.parseFloat(laststepData)) != 0) {
                        steps.setText(String.valueOf(Math.round(sensorEvent.values[0]) - Math.round(Float.parseFloat(laststepData))).trim());

                    } else {

                        steps.setText("0");
                    }


                    calorie.setText(String.format("%.2f", (sensorEvent.values[0] - Float.parseFloat(laststepData)) * 0.063) + "\ncal");
                    distance.setText(String.format("%.3f", (sensorEvent.values[0] - Float.parseFloat(laststepData)) * 0.000445) + "\nmile");
                    time.setText(String.format("%.2f", (sensorEvent.values[0] - Float.parseFloat(laststepData)) * 0.008333333) + "\nminute");

                    SharedPreferences preferences = getSharedPreferences("LastStep", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();


                }
            });


            ArrayList<String> array_list = mydb.getAllCotacts();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array_list);

            obj = (ListView) findViewById(R.id.steps_list);


            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();

            // write all the data entered by the user in SharedPreference and apply
            myEdit.putString("steps", String.valueOf(sensorEvent.values[0]));

            myEdit.apply();

        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }


}