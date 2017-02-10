package gr.gandg.george.gairtickets;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

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
        (new AirlineParser()).execute(getIntent().getStringExtra("allAirlines"));
    }

    private void displayResults() {
        String dispText = "\nΕΠΙΛΕΓΜΕΝΟ ΔΡΟΜΟΛΟΓΙΟ\n\n" + theItinerary;
        itineraryTextView.setText(dispText);
    }

    public void bookFlight(View v) {
        Toast.makeText(this, "Booking completed successfully", Toast.LENGTH_LONG).show();
    }


    private class AirlineParser extends AsyncTask<String, Void, Void >{

        private static final String LOG_TAG = "AirlineParser";
        private String API_KEY = null;
        private ArrayList<String>airlineCodes = new ArrayList<String>();
        private ArrayList<String>airlineNames = new ArrayList<String>();



        protected Void doInBackground(String... params) {
            if (params[0] == null) {
                return null;
            }

            String airlineSearchString = params[0];

            API_KEY =BuildConfig.IATA_CODES_API_KEY;;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String airlineJsonStr = null;

            String API_BASE_URL = "https://iatacodes.org/api/v6/airlines";

            try {

                String theURL = API_BASE_URL + "?api_key=" + API_KEY +
                        "&code=" + airlineSearchString ;
                Log.i(LOG_TAG, theURL);
                URL url = new URL(theURL);


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
                airlineJsonStr = buffer.toString();
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

            parseJson(airlineJsonStr);
            return null;
        }

        private void parseJson(String json) {

            try {
                JSONObject responseObject = new JSONObject(json);
                JSONArray airlinesArray = responseObject.getJSONArray("response");

                for (int i=0; i< airlinesArray.length();i++) {
                    JSONObject airlineObject = airlinesArray.getJSONObject(i);
                    String airlineCode = airlineObject.getString("code");
                    String airlineName  = airlineObject.getString("name");
                    airlineCodes.add(airlineCode);
                    airlineNames.add(airlineName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            TextView tv = ((TextView)findViewById(R.id.itinerary_textview));
            String theResult = tv.getText().toString();
            for (int i=0; i<airlineCodes.size(); i++) {
                theResult = theResult.replace("=" + airlineCodes.get(i).toString() + "=", airlineNames.get(i).toString());
            }
            tv.setText(theResult);
        }
    }

}
