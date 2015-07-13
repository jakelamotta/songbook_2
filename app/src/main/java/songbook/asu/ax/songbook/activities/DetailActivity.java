package songbook.asu.ax.songbook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import songbook.asu.ax.songbook.fragments.CategoryFragment;
import songbook.asu.ax.songbook.fragments.DetailFragment;
import songbook.asu.ax.songbook.fragments.MainFragment;
import songbook.asu.ax.songbook.R;

/**
 * Created by EIS i7 Gamer on 2015-03-02.
 */
public class DetailActivity extends SongbookActivity{

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private String mCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        if (intent.hasExtra(CategoryFragment.SELECTED_CATEGORY)){
            mCategory = intent.getStringExtra(CategoryFragment.SELECTED_CATEGORY);
        }

        if (savedInstanceState == null){
            Bundle bundle = getIntent().getBundleExtra(MainFragment.BUNDLE_KEY);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail_container, fragment).commit();
        }

        getSupportActionBar().setIcon(R.mipmap.asu_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        String className = parents.peek().getSimpleName();
        Intent parentActivityIntent = new Intent(this, parents.pop());
        parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (className.equals(SongsByCatActivity.class.getSimpleName()) && !this.mCategory.equals("")) {
            parentActivityIntent.putExtra(CategoryFragment.SELECTED_CATEGORY, mCategory);
        }

        startActivity(parentActivityIntent);
        finish();
        return true;
    }
}
