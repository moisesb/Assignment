package net.moisesborges.conferencetracker.db.scheme;

/**
 * Created by Moisés on 19/08/2016.
 */

public class ConferenceScheduleTable {
    public static final String NAME = "conference_schedule";

    public static class Columns {
        public static final String ID = "_id";
        public static final String DOCTOR_ID = "doctor_id";
        public static final String CONFERENCE_ID = "conference_id";
    }

    public static class Sql {
        public static final String CREATE_TABLE = "create table if not exists " + NAME + " (" +
                Columns.ID + " integer primary key autoincrement," +
                Columns.DOCTOR_ID + "," +
                Columns.CONFERENCE_ID + "," +
                "foreign key (" + Columns.DOCTOR_ID + ") references " + DoctorTable.NAME + "(" + DoctorTable.Columns.ID + ") , " +
                "foreign key (" + Columns.CONFERENCE_ID + ") references " + ConferenceTable.NAME + "(" + ConferenceTable.Columns.ID + ") on delete cascade)";

        public static final String DROP_TABLE = "drop table if exists " + NAME;
    }
}
