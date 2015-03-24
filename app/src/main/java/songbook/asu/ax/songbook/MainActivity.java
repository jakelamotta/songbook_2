package songbook.asu.ax.songbook;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import songbook.asu.ax.songbook.data.SongSyncAdapter;


public class MainActivity extends ActionBarActivity implements Callback{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "detail_fragment";
    private boolean mTwoPane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.song_detail_container) != null){
            mTwoPane = true;
            if (savedInstanceState == null){
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.song_detail_container, new DetailFragment()).commit();
            }
        }
        else{
            mTwoPane = false;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            MainFragment fragment = new MainFragment();
            fragmentTransaction.add(R.layout.fragment_main_ref, fragment);
            fragmentTransaction.commit();
        }

        updateSongbook();
        getSupportActionBar().setIcon(R.mipmap.asu_icon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent){
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            MainFragment fragment = (MainFragment) getSupportFragmentManager().getFragments().get(0);
            fragment.filterByName(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    private void updateSongbook(){
        SongSyncAdapter.syncImmediately(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_event){
            Intent intent = new Intent(this,EventActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_about){
            new AlertDialog.Builder(this)
                    .setTitle(this.getString(R.string.action_about))
                    .setMessage(this.getString(R.string.info))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

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
        updateSongbook();
        super.onStart();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public boolean getTwoPane(){
        return this.mTwoPane;
    }
}