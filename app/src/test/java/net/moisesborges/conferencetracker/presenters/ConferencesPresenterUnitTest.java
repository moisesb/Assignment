package net.moisesborges.conferencetracker.presenters;

import net.moisesborges.conferencetracker.data.ConferenceRepository;
import net.moisesborges.conferencetracker.conferences.ConferencesPresenter;
import net.moisesborges.conferencetracker.conferences.ConferencesView;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceViewModel;
import net.moisesborges.conferencetracker.services.LoginService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Moisés on 18/08/2016.
 */

public class ConferencesPresenterUnitTest {

    @Mock
    private ConferencesView mConferencesView;
    @Mock
    private LoginService mLoginService;
    @Mock
    private ConferenceRepository mConferenceRepository;
    @Captor
    private ArgumentCaptor<ConferenceRepository.OnGetConferencesCallback> mCallbackArgumentCaptor;
    private ConferencesPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPresenter = new ConferencesPresenter(mConferenceRepository,mLoginService);
        mPresenter.bindView(mConferencesView);
    }

    @After
    public void tearDown() throws Exception {
        mPresenter.unbindView();
    }

    @Test
    public void showAddConfForAdmin() {
        logAsAdmin(true);
        mPresenter.showAvailableFeatures();
        verify(mConferencesView).showAddConferenceOption(true);
    }

    @Test
    public void notShowAddConfForDoctor() throws Exception {
        when(mLoginService.isLoggedAsAdmin()).thenReturn(false);
        mPresenter.showAvailableFeatures();
        verify(mConferencesView,never()).showAddConferenceOption(true);
    }


    @Test
    public void loadNoConferencesForAdmins() throws Exception {
        List<Conference> emptyConferenceLists = new ArrayList<>();
        logAsAdmin(true);
        mPresenter.loadConferences(-1);

        verify(mConferenceRepository).getUpcomingConferences(mCallbackArgumentCaptor.capture());

        mCallbackArgumentCaptor.getValue().onGetConferences(emptyConferenceLists);

        InOrder inOrder = Mockito.inOrder(mConferencesView);
        inOrder.verify(mConferencesView).setProgressIndicator(true);
        inOrder.verify(mConferencesView).setProgressIndicator(false);
        verify(mConferencesView).showAddConferenceMessage();
    }

    private void logAsAdmin(boolean isAdmin) {
        when(mLoginService.isLoggedAsAdmin()).thenReturn(isAdmin);
    }

    @Test
    public void loadNoConferencesForDoctors() throws Exception {
        List<Conference> emptyConferenceLists = new ArrayList<>();
        logAsAdmin(false);
        mPresenter.loadConferences(-1);

        verify(mConferenceRepository).getUpcomingConferences(mCallbackArgumentCaptor.capture());

        mCallbackArgumentCaptor.getValue().onGetConferences(emptyConferenceLists);

        InOrder inOrder = Mockito.inOrder(mConferencesView);
        inOrder.verify(mConferencesView).setProgressIndicator(true);
        inOrder.verify(mConferencesView).setProgressIndicator(false);
        verify(mConferencesView).showNoUpcomingConferencesMessage();
    }

    @Test
    public void openConference() throws Exception {
        Conference conference = new Conference();
        conference.setId(100);

        mPresenter.openConference(conference);
        verify(mConferencesView).openConferenceDetails(conference.getId());
    }

    @Test
    public void addConferenceForAdmins() throws Exception {
        logAsAdmin(true);
        mPresenter.addConference();

        verify(mConferencesView).openNewConference();
    }

    @Test
    public void addConferenceForDoctors() throws Exception {
        logAsAdmin(false);
        mPresenter.addConference();
        verifyNoMoreInteractions(mConferencesView);
    }
}
