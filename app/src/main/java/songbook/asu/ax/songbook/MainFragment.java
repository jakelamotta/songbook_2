package songbook.asu.ax.songbook;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Button;
import android.widget.SearchView;

import java.util.List;

import songbook.asu.ax.songbook.data.SongContract;
import songbook.asu.ax.songbook.data.SongDbHelper;
import songbook.asu.ax.songbook.data.SongProvider;
import songbook.asu.ax.songbook.data.SongSyncAdapter;

/**
 * Created by EIS i7 Gamer on 2015-02-26.
 */
public class MainFragment extends Fragment implements LoaderCallbacks<Cursor>,SongFilter {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    public static final String SONG_NAME = "song_name";
    public static final String MELODY = "melody";
    public static final String TEXT = "song_text";
    public static final String BUNDLE_KEY = "fragment_bundle";
    private static final String SELECTED_POSITION = "selected_position";
    private SongAdapter mSongAdapter;
    private static final int SONG_LOADER = 0;
    private int mPosition = -1;
    private ListView mListView;

    private static final String[] SONG_COLUMNS = {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSongAdapter = new SongAdapter(getActivity(),null,0);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_POSITION)){
            mPosition = savedInstanceState.getInt(SELECTED_POSITION);
        }
        View rootView = inflater.inflate(R.layout.fragment_main_ref, container, false);

        mListView = (ListView) rootView.findViewById(R.id.list_view_songs);
        mListView.setAdapter(mSongAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                if (cursor != null) {
                    String name = cursor.getString(COL_SONG_NAME);

                    Callback mainActivity = (Callback) getActivity();
                    mainActivity.onItemSelected(SongContract.SongTable.buildSongUriWithName(), name);
                }

                mPosition = position;
            }
        });

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SONG_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

    }

    @Override
    public void onStart() {
        SongSyncAdapter.syncImmediately(getActivity());
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_POSITION,mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = SongContract.SongTable.COLUMN_SONG_NAME + " ASC";

        Uri songUri = SongContract.SongTable.buildSongUri();

        return new CursorLoader(getActivity(),
                songUri,
                SONG_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mSongAdapter.swapCursor(cursor);
        if (mPosition != ListView.INVALID_POSITION){
            mListView.smoothScrollToPosition(mPosition);
            mListView.setItemChecked(mPosition,true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mSongAdapter.swapCursor(null);
    }

    @Override
    public void filterByName(String query) {
        {
            mSongAdapter.swapCursor(getActivity().getContentResolver().query(
                            SongContract.SongTable.buildSongUriWithName(),
                            SONG_COLUMNS,
                            SongContract.SongTable.COLUMN_SONG_NAME + " LIKE ?",
                            new String[]{"%" + query + "%"},
                            null)
            );
        }
    }
}
