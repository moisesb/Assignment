package net.moisesborges.conferencetracker.suggestions;

import net.moisesborges.conferencetracker.data.SuggestionRepository;
import net.moisesborges.conferencetracker.model.TopicSuggestion;
import net.moisesborges.conferencetracker.mvp.PresenterBase;
import net.moisesborges.conferencetracker.services.LoginService;

import java.util.List;

/**
 * Created by Moisés on 19/08/2016.
 */

public class SuggestionsPresenter implements PresenterBase<SuggestionsView> {


    public static final int ALL_CONFERENCES_VALUE = -1;
    private SuggestionRepository mSuggestionRepository;
    private SuggestionsView mView;
    private LoginService mLoginService;

    public SuggestionsPresenter(SuggestionRepository suggestionRepository, LoginService loginService) {
        mLoginService = loginService;
        mSuggestionRepository = suggestionRepository;
    }

    public void showAvailableFeatures() {
        if (mView == null) {
            return;
        }

        boolean isAdmin = mLoginService.isLoggedAsAdmin();
        mView.allowAddSuggestions(!isAdmin);
    }

    public void loadSuggestions(long conferenceId) {
        mView.setSuggestionsProgressIndicator(true);

        if (conferenceId > ALL_CONFERENCES_VALUE) {
            mSuggestionRepository.getTopicSuggestionsForConferenceAsync(conferenceId,
                    new SuggestionRepository.OnGetTopicsCallback() {
                        @Override
                        public void onGetTopics(List<TopicSuggestion> topicSuggestions) {
                            showTopicSuggestions(topicSuggestions);
                        }
                    });
        } else {
            mSuggestionRepository.getTopicSuggestionsAsync(new SuggestionRepository.OnGetTopicsCallback() {
                @Override
                public void onGetTopics(List<TopicSuggestion> topicSuggestions) {
                    showTopicSuggestions(topicSuggestions);
                }
            });
        }
    }

    private void showTopicSuggestions(List<TopicSuggestion> topicSuggestions) {
        mView.setSuggestionsProgressIndicator(false);

        if (topicSuggestions.size() > 0) {
            mView.showTopicSuggestions(topicSuggestions);
        } else {
            mView.showNoSuggestionsMessage();
        }
    }

    public void addTopicSuggestion(long conferenceId, String suggestion) {
        if (mView == null) {
            return;
        }

        TopicSuggestion topicSuggestion = new TopicSuggestion();
        topicSuggestion.setConferenceId(conferenceId);
        topicSuggestion.setSuggestion(suggestion);

        mSuggestionRepository.addTopicSuggestion(topicSuggestion);
        mView.clearTopicSuggestionField();
        mView.showTopicSuggestion(topicSuggestion);
    }

    @Override
    public void bindView(SuggestionsView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
