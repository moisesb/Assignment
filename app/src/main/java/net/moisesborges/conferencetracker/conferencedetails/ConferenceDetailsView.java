package net.moisesborges.conferencetracker.conferencedetails;

import net.moisesborges.conferencetracker.mvp.ViewBase;

/**
 * Created by Moisés on 18/08/2016.
 */

public interface ConferenceDetailsView extends ViewBase {
    void allowAddToCalendar(boolean allowed);

    void allowInviteDoctors(boolean allowed);

    void setConferenceTitle(String title);

    void setConferenceVenue(String venue);

    void setConferencePlace(String place);

    void invitationSentAlert();

    void scheduleMadeAlert();

    void showRemoveScheduleIcon();

    void showAddScheduleIcon();

    void scheduleRemovedAlert();
}
