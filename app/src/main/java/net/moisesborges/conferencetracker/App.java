package net.moisesborges.conferencetracker;

import android.app.Application;

import net.moisesborges.conferencetracker.db.ConferenceDatabase;

/**
 * Created by Moisés on 18/08/2016.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ConferenceDatabase.init(getApplicationContext());
    }
}
