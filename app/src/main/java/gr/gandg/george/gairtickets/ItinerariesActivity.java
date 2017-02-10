package gr.gandg.george.gairtickets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ItinerariesActivity extends AppCompatActivity {
    String amadeusKey;

    private ListView resultsListView;
    private ArrayAdapter<String> mItineraryAdapter;
    private ArrayList<String> itineraryResults = new ArrayList<String>();
    private ArrayList<Itinerary> itineraryResultsObjects = new ArrayList<Itinerary>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itineraries);
        amadeusKey = BuildConfig.AMADEUS_API_KEY;

        resultsListView = (ListView)findViewById(R.id.resultsListView);

        mItineraryAdapter = new ArrayAdapter<String>(this,
                R.layout.itinerary_list_item, R.id.itinerary_listView, itineraryResults);

        resultsListView.setAdapter(mItineraryAdapter);

        progressDialog = ProgressDialog.show(this, "GAirTickets", "Αναζήτηση πτήσεων...", true);

        (new FlightsParser()).execute();

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Itinerary theItinerary = itineraryResultsObjects.get(i);
                selectItinerary(theItinerary);
            }
        });

    }

    public void selectItinerary(Itinerary theItinerary) {
        Intent itineraryItent = new Intent();
        itineraryItent.setClass(this, ItineraryActivity.class);
        itineraryItent.putExtra("theItinerary", theItinerary.detailView());
        itineraryItent.putExtra("allAirlines", theItinerary.allAirlines);
        startActivity(itineraryItent);
    }

    private class FlightsParser extends AsyncTask<String, Void, ArrayList<Itinerary>> {

        private static final String LOG_TAG = "FlightsParser";

        @Override
        protected ArrayList<Itinerary> doInBackground(String... params) {
            Intent in = getIntent();
            String origin = in.getStringExtra("fromAirport");
            String destination = in.getStringExtra("toAirport");
            String departure_date = in.getStringExtra("departureDate");
            Boolean aleretoure = in.getBooleanExtra("aleretoure", true);
            String return_date = aleretoure ? in.getStringExtra("returnDate"): "";
            int adults = in.getIntExtra("adultsNo", 1);
            int children = in.getIntExtra("childrenNo", 0);
            int infants =in.getIntExtra("infantNo", 0);
            String travelClass = in.getStringExtra("travelClass");
            String nonStop = in.getStringExtra("nonStop");

            StringBuilder sUrl = new StringBuilder("");
            sUrl.append("http://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?apikey=");
            sUrl.append(amadeusKey);
            sUrl.append("&origin=");
            sUrl.append(origin);
            sUrl.append("&destination=");
            sUrl.append(destination);
            sUrl.append("&departure_date=");
            sUrl.append(departure_date);
            if (aleretoure) {
                sUrl.append("&return_date=");
                sUrl.append(return_date);
            }
            sUrl.append("&adults=");
            sUrl.append(adults);
            sUrl.append("&children=");
            sUrl.append(children);
            sUrl.append("&infants=");
            sUrl.append(infants);
            sUrl.append("&nonstop=");
            sUrl.append(nonStop);
            //sUrl.append("&max_price=5000");
            sUrl.append("&travel_class=");
            sUrl.append(travelClass);
            sUrl.append("&currency=EUR");
            sUrl.append("&number_of_results=10");

            String theUrl = sUrl.toString();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String flightsJsonStr = null;



            try {

                Log.i(LOG_TAG, theUrl);
                URL url = new URL(theUrl);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

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
                flightsJsonStr = buffer.toString();
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

            return parseJson(flightsJsonStr);
        }

        /**
         * Parses the Json Result to an ArrayList of Itinerary objects
         */
        private ArrayList<Itinerary> parseJson(String json) {
            if (json == null) {
                return null;
            }
            ArrayList<Itinerary>results = new ArrayList<Itinerary>();
            try {
                JSONObject amadeusResponse = new JSONObject(json);
                JSONArray amadeusResults = amadeusResponse.getJSONArray("results");
                for (int i=0; i< amadeusResults.length();i++) {
                    Itinerary itinerary = new Itinerary();

                    JSONObject amadeusResult = amadeusResults.getJSONObject(i);
                    JSONArray amadeusItineraries = amadeusResult.getJSONArray("itineraries");
// --- outbound
                    JSONObject amadeusOutbound = amadeusItineraries.getJSONObject(0).getJSONObject("outbound");
                    JSONArray amadeusOutboundFlights = amadeusOutbound.getJSONArray("flights");

                    // outbound flights
                    for (int j=0; j<amadeusOutboundFlights.length(); j++) {
                        JSONObject amadeusFlight = amadeusOutboundFlights.getJSONObject(j);
                        Flight flight = new Flight();
                        flight.departsAt = amadeusFlight.getString("departs_at");
                        flight.arrivesAt = amadeusFlight.getString("arrives_at");
                        flight.originAirport = amadeusFlight.getJSONObject("origin").getString("airport");
                        flight.destinationAirport = amadeusFlight.getJSONObject("destination").getString("airport");
                        flight.marketingAirline = amadeusFlight.getString("marketing_airline");
                        flight.operatingAirline = amadeusFlight.getString("operating_airline");
                        flight.flightNumber = amadeusFlight.getString("flight_number");
                        flight.aircraft = amadeusFlight.getString("aircraft");
                        flight.travelClass = amadeusFlight.getJSONObject("booking_info").getString("travel_class");
                        flight.bookingCode = amadeusFlight.getJSONObject("booking_info").getString("booking_code");
                        flight.seatsRemaining = amadeusFlight.getJSONObject("booking_info").getInt("seats_remaining");
                        itinerary.outbound.add(flight);
                    }


// --- inbound
                    if (  amadeusItineraries.getJSONObject(0).has("inbound")) {

                        JSONObject amadeusInbound = amadeusItineraries.getJSONObject(0).getJSONObject("inbound");

                        JSONArray amadeusIboundFlights = amadeusOutbound.getJSONArray("flights");

                        // inbound flights
                        for (int j = 0; j < amadeusIboundFlights.length(); j++) {
                            JSONObject amadeusFlight = amadeusOutboundFlights.getJSONObject(j);
                            Flight flight = new Flight();
                            flight.departsAt = amadeusFlight.getString("departs_at");
                            flight.arrivesAt = amadeusFlight.getString("arrives_at");
                            flight.originAirport = amadeusFlight.getJSONObject("origin").getString("airport");
                            flight.destinationAirport = amadeusFlight.getJSONObject("destination").getString("airport");
                            flight.marketingAirline = amadeusFlight.getString("marketing_airline");
                            flight.operatingAirline = amadeusFlight.getString("operating_airline");
                            flight.flightNumber = amadeusFlight.getString("flight_number");
                            flight.aircraft = amadeusFlight.getString("aircraft");
                            flight.travelClass = amadeusFlight.getJSONObject("booking_info").getString("travel_class");
                            flight.bookingCode = amadeusFlight.getJSONObject("booking_info").getString("booking_code");
                            flight.seatsRemaining = amadeusFlight.getJSONObject("booking_info").getInt("seats_remaining");
                            itinerary.inbound.add(flight);
                        }
                    }
                    JSONObject amadeusFare = amadeusResult.getJSONObject("fare");
                    itinerary.totalPrice = amadeusFare.getDouble("total_price");
                    itinerary.refundable = amadeusFare.getJSONObject("restrictions").getBoolean("refundable");
                    itinerary.changePenalties = amadeusFare.getJSONObject("restrictions").getBoolean("change_penalties");

                    results.add(itinerary);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return  results;
        }
        @Override
        protected void onPostExecute(ArrayList<Itinerary> result) {

            if (result == null) {
                itineraryResultsObjects.clear();
                itineraryResults.clear();
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Κανένα Αποτέλεσμα!",Toast.LENGTH_LONG).show();
                finish();
            } else {
                itineraryResultsObjects = result;
                itineraryResults.clear();
                if (result != null) {
                    for (int i = 0; i < result.size(); i++) {
                        Itinerary it = result.get(i);
                        itineraryResults.add(it.toString());
                    }
                    mItineraryAdapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
            }
        }
    }

}
