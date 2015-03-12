package songbook.asu.ax.songbook;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import songbook.asu.ax.songbook.data.SongContract;
import songbook.asu.ax.songbook.data.SongSyncAdapter;

/**
 * Created by Kristian on 2015-03-10.
 */
public class EventFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EventFragment.class.getSimpleName();
    private static final int SONG_LOADER = 0;
    private SongAdapter mSongAdapter;

    private static final String[] SONG_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            SongContract.SongTable.NAME + "." + SongContract.SongTable._ID,
            SongContract.SongTable.COLUMN_SONG_ID,
            SongContract.SongTable.COLUMN_SONG_MELODY,
            SongContract.SongTable.COLUMN_SONG_NAME,
            SongContract.SongTable.COLUMN_LAST_UPADTED,
            SongContract.SongTable.COLUMN_TEXT};

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_SONG_ID = 0;
    static final int COL_SONG_IDENTIFIER = 1;
    static final int COL_SONG_MELODY = 2;
    static final int COL_SONG_NAME = 3;
    static final int COL_SONG_LASTUPDATED = 4;
    static final int COL_SONG_TEXT = 5;

    private static final String[] EVENT_COLUMNS = {
            SongContract.EventTable.NAME + "." + SongContract.EventTable._ID,
            SongContract.EventTable.COLUMN_EVENT_NAME,
            SongContract.EventTable.COLUMN_EVENT_DATE,
            SongContract.EventTable.COLUMN_CURRENT_SONG
    };

    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_NAME = 1;
    static final int COL_EVENT_DATE = 2;
    static final int COL_CURRENT_SONG = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSongAdapter = new SongAdapter(getActivity(),null,0);

        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_event);
        listView.setAdapter(mSongAdapter);

        if (!mSongAdapter.isEmpty()){
            Log.v(LOG_TAG,mSongAdapter.getItem(0).toString());
        }
        else{
            Log.v(LOG_TAG,"mSongAdapter is empty");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                    String name = cursor.getString(COL_SONG_NAME);
                    String melody = cursor.getString(COL_SONG_MELODY);
                    String text = cursor.getString(COL_SONG_TEXT);

                    Intent intent = new Intent(getActivity(),DetailActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString(MainFragment.SONG_NAME,name);
                    bundle.putString(MainFragment.TEXT,text);
                    bundle.putString(MainFragment.MELODY,melody);
                    intent.putExtra(MainFragment.BUNDLE_KEY,bundle);

                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG,"OnCreateLoader");
        String sortOrder = SongContract.SongTable.COLUMN_SONG_NAME + " ASC";

        Uri songUri = SongContract.SongTable.buildSongWithEventUri("song_with_event");

        return new CursorLoader(getActivity(),
                songUri,
                SONG_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.v(LOG_TAG,"OnLoadFinish");
        mSongAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mSongAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SONG_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
}
