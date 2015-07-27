package songbook.asu.ax.songbook.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.data.SongSyncAdapter;
import songbook.asu.ax.songbook.fragments.GuestbookFragment;

/**
 * Created by Kristian on 2015-04-16.
 */
public class GuestbookActivity extends SongbookActivity {

    private static final String LOG_TAG = GuestbookActivity.class.getSimpleName();
    public static String SYNC_GUESTBOOK_ONLY = "sync_guestbook";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guestbook);
        GuestbookFragment fragment = new GuestbookFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_guestbook_container, fragment).commit();
        setTitle("");
        getSupportActionBar().setIcon(R.mipmap.asu_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_guestbook).setVisible(false);
        menu.findItem(R.id.search_menu_item).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
