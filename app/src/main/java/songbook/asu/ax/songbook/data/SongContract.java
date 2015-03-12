package songbook.asu.ax.songbook.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by EIS i7 Gamer on 2015-02-28.
 */
public class SongContract {

    public static final String CONTENT_AUTHORITY = "songbook.asu.ax.songbook";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SONG = "song";
    public static final String PATH_EVENT = "event";
    public static final String PATH_SONG_WITH_EVENT = "song_with_event";

    public static final class EventTable implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENT).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;

        public static final String NAME = "event";
        public static final String COLUMN_EVENT_NAME = "event_name";

        //ID is used to query for songs from the server.
        public static final String COLUMN_CREATOR_ID = "creator_id";
        public static final String COLUMN_EVENT_DATE = "event_date";

        //A version control value
        public static final String COLUMN_LAST_UPADTED = "last_updated";
        public static final String COLUMN_CURRENT_SONG = "current_song";

        //Other information regarding the event, formatted in a specific way yet to be decided
        public static final String COLUMN_INFO = "event_info";

        public static Uri buildEventUri(String id) {
            //return ContentUris.withAppendedId(CONTENT_URI, id);
            return CONTENT_URI.buildUpon().build();//.appendPath("song").build();
        }
    }

    public static final class SongTable implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SONG).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SONG;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SONG;

        public static final String NAME = "song";
        public static final String COLUMN_SONG_NAME = "song_name";

        //ID is used to query for songs from the server.
        public static final String COLUMN_SONG_ID = "song_id";
        public static final String COLUMN_SONG_MELODY = "song_melody";

        //A version control value
        public static final String COLUMN_LAST_UPADTED = "last_updated";

        public static final String COLUMN_TEXT = "song_text";

        public static Uri buildSongUri(String id) {
            //return ContentUris.withAppendedId(CONTENT_URI, id);
            return CONTENT_URI.buildUpon().build();//.appendPath("song").build();
        }

        public static Uri buildSongWithEventUri(String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }

}


