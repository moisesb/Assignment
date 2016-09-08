package net.moisesborges.conferencetracker.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Moisés on 18/08/2016.
 */

public class DateUtils {

    public static Date dateInAdvance(int numOfDays) {
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear + numOfDays);
        return calendar.getTime();
    }

    public static Date dateInPast(int numOfDays) {
        return dateInAdvance(numOfDays * -1);
    }
}
