package net.moisesborges.conferencetracker.conferencedetails;

import android.support.annotation.NonNull;

import net.moisesborges.conferencetracker.data.CalendarInvitationRepository;
import net.moisesborges.conferencetracker.data.ConferenceRepository;
import net.moisesborges.conferencetracker.data.ScheduleRepository;
import net.moisesborges.conferencetracker.data.SuggestionRepository;
import net.moisesborges.conferencetracker.model.CalendarInvitation;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceSchedule;
import net.moisesborges.conferencetracker.model.TopicSuggestion;
import net.moisesborges.conferencetracker.mvp.PresenterBase;
import net.moisesborges.conferencetracker.services.LoginService;

import java.util.List;

/**
 * Created by Moisés on 18/08/2016.
 */

public class ConferenceDetailsPresenter implements PresenterBase<ConferenceDetailsView> {


    private ConferenceDetailsView mView;
    private final CalendarInvitationRepository mCalendarInvitationRepository;
    private final ScheduleRepository mScheduleRepository;
    private final ConferenceRepository mConferenceRepository;
    private final LoginService mLoginService;

    public ConferenceDetailsPresenter(@NonNull ConferenceRepository conferenceRepository,
                                      @NonNull CalendarInvitationRepository calendarInvitationRepository,
                                      @NonNull ScheduleRepository scheduleRepository,
                                      @NonNull LoginService loginService){
        mConferenceRepository = conferenceRepository;
        mScheduleRepository = scheduleRepository;
        mCalendarInvitationRepository = calendarInvitationRepository;
        mLoginService = loginService;
    }

    @Override
    public void bindView(ConferenceDetailsView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    public void showAvailableFeatures(long conferenceId) {
        if (mView == null) {
            return;
        }

        boolean isAdmin = mLoginService.isLoggedAsAdmin();
        mView.allowInviteDoctors(isAdmin);
        mView.allowAddToCalendar(!isAdmin);

        if (!isAdmin) {
            long doctorId = mLoginService.loggedDoctorId();
            ConferenceSchedule schedule = mScheduleRepository.getSchedule(doctorId, conferenceId);
            if (schedule == null) {
                mView.showAddScheduleIcon();
            }else {
                mView.showRemoveScheduleIcon();
            }
        }
    }

    public void loadConferenceInfo(final long conferenceId) {
        if (mView == null) {
            return;
        }

        mConferenceRepository.getConferenceAsync(conferenceId, new ConferenceRepository.OnGetConferenceCallback() {
            @Override
            public void onGetConference(Conference conference) {
                mView.setConferenceTitle(conference.getTitle());
                mView.setConferenceVenue(conference.getVenue());
                mView.setConferencePlace(conference.getPlace());
            }

            @Override
            public void onConferenceNotFound() {
                throw new IllegalArgumentException("there is not conference with id " + conferenceId);
            }
        });
    }



    public void sendCalendarInvitation(long conferenceId) {
        if (mView == null) {
            return;
        }

        CalendarInvitation calendarInvitation = new CalendarInvitation();
        calendarInvitation.setConferenceId(conferenceId);

        mCalendarInvitationRepository.addCalendarInvitation(calendarInvitation);

        mView.invitationSentAlert();
    }

    public void addToCalendar(long conferenceId) {
        if (mView == null) {
            return;
        }

        if (!mLoginService.isLoggedAsAdmin()) {
            long doctorId = mLoginService.loggedDoctorId();
            ConferenceSchedule schedule = mScheduleRepository.getSchedule(doctorId, conferenceId);
            if (schedule == null) {
                ConferenceSchedule conferenceSchedule = new ConferenceSchedule();
                conferenceSchedule.setDoctorId(doctorId);
                conferenceSchedule.setConferenceId(conferenceId);
                mScheduleRepository.addSchedule(conferenceSchedule);
                mView.showRemoveScheduleIcon();
                mView.scheduleMadeAlert();
            }else {
                mScheduleRepository.deleteSchedule(doctorId, conferenceId);
                mView.showAddScheduleIcon();
                mView.scheduleRemovedAlert();
            }
        }
    }
}
