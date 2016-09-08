package net.moisesborges.conferencetracker.model;

/**
 * Created by Moisés on 18/08/2016.
 */

public class CalendarInvitation {
    private long id;
    private long conferenceId;
    private String message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(long conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
