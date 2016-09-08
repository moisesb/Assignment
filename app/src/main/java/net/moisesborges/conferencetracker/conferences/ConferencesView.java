package net.moisesborges.conferencetracker.conferences;

import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceViewModel;
import net.moisesborges.conferencetracker.mvp.ViewBase;

import java.util.List;

/**
 * Created by Moisés on 18/08/2016.
 */

public interface ConferencesView extends ViewBase {
    void showAddConferenceOption(boolean show);

    void setProgressIndicator(boolean loading);

    void showConferences(List<ConferenceViewModel> conferences);

    void showAddConferenceMessage();

    void showNoUpcomingConferencesMessage();

    void openConferenceDetails(long id);

    void openNewConference();
}
