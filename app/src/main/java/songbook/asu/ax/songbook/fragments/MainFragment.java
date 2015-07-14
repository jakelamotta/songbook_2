package songbook.asu.ax.songbook.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
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
import songbook.asu.ax.songbook.activities.MainActivity;
import songbook.asu.ax.songbook.data.SongContract;
import songbook.asu.ax.songbook.data.SongSyncAdapter;

/**
 * Created by EIS i7 Gamer on 2015-02-26.
 */
public class MainFragment extends SongsFragment {

    public static final String BUNDLE_KEY = "fragment_bundle";
    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_ref, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        SongSyncAdapter.syncImmediately(getActivity());
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /*@Override
    public void onStop() {
        Bundle bundle = getArguments();
        if (bundle == null){
            bundle = new Bundle();
        }
        bundle.putBoolean(CATEGORY_MODE,mCategoryMode);
        bundle.putString(CURRENT_CATEGORY,mCurrentCategory);
        super.onStop();
    }

    @Override
    public void onResume() {
        Bundle bundle = getArguments();

        if (bundle != null){
            if (bundle.containsKey(CATEGORY_MODE)){
                this.mCategoryMode = bundle.getBoolean(CATEGORY_MODE);
                this.mCurrentCategory = bundle.getString(CURRENT_CATEGORY);
            }
        }

        super.onResume();
    }*/

}