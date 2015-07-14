package songbook.asu.ax.songbook.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.fragments.CategoryFragment;
import songbook.asu.ax.songbook.fragments.EventFragment;
import songbook.asu.ax.songbook.fragments.SongsByCatFragment;

/**
 * Created by konsult on 2015-07-12.
 */
public class SongsByCatActivity extends SongsActivity {

    private final static String LOG_TAG = SongsByCatActivity.class.getSimpleName();
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(CategoryFragment.SELECTED_CATEGORY)){
            selectedCategory = getIntent().getStringExtra(CategoryFragment.SELECTED_CATEGORY);
        }
        else if(savedInstanceState != null && savedInstanceState.containsKey(CategoryFragment.SELECTED_CATEGORY)){
            selectedCategory = savedInstanceState.getString(CategoryFragment.SELECTED_CATEGORY);
        }
        else if(selectedCategory != null) {
        }
        else{
            throw new NullPointerException();
        }



        if(!parents.empty()) {
            if (!parents.peek().getSimpleName().equals(LOG_TAG)) {
                parents.push(getClass());
            }
        }
        else{
            parents.push(getClass());
        }

        setContentView(R.layout.activity_song_from_category);
        checkWidth(savedInstanceState);

        setTitle("");
        getSupportActionBar().setIcon(R.mipmap.asu_icon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(selectedCategory != null){
            Log.v(LOG_TAG,"selectedcategory not null");
            outState.putString(CategoryFragment.SELECTED_CATEGORY,selectedCategory);
        }
        else{
            Log.v(LOG_TAG,"selectedCategory app null");
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResumeFragments() {
        Log.v(LOG_TAG,"onResumeFragments");
        super.onResumeFragments();
    }

    @Override
    protected void onResume() {
        Log.v(LOG_TAG,"onResumeResumes");
        super.onResume();
    }

    @Override
    protected void onStart() {

        Log.v(LOG_TAG,"onResumeStart");
        super.onStart();
    }
}
