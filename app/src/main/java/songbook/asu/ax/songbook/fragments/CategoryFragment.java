package songbook.asu.ax.songbook.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import songbook.asu.ax.songbook.CategoryAdapter;
import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.SongAdapter;
import songbook.asu.ax.songbook.SongFilter;
import songbook.asu.ax.songbook.activities.MainActivity;
import songbook.asu.ax.songbook.activities.SongsFromCategoryActivity;
import songbook.asu.ax.songbook.data.SongContract;

/**
 * Created by EIS i7 Gamer on 2015-04-22.
 */
public class CategoryFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = CategoryFragment.class.getSimpleName();
    private static final int SONG_LOADER = 0;
    private CategoryAdapter mCategoryAdapter;
    public static final String SELECTED_CATEGORY = "selected_category";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        mCategoryAdapter = new CategoryAdapter(getActivity(),null,0);

        final ListView listView = (ListView) rootView.findViewById(R.id.list_view_category);

        listView.setAdapter(mCategoryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra(SELECTED_CATEGORY,"other");
                startActivity(intent);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri songUri = SongContract.SongTable.buildCategoryUri();

        String sortOrder = SongContract.SongTable.COLUMN_CATEGORY + " ASC";

        return new CursorLoader(getActivity(),
                songUri,
                SONG_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCategoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCategoryAdapter.swapCursor(null);
    }
}
