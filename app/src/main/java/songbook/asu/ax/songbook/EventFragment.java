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
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

import songbook.asu.ax.songbook.data.SongContract;
/**
 * Created by Kristian on 2015-03-10.
 */
public class EventFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EventFragment.class.getSimpleName();
    private static final int SONG_LOADER = 0;
    private static final int EVENT_LOADER = 1;
    private SongAdapter mSongAdapter;
    private Cursor mCursor;
    private TextView mEventDateTextView = null;
    private TextView mEventNameTextView = null;
    private TextView mCurrentTextView = null;

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
            SongContract.SongTable.COLUMN_TEXT};

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_SONG_ID = 0;
    static final int COL_SONG_IDENTIFIER = 1;
    static final int COL_SONG_MELODY = 2;
    static final int COL_SONG_NAME = 3;
    static final int COL_SONG_LASTUPDATED = 4;
    static final int COL_SONG_TEXT = 5;
    static final int COL_SONG_EVENT_ID = 6;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                    String name = cursor.getString(COL_SONG_NAME);
                    String melody = cursor.getString(COL_SONG_MELODY);
                    String text = cursor.getString(COL_SONG_TEXT);

                    Intent intent = new Intent(getActivity(), DetailActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString(MainFragment.SONG_NAME, name);
                    bundle.putString(MainFragment.TEXT, text);
                    bundle.putString(MainFragment.MELODY, melody);
                    intent.putExtra(MainFragment.BUNDLE_KEY, bundle);

                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    private void updateEventInfo() {
        if (mEventNameTextView == null){
            mEventNameTextView = (TextView) getActivity().findViewById(R.id.event_name_tw);
        }
        if (mEventDateTextView == null){
            mEventDateTextView = (TextView) getActivity().findViewById(R.id.event_date_tw);
        }
        if (mCurrentTextView == null){
            mCurrentTextView = (TextView) getActivity().findViewById(R.id.event_current_tw);
        }

        if (mCursor.getCount() > 0) {
            mCursor.moveToFirst();

            mEventNameTextView.setText(mCursor.getString(COL_EVENT_NAME));
            mEventDateTextView.setText(mCursor.getString(COL_EVENT_DATE));

            mCurrentTextView.setText("Siffervisan");
        } else {
            mEventNameTextView.setText("There are no events today");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader;
        switch (i){
            case SONG_LOADER:{
                String sortOrder = SongContract.SongTable.COLUMN_SONG_NAME + " ASC";

                Uri songUri = SongContract.SongTable.buildSongWithEventUri();

                return new CursorLoader(getActivity(),
                        songUri,
                        SONG_COLUMNS,
                        null,
                        null,
                        sortOrder);
                }

            case EVENT_LOADER:{
                String sortOrder = SongContract.EventTable.COLUMN_EVENT_NAME + " ASC";

                Uri eventUri = SongContract.EventTable.buildEventUri("");

                cursorLoader = new CursorLoader(getActivity(),
                        eventUri,
                        EVENT_COLUMNS,
                        "event_date = ?",
                        new String[]{Utilities.formatDateString(new Date())},
                        sortOrder);

                return cursorLoader;
            }
            default:{
                throw new UnsupportedOperationException("not a correct id");
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int id = cursorLoader.getId();

        switch (id){
            case SONG_LOADER:{
                mSongAdapter.swapCursor(cursor);
                break;
            }
            case EVENT_LOADER:{
                mCursor = cursor;
                this.updateEventInfo();
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mSongAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SONG_LOADER, null, this);
        getLoaderManager().initLoader(EVENT_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }
}
