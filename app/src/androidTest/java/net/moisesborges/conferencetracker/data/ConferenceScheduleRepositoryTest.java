package net.moisesborges.conferencetracker.data;

import android.test.AndroidTestCase;

import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceAdmin;
import net.moisesborges.conferencetracker.model.ConferenceSchedule;
import net.moisesborges.conferencetracker.model.Doctor;
import net.moisesborges.conferencetracker.utils.DbHelper;
import net.moisesborges.conferencetracker.utils.TestHelper;

import java.util.List;

/**
 * Created by Moisés on 19/08/2016.
 */

public class ConferenceScheduleRepositoryTest extends AndroidTestCase {

    private ScheduleRepository mScheduleRepository;
    private Doctor mDoctor;
    private Conference mConference;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        DbHelper.initDbAndBeginTransaction(mContext);
        mScheduleRepository = ScheduleRepository.getInstance();

        ConferenceAdmin conferenceAdmin = TestHelper.createConferenceAdmin("admin_schedule@admin.com");
        UserRepository.getInstance()
                .addConferenceAdmin(conferenceAdmin);

        mConference = TestHelper.createConference(conferenceAdmin, "test conference with schedule");
        mConference.setAdministratorId(mConference.getId());
        ConferenceRepository.getInstance()
                .addConference(mConference);

        mDoctor = TestHelper.createDoctor("doctor_schedule@doctor.com");
        UserRepository.getInstance()
                .addDoctor(mDoctor);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        DbHelper.endTransaction();
    }

    public void testAddNewSchedule() throws Exception {
        ConferenceSchedule conferenceSchedule = new ConferenceSchedule();
        conferenceSchedule.setConferenceId(mConference.getId());
        conferenceSchedule.setDoctorId(mDoctor.getId());

        mScheduleRepository.addSchedule(conferenceSchedule);
        assertNotSame(0, conferenceSchedule.getId());

        List<ConferenceSchedule> conferenceSchedulesFromDb = mScheduleRepository.getSchedulesForDoctor(mDoctor.getId());
        assertNotNull(conferenceSchedulesFromDb);
        assertEquals(1, conferenceSchedulesFromDb.size());

        ConferenceSchedule schedule = conferenceSchedulesFromDb.get(0);
        assertEquals(conferenceSchedule.getId(), schedule.getId());
        assertEquals(conferenceSchedule.getConferenceId(), schedule.getConferenceId());
        assertEquals(conferenceSchedule.getDoctorId(), schedule.getDoctorId());
    }

}
