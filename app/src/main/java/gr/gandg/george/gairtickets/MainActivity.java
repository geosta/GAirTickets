package gr.gandg.george.gairtickets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * UOM Mobile Development 2017 project
 * (c) George Stathis - it01151
 */
public class MainActivity extends AppCompatActivity {

    public static final int ACTIVITY_SELECT_DATE = 1;
    public static final int ACTIVITY_SELECT_AIRPORT=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String amadeusKey;
        String iataCodesKey;

        amadeusKey = BuildConfig.AMADEUS_API_KEY;
        iataCodesKey = BuildConfig.IATA_CODES_API_KEY;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restorePreferences();

        RadioGroup flightTypeRadiogroup = (RadioGroup)findViewById(R.id.flight_type_radiogroup);
        flightTypeRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.aleretoure_radiobutton:
                        break;
                }
            }
        });
    }

    private void restorePreferences() {
        SharedPreferences preferences = getSharedPreferences("G_AIR_TICKETS", Context.MODE_PRIVATE);
        if (preferences.contains("fromAirport"))
            ((TextView)findViewById(R.id.from_airport_textview)).setText(preferences.getString("fromAirport",""));
        if (preferences.contains("toAirport"))
             ((TextView)findViewById(R.id.to_airport_textview)).setText(preferences.getString("toAirport",""));
        if (preferences.contains("departureDate"))
             ((TextView)findViewById(R.id.depature_date_textview)).setText(preferences.getString("departureDate",""));
        if (preferences.contains("returnDate"))
             ((TextView)findViewById(R.id.return_date_textview)).setText(preferences.getString("returnDate",""));
        if (preferences.contains("adultsNo"))
            ((TextView)findViewById(R.id.adults_textview)).setText(preferences.getString("adultsNo","1"));
        if (preferences.contains("childrenNo"))
            ((TextView)findViewById(R.id.children_textview)).setText(preferences.getString("childrenNo","0"));
        if (preferences.contains("infantNo"))
             ((TextView)findViewById(R.id.infants_textview)).setText(preferences.getString("infantNo","0"));

    }



    public void searchFlights(View v) {
        Intent itinerariesIntent = new Intent();
        itinerariesIntent.setClass(this, ItinerariesActivity.class);

        String fromAirport, toAirport, fromAirportId, toAirportId;
        boolean aleretoure;
        String departureDate, returnDate;
        String adultsNo, childrenNo, infantNo;
        String travelClass;
        String nonStop;

        SharedPreferences preferences = getSharedPreferences("G_AIR_TICKETS", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        try {

            fromAirport = ((TextView)findViewById(R.id.from_airport_textview)).getText().toString();
            toAirport = ((TextView)findViewById(R.id.to_airport_textview)).getText().toString();
            RadioButton aleretoure_radiobutton =  (RadioButton)findViewById(R.id.aleretoure_radiobutton);
            aleretoure = aleretoure_radiobutton.isChecked() ? true : false;
            departureDate = ((TextView)findViewById(R.id.depature_date_textview)).getText().toString();
            returnDate = ((TextView)findViewById(R.id.return_date_textview)).getText().toString();
            adultsNo = ((TextView)findViewById(R.id.adults_textview)).getText().toString();
            childrenNo = ((TextView)findViewById(R.id.children_textview)).getText().toString();
            infantNo = ((TextView)findViewById(R.id.infants_textview)).getText().toString();

            if (fromAirport.length()==0 || toAirport.length()==0 || departureDate.length()==0 ||
                    (aleretoure && returnDate.length()==0)) {
                Toast.makeText(this, "Συμπληρώστε τα στοιχεία...", Toast.LENGTH_LONG).show();
                return;
            }

            RadioGroup travelClassRadioGroup = (RadioGroup)findViewById(R.id.travel_class_radiogroup);
            travelClass = ((RadioButton)findViewById(travelClassRadioGroup.getCheckedRadioButtonId())).getText().toString();

            CheckBox nonstopCheckBox = (CheckBox)findViewById(R.id.nonstop_checkBox);
            nonStop = nonstopCheckBox.isChecked() ? "true" : "false";

            fromAirportId = fromAirport.substring(fromAirport.indexOf("[") + 1, fromAirport.indexOf("]"));
            toAirportId = toAirport.substring(toAirport.indexOf("[") + 1, toAirport.indexOf("]"));
            itinerariesIntent.putExtra("fromAirport",fromAirportId);
            itinerariesIntent.putExtra("toAirport",toAirportId);
            itinerariesIntent.putExtra("aleretoure",aleretoure);
            itinerariesIntent.putExtra("departureDate",departureDate);
            itinerariesIntent.putExtra("returnDate",returnDate);
            itinerariesIntent.putExtra("adultsNo",adultsNo);
            itinerariesIntent.putExtra("childrenNo",childrenNo);
            itinerariesIntent.putExtra("infantNo",infantNo);
            itinerariesIntent.putExtra("travelClass",travelClass);
            itinerariesIntent.putExtra("nonStop",nonStop);

            editor.putString("fromAirport",fromAirport);
            editor.putString("toAirport",toAirport);
            editor.putBoolean("aleretoure",aleretoure);
            editor.putString("departureDate",departureDate);
            editor.putString("returnDate",returnDate);
            editor.putString("adultsNo", adultsNo);
            editor.putString("childrenNo",childrenNo);
            editor.putString("infantNo", infantNo);
            editor.putString("travelClass",travelClass);
            editor.putString("nonStop",nonStop);
            editor.commit();

            startActivity(itinerariesIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Συμπληρώστε τα στοιχεία...", Toast.LENGTH_LONG).show();
        }
    }

    public void selectDate(View v) {
        Intent selectDateIntent = new Intent();
        selectDateIntent.setClass(this, SelectDateActivity.class);
        selectDateIntent.putExtra("dateTextViewName",v.getId());
        startActivityForResult(selectDateIntent,ACTIVITY_SELECT_DATE);
    }

    public void selectAirport(View v) {
        Intent selectAirportIntent = new Intent();
        selectAirportIntent.setClass(this, SelectAirportActivity.class);
        selectAirportIntent.putExtra("airportTextViewName",v.getId());
        startActivityForResult(selectAirportIntent,ACTIVITY_SELECT_AIRPORT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_SELECT_DATE) {
            if (resultCode == Activity.RESULT_OK) {
                String theDate = data.getStringExtra("selected_date");
                int theID = data.getIntExtra("dateTextViewName",0);
                ((TextView)findViewById(theID)).setText(theDate);
            }
        } else if (requestCode == ACTIVITY_SELECT_AIRPORT) {
            if (resultCode == Activity.RESULT_OK) {
                String theAirport = data.getStringExtra("selected_airport");
                int theID = data.getIntExtra("airportTextViewName",0);
                ((TextView)findViewById(theID)).setText(theAirport);
            }
        }
    }

}
