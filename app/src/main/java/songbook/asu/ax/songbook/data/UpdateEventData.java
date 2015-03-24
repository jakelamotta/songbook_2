package songbook.asu.ax.songbook.data;

import android.accounts.AccountManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import songbook.asu.ax.songbook.Utilities;

/**
 * Created by Kristian on 2015-03-18.
 */
public class UpdateEventData {

    private static final String LOG_TAG = UpdateEventData.class.getSimpleName();

    public static void main(String[] argvs){
        boolean bool = writeToServer(null);
        if (bool){
            System.out.println("true!");
        }
    }

    public static boolean writeToServer(@Nullable AccountManager manager){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String songJsonStr = null;

                // Construct the URL for the OpenWeatherMap query
        // Possible parameters are avaiable at OWM's forecast API page, at
        // http://openweathermap.org/API#forecast
        final String EVENT_BASE_URL = "http://83.255.37.175/songbook/updateEvent.php?";
        final String ID_PARAM = "song";
        final String EVENT_PARAM = "event";

        Uri builtUri;

        try {
            String timestamp;
            String event;

            builtUri = Uri.parse(EVENT_BASE_URL).buildUpon().appendQueryParameter(ID_PARAM,"0")
                    .appendQueryParameter(EVENT_PARAM, Utilities.formatDateString(new Date()))
                    .build();

            //Log.v(LOG_TAG,builtUri.toString());
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {

                // Nothing to do.
                return false;
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
                // Stream was empty. No point in parsing.
                return false;
            }
            songJsonStr = buffer.toString();

        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error ", e);
            System.out.println(e.getMessage());

            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (Exception e) {
            //Log.e(LOG_TAG, e.getMessage(), e);
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    System.out.println(e.getMessage());
                    //Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return true;
    }
}
