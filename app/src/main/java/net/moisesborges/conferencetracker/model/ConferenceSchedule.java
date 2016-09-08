package net.moisesborges.conferencetracker.model;

/**
 * Created by Moisés on 19/08/2016.
 */

public class ConferenceSchedule {
    private long id;
    private long doctorId;
    private long conferenceId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
