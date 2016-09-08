package net.moisesborges.conferencetracker.db.scheme;

/**
 * Created by Moisés on 19/08/2016.
 */

public class InvitationResponseTable {
    public static final String NAME = "invitation_response";

    public static class Columns {
        public static final String ID = "_id";
        public static final String INVITATION_ID = "invitation_id";
        public static final String DOCTOR_ID = "doctor_id";
        public static final String ACCEPTED = "accepted";
    }

    public static class Sql {
        public static final String CREATE_TABLE = "create table if not exists " + NAME + " (" +
                Columns.ID + " integer primary key autoincrement," +
                Columns.INVITATION_ID + "," +
                Columns.ACCEPTED + "," +
                Columns.DOCTOR_ID + "," +
                "foreign key (" + Columns.DOCTOR_ID + ") references " + DoctorTable.NAME + "(" + DoctorTable.Columns.ID + ")," +
                "foreign key (" + Columns.INVITATION_ID + ") references " + InvitationTable.NAME + "(" + InvitationTable.Columns.ID + ") on delete cascade)";

        public static final String DROP_TABLE = "drop table if exists " + NAME;
    }
}
