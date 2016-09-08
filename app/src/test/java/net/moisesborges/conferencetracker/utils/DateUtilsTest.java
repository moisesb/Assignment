package net.moisesborges.conferencetracker.utils;

import net.moisesborges.conferencetracker.util.*;
import net.moisesborges.conferencetracker.util.DateUtils;

import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Moisés on 18/08/2016.
 */

public class DateUtilsTest {

    @Test
    public void after() throws Exception {
        Date tomorow = DateUtils.dateInAdvance(1);
        long currentTimeMillis = System.currentTimeMillis();
        assertEquals(1, net.moisesborges.conferencetracker.utils.DateUtils.compareDates(tomorow.getTime(),currentTimeMillis));
    }

    @Test
    public void dameDay() throws Exception {
        Date today = DateUtils.dateInAdvance(0);
        long currentTimeMillis = System.currentTimeMillis();
        assertEquals(0, net.moisesborges.conferencetracker.utils.DateUtils.compareDates(today.getTime(),currentTimeMillis));
    }

    @Test
    public void before() throws Exception {
        Date before = DateUtils.dateInPast(1);
        long currentTimeMillis = System.currentTimeMillis();
        assertEquals(-1, net.moisesborges.conferencetracker.utils.DateUtils.compareDates(before.getTime(),currentTimeMillis));
    }
}
