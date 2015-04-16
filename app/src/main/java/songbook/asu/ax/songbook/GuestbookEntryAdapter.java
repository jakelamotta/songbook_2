package songbook.asu.ax.songbook;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import songbook.asu.ax.songbook.fragments.GuestbookFragment;

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
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.entryText.setText(cursor.getString(GuestbookFragment.COL_GUESTBOOK_ENTRY));

        viewHolder.posterText.setText(cursor.getString(GuestbookFragment.COL_GUESTBOOK_POSTER));
        viewHolder.timestampText.setText(cursor.getString(GuestbookFragment.COL_GUESTBOOK_TIMESTAMP));
    }
}
