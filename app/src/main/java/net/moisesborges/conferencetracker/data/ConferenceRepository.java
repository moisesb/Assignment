package net.moisesborges.conferencetracker.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import net.moisesborges.conferencetracker.db.ConferenceDatabase;
import net.moisesborges.conferencetracker.db.scheme.ConferenceTable;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceSchedule;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Moisés on 18/08/2016.
 */
public class ConferenceRepository {

    private static ConferenceRepository sInstance;

    private SQLiteDatabase mDatabase;

    private ConferenceRepository() {
        mDatabase = ConferenceDatabase.getInstance()
                .getWritableDatabase();
    }

    public void getConferenceAsync(final long conferenceId, final OnGetConferenceCallback callback) {
        AsyncTask<Void,Void,Conference> asyncTask = new AsyncTask<Void, Void, Conference>() {
            @Override
            protected Conference doInBackground(Void... params) {
                return getConference(conferenceId);
            }

            @Override
            protected void onPostExecute(Conference conference) {
                super.onPostExecute(conference);
                if (conference != null) {
                    callback.onGetConference(conference);
                }else {
                    callback.onConferenceNotFound();
                }
            }
        };
        asyncTask.execute();
    }

    public Conference getConference(long conferenceId) {
        String selection = ConferenceTable.Columns.ID + " = ?";
        String[] selectionArgs = {String.valueOf(conferenceId)};
        final Cursor cursor = mDatabase.query(ConferenceTable.NAME, null, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        return getConference(cursor);
    }

    private Conference getConference(Cursor cursor) {
        if (cursor.getCount() == 0 || cursor.isAfterLast()) {
            return null;
        }

        Conference conference = new Conference();
        conference.setId(cursor.getLong(cursor.getColumnIndex(ConferenceTable.Columns.ID)));
        conference.setVenue(cursor.getString(cursor.getColumnIndex(ConferenceTable.Columns.VENUE)));
        conference.setPlace(cursor.getString(cursor.getColumnIndex(ConferenceTable.Columns.PLACE)));
        conference.setAdministratorId(cursor.getLong(cursor.getColumnIndex(ConferenceTable.Columns.ADMIN_ID)));
        conference.setTitle(cursor.getString(cursor.getColumnIndex(ConferenceTable.Columns.TITLE)));
        conference.setNumOfDays(cursor.getInt(cursor.getColumnIndex(ConferenceTable.Columns.DURATION)));
        conference.setSpeciality(cursor.getString(cursor.getColumnIndex(ConferenceTable.Columns.SPECIALITY)));
        conference.setStartDate(cursor.getLong(cursor.getColumnIndex(ConferenceTable.Columns.START_DATE)));
        return conference;
    }

    public void getUpcomingConferences(final OnGetConferencesCallback callback) {
        AsyncTask<Void,Void,List<Conference>> asyncTask = new AsyncTask<Void, Void, List<Conference>>() {
            @Override
            protected List<Conference> doInBackground(Void... params) {

                Cursor query = mDatabase.query(ConferenceTable.NAME, null, null, null, null, null, null);
                query.moveToFirst();
                List<Conference> upcomingConferences = new ArrayList<>();
                while (!query.isAfterLast()) {
                    upcomingConferences.add(getConference(query));
                    query.moveToNext();
                }
                return upcomingConferences;
            }

            @Override
            protected void onPostExecute(List<Conference> conferences) {
                super.onPostExecute(conferences);
                callback.onGetConferences(conferences);

            }
        };
        asyncTask.execute();
    }

    public void addConference(Conference conference) {
        long id = mDatabase.insert(ConferenceTable.NAME, null, contentValuesFor(conference));
        conference.setId(id);
    }

    private ContentValues contentValuesFor(Conference conference) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConferenceTable.Columns.TITLE, conference.getTitle());
        contentValues.put(ConferenceTable.Columns.VENUE, conference.getVenue());
        contentValues.put(ConferenceTable.Columns.PLACE,conference.getPlace());
        contentValues.put(ConferenceTable.Columns.ADMIN_ID, conference.getAdministratorId());
        contentValues.put(ConferenceTable.Columns.DURATION, conference.getNumOfDays());
        contentValues.put(ConferenceTable.Columns.SPECIALITY, conference.getSpeciality());
        contentValues.put(ConferenceTable.Columns.START_DATE, conference.getStartDate());
        return contentValues;
    }

    public static ConferenceRepository getInstance() {
        if (sInstance == null) {
            sInstance = new ConferenceRepository();
        }
        return sInstance;
    }

    public void getScheduledConferences(final long doctorId, final OnGetConferencesCallback callback) {
        AsyncTask<Void,Void,List<Conference>> asyncTask = new AsyncTask<Void, Void, List<Conference>>() {
            @Override
            protected List<Conference> doInBackground(Void... params) {
                ScheduleRepository scheduleRepository = ScheduleRepository.getInstance();
                List<ConferenceSchedule> schedulesForDoctor = scheduleRepository.getSchedulesForDoctor(doctorId);
                List<Conference> scheduledConferences = new ArrayList<>();
                for (ConferenceSchedule conferenceSchedule : schedulesForDoctor) {
                    Conference conference = getConference(conferenceSchedule.getConferenceId());
                    scheduledConferences.add(conference);
                }
                return scheduledConferences;
            }

            @Override
            protected void onPostExecute(List<Conference> conferences) {
                super.onPostExecute(conferences);
                callback.onGetConferences(conferences);

            }
        };
        asyncTask.execute();
    }

    public void deleteConference(long conferenceId) {
        String selection = ConferenceTable.Columns.ID + " = " + conferenceId;
        mDatabase.delete(ConferenceTable.NAME, selection, null);
    }


    public interface OnGetConferenceCallback {
        void onGetConference(Conference conference);

        void onConferenceNotFound();
    }

    public interface OnGetConferencesCallback {
        void onGetConferences(List<Conference> conferences);
    }
}
