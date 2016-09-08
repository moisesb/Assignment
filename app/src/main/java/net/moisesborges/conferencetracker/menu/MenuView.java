package net.moisesborges.conferencetracker.menu;

import net.moisesborges.conferencetracker.mvp.ViewBase;

/**
 * Created by Moisés on 19/08/2016.
 */

public interface MenuView extends ViewBase {
    void navigateToInvitations(long doctorId);

    void showDoctorMenu(long doctorId);

    void showAdminMenu();

    void navigateToLogin();
}
