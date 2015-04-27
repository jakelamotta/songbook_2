package songbook.asu.ax.songbook.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import songbook.asu.ax.songbook.Callback;
import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.SongAdapter;
import songbook.asu.ax.songbook.SongFilter;
import songbook.asu.ax.songbook.activities.CategoryActivity;
import songbook.asu.ax.songbook.data.SongContract;
import songbook.asu.ax.songbook.data.SongSyncAdapter;

/**
 * Created by EIS i7 Gamer on 2015-02-26.
 */
public class MainFragment extends Fragment implements LoaderCallbacks<Cursor>,SongFilter {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    @Override
    public void filterByMelody(String query) {
    }

    public static final String SONG_NAME = "song_name";
    public static final String MELODY = "melody";
    public static final String TEXT = "song_text";
    public static final String BUNDLE_KEY = "fragment_bundle";
    private static final String SELECTED_POSITION = "selected_position";

    private SongAdapter mSongAdapter;
    private String mCategory = null;
    private static final int SONG_LOADER = 0;
    private int mPosition = -1;
    private ListView mListView;

    public static final String[] SONG_COLUMNS = {
            SongContract.SongTable.NAME + "." + SongContract.SongTable._ID,
            SongContract.SongTable.COLUMN_SONG_ID,
            SongContract.SongTable.COLUMN_SONG_MELODY,
            SongContract.SongTable.COLUMN_SONG_NAME,
            SongContract.SongTable.COLUMN_LAST_UPADTED,
            SongContract.SongTable.COLUMN_TEXT,
            SongContract.SongTable.COLUMN_CATEGORY};

    public static final int COL_SONG_ID = 0;
    public static final int COL_SONG_IDENTIFIER = 1;
    public static final int COL_SONG_MELODY = 2;
    public static final int COL_SONG_NAME = 3;
    public static final int COL_SONG_LASTUPDATED = 4;
    public static final int COL_SONG_TEXT = 5;
    public static final int COL_CATEGORY = 6;

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
            if (savedInstanceState.containsKey(CategoryActivity.SELECTED_CATEGORY)){
                mCategory = savedInstanceState.getString(CategoryActivity.SELECTED_CATEGORY);
            }
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
        if (mCategory != null){
            this.filterByCategory(mCategory);
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

    @Override
    public void filterByCategory(String query) {

        String noCategory = getActivity().getString(R.string.other_category);

        if (query.equals(noCategory)){
            query = "NULL";
        }

        mSongAdapter.swapCursor(getActivity().getContentResolver().query(
                        SongContract.SongTable.buildSongUriWithName(),
                        SONG_COLUMNS,
                        SongContract.SongTable.COLUMN_SONG_NAME + " = ?",
                        new String[]{query},
                        null)
        );
    }
}