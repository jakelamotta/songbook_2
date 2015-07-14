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

import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.activities.CategoryActivity;
import songbook.asu.ax.songbook.data.SongContract;

/**
 * Created by konsult on 2015-07-12.
 */
public class SongsByCatFragment extends SongsFragment {

    private static final String CURRENT_CATEGORY = CategoryFragment.SELECTED_CATEGORY;
    private static final String LOG_TAG = SongsByCatFragment.class.getSimpleName();

    @Override
    public void setCurrentCategory(String stringExtra) {
        this.mCategory = stringExtra;
    }

    @Override
    public void onStart() {
        Log.v(LOG_TAG,"in onCreateLoader");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.v(LOG_TAG,"in onCreateLoader");
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"in onCreateLoader");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.v(LOG_TAG,"in onCreateLoader");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_ref, container, false);
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(CURRENT_CATEGORY)){
            mCategory = intent.getStringExtra(CURRENT_CATEGORY);
            Log.v(LOG_TAG,"from intent: " + mCategory);
        }
        else if(savedInstanceState.containsKey(CURRENT_CATEGORY)){
            mCategory = savedInstanceState.getString(CURRENT_CATEGORY);
            Log.v(LOG_TAG,"from instancestate = " + mCategory);
        }
        else if(mCategory == null){
            Log.v(LOG_TAG, "mCategory == null");
            throw new NullPointerException();
        }

        return rootView;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.v(LOG_TAG,"in on loaderreset");
        super.onLoaderReset(cursorLoader);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG,"in onCreateLoader");
        return super.onCreateLoader(i, bundle);
    }

    @Override
    public void filterByCategory(String query) {
        Log.v(LOG_TAG,"in filterByCat");
        String noCategory = getActivity().getString(R.string.other_category);

        mSongAdapter.swapCursor(getActivity().getContentResolver().query(
                        SongContract.SongTable.buildSongUri(),
                        SONG_COLUMNS,
                        SongContract.SongTable.COLUMN_CATEGORY + " = ?",
                        new String[]{query},
                        null)
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.v(LOG_TAG,"in onLoadFinished");
        super.onLoadFinished(cursorLoader, cursor);
        filterByCategory(mCategory);
    }
}
