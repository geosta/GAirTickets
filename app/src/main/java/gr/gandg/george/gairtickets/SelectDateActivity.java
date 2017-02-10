package gr.gandg.george.gairtickets;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

public class SelectDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        CalendarView cv = (CalendarView)findViewById(R.id.calendarView);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                selectDate("" + i + "-" + in2digits((i1+1)) + "-" + in2digits(i2) );
            }
        });

    }

    private String in2digits(int i) {
        if (i<=9)
            return "0" + i;
        else
            return "" + i;
    }

    public void selectDate(String theDate) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("selected_date", theDate);
        resultIntent.putExtra("dateTextViewName", getIntent().getIntExtra("dateTextViewName",0));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
