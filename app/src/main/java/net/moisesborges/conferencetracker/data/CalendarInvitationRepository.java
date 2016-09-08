package net.moisesborges.conferencetracker.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import net.moisesborges.conferencetracker.db.ConferenceDatabase;
import net.moisesborges.conferencetracker.db.scheme.InvitationResponseTable;
import net.moisesborges.conferencetracker.db.scheme.InvitationTable;
import net.moisesborges.conferencetracker.model.CalendarInvitation;
import net.moisesborges.conferencetracker.model.CalendarInvitationResponse;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.InvitationViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moisés on 18/08/2016.
 */

public class CalendarInvitationRepository {

    private static CalendarInvitationRepository sInstance;
    private SQLiteDatabase mDatabase;

    private CalendarInvitationRepository() {
        mDatabase = ConferenceDatabase.getInstance()
                .getWritableDatabase();
    }

    public static CalendarInvitationRepository getInstance() {
        if (sInstance == null) {
            sInstance = new CalendarInvitationRepository();
        }
        return sInstance;
    }

    public void addCalendarInvitation(@NonNull CalendarInvitation calendarInvitation) {
        long id = mDatabase.insert(InvitationTable.NAME, null, contentValuesFor(calendarInvitation));
        calendarInvitation.setId(id);
    }

    private ContentValues contentValuesFor(CalendarInvitation calendarInvitation) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(InvitationTable.Columns.CONFERENCE_ID, calendarInvitation.getConferenceId());
        contentValues.put(InvitationTable.Columns.MESSAGE, calendarInvitation.getMessage());
        return contentValues;
    }

    public void getNotAnsweredCalendarInvitationsAsync(final long doctorId, final OnGetCalendarInvitationsCallback callback) {
        AsyncTask<Void, Void, List<CalendarInvitation>> asyncTask = new AsyncTask<Void, Void, List<CalendarInvitation>>() {
            @Override
            protected List<CalendarInvitation> doInBackground(Void... params) {
                return getNotAnsweredCalendarInvitations(doctorId);
            }

            @Override
            protected void onPostExecute(List<CalendarInvitation> calendarInvitations) {
                super.onPostExecute(calendarInvitations);
                callback.onGetCalendarInvitations(calendarInvitations);
            }
        };

        asyncTask.execute();
    }

    public void getNotAnsweredInvitationsAsync(final long doctorId, final OnGetInvitationsCallback callback) {
        AsyncTask<Void, Void, List<InvitationViewModel>> asyncTask = new AsyncTask<Void, Void, List<InvitationViewModel>>() {
            @Override
            protected List<InvitationViewModel> doInBackground(Void... params) {
                List<CalendarInvitation> notAnsweredCalendarInvitations = getNotAnsweredCalendarInvitations(doctorId);
                List<InvitationViewModel> notAnsweredInvitation = new ArrayList<>();
                ConferenceRepository conferenceRepository = ConferenceRepository.getInstance();

                for (final CalendarInvitation calendarInvitation : notAnsweredCalendarInvitations) {
                    Conference conference = conferenceRepository.getConference(calendarInvitation.getConferenceId());
                    CalendarInvitationResponse response = getCalendarInvitationResponse(calendarInvitation.getId(),doctorId);

                    InvitationViewModel invitation = new InvitationViewModel();
                    invitation.setResponse(response);
                    invitation.setCalendarInvitation(calendarInvitation);
                    invitation.setConference(conference);

                    notAnsweredInvitation.add(invitation);
                }
                return notAnsweredInvitation;
            }

            @Override
            protected void onPostExecute(List<InvitationViewModel> calendarInvitations) {
                super.onPostExecute(calendarInvitations);
                callback.onGetCalendarInvitations(calendarInvitations);
            }
        };

        asyncTask.execute();
    }

    public void addCalendarInvitationResponse(@NonNull CalendarInvitationResponse response) {
        long id = mDatabase.insert(InvitationResponseTable.NAME, null, contentValuesFor(response));
        response.setId(id);
    }

    private ContentValues contentValuesFor(CalendarInvitationResponse response) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(InvitationResponseTable.Columns.ACCEPTED, response.isAccepted());
        contentValues.put(InvitationResponseTable.Columns.INVITATION_ID, response.getInvitationId());
        contentValues.put(InvitationResponseTable.Columns.DOCTOR_ID, response.getDoctorId());
        return contentValues;
    }

    public List<CalendarInvitation> getNotAnsweredCalendarInvitations(long doctorId) {
        Cursor cursor = mDatabase.query(InvitationTable.NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        List<CalendarInvitation> notAnsweredCalendarInvitations = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            CalendarInvitation calendarInvitation = getCalendarInvitation(cursor);
            CalendarInvitationResponse calendarInvitationResponse = getCalendarInvitationResponse(calendarInvitation.getId(), doctorId);
            if (calendarInvitationResponse == null) {
                notAnsweredCalendarInvitations.add(calendarInvitation);
            }
            cursor.moveToNext();
        }
        return notAnsweredCalendarInvitations;
    }

    public CalendarInvitationResponse getCalendarInvitationResponse(long invitationId, long doctorId) {
        String conferenceSelection = InvitationResponseTable.Columns.INVITATION_ID + " = " + invitationId;
        String doctorSelection = InvitationResponseTable.Columns.DOCTOR_ID + " = " + doctorId;
        String selection = conferenceSelection + " and " + doctorSelection;
        Cursor cursor = mDatabase.query(InvitationResponseTable.NAME, null, selection, null, null, null, null);
        cursor.moveToFirst();
        return getCalendarInvitationResponse(cursor);
    }

    private CalendarInvitationResponse getCalendarInvitationResponse(Cursor cursor) {
        if (cursor.getCount() == 0 || cursor.isAfterLast()) {
            return null;
        }

        CalendarInvitationResponse calendarInvitationResponse = new CalendarInvitationResponse();
        calendarInvitationResponse.setId(cursor.getLong(cursor.getColumnIndex(InvitationResponseTable.Columns.ID)));
        calendarInvitationResponse.setInvitationId(cursor.getColumnIndex(InvitationResponseTable.Columns.INVITATION_ID));
        calendarInvitationResponse.setAccepted(cursor.getInt(cursor.getColumnIndex(InvitationResponseTable.Columns.ACCEPTED)) > 0);
        calendarInvitationResponse.setDoctorId(cursor.getLong(cursor.getColumnIndex(InvitationResponseTable.Columns.DOCTOR_ID)));
        return calendarInvitationResponse;
    }

    private CalendarInvitation getCalendarInvitation(Cursor cursor) {
        if (cursor.getCount() == 0 || cursor.isAfterLast()) {
            return null;
        }
        CalendarInvitation calendarInvitation = new CalendarInvitation();
        calendarInvitation.setId(cursor.getLong(cursor.getColumnIndex(InvitationTable.Columns.ID)));
        calendarInvitation.setMessage(cursor.getString(cursor.getColumnIndex(InvitationTable.Columns.MESSAGE)));
        calendarInvitation.setConferenceId(cursor.getLong(cursor.getColumnIndex(InvitationTable.Columns.CONFERENCE_ID)));
        return calendarInvitation;
    }

    public interface OnGetInvitationsCallback {
        void onGetCalendarInvitations(List<InvitationViewModel> Invitations);
    }

    public interface OnGetCalendarInvitationsCallback {
        void onGetCalendarInvitations(List<CalendarInvitation> calendarInvitations);
    }

    public interface OnGetCalendarInvitationResponsesCallback {
        void onGetCalendarInvitationResponses(List<CalendarInvitationResponse> calendarInvitationResponses);
    }
}
