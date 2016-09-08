package net.moisesborges.conferencetracker.data;

import android.test.AndroidTestCase;

import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceAdmin;
import net.moisesborges.conferencetracker.model.Doctor;
import net.moisesborges.conferencetracker.model.TopicSuggestion;
import net.moisesborges.conferencetracker.utils.DbHelper;
import net.moisesborges.conferencetracker.utils.TestHelper;

import java.util.List;

/**
 * Created by Moisés on 19/08/2016.
 */

public class SuggestionRepositoryTest extends AndroidTestCase {

    private SuggestionRepository mSuggestionRepository;
    private Doctor mDoctor;
    private Conference mConference;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        DbHelper.initDbAndBeginTransaction(mContext);
        mSuggestionRepository = SuggestionRepository.getInstance();

        ConferenceAdmin conferenceAdmin = TestHelper.createConferenceAdmin("admin_suggestion@admin.com");
        UserRepository.getInstance()
                .addConferenceAdmin(conferenceAdmin);

        mConference = TestHelper.createConference(conferenceAdmin, "test conference with suggestion");
        mConference.setAdministratorId(mConference.getId());
        ConferenceRepository.getInstance()
                .addConference(mConference);

        mDoctor = TestHelper.createDoctor("doctor_suggestion@doctor.com");
        UserRepository.getInstance()
                .addDoctor(mDoctor);

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        DbHelper.endTransaction();
    }

    public void testAddSuggestion() throws Exception {
        TopicSuggestion topicSuggestion = TestHelper.createTopicSuggestion("first suggestion", mConference, mDoctor);

        mSuggestionRepository.addTopicSuggestion(topicSuggestion);

        assertNotSame(0,topicSuggestion.getId());
        List<TopicSuggestion> topicSuggestionsForConference = mSuggestionRepository.getTopicSuggestionsForConference(mConference.getId());

        assertNotNull(topicSuggestionsForConference);
        assertEquals(1,topicSuggestionsForConference.size());
        TopicSuggestion suggestion = topicSuggestionsForConference.get(0);
        assertEquals(topicSuggestion.getId(),suggestion.getId());
        assertEquals(topicSuggestion.getConferenceId(),suggestion.getConferenceId());
        assertEquals(topicSuggestion.getDoctorId(),suggestion.getDoctorId());
        assertEquals(topicSuggestion.getSuggestion(),suggestion.getSuggestion());
    }
}
