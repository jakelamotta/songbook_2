package songbook.asu.ax.songbook.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.fragments.CategoryFragment;
import songbook.asu.ax.songbook.fragments.EventFragment;
import songbook.asu.ax.songbook.fragments.SongsByCatFragment;

/**
 * Created by konsult on 2015-07-12.
 */
public class SongsByCatActivity extends SongsActivity {

    private final static String LOG_TAG = SongsByCatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_song_from_category);
        checkWidth(savedInstanceState);

        if(!parents.empty()) {
            if (!parents.peek().getSimpleName().equals(LOG_TAG)) {
                parents.push(getClass());
            }
        }
        else{
            parents.push(getClass());
        }

        SongsByCatFragment fragment = new SongsByCatFragment();

        if (!mTwoPane){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_cat, fragment).commit();
        }

        setTitle("");
        getSupportActionBar().setIcon(R.mipmap.asu_icon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
