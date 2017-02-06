package gr.gandg.george.gairtickets;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * UOM Mobile Development 2017 project
 * (c) George Stathis - it01151
 */
public class MainActivity extends AppCompatActivity {

    TextView mainTextView;
    String amadeusKey;
    String iataCodesKey;

    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainTextView =  (TextView)findViewById(R.id.main_textview);

//
        amadeusKey = BuildConfig.AMADEUS_API_KEY;
        iataCodesKey = BuildConfig.IATA_CODES_API_KEY;
//
//
//        mainTextView.setText("Airline:\n" +  airline.execute("OA") + "\nAirport:\n" + airport.search("the"));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AirportParser airport= new AirportParser ();
        airport.execute();
        //AirportParser airport = new AirportParser(amadeusKey);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);

        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_from_airport);
        textView.setAdapter(adapter);

    }


    private class AirportParser extends AsyncTask<String, Void, String> {

        private static final String LOG_TAG = "AirportParser";
        private  String airportSearchString = "thess";
        private String API_KEY = null;



        protected String doInBackground(String... params) {
            if (airportSearchString == null) {
                return null;
            }

            API_KEY = amadeusKey;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String airportJsonStr = null;

            String API_BASE_URL = "https://api.sandbox.amadeus.com/v1.2/airports/autocomplete";

            try {

                String theURL = API_BASE_URL + "?apikey=" + API_KEY +
                        "&term=" + URLEncoder.encode(airportSearchString, "UTF-8");
                Log.i(LOG_TAG, theURL);
                URL url = new URL(theURL);


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                airportJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error " + e);
            } catch (Exception  e) {
                Log.e(LOG_TAG, e.getMessage() + e);
                e.printStackTrace();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream" + e);
                    }
                }
            }

            return airportJsonStr;
        }


        @Override
        protected void onPostExecute(String s) {
            TextView mtv = (TextView)findViewById(R.id.main_textview);
            mtv.setText(s);
        }
    }



}
