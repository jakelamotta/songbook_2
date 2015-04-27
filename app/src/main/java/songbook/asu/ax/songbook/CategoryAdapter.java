package songbook.asu.ax.songbook;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import songbook.asu.ax.songbook.fragments.CategoryFragment;
import songbook.asu.ax.songbook.fragments.MainFragment;

/**
 * Created by Kristian on 2015-04-23.
 */
public class CategoryAdapter extends CursorAdapter {

    private static final String LOG_TAG = CategoryAdapter.class.getSimpleName();

    public static class ViewHolder{
        public final ImageView iconView;
        public final TextView categoryTextView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.image_view_category);
            categoryTextView = (TextView) view.findViewById(R.id.text_view_category);
        }
    }

    public CategoryAdapter(Context context, Cursor cursor,int flags){
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.list_item_category;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.categoryTextView.setText(cursor.getString(CategoryFragment.COL_CATEGORY));

        viewHolder.iconView.setImageResource(R.mipmap.arrow_no_border);
    }
}
