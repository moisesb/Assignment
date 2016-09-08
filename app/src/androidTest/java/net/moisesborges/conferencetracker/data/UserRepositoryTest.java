package net.moisesborges.conferencetracker.data;

import android.test.AndroidTestCase;

import net.moisesborges.conferencetracker.db.ConferenceDatabase;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceAdmin;
import net.moisesborges.conferencetracker.model.Doctor;
import net.moisesborges.conferencetracker.utils.DbHelper;

/**
 * Created by Moisés on 19/08/2016.
 */

public class UserRepositoryTest extends AndroidTestCase {

    private UserRepository mUserRepository;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DbHelper.initDbAndBeginTransaction(mContext);

        mUserRepository = UserRepository.getInstance();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        DbHelper.endTransaction();
    }

    public void testAddConferenceAdmin() throws Exception {
        ConferenceAdmin conferenceAdmin = new ConferenceAdmin();
        conferenceAdmin.setEmail("admin@admin.test.com");

        ConferenceAdmin conferenceAdminFromDb = mUserRepository.getConferenceAdmin(conferenceAdmin.getEmail());
        assertNull(conferenceAdminFromDb);

        mUserRepository.addConferenceAdmin(conferenceAdmin);

        conferenceAdminFromDb = mUserRepository.getConferenceAdmin(conferenceAdmin.getEmail());
        assertNotNull(conferenceAdminFromDb);
        assertEquals(conferenceAdmin.getEmail(),conferenceAdminFromDb.getEmail());
        assertNotSame(0, conferenceAdminFromDb.getId());

        assertEquals(conferenceAdmin.getId(), conferenceAdminFromDb.getId());

    }

    public void testAddDoctor() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setEmail("doctor@test.com");

        Doctor doctorFromDb = mUserRepository.getDoctor(doctor.getEmail());
        assertNull(doctorFromDb);

        mUserRepository.addDoctor(doctor);

        doctorFromDb = mUserRepository.getDoctor(doctor.getEmail());
        assertNotNull(doctorFromDb);
        assertEquals(doctor.getEmail(),doctorFromDb.getEmail());
        assertNotSame(0, doctorFromDb);

    }
}
