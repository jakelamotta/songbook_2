package songbook.asu.ax.songbook.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;
import java.util.Iterator;

import songbook.asu.ax.songbook.Utilities;


/**
 * Created by EIS i7 Gamer on 2015-02-28.
 */
public class SongProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String LOG_TAG = SongProvider.class.getSimpleName();

    private SongDbHelper mHelper;

    private static final int SONG = 100;
    private static final int EVENT = 101;
    private static final int SONG_WITH_EVENT = 102;
    private static final int SONG_WITH_NAME = 103;
    private static final int GUESTBOOK = 104;
    private static final int CATEGORY = 105;
    private static final int SONG_WITH_CATEGORY = 106;

    private static SQLiteQueryBuilder songByEventQueryBuilder;
    private static SQLiteQueryBuilder uniqueCategoryQueryBuilder;

    static{
        uniqueCategoryQueryBuilder = new SQLiteQueryBuilder();
        uniqueCategoryQueryBuilder.setTables(
                SongContract.SongTable.NAME + " GROUP BY " +
                SongContract.SongTable.COLUMN_CATEGORY
        );
    }

    static{
        songByEventQueryBuilder = new SQLiteQueryBuilder();
        songByEventQueryBuilder.setTables(
                SongContract.SongTable.NAME + " INNER JOIN " +
                        SongContract.EventHasSongTable.NAME +
                        " ON " + SongContract.SongTable.NAME +
                        "." + SongContract.SongTable.COLUMN_SONG_ID +
                        " = " + SongContract.EventHasSongTable.NAME +
                        "." + SongContract.EventHasSongTable.COLUMN_SONG_ID);
    }

    //event.date = ?
    private static final String sEventDateSelection =
            SongContract.EventTable.NAME+
                    "." + SongContract.EventTable.COLUMN_EVENT_DATE + " = ? ";

    //event_has_song.event_id = ?
    private static final String sSongWithEventSelection =
            SongContract.EventHasSongTable.COLUMN_EVENT_ID + " = ? ";

    //song.song_id = ?
    private static final String sSongWithIdSelection =
            SongContract.SongTable.NAME +
                    "." + SongContract.SongTable.COLUMN_SONG_ID + " = ? ";

    private static final String sSongWithName = SongContract.SongTable.NAME +
            "." + SongContract.SongTable.COLUMN_SONG_NAME + " = ?";

    private Cursor getUniqueCategory(String[] projection, String sortOrder, String selection, String[] selectionArgs){
        return uniqueCategoryQueryBuilder.query(mHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getSongsByEvent(Uri uri, String[] projection, String sortOrder){
        String[] selectionArgs;
        String selection;


        String id = "-1";

        Cursor cursor = getEventByDate(new String[]{SongContract.EventTable.COLUMN_EVENT_ID,SongContract.EventTable.COLUMN_EVENT_DATE},
                SongContract.EventTable.COLUMN_EVENT_ID);

        try{
            cursor.moveToFirst();
            id = cursor.getString(cursor.getColumnIndex(SongContract.EventTable.COLUMN_EVENT_ID));
        }
        catch (Exception e){
            Log.e(LOG_TAG,e.getMessage());
        }

        selection = sSongWithEventSelection;
        selectionArgs = new String[]{id};

        return songByEventQueryBuilder.query(mHelper.getReadableDatabase(),
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

    }

    private Cursor getSongByName(Uri uri, String[] projection, @Nullable String selection, String [] selectionArgs,String sortOrder){
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SongContract.SongTable.NAME);

        if (selection == null) {
            selection = sSongWithName;
        }

        return queryBuilder.query(mHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getEventByDate(String[] projection, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(SongContract.EventTable.NAME);

        String[] selectionArgs;
        String selection;

        selection = sEventDateSelection;
        selectionArgs = new String[]{Utilities.formatDateString(new Date())};

        return queryBuilder.query(mHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public boolean onCreate() {
        mHelper = new SongDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CATEGORY: {
                Log.v(LOG_TAG,"In category");
                /*retCursor = mHelper.getReadableDatabase().query(
                        SongContract.SongTable.NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                )*/
                retCursor = getUniqueCategory(projection,sortOrder,selection,selectionArgs);
                Log.v(LOG_TAG,Integer.toString(retCursor.getCount()));
                break;
            }
            case SONG_WITH_CATEGORY:{
                retCursor = mHelper.getReadableDatabase().query(
                        SongContract.SongTable.NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case GUESTBOOK: {

                retCursor = mHelper.getReadableDatabase().query(
                        SongContract.GuestbookTable.NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case SONG: {
                retCursor = mHelper.getReadableDatabase().query(
                        SongContract.SongTable.NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case SONG_WITH_EVENT: {
                retCursor = getSongsByEvent(uri,projection,sortOrder);
                break;
            }
            case EVENT:{
                retCursor = mHelper.getReadableDatabase().query(
                        SongContract.EventTable.NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case SONG_WITH_NAME:{
                retCursor = getSongByName(uri,projection,selection,selectionArgs,sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri + " " + Integer.toString(match));
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SONG:
                return SongContract.SongTable.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Iterator it = values.keySet().iterator();

        while (it.hasNext()){
            String key = it.next().toString();
        }

        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case GUESTBOOK: {
                long _id = db.insert(SongContract.GuestbookTable.NAME, null, values);
                Log.v(LOG_TAG,Long.toString(_id));

                if (_id > 0) {
                    returnUri = SongContract.GuestbookTable.buildGuestbookUri();
                } else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case SONG: {
                long _id = db.insert(SongContract.SongTable.NAME, null, values);
                if ( _id > 0 )
                    returnUri = SongContract.SongTable.buildSongUri();
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case EVENT: {
                throw new UnsupportedOperationException("Not supported yet");
            }
            case SONG_WITH_EVENT:{
                throw new UnsupportedOperationException("Not supported yet");
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case GUESTBOOK:
                rowsDeleted = db.delete(SongContract.GuestbookTable.NAME,selection,selectionArgs);
                break;
            case SONG:
                rowsDeleted = db.delete(
                        SongContract.SongTable.NAME, selection, selectionArgs);
                break;
            case EVENT:
                rowsDeleted = db.delete(
                        SongContract.EventTable.NAME, selection, selectionArgs);
            case SONG_WITH_EVENT:{
                rowsDeleted = db.delete(
                        SongContract.EventHasSongTable.NAME, selection, selectionArgs);
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri + " in delete");
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case SONG:{
                rowsUpdated = db.update(SongContract.SongTable.NAME, values, selection,
                        selectionArgs);
                break;
            }
            case EVENT: {
                rowsUpdated = db.update(SongContract.EventTable.NAME, values, selection,
                        selectionArgs);
                break;
            }
            case SONG_WITH_EVENT:
            {
                rowsUpdated = db.update(SongContract.EventHasSongTable.NAME, values, selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SONG:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        String[] tempArray = new String[1];
                        tempArray[0] = value.getAsString("song_name");
                        db.delete(SongContract.SongTable.NAME,"song_name = ?",tempArray);
                        long _id = db.insert(SongContract.SongTable.NAME, null, value);

                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case EVENT:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        String[] tempArray = new String[1];
                        tempArray[0] = value.getAsString("event_name");
                        db.delete(SongContract.EventTable.NAME,"event_name = ?",tempArray);
                        long _id = db.insert(SongContract.EventTable.NAME, null, value);

                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case SONG_WITH_EVENT: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        String[] tempArray = new String[1];
                        tempArray[0] = value.getAsString("id");
                        db.delete(SongContract.EventHasSongTable.NAME, "id = ?", tempArray);
                        long _id = db.insert(SongContract.EventHasSongTable.NAME, null, value);

                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SongContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority,SongContract.PATH_SONG + "/" + SongContract.PATH_SONG_WITH_EVENT,SONG_WITH_EVENT);
        matcher.addURI(authority, SongContract.PATH_SONG, SONG);
        matcher.addURI(authority,SongContract.PATH_EVENT,EVENT);
        matcher.addURI(authority,SongContract.PATH_SONG + "/" + SongContract.PATH_EVENT_WITH_NAME,SONG_WITH_NAME);
        matcher.addURI(authority,SongContract.PATH_GUESTBOOK,GUESTBOOK);
        matcher.addURI(authority,SongContract.PATH_SONG + "/" + SongContract.PATH_SONG_WITH_CATEGORY,SONG_WITH_CATEGORY);
        matcher.addURI(authority,SongContract.PATH_SONG + "/" + SongContract.PATH_CATEGORY,CATEGORY);
        return matcher;
    }
}
