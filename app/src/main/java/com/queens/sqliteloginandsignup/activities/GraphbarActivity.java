package com.queens.sqliteloginandsignup.activities;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.queens.sqliteloginandsignup.R;
import com.queens.sqliteloginandsignup.sql.DatabaseHelper;
import com.queens.sqliteloginandsignup.sql.StepDBHelper;
import com.queens.sqliteloginandsignup.utility.DayAxisValueFormatter;
import com.queens.sqliteloginandsignup.utility.Fill;
import com.queens.sqliteloginandsignup.utility.MyAxisValueFormatter;
import com.queens.sqliteloginandsignup.utility.XYMarkerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraphbarActivity extends AppCompatActivity {

    private static final int PERMISSION_STORAGE = 0;
    private BarChart chart;
    protected Typeface tfLight;

    StepDBHelper stepdb;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_graphbar2);

        setTitle("BarChartActivity");
        getSupportActionBar().hide();


        stepdb = new StepDBHelper(this);
        databaseHelper = new DatabaseHelper(this);


        chart = findViewById(R.id.chart1);
        chart.setClickable(false);
        chart.highlightValue(null);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setScaleXEnabled(false);
        chart.setScaleYEnabled(false);
        chart.setPinchZoom(false);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(10);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(tfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        SingleSelectToggleGroup single = (SingleSelectToggleGroup) findViewById(R.id.group_choices);
        single.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {

                if (R.id.choice_a == checkedId) {
                    setDataday(1, 0);
                    chart.invalidate();

                } else if (R.id.choice_b == checkedId) {

                    setDataweek(7, 0);
                    chart.invalidate();
                } else if (R.id.choice_c == checkedId) {

                    setDatamonth(31, 1);
                    chart.invalidate();
                }
            }
        });

    }

    private void setDatamonth(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(1, 0));
        values.add(new BarEntry(31, 0));
        for (int i = (int) start; i < start + count; i++) {

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
            String formattedDate = df.format(c);

            String[] cal = formattedDate.split("/");

            SharedPreferences user_name = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String user = user_name.getString("user_name", "");

            ArrayList<String> array_list = stepdb.getAllCotacts();

            for (int j = 0; j < array_list.size(); j++) {

                String[] steps = array_list.get(j).split("-");
                if (steps[1].equalsIgnoreCase(user)) {

                    int step = Integer.parseInt(steps[2]);

                    String[] day = steps[0].split("/");

                    int days = Integer.parseInt(day[0]);

                    int cals = Integer.parseInt(cal[0]);



                    values.add(new BarEntry(days, step));


                }
            }

        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "YOUR DETAILED STEP COUNT");

//           set1.setDrawIcons(false);

            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);


            List<Fill> gradientFills = new ArrayList<>();
            gradientFills.add(new Fill(startColor1, startColor1));
            //     set1.setFills(gradientFills);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }

    private void setDataweek(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = (int) start; i < start + count; i++) {


            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                String[] cal = formattedDate.split("/");

                SharedPreferences user_name = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                String user = user_name.getString("user_name", "");

                ArrayList<String> array_list = stepdb.getAllCotacts();

                for (int j = 0; j < array_list.size(); j++) {

                    String[] steps = array_list.get(j).split("-");

                    if (steps[1].equalsIgnoreCase(user)) {

                        int step = Integer.parseInt(steps[2]);

                        String[] day = steps[0].split("/");

                        int days = Integer.parseInt(day[0]);

                        int cals = Integer.parseInt(cal[0]);
                        String[] startdate = format.format(calendar.getTime()).split("/");
                        int stratday = Integer.parseInt(startdate[1]);


                        if (days > stratday && days < stratday + 7) {

                            values.add(new BarEntry(days, step));
                        }

                        if (days == stratday ) {
                            values.add(new BarEntry(stratday, step));
                        }else if(days == stratday + 7){

                            values.add(new BarEntry(stratday + 7, step));
                        } else {
                            values.add(new BarEntry(stratday, 0));
                            values.add(new BarEntry(stratday + 7, 0));

                        }


                    }

                }

        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "YOUR DEtAILED STEP COUNT");

//           set1.setDrawIcons(false);

            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);

            List<Fill> gradientFills = new ArrayList<>();
            gradientFills.add(new Fill(startColor1, endColor1));
            gradientFills.add(new Fill(startColor2, endColor2));
            gradientFills.add(new Fill(startColor3, endColor3));
            gradientFills.add(new Fill(startColor4, endColor4));
            gradientFills.add(new Fill(startColor5, endColor5));

            //     set1.setFills(gradientFills);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }

    private void setDataday(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = (int) start; i < start + count; i++) {
            float val = (float) (Math.random() * (range + 1));

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                String[] cal = formattedDate.split("/");

                SharedPreferences user_name = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                String user = user_name.getString("user_name", "");

                ArrayList<String> array_list =stepdb.getAllCotacts();

                for (int j = 0; j < array_list.size(); j++) {

                    String[] steps = array_list.get(j).split("-");
                    if (steps[1].equalsIgnoreCase(user)) {

                        int step = Integer.parseInt(steps[2]);

                        String[] day = steps[0].split("/");

                        int days = Integer.parseInt(day[0]);

                        int cals = Integer.parseInt(cal[0]);

                        if (days == cals) {

                            values.add(new BarEntry(days, step));
                        }
                    }
                }

        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "YOUR DETAILED STEP COUNT");

//           set1.setDrawIcons(false);

            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);

            List<Fill> gradientFills = new ArrayList<>();
            gradientFills.add(new Fill(startColor1, endColor1));
            gradientFills.add(new Fill(startColor2, endColor2));
            gradientFills.add(new Fill(startColor3, endColor3));
            gradientFills.add(new Fill(startColor4, endColor4));
            gradientFills.add(new Fill(startColor5, endColor5));

            //     set1.setFills(gradientFills);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }

}
