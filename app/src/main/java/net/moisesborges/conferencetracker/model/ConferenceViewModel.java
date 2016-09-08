package net.moisesborges.conferencetracker.model;

/**
 * Created by Moisés on 20/08/2016.
 */

public class ConferenceViewModel {
    private final Conference conference;
    private final boolean hasPermitionToEdit;

    public ConferenceViewModel(Conference conference, boolean hasPermitionToEdit) {
        this.conference = conference;
        this.hasPermitionToEdit = hasPermitionToEdit;
    }

    public Conference getConference() {
        return conference;
    }

    public boolean hasPermitionToEdit() {
        return hasPermitionToEdit;
    }
}
