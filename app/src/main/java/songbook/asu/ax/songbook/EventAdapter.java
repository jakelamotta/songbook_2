package songbook.asu.ax.songbook;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import songbook.asu.ax.songbook.fragments.EventCalendarFragment;

/**
 * Created by Kristian on 2015-04-09.
 */
public class EventAdapter extends CursorAdapter{

    private static final String LOG_TAG = EventAdapter.class.getSimpleName();

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

    public EventAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.list_item_event;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.arrowView.setImageResource(R.mipmap.arrow_no_border);

        String name = cursor.getString(EventCalendarFragment.COL_EVENT_NAME);
        viewHolder.nameView.setText(name);

        String dateString = Utilities.decorateDateString(cursor.getString(EventCalendarFragment.COL_EVENT_DATE));
        viewHolder.dateView.setText(String.format(context.getString(R.string.melody),dateString));
    }
}