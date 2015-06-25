package songbook.asu.ax.songbook.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.SongFilter;
import songbook.asu.ax.songbook.data.SongSyncAdapter;

/**
 * Created by konsult on 2015-06-20.
 */
public class SongbookActivity extends ActionBarActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_guestbook){
            Intent intent = new Intent(this,GuestbookActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_category){
            Intent intent = new Intent(this,CategoryActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_event){
            Intent intent = new Intent(this,EventActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_add_song){
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_to_api)));
            startActivity(myIntent);
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
