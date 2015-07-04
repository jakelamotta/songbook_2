package songbook.asu.ax.songbook.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import songbook.asu.ax.songbook.GuestbookEntryAdapter;
import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.Utilities;
import songbook.asu.ax.songbook.data.SongContract;
import songbook.asu.ax.songbook.data.SongSyncAdapter;

/**
 * Created by Kristian on 2015-04-16.
 */
public class GuestbookFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = GuestbookFragment.class.getSimpleName();
    private static final int ENTRY_LOADER = 0;
    private static final String ENTRY_POST_URL = "http://songbook.asu.ax/api/message";

    private static GuestbookEntryAdapter mGuestbookEntryAdapter;
    private Cursor mCursor;

    private static final String[] GUESTBOOK_COLUMNS = {
            SongContract.GuestbookTable.NAME + "." + SongContract.GuestbookTable._ID,
            SongContract.GuestbookTable.COLUMN_ENTRY,
            SongContract.GuestbookTable.COLUMN_POSTER,
            SongContract.GuestbookTable.COLUMN_TIMESTAMP
    };

    public static final int COL_GUESTBOOK_ID = 0;
    public static final int COL_GUESTBOOK_ENTRY = 1;
    public static final int COL_GUESTBOOK_POSTER = 2;
    public static final int COL_GUESTBOOK_TIMESTAMP = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mGuestbookEntryAdapter = new GuestbookEntryAdapter(getActivity(),null,0);

        View rootView = inflater.inflate(R.layout.fragment_guestbook, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_guestbook);
        listView.setAdapter(mGuestbookEntryAdapter);

        final EditText editTextEntry = (EditText) rootView.findViewById(R.id.edit_entry);
        final EditText editTextPoster = (EditText) rootView.findViewById(R.id.edit_poster_name);

        Button button = (Button) rootView.findViewById(R.id.post_entry_button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entryText = editTextEntry.getText().toString();
                String posterText = editTextPoster.getText().toString();

                Random rand = new Random();

                ContentValues guestbookValues = new ContentValues();

                guestbookValues.put(SongContract.GuestbookTable.COLUMN_ENTRY, entryText);
                guestbookValues.put(SongContract.GuestbookTable.COLUMN_POSTER, String.format(getActivity().getString(R.string.poster), posterText));
                guestbookValues.put(SongContract.GuestbookTable.COLUMN_TIMESTAMP, String.format(getActivity().getString(R.string.date), Utilities.getTimeStamp()));

                getActivity().getContentResolver().insert(SongContract.GuestbookTable.buildGuestbookUri(),
                        guestbookValues);

                if(!validate()) {
                    Toast.makeText(getActivity(), "Enter some data!", Toast.LENGTH_LONG).show();
                    // call AsynTask to perform network operation on separate thread
                    HttpAsyncTask task = new HttpAsyncTask();
                    task.setBody(entryText);
                    task.setName(posterText);
                    task.execute(ENTRY_POST_URL);

                    editTextEntry.setText(getResources().getString(R.string.enter_entry));
                    editTextPoster.setText(getResources().getString(R.string.enter_name));
                }
            }
        });

        return rootView;
    }

    private boolean validate() {
        return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        public String name;
        public String body;

        @Override
        protected String doInBackground(String... urls) {
            String result =  POST(name,body);
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity(), "Data Sent!", Toast.LENGTH_LONG).show();
        }

        public void setName(String n){
            this.name = n;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = SongContract.GuestbookTable.COLUMN_TIMESTAMP + " DESC";

        Uri uri = SongContract.GuestbookTable.buildGuestbookUri();

        return new CursorLoader(getActivity(),
                uri,
                GUESTBOOK_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int size = data.getColumnCount();

        for (int i=1;i<size;i++){
            Log.v(LOG_TAG,data.getColumnName(i));
        }

        mGuestbookEntryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGuestbookEntryAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(ENTRY_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    private String POST(String posterText, String entryText){
        String result = "";

        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpPost httpPost = new HttpPost(ENTRY_POST_URL);

        try {
            JSONObject postObject = new JSONObject();
            postObject.accumulate("name", posterText);
            postObject.accumulate("body", entryText);
            String jsonString = postObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(jsonString);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        }
        catch (Exception e){
            Log.e(LOG_TAG,e.getMessage());
        }
        finally{

        }
        return result;
    }
}
