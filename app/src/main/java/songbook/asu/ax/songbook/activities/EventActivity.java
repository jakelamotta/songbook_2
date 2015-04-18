package songbook.asu.ax.songbook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import songbook.asu.ax.songbook.fragments.EventFragment;
import songbook.asu.ax.songbook.R;

/**
 * Created by Kristian on 2015-03-10.
 */
public class EventActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        EventFragment fragment = new EventFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_event_container, fragment).commit();

        getSupportActionBar().setIcon(R.mipmap.asu_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_guestbook){
            Intent intent = new Intent(this,GuestbookActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}