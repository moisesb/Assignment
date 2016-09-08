package net.moisesborges.conferencetracker.menu;

import net.moisesborges.conferencetracker.mvp.PresenterBase;
import net.moisesborges.conferencetracker.services.LoginService;

/**
 * Created by Moisés on 19/08/2016.
 */

public class MenuPresenter implements PresenterBase<MenuView>{

    private final LoginService mLoginService;
    private MenuView mView;

    public MenuPresenter(LoginService loginService) {
        mLoginService = loginService;
    }

    public void showAvailableFeatures() {
        boolean isAdmin = mLoginService.isLoggedAsAdmin();

        if (isAdmin) {
            mView.showAdminMenu();
        }else {
            long doctorId = mLoginService.loggedDoctorId();
            mView.showDoctorMenu(doctorId);
        }

    }

    public void openInvitations() {
        if (mView == null) {
            return;
        }

        if (!mLoginService.isLoggedAsAdmin()) {
            long doctorId = mLoginService.loggedDoctorId();
            mView.navigateToInvitations(doctorId);
        }
    }

    public void logout() {
        if (mView == null) {
            return;
        }

        mLoginService.logout();
        mView.navigateToLogin();
    }

    @Override
    public void bindView(MenuView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }

}
