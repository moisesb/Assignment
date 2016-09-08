package net.moisesborges.conferencetracker.utils;

import android.content.Context;
import android.test.RenamingDelegatingContext;

import net.moisesborges.conferencetracker.db.ConferenceDatabase;
import net.moisesborges.conferencetracker.model.Conference;

/**
 * Created by Moisés on 19/08/2016.
 */

public class DbHelper {

    public static void initDbAndBeginTransaction(Context context) {
        RenamingDelegatingContext newContext = new RenamingDelegatingContext(context, "test_");
        ConferenceDatabase.init(newContext);
    }

    public static void endTransaction() {
    }
}
