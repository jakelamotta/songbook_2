package songbook.asu.ax.songbook.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.fragments.GuestbookFragment;

/**
 * Created by Kristian on 2015-04-16.
 */
public class GuestbookActivity extends ActionBarActivity {

    private static final String LOG_TAG = GuestbookActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guestbook);

        GuestbookFragment fragment = new GuestbookFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_guestbook_container, fragment).commit();

        getSupportActionBar().setIcon(R.mipmap.asu_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Log.v(LOG_TAG,"onCreate was finished");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(LOG_TAG,"in onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_guestbook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

}
