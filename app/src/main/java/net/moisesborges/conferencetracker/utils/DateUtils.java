package net.moisesborges.conferencetracker.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Moisés on 18/08/2016.
 */

public class DateUtils {


    private static final String PATTERN = "yyyy-MM-dd HH:mm";
    private static DateFormat UI_DATE_FORMAT = SimpleDateFormat.getInstance();

    public static String dateToUiString(Date date) {
        try {
            return UI_DATE_FORMAT.format(date).split(" ")[0];
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDate(int year, int monthOfYear, int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        return calendar.getTime();
    }

    public static long getCurrentTimeInMillis() {
        return System.currentTimeMillis();
    }

    /**
     *
     * @param firstDate
     * @param secondDate
     * @return -1 if firstDate is before secondDate, 0 if firstDAte is equal to secondDate and 1 if firstDate if after secondDate
     */
    public static int compareDates(long firstDate, long secondDate) {
        Calendar firstCalendar = Calendar.getInstance();
        firstCalendar.setTimeInMillis(firstDate);

        Calendar secondCalendar = Calendar.getInstance();
        secondCalendar.setTimeInMillis(secondDate);

        int year1 = firstCalendar.get(Calendar.YEAR);
        int year2 = secondCalendar.get(Calendar.YEAR);

        if (year1 < year2) {
            return -1;
        } else if (year1 > year2) {
            return 1;
        } else {
            int dayOfYear1 = firstCalendar.get(Calendar.DAY_OF_YEAR);
            int dayOfYear2 = secondCalendar.get(Calendar.DAY_OF_YEAR);
            if (dayOfYear1 < dayOfYear2) {
                return -1;
            } else if (dayOfYear1 > dayOfYear2) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
