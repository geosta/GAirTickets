package gr.gandg.george.gairtickets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ItineraryActivity extends AppCompatActivity {

    private TextView itineraryTextView;
    String theItinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        itineraryTextView = (TextView) findViewById(R.id.itinerary_textview);
        theItinerary = getIntent().getStringExtra("theItinerary");
        displayResults();
    }

    private void displayResults() {
        String dispText = "\nΕΠΙΛΕΓΜΕΝΟ ΔΡΟΜΟΛΟΓΙΟ\n\n" + theItinerary;
        itineraryTextView.setText(dispText);
    }

    public void bookNow(View v) {
        Toast.makeText(this, "Booking completed successfully", Toast.LENGTH_LONG).show();
    }
}
