package net.moisesborges.conferencetracker.editconference;

import android.support.annotation.NonNull;

import net.moisesborges.conferencetracker.data.ConferenceRepository;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.mvp.PresenterBase;
import net.moisesborges.conferencetracker.services.LoginService;

/**
 * Created by Moisés on 18/08/2016.
 */

public class EditConferencePresenter implements PresenterBase<NewConferenceView> {

    private final LoginService mLoginService;
    private final ConferenceRepository mConferenceRepository;
    private NewConferenceView mView;

    public EditConferencePresenter(@NonNull ConferenceRepository conferenceRepository, @NonNull LoginService loginService) {
        mLoginService = loginService;
        mConferenceRepository = conferenceRepository;
    }

    public void addNewConference(String name,
                                 String venue,
                                 String place,
                                 long startDate,
                                 String numOfDays,
                                 String speciality) {
        if (mView == null) {
            return;
        }
        int numberOfConferenceDays = 0;
        if (numOfDays.isEmpty()) {
            mView.showInvalidNumberOfDaysMessage();
            return;
        }else {
            numberOfConferenceDays = Integer.valueOf(numOfDays);
        }

        long adminId = mLoginService.loggedAdminId();

        Conference conference = new Conference(name, place, venue, startDate, numberOfConferenceDays, speciality);
        conference.setAdministratorId(adminId);

        if (!conference.isNameValid()) {
            mView.showInvalidNameMessage();
            return;
        }

        if (!conference.isVenueValid()) {
            mView.showInvalidVenueMessage();
            return;
        }

        if (!conference.isPlaceValid()) {
            mView.showInvalidPlaceMessage();
            return;
        }

        if (!conference.isStartDateValid()) {
            mView.showInvalidStartDateMessage();
            return;
        }

        if(!conference.isNumOfDaysValid()) {
            mView.showInvalidNumberOfDaysMessage();
            return;
        }

        if (!conference.isSpecialityValid()) {
            mView.showInvalidSpeciality();
            return;
        }

        mConferenceRepository.addConference(conference);
        mView.close();
    }

    @Override
    public void bindView(NewConferenceView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
