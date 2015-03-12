package songbook.asu.ax.songbook;

import android.util.Log;

import java.sql.Date;

/**
 * Created by Kristian on 2015-03-07.
 */
public class Utilities {

    private static final String LOG_TAG = Utilities.class.getSimpleName();

    //For testing purposes
    public static void main(String[] argvs){
        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        System.out.println(formatDateString(sqlDate.toString()));

    }

    public static String formatDateString(String date){
        date = date.replace("-","");
        if (date.length() != 8){
            Log.e(LOG_TAG,"Date is not in the correct format: " + date + " " + Integer.toString(date.length()));
            throw new IllegalArgumentException();
        }
        return date;
    }
}


