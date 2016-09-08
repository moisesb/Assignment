package net.moisesborges.conferencetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.moisesborges.conferencetracker.R;
import net.moisesborges.conferencetracker.model.TopicSuggestion;

import java.util.List;

/**
 * Created by Moisés on 18/08/2016.
 */

public class TopicSuggestionsAdapter extends RecyclerView.Adapter<TopicSuggestionsAdapter.ViewHolder> {

    private List<TopicSuggestion> mTopicSuggestions;

    public TopicSuggestionsAdapter(@NonNull List<TopicSuggestion> topicSuggestions) {
        mTopicSuggestions = topicSuggestions;
    }

    public void replaceData(@NonNull List<TopicSuggestion> topicSuggestions) {
        mTopicSuggestions = topicSuggestions;
        notifyDataSetChanged();
    }

    public void addSuggestion(@NonNull TopicSuggestion topicSuggestion) {
        mTopicSuggestions.add(topicSuggestion);
        notifyItemInserted(mTopicSuggestions.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_topic_suggestion, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TopicSuggestion topicSuggestion = mTopicSuggestions.get(position);
        holder.suggestion.setText(topicSuggestion.getSuggestion());
    }

    @Override
    public int getItemCount() {
        return mTopicSuggestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView suggestion;

        public ViewHolder(View itemView) {
            super(itemView);
            suggestion = (TextView) itemView.findViewById(R.id.suggestion_text_view);
        }
    }

}
