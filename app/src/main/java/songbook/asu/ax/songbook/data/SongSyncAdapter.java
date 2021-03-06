package songbook.asu.ax.songbook.data;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
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
import java.util.Date;
import java.util.Iterator;

import java.util.Vector;

import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.Utilities;
import songbook.asu.ax.songbook.activities.GuestbookActivity;
import songbook.asu.ax.songbook.activities.MainActivity;

/**
 * Created by EIS i7 Gamer on 2015-02-26.
 */
public class SongSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String PREFERENCE_GUESTBOOK_UPDATED = "preference_guestbook_last_synced";
    private static final String PREFERENCE_UPDATED = "preference_last_synced";
    private static final long SONG_SYNC_INTERVAL = 60 * 1 * 1000;
    public final String LOG_TAG = SongSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in milliseconds.
    // 60 seconds (1 minute) * 60 * 24 = 24 hours
    public static final int SYNC_INTERVAL = 60 * 60 * 24*1000;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

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

        String last_updated = prefs.getString(PREFERENCE_UPDATED, "20010101");
        String songJsonStr = null;

        final String SONGBOOK_BASE_URL = "http://songbook.asu.ax/api/";
        final String SONG_URL = SONGBOOK_BASE_URL + "song";
        final String EVENT_URL = SONGBOOK_BASE_URL + "event";
        final String GUESTBOOK_URL = SONGBOOK_BASE_URL + "message";
        final String GUESTBOOK_AFTER_DATE_URL = GUESTBOOK_URL + "/after/";
        final String ID_PARAM = "song";
        final String TIME_PARAM = "timestamp";
        final String EVENT_PARAM = "event";

        Uri builtUri;
        URL url;
        InputStream inputStream;
        StringBuffer buffer;
        String line;
        String formattedDateString = "";

        try {
            if (!prefs.getBoolean(GuestbookActivity.SYNC_GUESTBOOK_ONLY,false) && Utilities.getTimeStamp()-prefs.
                    getLong(MainActivity.LAST_SYNCED,0) > SONG_SYNC_INTERVAL) {
                builtUri = Uri.parse(SONG_URL).buildUpon()
                        .appendQueryParameter(ID_PARAM, "0")
                        .appendQueryParameter(TIME_PARAM, last_updated)
                        .appendQueryParameter(EVENT_PARAM, "")
                        .build();

                url = new URL(builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                inputStream = urlConnection.getInputStream();
                buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

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

                //formattedDateString = Utilities.formatDateString(new Date());

                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(MainActivity.LAST_SYNCED, Utilities.getTimeStamp());
                editor.apply();
            }
            /***************************************************************************************
             ************************GET GUESTBOOK ENTRIES******************************************
             *************************************************************************************/
            if (prefs.getBoolean(GuestbookActivity.SYNC_GUESTBOOK_ONLY,true)) {
                String after = "0";
                if (prefs.contains(PREFERENCE_GUESTBOOK_UPDATED)) {
                    after = prefs.getString(PREFERENCE_GUESTBOOK_UPDATED, "0");
                }

                builtUri = Uri.parse(GUESTBOOK_AFTER_DATE_URL + after).buildUpon()
                        .build();

                url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                inputStream = urlConnection.getInputStream();
                buffer = new StringBuffer();

                if (inputStream == null) {
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = reader.readLine()) != null) {
                    //Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    //But it does make debugging a *lot* easier if you print out the completed
                    //buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return;
                }

                String guestbookJsonStr = buffer.toString();
                String newTimeStamp = getGuestbookDataFromJson(guestbookJsonStr);
                SharedPreferences.Editor editor = prefs.edit();
                if (newTimeStamp != null) {

                    int timeStamp = Integer.parseInt(newTimeStamp);
                    if (timeStamp != 0) {
                        newTimeStamp = String.valueOf(timeStamp++);
                        //Update the shared preference

                        editor.putString(PREFERENCE_UPDATED, formattedDateString);
                        editor.putString(PREFERENCE_GUESTBOOK_UPDATED, newTimeStamp);
                        editor.apply();
                    }
                }

                editor.putBoolean(GuestbookActivity.SYNC_GUESTBOOK_ONLY,false);
                editor.apply();
            }
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

    private String getGuestbookDataFromJson(String jsonStr) {
        String newTimeStamp ="0";

        try {
            JSONArray entryList = new JSONArray(jsonStr);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(entryList.length());
            JSONObject tempObj;

            for (int i=0; i<entryList.length();i++){
                tempObj = entryList.getJSONObject(i);
                String name = tempObj.getString("name");
                String body = tempObj.getString("body");
                String timestamp_ = tempObj.getString("timestamp");

                if (i==0){
                newTimeStamp = timestamp_;
                }

                ContentValues entryValues = new ContentValues();
                entryValues.put(SongContract.GuestbookTable.COLUMN_POSTER, name);
                entryValues.put(SongContract.GuestbookTable.COLUMN_ENTRY, body);
                entryValues.put(SongContract.GuestbookTable.COLUMN_TIMESTAMP, timestamp_);
                cVVector.add(entryValues);
            }

            int inserted = 0;

            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                //getContext().getContentResolver().insert(SongContract.GuestbookTable.buildGuestbookUri(),cvArray[0]);
                getContext().getContentResolver().bulkInsert(SongContract.GuestbookTable.CONTENT_URI, cvArray);
            }
        }

        catch (Exception e){
            Log.e(LOG_TAG,e.getMessage());
        }

        return newTimeStamp;
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),context.getString(R.string.content_authority) , bundle);
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
        try {
            JSONArray songList = new JSONArray(jsonStr);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(songList.length());
            JSONObject tempObj;

            for (int i=0;i<songList.length();i++){
                tempObj = songList.getJSONObject(i);
                String id = tempObj.getString("id");
                String melody = tempObj.getString("melody");
                String text = tempObj.getString("text");
                String name = tempObj.getString("name");
                String category = tempObj.getString("category");

                if (category.isEmpty()){
                    category = "other";
                }

                ContentValues songValues = new ContentValues();
                songValues.put(SongContract.SongTable.COLUMN_SONG_ID, id);
                songValues.put(SongContract.SongTable.COLUMN_SONG_NAME, name);
                songValues.put(SongContract.SongTable.COLUMN_SONG_MELODY, melody);
                songValues.put(SongContract.SongTable.COLUMN_TEXT, text);
                songValues.put(SongContract.SongTable.COLUMN_CATEGORY,category);
                songValues.put(SongContract.SongTable.COLUMN_LAST_UPADTED,"20150306");
                cVVector.add(songValues);
            }

            int inserted = 0;

            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(SongContract.SongTable.CONTENT_URI, cvArray);
            }

            /*keys = eventArray.keys();
            cVVector = new Vector<ContentValues>(eventArray.length());

            while (keys.hasNext()){
                key = keys.next().toString();
                tempObj = new JSONObject(eventArray.getString(key));
                String id = tempObj.getString("id");
                String name = key.toString();
                String date = tempObj.getString("date");
                //String current_song = tempObj.getString("current_song");

                ContentValues eventValues = new ContentValues();
                eventValues.put(SongContract.EventTable.COLUMN_EVENT_NAME,name);
                eventValues.put(SongContract.EventTable.COLUMN_EVENT_DATE,date);
                eventValues.put(SongContract.EventTable.COLUMN_CURRENT_SONG,"placeholdertext, line 288 SongSyncAdapter");
                eventValues.put(SongContract.EventTable.COLUMN_EVENT_ID,id);
                cVVector.add(eventValues);
            }

            inserted = 0;

            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(SongContract.EventTable.CONTENT_URI, cvArray);
            }

            keys = relationshipArray.keys();
            cVVector = new Vector<ContentValues>(relationshipArray.length());

            while (keys.hasNext()){
                key = keys.next().toString();
                tempObj = new JSONObject(relationshipArray.getString(key));
                String id = key.toString();
                String tempSongId = tempObj.getString("songId");
                String tempEventId = tempObj.getString("eventId");

                ContentValues relationshipValues = new ContentValues();
                relationshipValues.put(SongContract.EventHasSongTable._ID,id);
                relationshipValues.put(SongContract.EventHasSongTable.COLUMN_EVENT_ID,tempEventId);
                relationshipValues.put(SongContract.EventHasSongTable.COLUMN_SONG_ID,tempSongId);
                cVVector.add(relationshipValues);
            }

            inserted = 0;

            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(SongContract.SongTable.buildSongWithEventUri(), cvArray);
            }*/

        }
        catch (JSONException e){
            Log.e(LOG_TAG,e.getMessage());
        }
    }
}
