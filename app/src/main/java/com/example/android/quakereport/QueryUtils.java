package com.example.android.quakereport;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static ArrayList<Earthquake> fetchEarthquakeData(String url) {
        URL urlConnection = createUrl(url);
        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(urlConnection);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "fetchEarthquakeData: ", e);
        }

        return extractEarthquakes(jsonResponse);
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<Earthquake> extractEarthquakes(String json) {
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray features = jsonObject.optJSONArray("features");
            for(int i = 0; i < features.length(); i++) {
                JSONObject properties = features.getJSONObject(i).optJSONObject("properties");
                earthquakes.add(new Earthquake(properties.optDouble("mag"),
                        properties.optString("place"), properties.optLong("time"),
                        properties.optString("url")));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null)
            return jsonResponse;

        InputStream in = null;
        HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();

        try {
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == urlConnection.HTTP_OK) {
                in = urlConnection.getInputStream();
                jsonResponse = streamReader(in);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "makeHttpRequest: ", e.getCause());
        }
        finally {
            if(urlConnection != null)
            urlConnection.disconnect();

            if(in != null)
            in.close();
        }
        return jsonResponse;
    }

    private static String streamReader(InputStream input) {
        StringBuilder output = new StringBuilder();
        String nextLine;
        try {
            if(input != null) {
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(input, Charset.forName("UTF-8")));
                while((nextLine = reader.readLine()) != null)
                    output.append(nextLine);
                reader.close();
            }
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "streamReader: ", e);
        }

        return output.toString();
    }

    /**
     * Returns new URL object from the given string URL.
     */
    @Nullable
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

}