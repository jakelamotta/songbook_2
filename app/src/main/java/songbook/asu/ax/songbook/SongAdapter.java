package songbook.asu.ax.songbook;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import songbook.asu.ax.songbook.fragments.MainFragment;

/**
 * Created by EIS i7 Gamer on 2015-02-28.
 */
public class SongAdapter extends CursorAdapter {
    private static final String LOG_TAG = SongAdapter.class.getSimpleName();

    public static class ViewHolder{
        public final ImageView iconView;
        public final TextView nameView;
        public final TextView melodyView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.image_view_item);
            nameView = (TextView) view.findViewById(R.id.text_view_songname);
            melodyView =(TextView) view.findViewById(R.id.text_view_melody);
        }
    }

    public SongAdapter(Context context, Cursor cursor,int flags){
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        int layoutId = R.layout.list_item_song;

        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.v(LOG_TAG,"in BindView");
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.iconView.setImageResource(R.mipmap.arrow_no_border);

        String name = cursor.getString(MainFragment.COL_SONG_NAME);
        viewHolder.nameView.setText(name);

        String melody = cursor.getString(MainFragment.COL_SONG_MELODY);
        viewHolder.melodyView.setText(String.format(context.getString(R.string.melody),melody));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(LOG_TAG, "in GetView");
        return super.getView(position, convertView, parent);
    }
}