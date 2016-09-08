package net.moisesborges.conferencetracker.conferences;

import android.support.annotation.NonNull;

import net.moisesborges.conferencetracker.data.ConferenceRepository;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceViewModel;
import net.moisesborges.conferencetracker.mvp.PresenterBase;
import net.moisesborges.conferencetracker.services.LoginService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moisés on 18/08/2016.
 */

public class ConferencesPresenter implements PresenterBase<ConferencesView> {


    public static final int NO_DOCTOR_ID_VALUE = -1;
    private final LoginService mLoginService;
    private final ConferenceRepository mConferenceRepository;
    private ConferencesView mView;

    public ConferencesPresenter(@NonNull ConferenceRepository conferenceRepository,
                                @NonNull LoginService loginService) {
        mConferenceRepository = conferenceRepository;
        mLoginService = loginService;
    }

    public void showAvailableFeatures() {
        if (mView == null) {
            return;
        }

        mView.showAddConferenceOption(mLoginService.isLoggedAsAdmin());
    }

    public void loadConferences(long doctorId) {
        if (mView == null) {
            return;
        }

        mView.setProgressIndicator(true);

        ConferenceRepository.OnGetConferencesCallback callback = new ConferenceRepository.OnGetConferencesCallback() {
            @Override
            public void onGetConferences(List<Conference> conferences) {
                mView.setProgressIndicator(false);
                if (conferences.size() > 0) {
                    List<ConferenceViewModel> conferenceViewModels = new ArrayList<>();
                    boolean loggedAsAdmin = mLoginService.isLoggedAsAdmin();
                    for (final Conference conference : conferences) {
                        conferenceViewModels.add(new ConferenceViewModel(conference, loggedAsAdmin));
                    }
                    mView.showConferences(conferenceViewModels);
                } else {
                    if (mLoginService.isLoggedAsAdmin()) {
                        mView.showAddConferenceMessage();
                    } else {
                        mView.showNoUpcomingConferencesMessage();
                    }
                }
            }
        };

        if (doctorId == NO_DOCTOR_ID_VALUE) {
            mConferenceRepository.getUpcomingConferences(callback);
        }else {
            mConferenceRepository.getScheduledConferences(doctorId,callback);
        }
    }

    public void openConference(Conference conference) {
        if (mView == null) {
            return;
        }

        mView.openConferenceDetails(conference.getId());
    }

    @Override
    public void bindView(ConferencesView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    public void addConference() {
        if (mView == null) {
            return;
        }

        if (mLoginService.isLoggedAsAdmin()) {
            mView.openNewConference();
        }
    }
}
