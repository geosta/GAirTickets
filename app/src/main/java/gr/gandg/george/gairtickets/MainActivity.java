package gr.gandg.george.gairtickets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * UOM Mobile Development 2017 project
 * (c) George Stathis - it01151
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView mainTextView =  (TextView)findViewById(R.id.main_textview);
        String amadeusKey;
        String iataCodesKey;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amadeusKey = BuildConfig.AMADEUS_API_KEY;
        iataCodesKey = BuildConfig.IATA_CODES_API_KEY;

        AirlineParser airline = new AirlineParser(getApplicationContext(), iataCodesKey);
        AirportParser airport = new AirportParser(amadeusKey);

        mainTextView.setText("Airline:\n" +  airline.execute("OA") + "\nAirport:\n" + airport.search("the"));

    }

    private void main_from() {
/*        Properties properties = new Properties();

        String amadeusKey;
        String iataCodesKey;

        FileInputStream is;
        try {
            is = new FileInputStream("GAirTickets.ini");
            properties.load(is);
            is.close();
        }
        catch (IOException e) {
        }

        amadeusKey = properties.getProperty("amadeusKey");
        iataCodesKey = properties.getProperty("iataCodesKey");


        AirlineParser airline = new AirlineParser(iataCodesKey);
        System.out.println(airline.search("OA"));

        AirportParser airport = new AirportParser(amadeusKey);
        System.out.println(airport.search("the"));
*/
    }
}
