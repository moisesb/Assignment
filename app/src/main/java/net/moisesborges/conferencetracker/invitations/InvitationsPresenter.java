package net.moisesborges.conferencetracker.invitations;

import android.support.annotation.NonNull;

import net.moisesborges.conferencetracker.data.CalendarInvitationRepository;
import net.moisesborges.conferencetracker.data.ScheduleRepository;
import net.moisesborges.conferencetracker.model.CalendarInvitationResponse;
import net.moisesborges.conferencetracker.model.ConferenceSchedule;
import net.moisesborges.conferencetracker.model.InvitationViewModel;
import net.moisesborges.conferencetracker.mvp.PresenterBase;

import java.util.List;

/**
 * Created by Moisés on 19/08/2016.
 */

public class InvitationsPresenter implements PresenterBase<InvitationsView> {

    private InvitationsView mView;
    private final ScheduleRepository mScheduleRepository;
    private final CalendarInvitationRepository mCalendarInvitationRepository;

    public InvitationsPresenter(@NonNull ScheduleRepository scheduleRepository,
                                @NonNull CalendarInvitationRepository calendarInvitationRepository) {
        mScheduleRepository = scheduleRepository;
        mCalendarInvitationRepository = calendarInvitationRepository;
    }

    public void loadInvitations(long doctorid) {
        if (mView == null) {
            return;
        }

        mView.setProgressIndicator(true);
        mCalendarInvitationRepository.getNotAnsweredInvitationsAsync(doctorid,
                new CalendarInvitationRepository.OnGetInvitationsCallback() {
                    @Override
                    public void onGetCalendarInvitations(List<InvitationViewModel> invitations) {
                        mView.setProgressIndicator(false);
                        if (invitations.size() > 0) {
                            mView.showInvitations(invitations);
                        }else {
                            mView.showNoInvitationsMessage();
                        }
                    }
                });
    }

    public void respondToInvitation(InvitationViewModel invitation, boolean accepted, long doctorId) {
        if (mView == null) {
            return;
        }

        CalendarInvitationResponse response = new CalendarInvitationResponse();
        response.setDoctorId(doctorId);
        response.setInvitationId(invitation.getCalendarInvitation().getId());
        response.setAccepted(accepted);

        mCalendarInvitationRepository.addCalendarInvitationResponse(response);

        invitation.setResponse(response);

        if (accepted) {
            ConferenceSchedule conferenceSchedule = new ConferenceSchedule();
            conferenceSchedule.setConferenceId(invitation.getCalendarInvitation().getConferenceId());
            conferenceSchedule.setDoctorId(doctorId);
            mScheduleRepository.addSchedule(conferenceSchedule);
        }

        mView.updateInvitation(invitation);
    }

    @Override
    public void bindView(InvitationsView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
