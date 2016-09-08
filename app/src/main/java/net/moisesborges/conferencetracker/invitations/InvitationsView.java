package net.moisesborges.conferencetracker.invitations;

import net.moisesborges.conferencetracker.model.InvitationViewModel;
import net.moisesborges.conferencetracker.mvp.ViewBase;

import java.util.List;

/**
 * Created by Moisés on 19/08/2016.
 */

public interface InvitationsView extends ViewBase {
    void updateInvitation(InvitationViewModel invitation);

    void setProgressIndicator(boolean loading);

    void showNoInvitationsMessage();

    void showInvitations(List<InvitationViewModel> invitations);
}
