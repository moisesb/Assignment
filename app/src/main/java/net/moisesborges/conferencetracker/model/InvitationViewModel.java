package net.moisesborges.conferencetracker.model;

/**
 * Created by Moisés on 19/08/2016.
 */

public class InvitationViewModel {
    private CalendarInvitation calendarInvitation;
    private Conference conference;
    private CalendarInvitationResponse response;

    public InvitationViewModel() {
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public CalendarInvitation getCalendarInvitation() {
        return calendarInvitation;
    }

    public void setCalendarInvitation(CalendarInvitation calendarInvitation) {
        this.calendarInvitation = calendarInvitation;
    }

    public CalendarInvitationResponse getResponse() {
        return response;
    }

    public void setResponse(CalendarInvitationResponse response) {
        this.response = response;
    }
}
