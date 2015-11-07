package songbook.asu.ax.songbook;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import songbook.asu.ax.songbook.activities.SongbookActivity;

/**
 * Created by Kristian on 2015-03-07.
 */
public class Utilities {

    private static final String LOG_TAG = Utilities.class.getSimpleName();

    //For testing purposes
    public static void main(String[] argvs){
        //java.util.Date date = new java.util.Date();
        //System.out.println(decorateDateString(formatDateString(date)));
        Calendar cal = Calendar.getInstance();
    }

    public static long getTimeStamp(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public static String msTimeToDate(long timeInMs, Context context){
        //long timeInMs = Long.parseLong(string)*1000;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMs);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH); // Note: zero based!
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        String yearString = Integer.toString(year);
        String monthString = Utilities.getMonthFromInt(month, context);
        String dayString = Integer.toString(day);
        String hourString = Utilities.padString(Integer.toString(hour), 2, "0", "");
        String minString = Utilities.padString(Integer.toString(minute),2,"0","");

        return hourString+":"+minString+" "+dayString+getPostfixSwe(day)+" "+monthString+" "+yearString;
    }

    private static String getPostfixSwe(int day){
        String postfix = ":e";

        if(day == 1 || day == 2 || day == 21 || day == 22 || day == 31){
            postfix = ":a";
        }

        return postfix;
    }

    private static String padString(String toPad, int length, String sign, String flag){
        while (toPad.length() < length){
            if (flag == "after"){
                toPad = toPad + sign;
            }
            else{
                toPad = sign + toPad;
            }
        }

        return toPad;
    }

    private static String getMonthFromInt(int i, Context context){
        String month = "";
        switch (i){
            case 0:
                month = context.getString(R.string.january);
                break;
            case 1:
                month = context.getString(R.string.february);
                break;
            case 2:
                month = context.getString(R.string.march);
                break;
            case 3:
                month = context.getString(R.string.april);
                break;
            case 4:
                month = context.getString(R.string.may);
                break;
            case 5:
                month = context.getString(R.string.june);
                break;
            case 6:
                month = context.getString(R.string.july);
                break;
            case 7:
                month = context.getString(R.string.august);
                break;
            case 8:
                month = context.getString(R.string.september);
                break;
            case 9:
                month = context.getString(R.string.october);
                break;
            case 10:
                month = context.getString(R.string.november);
                break;
            case 11:
                month = context.getString(R.string.december);
                break;
        }


        return month;
    }
}


