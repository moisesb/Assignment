package net.moisesborges.conferencetracker.data;

import android.test.AndroidTestCase;

import net.moisesborges.conferencetracker.db.ConferenceDatabase;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceAdmin;
import net.moisesborges.conferencetracker.utils.DbHelper;
import net.moisesborges.conferencetracker.utils.TestHelper;

/**
 * Created by Moisés on 19/08/2016.
 */

public class ConferenceRepositoryTest extends AndroidTestCase {

    private ConferenceRepository mConferenceRepository;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        DbHelper.initDbAndBeginTransaction(mContext);

        mConferenceRepository = ConferenceRepository.getInstance();

    }



    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        DbHelper.endTransaction();
    }


    public void testAddConference() throws Exception {
        ConferenceAdmin conferenceAdmin = TestHelper.createConferenceAdmin("admin@admin.com");
        UserRepository.getInstance()
                .addConferenceAdmin(conferenceAdmin);
        final Conference conference = TestHelper.createConference(conferenceAdmin, "conferenceTitle");

        mConferenceRepository.addConference(conference);

        Conference conferenceFromDb = mConferenceRepository.getConference(conference.getId());
        assertNotNull(conferenceFromDb);
        assertEquals(conference.getId(), conferenceFromDb.getId());
        assertEquals(conference.getTitle(), conferenceFromDb.getTitle());
        assertEquals(conference.getVenue(), conferenceFromDb.getVenue());
        assertEquals(conference.getPlace(), conferenceFromDb.getPlace());
        assertEquals(conference.getSpeciality(), conferenceFromDb.getSpeciality());
        assertEquals(conference.getNumOfDays(), conferenceFromDb.getNumOfDays());
        assertEquals(conference.getStartDate(), conferenceFromDb.getStartDate());
        assertEquals(conference.getAdministratorId(), conferenceFromDb.getAdministratorId());

    }




    public void testUpcomingConferences() throws Exception {
        ConferenceAdmin conferenceAdmin = new ConferenceAdmin();
        conferenceAdmin.setEmail("admin2@admin.com");
        UserRepository.getInstance()
                .addConferenceAdmin(conferenceAdmin);

        final Conference conference = new Conference();
        conference.setTitle("Test conference");
        conference.setVenue("anywhere");
        conference.setAdministratorId(conference.getId());
        conference.setPlace("Teste place");

        final Conference conference2 = new Conference();
        conference2.setTitle("Test conference2");
        conference2.setVenue("anywhere");
        conference2.setAdministratorId(conference2.getId());
        conference2.setPlace("Teste place");

        mConferenceRepository.addConference(conference);
        mConferenceRepository.addConference(conference2);

    }
}
