package com.fome.planster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fome.planster.adapters.CalendarAdapter;
import com.fome.planster.models.Day;
import com.fome.planster.models.Month;
import com.google.gson.Gson;

import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private Date date;
    private LinearLayout monthSign;
    private GridView calendar;
    private CalendarAdapter calendarAdapter;

    Gson gson = new Gson ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        bindViews();

        refreshCalendar(new Date());

    }

    void bindViews () {
        FontManager.setFont(this, findViewById(R.id.calendar_activity), FontManager.BOLDFONT);

        monthSign = (LinearLayout) findViewById(R.id.month_sign);
        calendar = (GridView) findViewById(R.id.calendar);
        calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectDay((Day) calendarAdapter.getItem(position));
            }
        });

        monthSign.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPrevMonth();
            }
        });
        monthSign.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextMonth();
            }
        });
    }

    void refreshCalendar (Date date) {
        this.date = date;
        ((TextView) monthSign.getChildAt(0)).setText (DateManager.getMonthName(DateManager.getDateAfterMonths(date, -1)));

        ((TextView) monthSign.getChildAt(1)).setText(String.valueOf(DateManager.getMonthName(date)));

        ((TextView) monthSign.getChildAt(2)).setText (DateManager.getMonthName(DateManager.getDateAfterMonths(date, 1)));
        calendarAdapter = new CalendarAdapter(this, new Month(date));
        calendar.setAdapter(calendarAdapter);
    }

    void selectDay (Day day) {
        Intent intent = new Intent();
        intent.putExtra("date", gson.toJson(day.getDate()));
        setResult(RESULT_OK, intent);
        finish();
    }

    void openPrevMonth () {
        refreshCalendar(DateManager.getDateAfterMonths(date, -1));
    }

    void openNextMonth() {
        refreshCalendar(DateManager.getDateAfterMonths(date, 1));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        UIManager.equalizeGridSpacing(calendar);
    }

}
