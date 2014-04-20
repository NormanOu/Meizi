package awesome.blue.meizi.util;


import android.content.Context;
import awesome.blue.meizi.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    /**
     * 一分钟的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MINUTE = 60 * 1000;

    /**
     * 一小时的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;

    /**
     * 一天的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;

    /**
     * 一月的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MONTH = 30 * ONE_DAY;

    /**
     * 一年的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_YEAR = 12 * ONE_MONTH;

    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssz";

    private static String second;
    private static String minute;
    private static String hour;
    private static String day;
    private static String month;
    private static String year;
    private static String before;
    private static String rightNow;
    private static String timeError;
    
    public static Date parseDate(String dateStr) {
        Date parsed = new Date();
        try {
            SimpleDateFormat format =
                new SimpleDateFormat(DATE_PATTERN);
            parsed = format.parse(dateStr);
        }
        catch(ParseException pe) {
            throw new IllegalArgumentException();
        }

        return parsed;
    }
    
    public static String timeFromNow(Context context, Date date) {
        if (second == null) {
            second = context.getResources().getString(R.string.time_second);
            minute = context.getResources().getString(R.string.time_minute);
            hour = context.getResources().getString(R.string.time_hour);
            day = context.getResources().getString(R.string.time_day);
            month = context.getResources().getString(R.string.time_month);
            year = context.getResources().getString(R.string.time_year);
            before = context.getResources().getString(R.string.time_before);
            rightNow = context.getResources().getString(R.string.time_right_now);
            timeError = context.getResources().getString(R.string.time_err);
        }

        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - date.getTime();
        long timeIntoFormat;
        String resultTimeFromNow;
        if (timePassed < 0) {
            resultTimeFromNow = timeError;
        } else if (timePassed < ONE_MINUTE) {
            resultTimeFromNow = rightNow;
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            resultTimeFromNow =  timeIntoFormat + minute + before;
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            resultTimeFromNow =  timeIntoFormat + hour + before;
        } else if (timePassed < ONE_MONTH) {
            timeIntoFormat = timePassed / ONE_DAY;
            resultTimeFromNow =  timeIntoFormat + day + before;
        } else if (timePassed < ONE_YEAR) {
            timeIntoFormat = timePassed / ONE_MONTH;
            resultTimeFromNow =  timeIntoFormat + month + before;
        } else {
            timeIntoFormat = timePassed / ONE_YEAR;
            resultTimeFromNow =  timeIntoFormat + year + before;
        }
        
        return resultTimeFromNow;
    }
}
