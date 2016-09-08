package net.moisesborges.conferencetracker.utils;

import android.support.annotation.NonNull;

import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceAdmin;
import net.moisesborges.conferencetracker.model.Doctor;
import net.moisesborges.conferencetracker.model.TopicSuggestion;

/**
 * Created by Moisés on 19/08/2016.
 */

public class TestHelper {

    @NonNull
    public static ConferenceAdmin createConferenceAdmin(String email) {
        ConferenceAdmin conferenceAdmin = new ConferenceAdmin();
        conferenceAdmin.setEmail(email);
        return conferenceAdmin;
    }

    @NonNull
    public static Conference createConference(ConferenceAdmin conferenceAdmin, String conferenceTitle) {
        final Conference conference = new Conference();
        conference.setTitle(conferenceTitle);
        conference.setVenue("anywhere");
        conference.setAdministratorId(conferenceAdmin.getId());
        conference.setPlace("Teste place");
        conference.setSpeciality("Test speciality");
        conference.setNumOfDays(5);
        conference.setStartDate(DateUtils.getCurrentTimeInMillis());
        return conference;
    }

    @NonNull
    public static Doctor createDoctor(String email) {
        Doctor doctor = new Doctor();
        doctor.setEmail(email);
        return doctor;
    }

    @NonNull
    public static TopicSuggestion createTopicSuggestion(String message, Conference conference, Doctor doctor) {
        TopicSuggestion topicSuggestion = new TopicSuggestion();
        topicSuggestion.setSuggestion(message);
        topicSuggestion.setConferenceId(conference.getId());
        topicSuggestion.setDoctorId(doctor.getId());
        return topicSuggestion;
    }
}
