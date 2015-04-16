package songbook.asu.ax.songbook.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

import songbook.asu.ax.songbook.DetailActivity;
import songbook.asu.ax.songbook.GuestbookEntryAdapter;
import songbook.asu.ax.songbook.MainFragment;
import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.SongAdapter;
import songbook.asu.ax.songbook.data.SongContract;

/**
 * Created by Kristian on 2015-04-16.
 */
public class GuestbookFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = GuestbookFragment.class.getSimpleName();
    private static final int ENTRY_LOADER = 0;

    private GuestbookEntryAdapter mGuestbookEntryAdapter;
    private Cursor mCursor;

    private static final String[] GUESTBOOK_COLUMNS = {
            SongContract.GuestbookTable.NAME + "." + SongContract.GuestbookTable._ID,
            SongContract.GuestbookTable.COLUMN_ENTRY,
            SongContract.GuestbookTable.COLUMN_POSTER,
            SongContract.GuestbookTable.COLUMN_TIMESTAMP
    };

    static final int COL_GUESTBOOK_ID = 0;
    static final int COL_GUESTBOOK_ENTRY = 1;
    static final int COL_GUESTBOOK_POSTER = 2;
    static final int COL_GUESTBOOK_TIMESTAMP = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mGuestbookEntryAdapter = new GuestbookEntryAdapter(getActivity(),null,0);

        View rootView = inflater.inflate(R.layout.fragment_guestbook, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_guestbook);
        listView.setAdapter(mGuestbookEntryAdapter);

        Button button = (Button) rootView.findViewById(R.id.post_entry_button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues guestbookValues = new ContentValues();
                guestbookValues.put(SongContract.GuestbookTable.COLUMN_ENTRY,"test entry");
                guestbookValues.put(SongContract.GuestbookTable.COLUMN_POSTER,"test poster");
                guestbookValues.put(SongContract.GuestbookTable.COLUMN_TIMESTAMP,"test timestamp");

                getActivity().getContentResolver().insert(SongContract.GuestbookTable.buildGuestbookUri(),
                        guestbookValues);
            }
        });


        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String name = cursor.getString(COL_GUESTBOOK_ID);

                    Uri contentUri = SongContract.SongTable.buildSongUriWithName();
                    Intent intent = new Intent(getActivity(),DetailActivity.class).setData(contentUri);
                    intent.putExtra(MainFragment.SONG_NAME,name);

                    startActivity(intent);
                }
            }
        });*/

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = SongContract.GuestbookTable.COLUMN_TIMESTAMP + " ASC";

        Uri songUri = SongContract.GuestbookTable.buildGuestbookUri();

        return new CursorLoader(getActivity(),
                songUri,
                GUESTBOOK_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mGuestbookEntryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGuestbookEntryAdapter.swapCursor(null);
    }
}
