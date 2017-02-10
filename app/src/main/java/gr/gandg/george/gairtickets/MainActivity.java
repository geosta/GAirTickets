package gr.gandg.george.gairtickets;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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


    public void searchFlights(View v) {
        Intent itinerariesIntent = new Intent();
        itinerariesIntent.setClass(this, ItinerariesActivity.class);

        String fromAirport, toAirport;
        boolean aleretoure;
        String departureDate, returnDate;
        String adultsNo, childrenNo, infantNo;
        String travelClass;
        String nonStop;

        fromAirport = ((TextView)findViewById(R.id.from_airport_textview)).getText().toString();
        toAirport = ((TextView)findViewById(R.id.to_airport_textview)).getText().toString();
        RadioButton aleretoure_radiobutton =  (RadioButton)findViewById(R.id.aleretoure_radiobutton);
        aleretoure = aleretoure_radiobutton.isChecked() ? true : false;
        departureDate = ((TextView)findViewById(R.id.depature_date_textview)).getText().toString();
        returnDate = ((TextView)findViewById(R.id.return_date_textview)).getText().toString();
        adultsNo = ((TextView)findViewById(R.id.adults_textview)).getText().toString();
        childrenNo = ((TextView)findViewById(R.id.children_textview)).getText().toString();
        infantNo = ((TextView)findViewById(R.id.infants_textview)).getText().toString();

        RadioGroup travelClassRadioGroup = (RadioGroup)findViewById(R.id.travel_class_radiogroup);
        travelClass = ((RadioButton)findViewById(travelClassRadioGroup.getCheckedRadioButtonId())).getText().toString();

        CheckBox nonstopCheckBox = (CheckBox)findViewById(R.id.nonstop_checkBox);
        nonStop = nonstopCheckBox.isChecked() ? "true" : "false";

        fromAirport = fromAirport.substring(fromAirport.indexOf("[") + 1, fromAirport.indexOf("]"));
        toAirport = toAirport.substring(toAirport.indexOf("[") + 1, toAirport.indexOf("]"));
        itinerariesIntent.putExtra("fromAirport",fromAirport);
        itinerariesIntent.putExtra("toAirport",toAirport);
        itinerariesIntent.putExtra("aleretoure",aleretoure);
        itinerariesIntent.putExtra("departureDate",departureDate);
        itinerariesIntent.putExtra("returnDate",returnDate);
        itinerariesIntent.putExtra("adultsNo",adultsNo);
        itinerariesIntent.putExtra("childrenNo",childrenNo);
        itinerariesIntent.putExtra("infantNo",infantNo);
        itinerariesIntent.putExtra("travelClass",travelClass);
        itinerariesIntent.putExtra("nonStop",nonStop);


        startActivity(itinerariesIntent);
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
