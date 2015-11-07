package songbook.asu.ax.songbook.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Stack;

import songbook.asu.ax.songbook.Callback;
import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.SongFilter;
import songbook.asu.ax.songbook.data.SongSyncAdapter;
import songbook.asu.ax.songbook.fragments.CategoryFragment;
import songbook.asu.ax.songbook.fragments.DetailFragment;
import songbook.asu.ax.songbook.fragments.MainFragment;
import songbook.asu.ax.songbook.fragments.SongsFragment;

/**
 * Created by konsult on 2015-07-12.
 */
public class SongsActivity extends SongbookActivity implements Callback {

    private final static String LOG_TAG = SongsActivity.class.getSimpleName();
    protected static final String DETAILFRAGMENT_TAG = "detail_fragment";
    protected boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setIcon(R.mipmap.asu_icon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected void checkWidth(Bundle savedInstanceState) {
        if (findViewById(R.id.song_detail_container) != null){
            mTwoPane = true;

            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.song_detail_container, new DetailFragment()).commit();
            }
        }
        else{
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.search_menu_item).getActionView();

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SongFilter filter = (SongFilter) getSupportFragmentManager().getFragments().get(0);
                filter.filterByName(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public void onItemSelected(Uri contentUri,String name, @Nullable String category){
        if (mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI,contentUri);
            args.putString(SongsFragment.SONG_NAME,name);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.song_detail_container,fragment,
                    DETAILFRAGMENT_TAG).commit();
        }
        else if(!mTwoPane){
            Intent intent = new Intent(this,DetailActivity.class).setData(contentUri);

            if (category != null){
                intent.putExtra(CategoryFragment.SELECTED_CATEGORY,category);

            }

            intent.putExtra(SongsFragment.SONG_NAME,name);
            startActivity(intent);
        }
    }
}
