package net.moisesborges.conferencetracker.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.moisesborges.conferencetracker.db.ConferenceDatabase;
import net.moisesborges.conferencetracker.db.scheme.ConferenceAdminTable;
import net.moisesborges.conferencetracker.db.scheme.DoctorTable;
import net.moisesborges.conferencetracker.model.ConferenceAdmin;
import net.moisesborges.conferencetracker.model.Doctor;

/**
 * Created by Moisés on 19/08/2016.
 */

public class UserRepository {

    private static UserRepository sInstance;

    public SQLiteDatabase mDatabase;

    private UserRepository() {
        mDatabase = ConferenceDatabase.getInstance()
                .getWritableDatabase();
    }

    public static UserRepository getInstance(){
        if (sInstance == null) {
            sInstance = new UserRepository();
        }
        return sInstance;
    }


    public Doctor getDoctor(long doctorId) {
        String selection = DoctorTable.Columns.ID + " = " + doctorId;
        return getDoctor(selection, null);
    }

    public Doctor getDoctor(String email) {
        String selection = DoctorTable.Columns.EMAIL + " = ?";
        String[] selectionArgs = {email};
        return getDoctor(selection, selectionArgs);
    }

    private Doctor getDoctor(String selection, String[] selectionArgs) {
        Cursor cursor = mDatabase.query(DoctorTable.NAME, null, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        return getDoctor(cursor);
    }

    private Doctor getDoctor(Cursor cursor) {
        if (cursor.getCount() == 0 || cursor.isAfterLast()) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(cursor.getLong(cursor.getColumnIndex(DoctorTable.Columns.ID)));
        doctor.setEmail(cursor.getString(cursor.getColumnIndex(DoctorTable.Columns.EMAIL)));
        return doctor;
    }

    public void addDoctor(Doctor doctor) {
        long id = mDatabase.insert(DoctorTable.NAME, null, contentValuesFor(doctor));
        doctor.setId(id);
    }

    private ContentValues contentValuesFor(Doctor doctor) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DoctorTable.Columns.EMAIL, doctor.getEmail());
        return contentValues;
    }

    public ConferenceAdmin getConferenceAdmin(long conferenceAdminId) {
        String selection = ConferenceAdminTable.Columns.ID + " = " + conferenceAdminId;
        return getConferenceAdmin(selection,null);
    }

    public ConferenceAdmin getConferenceAdmin(String email) {
        String selection = ConferenceAdminTable.Columns.EMAIL + " = ?";
        String[] selectionArgs = {email};
        return getConferenceAdmin(selection, selectionArgs);
    }

    private ConferenceAdmin getConferenceAdmin(String selection, String[] selectionArgs) {
        Cursor cursor = mDatabase.query(ConferenceAdminTable.NAME, null, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        return getConferenceAdmin(cursor);
    }

    private ConferenceAdmin getConferenceAdmin(Cursor cursor) {
        if (cursor.getCount() == 0 || cursor.isAfterLast()) {
            return null;
        }
        ConferenceAdmin conferenceAdmin = new ConferenceAdmin();
        conferenceAdmin.setId(cursor.getLong(cursor.getColumnIndex(ConferenceAdminTable.Columns.ID)));
        conferenceAdmin.setEmail(cursor.getString(cursor.getColumnIndex(ConferenceAdminTable.Columns.EMAIL)));
        return conferenceAdmin;
    }

    public void addConferenceAdmin(ConferenceAdmin conferenceAdmin) {
        long id = mDatabase.insert(ConferenceAdminTable.NAME, null, contentValuesFor(conferenceAdmin));
        conferenceAdmin.setId(id);
    }

    private ContentValues contentValuesFor(ConferenceAdmin conferenceAdmin) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConferenceAdminTable.Columns.EMAIL, conferenceAdmin.getEmail());
        return contentValues;
    }


}
