package net.moisesborges.conferencetracker.services;

import android.content.Context;
import android.content.SharedPreferences;

import net.moisesborges.conferencetracker.data.UserRepository;
import net.moisesborges.conferencetracker.model.ConferenceAdmin;
import net.moisesborges.conferencetracker.model.Doctor;

/**
 * Created by Moisés on 18/08/2016.
 */

public class LoginService {

    public static final String LOGIN_SHARED_PREF_NAME = "LoginPref";
    public static final String LOGGED_AS_ADMIN = "loggedAsAdmin";
    public static final String ID_OF_LOGGED_USER = "loggedId";
    private final Context mContext;
    private final SharedPreferences mSharedPreferences;

    public LoginService(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(LOGIN_SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setLoggedAsAdmin(boolean loggedAsAdmin) {
        mSharedPreferences.edit()
                .putBoolean(LOGGED_AS_ADMIN,loggedAsAdmin)
                .apply();
    }

    public void setLoggedId(long loggedId) {
        mSharedPreferences.edit()
                .putLong(ID_OF_LOGGED_USER,loggedId)
                .apply();
    }

    private long getLoggedId() {
        return mSharedPreferences.getLong(ID_OF_LOGGED_USER, -1);
    }

    public boolean isLoggedAsAdmin() {
        return mSharedPreferences.getBoolean(LOGGED_AS_ADMIN, false);
    }

    public long loggedDoctorId() {
        return getLoggedId();
    }

    public long loggedAdminId() {
        return getLoggedId();
    }

    public void logAsAdmin(String email, String password) {
        UserRepository userRepository = UserRepository.getInstance();
        String defaultEmail = "admin@admin.com";
        ConferenceAdmin conferenceAdmin = userRepository.getConferenceAdmin(defaultEmail);
        if (conferenceAdmin == null) {
            conferenceAdmin = new ConferenceAdmin();
            conferenceAdmin.setEmail(defaultEmail);
            userRepository.addConferenceAdmin(conferenceAdmin);
        }
        setLoggedId(conferenceAdmin.getId());
        setLoggedAsAdmin(true);
    }

    public void logAsDoctor(String email, String password) {
        UserRepository userRepository = UserRepository.getInstance();
        String defaultEmail = "doctor@conferences.com";
        Doctor doctor = userRepository.getDoctor(email == null || email.isEmpty() ? defaultEmail: email);
        if (doctor == null) {
            doctor = new Doctor();
            doctor.setEmail(email == null || email.isEmpty() ? defaultEmail: email);
            userRepository.addDoctor(doctor);
        }
        setLoggedId(doctor.getId());
        setLoggedAsAdmin(false);
    }

    public void logout() {
        setLoggedAsAdmin(false);
        setLoggedId(-1);
    }
}
