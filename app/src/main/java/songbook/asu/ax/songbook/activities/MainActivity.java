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


public class MainActivity extends SongsActivity{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private void updateSongbook(){
        SongSyncAdapter.syncImmediately(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkWidth(savedInstanceState);
        if(!parents.empty()){
            if (!parents.peek().getSimpleName().equals(LOG_TAG)){
                parents.push(getClass());
            }
        }
        else{
            parents.push(getClass());
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateSongbook();
    }
}