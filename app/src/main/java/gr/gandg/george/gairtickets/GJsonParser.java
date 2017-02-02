package gr.gandg.george.gairtickets;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by george on 1/2/2017.
 */

public class GJsonParser {

    String JsonStr=null;

    GJsonParser(String _JsonStr) {
        JsonStr = _JsonStr;
    }

//    public void Process() {
//        try {
//            JSONObject resultsJson = new JSONObject(JsonStr);
//
//            JSONArray resultsArray = resultsJson.getJSONArray(OWM_LIST);
//
//            JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
//            String cityName = cityJson.getString(OWM_CITY_NAME);
//
//            double cityLatitude = cityCoord.getDouble(OWM_LATITUDE);
//            double cityLongitude = cityCoord.getDouble(OWM_LONGITUDE);
//
//            long locationId = addLocation(locationSetting, cityName, cityLatitude, cityLongitude);
//
//
//            for(int i = 0; i < weatherArray.length(); i++) {
//                // These are the values that will be collected.
//
//                // Get the JSON object representing the day
//                JSONObject dayForecast = weatherArray.getJSONObject(i);
//
//                // Cheating to convert this to UTC time, which is what we want anyhow
//                dateTime = dayTime.setJulianDay(julianStartDay+i);
//
//                pressure = dayForecast.getDouble(OWM_PRESSURE);
//                humidity = dayForecast.getInt(OWM_HUMIDITY);
//                windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
//                windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);
//
//                // Description is in a child array called "weather", which is 1 element long.
//                // That element also contains a weather code.
//                JSONObject weatherObject =
//                        dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
//                description = weatherObject.getString(OWM_DESCRIPTION);
//                weatherId = weatherObject.getInt(OWM_WEATHER_ID);
//
//                // Temperatures are in a child object called "temp".  Try not to name variables
//                // "temp" when working with temperature.  It confuses everybody.
//                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
//                high = temperatureObject.getDouble(OWM_MAX);
//                low = temperatureObject.getDouble(OWM_MIN);
//
//                ContentValues weatherValues = new ContentValues();
//
//                weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationId);
//                weatherValues.put(WeatherEntry.COLUMN_DATE, dateTime);
//                weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, humidity);
//                weatherValues.put(WeatherEntry.COLUMN_PRESSURE, pressure);
//                weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
//                weatherValues.put(WeatherEntry.COLUMN_DEGREES, windDirection);
//                weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, high);
//                weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, low);
//                weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, description);
//                weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, weatherId);
//
//                cVVector.add(weatherValues);
//            }
//
//            int inserted = 0;
//            // add to database
//            if ( cVVector.size() > 0 ) {
//                ContentValues[] cvArray = new ContentValues[cVVector.size()];
//                cVVector.toArray(cvArray);
//                inserted = mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI, cvArray);
//            }
//
//            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");
//
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, e.getMessage(), e);
//            e.printStackTrace();
//        }
//    }

}
