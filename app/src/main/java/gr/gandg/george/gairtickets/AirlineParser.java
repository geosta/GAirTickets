package gr.gandg.george.gairtickets;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static java.security.AccessController.getContext;

/**
 * Created by george on 1/2/2017.
 */

public class AirlineParser extends AsyncTask<String, Void, Void> {

    public final String LOG_TAG = "AirlineParser";
    public String airlineSearchString = null;
    private String API_KEY = null;

    private final TextView theTextView;

    AirlineParser(TextView _textView, String _apiKey) {
        theTextView = _textView;
        API_KEY = _apiKey;
    }




    @Override
    protected Void doInBackground(String... params) {
        airlineSearchString = params[0];

        if (airlineSearchString == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String airportJsonStr = null;

        String API_BASE_URL = "https://iatacodes.org/api/v6/airlines";

        try {

            String theURL = API_BASE_URL + "?api_key=" + API_KEY +
                    "&code=\"" + URLEncoder.encode(airlineSearchString, "UTF-8") + "\"";
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

            theTextView.setText(theTextView.getText() + "\n" + airportJsonStr);


        } catch (IOException e) {
            System.out.println(LOG_TAG + "Error " + e);
        } catch (Exception  e) {
            System.out.println(LOG_TAG + e.getMessage() + e);
            e.printStackTrace();

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    System.out.println(LOG_TAG + "Error closing stream" + e);
                }
            }
        }

        return null;
    }


}
