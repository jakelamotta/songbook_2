package songbook.asu.ax.songbook;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import songbook.asu.ax.songbook.activities.SongbookActivity;
import songbook.asu.ax.songbook.data.SongContract;
import songbook.asu.ax.songbook.data.SongSyncAdapter;

/**
 * Created by EIS i7 Gamer on 2015-07-12.
 */
public class GuestbookFormDialog extends Dialog implements View.OnClickListener {

    private Activity context;
    private final static String LOG_TAG = GuestbookFormDialog.class.getSimpleName();
    private static final String ENTRY_POST_URL = "http://songbook.asu.ax/api/message";
    private EditText editTextEntry;
    private EditText editTextPoster;
    private Button yesBtn;
    private Button cancelBtn;
    private CheckBox checkBox;

    public GuestbookFormDialog(Activity activity){
        super(activity);
        this.context = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_form);
        editTextEntry = (EditText)findViewById(R.id.edit_entry);
        editTextPoster = (EditText)findViewById(R.id.edit_poster_name);
        checkBox = (CheckBox) findViewById(R.id.make_private_cb);
        yesBtn = (Button) findViewById(R.id.btn_yes);
        cancelBtn = (Button) findViewById(R.id.btn_no);
        yesBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_yes:
                String entryText = editTextEntry.getText().toString();
                String posterText = editTextPoster.getText().toString();

                if(!validate() && !checkBox.isChecked()) {
                    // call AsynTask to perform network operation on separate thread
                    HttpAsyncTask task = new HttpAsyncTask();
                    task.setBody(entryText);
                    task.setName(posterText);
                    task.execute(ENTRY_POST_URL);

                    editTextEntry.setText(getContext().getResources().getString(R.string.enter_entry));
                    editTextPoster.setText(getContext().getResources().getString(R.string.enter_name));

                    SongbookActivity.updateGuestbook(getContext());
                }
                else if(checkBox.isChecked()){
                    ContentValues entryValues = new ContentValues();
                    entryValues.put(SongContract.GuestbookTable.COLUMN_POSTER, posterText);
                    entryValues.put(SongContract.GuestbookTable.COLUMN_ENTRY, entryText);
                    entryValues.put(SongContract.GuestbookTable.COLUMN_TIMESTAMP, Long.toString(Utilities.getTimeStamp()/1000));

                    getContext().getContentResolver().insert(SongContract.GuestbookTable.buildGuestbookUri(),entryValues);
                }
                break;
            default:
                break;
        }

        this.cancel();

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

    private String POST(String posterText, String entryText){
        String result = "";

        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpPost httpPost = new HttpPost(ENTRY_POST_URL);
        StringEntity input;
        try {
            JSONObject postObject = new JSONObject();
            postObject.accumulate("name", posterText);

            postObject.accumulate("body", entryText);

            String jsonString = postObject.toString();
            byte[] bytes = jsonString.getBytes();
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(jsonString);


            se.setContentType("application/json;charset=utf-8");

            // 6. set httpPost Entity
            httpPost.setEntity(new ByteArrayEntity(bytes));

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");

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
            Log.e(LOG_TAG, e.getMessage());
        }
        finally{

        }


        return result;
    }
}
