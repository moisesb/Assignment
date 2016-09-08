package net.moisesborges.conferencetracker.db.scheme;

/**
 * Created by Moisés on 19/08/2016.
 */

public class DoctorTable {
    public static final String NAME = "doctor";
    public static final String DOT = ".";

    public static class Columns {
        public static final String ID = "_id";
        public static final String EMAIL = "email";
    }

    public static class Sql {
        public static final String CREATE_TABLE = "create table if not exists " + NAME + " (" +
                Columns.ID + " integer primary key autoincrement," +
                Columns.EMAIL + " unique )";

        public static final String DROP_TABLE = "drop table if exists " + NAME;
    }
}
