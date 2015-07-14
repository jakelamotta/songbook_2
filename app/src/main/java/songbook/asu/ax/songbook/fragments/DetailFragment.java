package songbook.asu.ax.songbook.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Iterator;

import songbook.asu.ax.songbook.Loaders;
import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.activities.DetailActivity;
import songbook.asu.ax.songbook.activities.SongbookActivity;
import songbook.asu.ax.songbook.data.SongContract;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static final String DETAIL_URI = "detail_uri";

    private Uri mUri;

    private static final String[] SONG_COLUMNS = {
            SongContract.SongTable.NAME + "." +SongContract.SongTable._ID,
            SongContract.SongTable.COLUMN_SONG_NAME,
            SongContract.SongTable.COLUMN_SONG_MELODY,
            SongContract.SongTable.COLUMN_TEXT
    };

    public static final int COL_ID = 0;
    public static final int COL_NAME = 1;
    public static final int COL_MELODY = 2;
    public static final int COL_TEXT = 3;
    private TextView mNameView;
    private TextView mMelodyView;
    private TextView mTextView1;
    private String mName;
    private PowerManager.WakeLock wakeLock;

    public DetailFragment(){
        super();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "wltag");
        wakeLock.acquire();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutReference = R.layout.fragment_detail_ref;

        if (getActivity().getClass().getSimpleName().equals(DetailActivity.class.getSimpleName())){
            layoutReference = R.layout.fragment_detail_ref2;
        }

        View rootView = inflater.inflate(layoutReference, container, false);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(DETAIL_URI)  ) {
            mUri = bundle.getParcelable(DETAIL_URI);
            mName = bundle.getString(MainFragment.SONG_NAME);
        }
        else{
            Intent intent = getActivity().getIntent();
            mUri = intent.getData();
            mName = intent.getStringExtra(MainFragment.SONG_NAME);
        }

        mNameView = (TextView) rootView.findViewById(R.id.song_name_textview_detail);
        mMelodyView = (TextView) rootView.findViewById(R.id.melody_textview_detail);
        mTextView1 = (TextView) rootView.findViewById(R.id.text_text_view_detail);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri){
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    SONG_COLUMNS,
                    null,
                    new String[]{mName},
                    null);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        SongbookActivity activity = (SongbookActivity)getActivity();

        Iterator it = activity.getSupportFragmentManager().getFragments().iterator();

        while (it.hasNext()){
            Fragment f = (Fragment) it.next();
            try{
                f.getLoaderManager().getLoader(Loaders.SONG_BY_CAT_LOADER.ordinal()).forceLoad();
            }
            catch(Exception e){
            }
        }
        if (data != null && data. moveToFirst()){
            mNameView.setText(data.getString(COL_NAME));
            mMelodyView.setText(String.format(getActivity().getString(R.string.melody),data.getString(COL_MELODY)));
            String songText = data.getString(COL_TEXT);

            mTextView1.setText(songText);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        Log.v(LOG_TAG, "onLoaderReset");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(Loaders.DETAI_LOADER.ordinal(),null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        wakeLock.release();
        super.onDestroy();
    }
}