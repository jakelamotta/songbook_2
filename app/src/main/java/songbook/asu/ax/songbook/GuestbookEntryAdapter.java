package songbook.asu.ax.songbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import songbook.asu.ax.songbook.data.SongContract;
import songbook.asu.ax.songbook.fragments.GuestbookFragment;
import songbook.asu.ax.songbook.fragments.MainFragment;

/**
 * Created by Kristian on 2015-04-16.
 */
public class GuestbookEntryAdapter extends CursorAdapter {

    private static final String LOG_TAG = GuestbookEntryAdapter.class.getSimpleName();

    public static class ViewHolder{
        public final TextView entryText;
        public final TextView posterText;
        public final TextView timestampText;

        public ViewHolder(View view) {
            entryText = (TextView) view.findViewById(R.id.textview_entry);
            posterText = (TextView) view.findViewById(R.id.textview_poster);
            timestampText =(TextView) view.findViewById(R.id.textview_timestamp);
        }
    }

    public GuestbookEntryAdapter(Context context, Cursor cursor,int flag){
        super(context,cursor,flag);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.list_item_guestbookentry;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.entryText.setText(cursor.getString(GuestbookFragment.COL_GUESTBOOK_ENTRY));

        viewHolder.posterText.setText(context.getString(R.string.poster,cursor.getString(GuestbookFragment.COL_GUESTBOOK_POSTER)));
        Log.v(LOG_TAG,cursor.getString(GuestbookFragment.COL_GUESTBOOK_TIMESTAMP));
        viewHolder.timestampText.setText(context.getString(R.string.date,Utilities.
                msTimeToDate(Long.parseLong(cursor.getString(GuestbookFragment.COL_GUESTBOOK_TIMESTAMP))*1000,context)));

        final String id = getCursor().getString(GuestbookFragment.COL_GUESTBOOK_ID);

        /*viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.confirmation_dialog_title)
                        .setMessage(R.string.really_quit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.getContentResolver().delete(SongContract.GuestbookTable.buildGuestbookUri(),SongContract.GuestbookTable._ID + " = ?",new String[]{id});
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            }
        });*/
    }
}
