package net.moisesborges.conferencetracker.model;

import net.moisesborges.conferencetracker.utils.DateUtils;

/**
 * Created by Moisés on 18/08/2016.
 */

public class Conference {
    private long id;
    private String title;
    private String place;
    private String venue;
    private long startDate;
    private int numOfDays;
    private String speciality;
    private long administratorId;

    public Conference() {
    }

    public Conference(String title, String place, String venue, long startDate, int numOfDays, String speciality) {
        this.title = title;
        this.place = place;
        this.venue = venue;
        this.startDate = startDate;
        this.numOfDays = numOfDays;
        this.speciality = speciality;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public int getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(int numOfDays) {
        this.numOfDays = numOfDays;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public long getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(long administratorId) {
        this.administratorId = administratorId;
    }

    public boolean isNameValid() {
        return title != null && !title.isEmpty();
    }

    public boolean isVenueValid() {
        return venue != null && !venue.isEmpty();
    }


    public boolean isPlaceValid() {
        return place != null && !place.isEmpty();
    }

    public boolean isStartDateValid() {
        return DateUtils.compareDates(startDate, System.currentTimeMillis()) == 1;
    }

    public boolean isNumOfDaysValid() {
        return numOfDays > 0;
    }

    public boolean isSpecialityValid() {
        return speciality != null && !speciality.isEmpty();
    }
}
