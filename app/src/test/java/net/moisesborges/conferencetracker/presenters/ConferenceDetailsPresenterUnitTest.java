package net.moisesborges.conferencetracker.presenters;

import net.moisesborges.conferencetracker.conferencedetails.ConferenceDetailsPresenter;
import net.moisesborges.conferencetracker.conferencedetails.ConferenceDetailsView;
import net.moisesborges.conferencetracker.data.CalendarInvitationRepository;
import net.moisesborges.conferencetracker.data.ConferenceRepository;
import net.moisesborges.conferencetracker.data.ScheduleRepository;
import net.moisesborges.conferencetracker.data.SuggestionRepository;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.Speciality;
import net.moisesborges.conferencetracker.services.LoginService;
import net.moisesborges.conferencetracker.util.DateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Moisés on 18/08/2016.
 */

public class ConferenceDetailsPresenterUnitTest {

    @Mock
    private ConferenceRepository mConferenceRepository;
    @Mock
    private LoginService mLoginService;
    @Mock
    private ConferenceDetailsView mConferenceDetailsView;
    @Mock
    private CalendarInvitationRepository mCalendarInvitationRepository;
    @Mock
    private ScheduleRepository mScheduleRepository;

    @Captor
    private ArgumentCaptor<ConferenceRepository.OnGetConferenceCallback> mArgumentCaptor;

    private ConferenceDetailsPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mPresenter = new ConferenceDetailsPresenter(mConferenceRepository, mCalendarInvitationRepository,mScheduleRepository, mLoginService);
        mPresenter.bindView(mConferenceDetailsView);
    }

    @After
    public void tearDown() throws Exception {
        mPresenter.unbindView();
    }

    @Test
    public void showAvailableFeaturesForAdmins() throws Exception {
        logAsAdmin(true);

        mPresenter.showAvailableFeatures(-1);
        verify(mConferenceDetailsView).allowInviteDoctors(true);
        verify(mConferenceDetailsView).allowAddToCalendar(false);
    }

    @Test
    public void showAvailableFeaturesForDoctors() throws Exception {
        logAsAdmin(false);

        mPresenter.showAvailableFeatures(-1);
        verify(mConferenceDetailsView).allowInviteDoctors(false);
        verify(mConferenceDetailsView).allowAddToCalendar(true);
    }

    @Test
    public void loadConferenceDetails() throws Exception {
        long conferenceId = 100;
        Conference conference = new Conference("Test Conference",
                "Rio",
                "Copacabana Palace",
                DateUtils.dateInAdvance(1).getTime(),
                5,
                "Test Speciality");



        mPresenter.loadConferenceInfo(conferenceId);

        verify(mConferenceRepository).getConferenceAsync(eq(conferenceId), mArgumentCaptor.capture());

        mArgumentCaptor.getValue().onGetConference(conference);

        verify(mConferenceDetailsView).setConferenceTitle(conference.getTitle());
        verify(mConferenceDetailsView).setConferencePlace(conference.getPlace());
        verify(mConferenceDetailsView).setConferenceVenue(conference.getVenue());


    }

    private void logAsAdmin(boolean isAdmin) {
        when(mLoginService.isLoggedAsAdmin()).thenReturn(isAdmin);
    }
}
