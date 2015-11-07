package songbook.asu.ax.songbook.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import songbook.asu.ax.songbook.Callback;
import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.SongFilter;
import songbook.asu.ax.songbook.data.SongSyncAdapter;
import songbook.asu.ax.songbook.fragments.CategoryFragment;
import songbook.asu.ax.songbook.fragments.DetailFragment;
import songbook.asu.ax.songbook.fragments.MainFragment;


public class MainActivity extends SongbookActivity implements Callback {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "detail_fragment";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        if (intent != null){
            if (intent.hasExtra(CategoryFragment.SELECTED_CATEGORY)){
                SongFilter filter = (SongFilter) getSupportFragmentManager().getFragments().get(0);
                filter.setCategoryMode(true);
                filter.setCurrentCategory(intent.getStringExtra(CategoryFragment.SELECTED_CATEGORY));
            }
        }

        checkWidth(savedInstanceState);
        //updateSongbook();
        getSupportActionBar().setIcon(R.mipmap.asu_icon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void checkWidth(Bundle savedInstanceState) {
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_allsongs).setVisible(false);
        return result;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
    }

    private void updateSongbook(){
        SongSyncAdapter.syncImmediately(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(Uri contentUri,String name){

        if (mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI,contentUri);
            args.putString(MainFragment.SONG_NAME,name);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.song_detail_container,fragment,
                    DETAILFRAGMENT_TAG).commit();
        }
        else if(!mTwoPane){
            Intent intent = new Intent(this,DetailActivity.class).setData(contentUri);
            intent.putExtra(MainFragment.SONG_NAME,name);
            startActivity(intent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        updateSongbook();
    }
}