package net.moisesborges.conferencetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.moisesborges.conferencetracker.R;
import net.moisesborges.conferencetracker.model.InvitationViewModel;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by Moisés on 19/08/2016.
 */

public class InvitationsAdapter extends RecyclerView.Adapter<InvitationsAdapter.ViewHolder> {

    private final OnInvitationClickCallback mAcceptClick;
    private final OnInvitationClickCallback mDeclineClick;
    private List<InvitationViewModel> mInvitations;

    public InvitationsAdapter(@NonNull List<InvitationViewModel> invitations,
                              @NonNull OnInvitationClickCallback acceptClick,
                              @NonNull OnInvitationClickCallback declineClick) {
        mInvitations = invitations;
        mAcceptClick = acceptClick;
        mDeclineClick = declineClick;
    }

    public void replaceData(List<InvitationViewModel> invitations) {
        mInvitations = invitations;
        notifyDataSetChanged();
    }

    public void updateInvitation(InvitationViewModel invitation) {
        int position = mInvitations.indexOf(invitation);
        if (position >= 0) {
            notifyItemChanged(position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_invitation, parent, false);
        return new ViewHolder(layout, mAcceptClick, mDeclineClick);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InvitationViewModel invitation = mInvitations.get(position);
        holder.conferenceTitleTextView.setText(invitation.getConference().getTitle());
        holder.messageTextView.setText(invitation.getCalendarInvitation().getMessage());
        if (invitation.getResponse() != null) {
            if (invitation.getResponse().isAccepted()) {
                holder.declineButton.setVisibility(View.GONE);
                holder.acceptButton.setText(R.string.accepted_button);
                holder.acceptButton.setEnabled(false);
            }else {
                holder.acceptButton.setVisibility(View.GONE);
                holder.declineButton.setText(R.string.declined_button);
                holder.declineButton.setEnabled(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mInvitations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final AutofitTextView conferenceTitleTextView;
        final TextView messageTextView;
        final Button acceptButton;
        final Button declineButton;

        public ViewHolder(View itemView,
                          @NonNull final OnInvitationClickCallback acceptClick,
                          @NonNull final OnInvitationClickCallback declineClick) {
            super(itemView);
            conferenceTitleTextView = (AutofitTextView) itemView.findViewById(R.id.conference_name_text_view);
            messageTextView = (TextView) itemView.findViewById(R.id.invitation_message_text_view);
            acceptButton = (Button) itemView.findViewById(R.id.accept_button);
            declineButton = (Button) itemView.findViewById(R.id.decline_button);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    InvitationViewModel invitation = mInvitations.get(pos);
                    acceptClick.onInvitationClick(invitation);
                }
            });

            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    InvitationViewModel invitation = mInvitations.get(pos);
                    declineClick.onInvitationClick(invitation);
                }
            });
        }
    }

    public interface OnInvitationClickCallback {
        void onInvitationClick(InvitationViewModel invitation);
    }
}
