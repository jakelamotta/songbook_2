package songbook.asu.ax.songbook;

import android.util.Log;

import java.sql.Date;
import java.util.HashMap;

/**
 * Created by Kristian on 2015-03-07.
 */
public class Utilities {

    private static final String LOG_TAG = Utilities.class.getSimpleName();

    //For testing purposes
    public static void main(String[] argvs){
java.util.Date date = new java.util.Date();
        System.out.println(decorateDateString(formatDateString(date)));
    }

    public static String formatDateString(java.util.Date date){
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String dateString = sqlDate.toString();
        dateString = dateString.toString().replace("-","");
        if (dateString.length() != 8){
            Log.e(LOG_TAG,"Date is not in the correct format: " + date + " " + Integer.toString(dateString.length()));
            throw new IllegalArgumentException();
        }
        return dateString;
    }

    public static String decorateDateString(String formattedDate){
        String year = formattedDate.substring(0,4);
        String month = formattedDate.substring(4,6);
        String day = formattedDate.substring(6,8);
        String postfix;

        switch (month){
            case "01":
                month = "Januari";
                break;
            case "02":
                month = "Februari";
                break;
            case "03":
                month = "Mars";
                break;
            case "04":
                month = "April";
                break;
            case "05":
                month = "Maj";
                break;
            case "06":
                month = "Juni";
                break;
            case "07":
                month = "Juli";
                break;
            case "08":
                month = "Augusti";
                break;
            case "09":
                month = "September";
                break;
            case "10":
                month = "Oktober";
                break;
            case "11":
                month = "November";
                break;
            case "12":
                month = "December";
                break;
        }

        switch (day.substring(1)){
            case "1":
                postfix = ":a";
                break;
            case "2":
                postfix = ":a";
                break;
            default:
                postfix = ":e";
                break;
        }

        String finalString = day + postfix + " " + month + " " + year;
        return finalString;
    }
}


