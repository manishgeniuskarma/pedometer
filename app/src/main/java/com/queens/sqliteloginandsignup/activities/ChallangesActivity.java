package com.queens.sqliteloginandsignup.activities;

import static com.kizitonwose.calendar.core.ExtensionsKt.daysOfWeek;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import com.queens.sqliteloginandsignup.R;
import com.queens.sqliteloginandsignup.databinding.Example1CalendarDayBinding;
import com.queens.sqliteloginandsignup.sql.DBHelper;
import com.queens.sqliteloginandsignup.sql.DatabaseHelper;

import org.jetbrains.annotations.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

public class ChallangesActivity extends AppCompatActivity {
    private CalendarView monthCalendarView;
    private TextView exOneYearText;
    private TextView exOneMonthText;
    private ArrayList<LocalDate> selectedDates = new ArrayList<>();
    private LocalDate today = LocalDate.now();
    private DBHelper mydb;
    private DatabaseHelper databaseHelper;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challanges);
        getSupportActionBar().hide();

        monthCalendarView = findViewById(R.id.exOneCalendar);
        exOneYearText = findViewById(R.id.exOneYearText);
        exOneMonthText = findViewById(R.id.exOneMonthText);

        mydb = new DBHelper((Context) this);
        databaseHelper = new DatabaseHelper((Context) this);
        SharedPreferences user_name = this.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String user = user_name.getString("user_name", "");

        ArrayList<String> array_list = mydb.getAllCotacts();
        List<DayOfWeek> daysOfWeek = daysOfWeek();


        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(100L);
        YearMonth endMonth = currentMonth.plusMonths(100L);
        setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
        for (int i = 0; i < array_list.size()-1; i++) {
            String[] steps = array_list.get(i).toString().split("-");
            if (steps[1].equalsIgnoreCase(user)) {
                String step = steps[2].toString();
                String[] day = steps[0].split("/");
                String days = day[0];
                dateClicked(LocalDate.parse(steps[0], formatter));
            }
        }
    }


    private void setupMonthCalendar(YearMonth startMonth, YearMonth endMonth, YearMonth currentMonth, List daysOfWeek) {
        class DayViewContainer extends ViewContainer {
            final View view;
            CalendarDay calendarDay = null;
            TextView textView = null;
            public DayViewContainer(View view1) {
                super(view1);
                view = view1;
                textView =  Example1CalendarDayBinding.bind(view).exOneDayText;
            }
        }
        MonthDayBinder<DayViewContainer> monthDayBinder = new MonthDayBinder<DayViewContainer>() {
            @Override
            public void bind(@NonNull DayViewContainer container, CalendarDay data) {
                container.calendarDay =data;
                bindDate(data.getDate(), container.textView, data.getPosition() == DayPosition.MonthDate);
            }

            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

        };
        monthCalendarView.setDayBinder(monthDayBinder);
        monthCalendarView.setMonthScrollListener(calendarMonth -> {
            updateTitle();
            return null;
        });
        monthCalendarView.setup(startMonth, endMonth, (DayOfWeek) CollectionsKt.first(daysOfWeek));
        monthCalendarView.scrollToMonth(currentMonth);

    }




    @SuppressLint({"SetTextI18n"})
    private final void updateTitle() {
        CalendarMonth month = monthCalendarView.findFirstVisibleMonth();
        exOneYearText.setText(String.valueOf(month.getYearMonth().getYear()));
        exOneMonthText.setText(String.valueOf(month.getYearMonth().getMonth()));
    }

    private void bindDate(LocalDate date, TextView textView, boolean isSelectable) {
        textView.setText((CharSequence) String.valueOf(date.getDayOfMonth()));
        if (isSelectable) {
            if (selectedDates.contains(date)) {
                textView.setTextColor(getColor(R.color.colorBackground));
                textView.setBackgroundResource(R.drawable.example_1_selected_bg);
            } else if (Intrinsics.areEqual(this.today, date)) {
                textView.setTextColor(getColor(R.color.colorBackground));
                textView.setBackgroundResource(R.drawable.example_1_today_bg);
            } else {
                textView.setTextColor(getColor(R.color.colorBackground));
                textView.setBackgroundResource(0);
            }
        } else {
            textView.setTextColor(getColor(R.color.colorBackground));
            textView.setBackgroundResource(0);
        }

    }

    private void dateClicked(LocalDate date) {
        if (selectedDates.contains(date)) {
            selectedDates.remove(date);
        } else {
            selectedDates.add(date);
        }
        monthCalendarView.notifyDateChanged(date);
    }
}
