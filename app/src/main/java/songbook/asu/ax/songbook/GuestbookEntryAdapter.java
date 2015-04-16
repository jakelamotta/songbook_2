package songbook.asu.ax.songbook;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import songbook.asu.ax.songbook.activities.GuestbookActivity;

/**
 * Created by Kristian on 2015-04-16.
 */
public class GuestbookEntryAdapter extends CursorAdapter {

    private static final String LOG_TAG = GuestbookEntryAdapter.class.getSimpleName();

    public static class ViewHolder{
        public final ImageView arrowView;
        public final TextView nameView;
        public final TextView dateView;

        public ViewHolder(View view) {
            arrowView = (ImageView) view.findViewById(R.id.image_view_item);
            nameView = (TextView) view.findViewById(R.id.text_view_songname);
            dateView =(TextView) view.findViewById(R.id.text_view_melody);
        }
    }

    public GuestbookEntryAdapter(Context context, Cursor cursor,int flag){
        super(context,cursor,flag);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
