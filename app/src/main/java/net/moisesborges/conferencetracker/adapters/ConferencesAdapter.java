package net.moisesborges.conferencetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.moisesborges.conferencetracker.R;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceViewModel;
import net.moisesborges.conferencetracker.utils.DateUtils;

import java.util.Date;
import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by Moisés on 18/08/2016.
 */

public class ConferencesAdapter extends RecyclerView.Adapter<ConferencesAdapter.ViewHolder> {

    private final OnConferenceClickCallback mCallback;
    private List<ConferenceViewModel> mConferences;

    public ConferencesAdapter(@NonNull List<ConferenceViewModel> conferences,
                              @NonNull OnConferenceClickCallback callback) {
        super();
        mConferences = conferences;
        mCallback = callback;
    }

    public void replaceData(@NonNull List<ConferenceViewModel> conferences) {
        mConferences = conferences;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_conference, parent, false);
        return new ViewHolder(layout, mCallback);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ConferenceViewModel conferenceViewModel = mConferences.get(position);
        Conference conference = conferenceViewModel.getConference();
        holder.conferenceName.setText(conference.getTitle());
        String startDate = DateUtils.dateToUiString(new Date(conference.getStartDate()));
        holder.conferenceStartDate.setText(startDate);
        holder.conferencePlace.setText(conference.getPlace());

        if (conferenceViewModel.hasPermitionToEdit()) {
            holder.buttonsLayout.setVisibility(View.VISIBLE);
        } else {
            holder.buttonsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mConferences.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final AutofitTextView conferenceName;
        final TextView conferencePlace;
        final TextView conferenceStartDate;
        final LinearLayout buttonsLayout;

        public ViewHolder(View itemView,
                          final OnConferenceClickCallback callback) {
            super(itemView);
            conferenceName = (AutofitTextView) itemView.findViewById(R.id.conference_name_text_view);
            conferencePlace = (TextView) itemView.findViewById(R.id.conference_place_text_view);
            conferenceStartDate = (TextView) itemView.findViewById(R.id.conference_date_text_view);
            buttonsLayout = (LinearLayout) itemView.findViewById(R.id.buttons_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ConferenceViewModel conferenceViewModel = mConferences.get(position);
                    callback.onConferenceClick(conferenceViewModel.getConference());
                }
            });
        }
    }

    public interface OnConferenceClickCallback {
        void onConferenceClick(Conference conference);
    }
}
