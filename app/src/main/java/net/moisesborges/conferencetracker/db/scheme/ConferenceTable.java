package net.moisesborges.conferencetracker.db.scheme;

/**
 * Created by Moisés on 19/08/2016.
 */

public class ConferenceTable {

    public static final String NAME = "conference";

    public static class Columns {
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String VENUE = "venue";
        public static final String PLACE = "place";
        public static final String START_DATE = "start_date";
        public static final String DURATION = "duration";
        public static final String SPECIALITY = "speciality";
        public static final String ADMIN_ID = "admin_id";
    }

    public static class Sql {
        public static final String CREATE_TABLE = "create table if not exists " + NAME + " (" +
                Columns.ID + " integer primary key autoincrement," +
                Columns.TITLE + "," +
                Columns.VENUE + "," +
                Columns.PLACE + "," +
                Columns.ADMIN_ID + "," +
                Columns.START_DATE + "," +
                Columns.DURATION + "," +
                Columns.SPECIALITY + "," +
                "foreign key (" + Columns.ADMIN_ID + ") references " + ConferenceAdminTable.NAME + "(" + ConferenceAdminTable.Columns.ID + ") )";

        public static final String DROP_TABLE = "drop table if exists " + NAME;
    }
}
