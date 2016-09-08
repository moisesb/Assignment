package net.moisesborges.conferencetracker.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import net.moisesborges.conferencetracker.db.ConferenceDatabase;
import net.moisesborges.conferencetracker.db.scheme.ConferenceScheduleTable;
import net.moisesborges.conferencetracker.model.ConferenceSchedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moisés on 19/08/2016.
 */

public class ScheduleRepository {

    private static ScheduleRepository sInstance;

    private SQLiteDatabase mDatabase;

    private ScheduleRepository() {
        mDatabase = ConferenceDatabase.getInstance()
                .getWritableDatabase();
    }

    public static ScheduleRepository getInstance() {
        if (sInstance == null) {
            sInstance = new ScheduleRepository();
        }
        return sInstance;
    }

    public void getSchedulesForDoctorAsync(final long doctorId, final OnGetSchedulesCallback callback) {
        AsyncTask<Void,Void,List<ConferenceSchedule>> asyncTask = new AsyncTask<Void, Void, List<ConferenceSchedule>>() {
            @Override
            protected List<ConferenceSchedule> doInBackground(Void... params) {
                return getSchedulesForDoctor(doctorId);
            }

            @Override
            protected void onPostExecute(List<ConferenceSchedule> conferenceSchedules) {
                super.onPostExecute(conferenceSchedules);
                callback.onGetSchedules(conferenceSchedules);
            }
        };
        asyncTask.execute();
    }

    public void addSchedule(ConferenceSchedule conferenceSchedule) {
        long id = mDatabase.insert(ConferenceScheduleTable.NAME, null, contentValuesFor(conferenceSchedule));
        conferenceSchedule.setId(id);
    }

    private ContentValues contentValuesFor(ConferenceSchedule conferenceSchedule) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConferenceScheduleTable.Columns.CONFERENCE_ID, conferenceSchedule.getConferenceId());
        contentValues.put(ConferenceScheduleTable.Columns.DOCTOR_ID, conferenceSchedule.getDoctorId());
        return contentValues;
    }

    public List<ConferenceSchedule> getSchedulesForDoctor(long doctorId) {
        String selection = ConferenceScheduleTable.Columns.DOCTOR_ID + " = " + doctorId;
        Cursor cursor = mDatabase.query(ConferenceScheduleTable.NAME, null, selection, null, null, null, null);
        cursor.moveToFirst();
        List<ConferenceSchedule> conferenceSchedules = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            conferenceSchedules.add(getConferenceSchedule(cursor));
            cursor.moveToNext();
        }
        return conferenceSchedules;
    }

    private ConferenceSchedule getConferenceSchedule(Cursor cursor) {
        if (cursor.getCount() == 0 || cursor.isAfterLast()) {
            return null;
        }

        ConferenceSchedule conferenceSchedule = new ConferenceSchedule();
        conferenceSchedule.setId(cursor.getLong(cursor.getColumnIndex(ConferenceScheduleTable.Columns.ID)));
        conferenceSchedule.setDoctorId(cursor.getLong(cursor.getColumnIndex(ConferenceScheduleTable.Columns.DOCTOR_ID)));
        conferenceSchedule.setConferenceId(cursor.getLong(cursor.getColumnIndex(ConferenceScheduleTable.Columns.CONFERENCE_ID)));

        return conferenceSchedule;
    }

    public ConferenceSchedule getSchedule(long doctorId, long conferenceId) {
        String conferenceSelection = ConferenceScheduleTable.Columns.CONFERENCE_ID + " = " + conferenceId;
        String doctorSelection = ConferenceScheduleTable.Columns.DOCTOR_ID + " = " + doctorId;
        String selection = conferenceSelection + " and " + doctorSelection;
        Cursor cursor = mDatabase.query(ConferenceScheduleTable.NAME, null, selection, null, null, null, null);
        cursor.moveToFirst();
        return getConferenceSchedule(cursor);
    }

    public void deleteSchedule(long doctorId, long conferenceId) {
        String conferenceSelection = ConferenceScheduleTable.Columns.CONFERENCE_ID + " = " + conferenceId;
        String doctorSelection = ConferenceScheduleTable.Columns.DOCTOR_ID + " = " + doctorId;
        String selection = conferenceSelection + " and " + doctorSelection;
        mDatabase.delete(ConferenceScheduleTable.NAME, selection, null);
    }

    public interface OnGetSchedulesCallback {
        void onGetSchedules(List<ConferenceSchedule> conferenceSchedules);
    }
}
