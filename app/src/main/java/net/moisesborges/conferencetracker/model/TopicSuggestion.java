package net.moisesborges.conferencetracker.model;

/**
 * Created by Moisés on 18/08/2016.
 */

public class TopicSuggestion {
    private long id;
    private String suggestion;
    private long doctorId;
    private long conferenceId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public long getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(long conferenceId) {
        this.conferenceId = conferenceId;
    }
}
