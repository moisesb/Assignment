package net.moisesborges.conferencetracker.model;

/**
 * Created by Moisés on 18/08/2016.
 */

public class Speciality {
    private long id;
    private String name;

    public Speciality() {
    }

    public Speciality(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
