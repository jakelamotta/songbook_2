package songbook.asu.ax.songbook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by EIS i7 Gamer on 2015-02-28.
 */
public class SongDbHelper extends SQLiteOpenHelper {

    //Change this value when making db updates
    private static final int DATABASE_VERSION = 9;
    static final String DATABASE_NAME = "songs.db";

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
                SongContract.SongTable.COLUMN_TEXT + " TEXT" +
                " );";

        final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " + SongContract.EventTable.NAME + " (" +
                SongContract.EventTable._ID + " INTEGER PRIMARY KEY," +
                SongContract.EventTable.COLUMN_EVENT_NAME + " TEXT UNIQUE NOT NULL, " +
                SongContract.EventTable.COLUMN_EVENT_DATE + " TEXT NOT NULL, " +
                SongContract.EventTable.COLUMN_LAST_UPADTED + " INTEGER, " +
                SongContract.EventTable.COLUMN_CURRENT_SONG + " INTEGER, " +
                SongContract.EventTable.COLUMN_INFO + " TEXT " +
                " );";

        final String SQL_CREATE_EVENT_HAS_SONG_TABLE = "CREATE TABLE";

        db.execSQL(SQL_CREATE_EVENT_TABLE);
        db.execSQL(SQL_CREATE_SONG_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SongContract.SongTable.NAME);
        onCreate(db);
    }
}


