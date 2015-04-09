package songbook.asu.ax.songbook;

import android.util.Log;

/**
 * Created by Kristian on 2015-03-07.
 */
public class Utilities {

    private static final String LOG_TAG = Utilities.class.getSimpleName();
    private static final String JANUARY = "januari";
    private static final String FEBRUARY = "februari";
    private static final String MARCH = "mars";
    private static final String APRIL = "april";
    private static final String MAY = "maj";
    private static final String JUNE = "juni";
    private static final String JULY = "juli";
    private static final String AUGUST = "augusti";
    private static final String SEPTEMBER = "september";
    private static final String OCTOBER = "oktober";
    private static final String NOVEMBER = "november";
    private static final String DECEMBER = "december";

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
                month = JANUARY;
                break;
            case "02":
                month = FEBRUARY;
                break;
            case "03":
                month = MARCH;
                break;
            case "04":
                month = APRIL;
                break;
            case "05":
                month = MAY;
                break;
            case "06":
                month = JUNE;
                break;
            case "07":
                month = JULY;
                break;
            case "08":
                month = AUGUST;
                break;
            case "09":
                month = SEPTEMBER;
                break;
            case "10":
                month = OCTOBER;
                break;
            case "11":
                month = NOVEMBER;
                break;
            case "12":
                month = DECEMBER;
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


