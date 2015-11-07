package songbook.asu.ax.songbook.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.WindowManager;
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
import songbook.asu.ax.songbook.GuestbookFormDialog;
import songbook.asu.ax.songbook.Loaders;
import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.Utilities;
import songbook.asu.ax.songbook.activities.MainActivity;
import songbook.asu.ax.songbook.data.SongContract;
import songbook.asu.ax.songbook.data.SongSyncAdapter;

/**
 * Created by Kristian on 2015-04-16.
 */
public class GuestbookFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = GuestbookFragment.class.getSimpleName();
    private static final String ENTRY_POST_URL = "http://songbook.asu.ax/api/message";

    private static GuestbookEntryAdapter mGuestbookEntryAdapter;

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

        View rootView = inflater.inflate(R.layout.fragment_guestbook_ref, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_guestbook);
        listView.setAdapter(mGuestbookEntryAdapter);

        ImageButton button = (ImageButton) rootView.findViewById(R.id.add_post);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuestbookFormDialog cdd = new GuestbookFormDialog(getActivity());
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(cdd.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;

                cdd.show();
            }
        });

        return rootView;
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
        getLoaderManager().initLoader(Loaders.GUESTBOOK_LOADER.ordinal(),null,this);
        super.onActivityCreated(savedInstanceState);
    }

}
