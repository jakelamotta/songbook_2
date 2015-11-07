package songbook.asu.ax.songbook.fragments;

import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import songbook.asu.ax.songbook.Loaders;
import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.activities.CategoryActivity;
import songbook.asu.ax.songbook.data.SongContract;

/**
 * Created by konsult on 2015-07-12.
 */
public class SongsByCatFragment extends SongsFragment {

    private static final String CURRENT_CATEGORY = CategoryFragment.SELECTED_CATEGORY;
    private static final String LOG_TAG = SongsByCatFragment.class.getSimpleName();

    public void setCurrentCategory(String stringExtra) {
        this.mCategory = stringExtra;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_ref, container, false);
        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(CURRENT_CATEGORY)){
            mCategory = intent.getStringExtra(CURRENT_CATEGORY);
        }
        else if(savedInstanceState.containsKey(CURRENT_CATEGORY)){
            mCategory = savedInstanceState.getString(CURRENT_CATEGORY);
        }
        else if(mCategory == null){
            Log.v(LOG_TAG, "mCategory == null");
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(Loaders.SONG_BY_CAT_LOADER.ordinal(), null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return super.onCreateLoader(i, bundle);
    }

    @Override
    public void filterByCategory(String query) {
        mSongAdapter.swapCursor(getActivity().getContentResolver().query(
                        SongContract.SongTable.buildSongWithCategoryUri(),
                        SONG_COLUMNS,
                        SongContract.SongTable.COLUMN_CATEGORY + " = ?",
                        new String[]{query},
                        null)
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        super.onLoadFinished(cursorLoader, cursor);
        filterByCategory(mCategory);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        super.onLoaderReset(cursorLoader);
        mSongAdapter.swapCursor(null);
    }
}
