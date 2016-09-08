package net.moisesborges.conferencetracker.data;

import android.test.AndroidTestCase;

import net.moisesborges.conferencetracker.model.CalendarInvitation;
import net.moisesborges.conferencetracker.model.CalendarInvitationResponse;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceAdmin;
import net.moisesborges.conferencetracker.model.Doctor;
import net.moisesborges.conferencetracker.utils.DbHelper;
import net.moisesborges.conferencetracker.utils.TestHelper;

import java.util.List;

/**
 * Created by Moisés on 19/08/2016.
 */

public class CalendarInvitationRepositoryTest extends AndroidTestCase {

    private CalendarInvitationRepository mCalendarInvitationRepository;
    private Doctor mDoctor;
    private Conference mConference;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        DbHelper.initDbAndBeginTransaction(mContext);
        mCalendarInvitationRepository = CalendarInvitationRepository.getInstance();

        ConferenceAdmin conferenceAdmin = TestHelper.createConferenceAdmin("admin_invitation@admin.com");
        UserRepository.getInstance()
                .addConferenceAdmin(conferenceAdmin);

        mConference = TestHelper.createConference(conferenceAdmin, "test conference with invitation");
        mConference.setAdministratorId(mConference.getId());
        ConferenceRepository.getInstance()
                .addConference(mConference);

        mDoctor = TestHelper.createDoctor("doctor_invitation@doctor.com");
        UserRepository.getInstance()
                .addDoctor(mDoctor);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        DbHelper.endTransaction();
    }

    public void testAddCalendarInvitation() throws Exception {
        CalendarInvitation calendarInvitation = new CalendarInvitation();
        calendarInvitation.setConferenceId(mConference.getId());
        calendarInvitation.setMessage("Hello");

        mCalendarInvitationRepository.addCalendarInvitation(calendarInvitation);
        assertNotSame(0, calendarInvitation.getId());

        List<CalendarInvitation> notAnsweredCalendarInvitations = mCalendarInvitationRepository.getNotAnsweredCalendarInvitations(mDoctor.getId());
        assertNotNull(notAnsweredCalendarInvitations);
        assertEquals(1,notAnsweredCalendarInvitations.size());

        CalendarInvitation invitation = notAnsweredCalendarInvitations.get(0);
        assertEquals(calendarInvitation.getId(), invitation.getId());
        assertEquals(calendarInvitation.getConferenceId(), invitation.getConferenceId());
        assertEquals(calendarInvitation.getMessage(), invitation.getMessage());

        CalendarInvitationResponse invitationResponse = new CalendarInvitationResponse();
        invitationResponse.setDoctorId(mDoctor.getId());
        invitationResponse.setAccepted(true);
        invitationResponse.setInvitationId(calendarInvitation.getId());

        mCalendarInvitationRepository.addCalendarInvitationResponse(invitationResponse);
        assertNotSame(0, invitationResponse.getId());

        CalendarInvitationResponse invitationResponseFromDb = mCalendarInvitationRepository.getCalendarInvitationResponse(invitationResponse.getId(), mDoctor.getId());
        assertNotNull(invitationResponseFromDb);
        assertEquals(invitationResponse.getId(), invitationResponseFromDb.getId());
        assertEquals(invitationResponse.getDoctorId(), invitationResponseFromDb.getDoctorId());
        assertEquals(invitationResponse.getInvitationId(), invitationResponseFromDb.getInvitationId());

        List<CalendarInvitation> notAnsweredCalendarInvitationsAfter = mCalendarInvitationRepository.getNotAnsweredCalendarInvitations(mDoctor.getId());
        assertNotNull(notAnsweredCalendarInvitationsAfter);
        assertEquals(0,notAnsweredCalendarInvitationsAfter.size());

    }
}
