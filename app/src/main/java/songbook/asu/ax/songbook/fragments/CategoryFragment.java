package songbook.asu.ax.songbook.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import songbook.asu.ax.songbook.SongAdapter;
import songbook.asu.ax.songbook.SongFilter;
import songbook.asu.ax.songbook.data.SongContract;

/**
 * Created by EIS i7 Gamer on 2015-04-22.
 */
public class CategoryFragment extends Fragment implements LoaderCallbacks<Cursor>, SongFilter {

    private static final String LOG_TAG = CategoryFragment.class.getSimpleName();
    private static final int SONG_LOADER = 0;
    private SongAdapter mSongAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SONG_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri songUri = SongContract.SongTable.buildSongUri();
        String sortOrder = SongContract.SongTable.COLUMN_SONG_NAME + " ASC";

        return new CursorLoader(getActivity(),
                songUri,
                MainFragment.SONG_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void filterByCategory(String query) {
        mSongAdapter.swapCursor(getActivity().getContentResolver().query(
                        SongContract.SongTable.buildSongUriWithName(),
                        MainFragment.SONG_COLUMNS,
                        SongContract.SongTable.COLUMN_SONG_NAME + " = ?",
                        new String[]{query},
                        null)
        );
    }

    @Override
    public void filterByName(String query) {

    }

    @Override
    public void filterByMelody(String query) {

    }
}
