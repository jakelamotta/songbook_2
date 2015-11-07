package songbook.asu.ax.songbook.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import songbook.asu.ax.songbook.fragments.EventFragment;
import songbook.asu.ax.songbook.R;

/**
 * Created by Kristian on 2015-03-10.
 */
public class EventActivity extends SongbookActivity {

    private static final String LOG_TAG = EventActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);

        EventFragment fragment = new EventFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_event_container, fragment).commit();
        setTitle("");
        getSupportActionBar().setIcon(R.mipmap.asu_icon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //menu.findItem(R.id.action_event).setVisible(false);
        menu.findItem(R.id.search_menu_item).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}