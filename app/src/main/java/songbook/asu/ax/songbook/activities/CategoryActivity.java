package songbook.asu.ax.songbook.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.fragments.CategoryFragment;
import songbook.asu.ax.songbook.fragments.GuestbookFragment;
import songbook.asu.ax.songbook.fragments.MainFragment;

/**
 * Created by EIS i7 Gamer on 2015-04-22.
 */
public class CategoryActivity extends SongbookActivity{

    public static final String SELECTED_CATEGORY = "category";
    private static final String LOG_TAG = CategoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        CategoryFragment fragment = new CategoryFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_category_container,fragment).commit();
        setTitle("");
        getSupportActionBar().setIcon(R.mipmap.asu_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_category).setVisible(false);
        menu.findItem(R.id.search_menu_item).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
