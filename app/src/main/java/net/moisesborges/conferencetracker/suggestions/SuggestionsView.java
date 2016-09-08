package net.moisesborges.conferencetracker.suggestions;

import net.moisesborges.conferencetracker.model.TopicSuggestion;
import net.moisesborges.conferencetracker.mvp.ViewBase;

import java.util.List;

/**
 * Created by Moisés on 19/08/2016.
 */

public interface SuggestionsView extends ViewBase {
    void clearTopicSuggestionField();

    void showTopicSuggestion(TopicSuggestion topicSuggestion);

    void allowAddSuggestions(boolean allowed);

    void setSuggestionsProgressIndicator(boolean loading);

    void showTopicSuggestions(List<TopicSuggestion> topicSuggestions);

    void showNoSuggestionsMessage();
}
