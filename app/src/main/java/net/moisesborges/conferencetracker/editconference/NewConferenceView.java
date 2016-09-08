package net.moisesborges.conferencetracker.editconference;

import net.moisesborges.conferencetracker.mvp.ViewBase;

/**
 * Created by Moisés on 18/08/2016.
 */

public interface NewConferenceView extends ViewBase {
    void showInvalidNameMessage();

    void showInvalidPlaceMessage();

    void showInvalidVenueMessage();

    void showInvalidStartDateMessage();

    void showInvalidNumberOfDaysMessage();

    void showInvalidSpeciality();

    void close();
}
