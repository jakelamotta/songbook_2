package songbook.asu.ax.songbook.data;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.Utilities;

/**
 * Created by EIS i7 Gamer on 2015-02-26.
 */
public class SongSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = SongSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in milliseconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int SONG_NOTIFICATION_ID = 3004;
    private static final String PREFERENCE_UPDATED = "preference_last_updated";

    public SongSyncAdapter(Context context, boolean autoInitialize){
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String last_updated = prefs.getString(PREFERENCE_UPDATED,"20010101");
        // Will contain the raw JSON response as a string.
        String songJsonStr = null;
        String format = "json";
        String units = "metric";
        int numDays = 14;
        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are avaiable at OWM's forecast API page, at
        // http://openweathermap.org/API#forecast
        final String SONG_BASE_URL = "http://83.255.37.175/songbook/dbtest.php?";
        final String ID_PARAM = "song";
        final String TIME_PARAM = "timestamp";
        final String EVENT_PARAM = "event";

        Uri builtUri;

        try {
            String timestamp;
            String event;

            builtUri = Uri.parse(SONG_BASE_URL).buildUpon()
                    .appendQueryParameter(ID_PARAM,"0")
                    .appendQueryParameter(TIME_PARAM, last_updated)
                    .appendQueryParameter(EVENT_PARAM,"0")
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
                return;
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
                return;
            }
            songJsonStr = buffer.toString();

            getSongDataFromJson(songJsonStr);

            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            String formattedDateString = Utilities.formatDateString(sqlDate.toString());

            //Update the shared preference
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PREFERENCE_UPDATED,formattedDateString);
            editor.apply();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return;
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet. If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {
            /*
            * Add the account and account type, no password or user data
            * If successful, return the Account object, otherwise report an error.
            */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
            * If you don't set android:syncable="true" in
            * in your <provider> element in the manifest,
            * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
            * here.
            */
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {

        /*
        * Since we've created an account
        */
        SongSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
        * Without calling setSyncAutomatically, our periodic sync will not be enabled.
        */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
        * Finally, let's do a sync to get things started
        */
        syncImmediately(context);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority =  context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private void getSongDataFromJson(String jsonStr)throws JSONException {
        final String OWM_SONG = "songs";

        try {
            JSONObject songListJson = new JSONObject(jsonStr);

            JSONObject songArray = songListJson.getJSONObject("songs");
            JSONObject eventArray = songListJson.getJSONObject("events");
            JSONObject relationshipArray = songListJson.getJSONObject("eventHasSong");

            Iterator keys = songArray.keys();

            Vector<ContentValues> cVVector = new Vector<ContentValues>(songArray.length());
            String key;
            JSONObject tempObj;
            while (keys.hasNext()){
                key = keys.next().toString();
                tempObj = new JSONObject(songArray.getString(key));
                String id = tempObj.getString("id");
                String melody = tempObj.getString("melody");
                String text = tempObj.getString("text");

                ContentValues songValues = new ContentValues();
                songValues.put(SongContract.SongTable.COLUMN_SONG_ID, id);
                songValues.put(SongContract.SongTable.COLUMN_SONG_NAME, key);
                songValues.put(SongContract.SongTable.COLUMN_SONG_MELODY, melody);
                songValues.put(SongContract.SongTable.COLUMN_TEXT, text);
                songValues.put(SongContract.SongTable.COLUMN_LAST_UPADTED,"20150306");
                cVVector.add(songValues);
            }

            keys = eventArray.keys();

            while (keys.hasNext()){
                key = keys.next().toString();
                tempObj = new JSONObject(eventArray.getString(key));
                String id = tempObj.getString("id");
                String date = tempObj.getString("date");
                String current_song = tempObj.getString("current_song");

                ContentValues eventValues = new ContentValues();
                eventValues.put(SongContract.EventTable.COLUMN_EVENT_DATE,date);
                eventValues.put(SongContract.EventTable.COLUMN_CURRENT_SONG,current_song);

            }

                int inserted = 0;

                // add to database
                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    getContext().getContentResolver().bulkInsert(SongContract.SongTable.CONTENT_URI, cvArray);
                }
        }
        catch (Exception e){
            Log.e(LOG_TAG,e.getMessage());
        }
    }
}
