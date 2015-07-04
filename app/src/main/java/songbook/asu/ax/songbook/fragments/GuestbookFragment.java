package songbook.asu.ax.songbook.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
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

import java.util.Random;

import songbook.asu.ax.songbook.GuestbookEntryAdapter;
import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.Utilities;
import songbook.asu.ax.songbook.data.SongContract;

/**
 * Created by Kristian on 2015-04-16.
 */
public class GuestbookFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = GuestbookFragment.class.getSimpleName();
    private static final int ENTRY_LOADER = 0;

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

                guestbookValues.put(SongContract.GuestbookTable.COLUMN_ENTRY,entryText);
                guestbookValues.put(SongContract.GuestbookTable.COLUMN_POSTER,String.format(getActivity().getString(R.string.poster),posterText));
                guestbookValues.put(SongContract.GuestbookTable.COLUMN_TIMESTAMP, String.format(getActivity().getString(R.string.date),Utilities.getTimeStamp()));

                getActivity().getContentResolver().insert(SongContract.GuestbookTable.buildGuestbookUri(),
                        guestbookValues);


                editTextEntry.setText(getResources().getString(R.string.enter_entry));
                editTextPoster.setText(getResources().getString(R.string.enter_name));
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
        getLoaderManager().initLoader(ENTRY_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }
}
