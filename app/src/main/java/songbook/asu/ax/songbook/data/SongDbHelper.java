package songbook.asu.ax.songbook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by EIS i7 Gamer on 2015-02-28.
 */
public class SongDbHelper extends SQLiteOpenHelper {

    //Change this value when making db updates
    private static final int DATABASE_VERSION = 29;
    static final String DATABASE_NAME = "songs.db";
    private static final String LOG_TAG = SongDbHelper.class.getSimpleName();

    public SongDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_SONG_TABLE = "CREATE TABLE " + SongContract.SongTable.NAME + " (" +
                SongContract.SongTable._ID + " INTEGER PRIMARY KEY," +
                SongContract.SongTable.COLUMN_SONG_NAME + " TEXT UNIQUE NOT NULL, " +
                SongContract.SongTable.COLUMN_SONG_ID + " INTEGER UNIQUE NOT NULL, " +
                SongContract.SongTable.COLUMN_SONG_MELODY + " TEXT, " +
                SongContract.SongTable.COLUMN_LAST_UPADTED + " INTEGER, " +
                SongContract.SongTable.COLUMN_CATEGORY + " TEXT, " +
                SongContract.SongTable.COLUMN_TEXT + " TEXT" +
                " );";

        final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " + SongContract.EventTable.NAME + " (" +
                SongContract.EventTable._ID + " INTEGER PRIMARY KEY," +
                SongContract.EventTable.COLUMN_EVENT_NAME + " TEXT UNIQUE NOT NULL, " +
                SongContract.EventTable.COLUMN_EVENT_DATE + " TEXT NOT NULL, " +
                SongContract.EventTable.COLUMN_LAST_UPADTED + " INTEGER, " +
                SongContract.EventTable.COLUMN_CURRENT_SONG + " INTEGER, " +
                SongContract.EventTable.COLUMN_EVENT_ID + " INTEGER UNIQUE, " +
                SongContract.EventTable.COLUMN_INFO + " TEXT " +
                " );";

        final String SQL_CREATE_EVENT_HAS_SONG_TABLE = "CREATE TABLE " + SongContract.EventHasSongTable.NAME + " (" +
                SongContract.EventHasSongTable._ID + " INTEGER PRIMARY KEY, " +
                SongContract.EventHasSongTable.COLUMN_EVENT_ID + " INTEGER, " +
                SongContract.EventHasSongTable.COLUMN_SONG_ID + " INTEGER " +
                " );";

        final String SQL_CREATE_GUESTBOOK = "CREATE TABLE " + SongContract.GuestbookTable.NAME + " (" +
                SongContract.GuestbookTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SongContract.GuestbookTable.COLUMN_POSTER + " TEXT, " +
                SongContract.GuestbookTable.COLUMN_ENTRY + " TEXT, " +
                SongContract.GuestbookTable.COLUMN_TIMESTAMP + " TEXT " +
                " );";

        db.execSQL(SQL_CREATE_GUESTBOOK);
        db.execSQL(SQL_CREATE_EVENT_HAS_SONG_TABLE);
        db.execSQL(SQL_CREATE_EVENT_TABLE);
        db.execSQL(SQL_CREATE_SONG_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SongContract.GuestbookTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SongContract.SongTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SongContract.EventHasSongTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SongContract.EventTable.NAME);
        onCreate(db);
    }
}


