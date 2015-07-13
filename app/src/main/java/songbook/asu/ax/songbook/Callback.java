package songbook.asu.ax.songbook;

import android.net.Uri;

/**
 * Created by Kristian on 2015-03-20.
 */
public interface Callback {
    void onItemSelected(Uri uri, String name, String cat);
}
