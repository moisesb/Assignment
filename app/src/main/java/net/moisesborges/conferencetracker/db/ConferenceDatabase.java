package net.moisesborges.conferencetracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import net.moisesborges.conferencetracker.db.scheme.ConferenceAdminTable;
import net.moisesborges.conferencetracker.db.scheme.ConferenceScheduleTable;
import net.moisesborges.conferencetracker.db.scheme.ConferenceTable;
import net.moisesborges.conferencetracker.db.scheme.DoctorTable;
import net.moisesborges.conferencetracker.db.scheme.InvitationResponseTable;
import net.moisesborges.conferencetracker.db.scheme.InvitationTable;
import net.moisesborges.conferencetracker.db.scheme.SuggestionTable;

/**
 * Created by Moisés on 19/08/2016.
 */

public class ConferenceDatabase extends SQLiteOpenHelper {


    public static final String CONFERENCE_DB = "conference.db";
    public static final String NAME = CONFERENCE_DB;
    public static final int VERSION = 6;

    private static ConferenceDatabase sInstance;

    private ConferenceDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    public static void init(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new ConferenceDatabase(context);
        }
    }

    public static ConferenceDatabase getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("should initialize the database first");
        }
        return sInstance;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DoctorTable.Sql.CREATE_TABLE);
        db.execSQL(ConferenceAdminTable.Sql.CREATE_TABLE);
        db.execSQL(ConferenceTable.Sql.CREATE_TABLE);
        db.execSQL(SuggestionTable.Sql.CREATE_TABLE);
        db.execSQL(InvitationTable.Sql.CREATE_TABLE);
        db.execSQL(InvitationResponseTable.Sql.CREATE_TABLE);
        db.execSQL(ConferenceScheduleTable.Sql.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(InvitationResponseTable.Sql.DROP_TABLE);
        db.execSQL(InvitationTable.Sql.DROP_TABLE);
        db.execSQL(ConferenceScheduleTable.Sql.DROP_TABLE);
        db.execSQL(SuggestionTable.Sql.DROP_TABLE);
        db.execSQL(ConferenceTable.Sql.DROP_TABLE);
        db.execSQL(DoctorTable.Sql.DROP_TABLE);
        db.execSQL(ConferenceAdminTable.Sql.DROP_TABLE);
        onCreate(db);
    }
}
