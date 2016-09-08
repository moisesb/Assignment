package net.moisesborges.conferencetracker.suggestions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import net.moisesborges.conferencetracker.R;
import net.moisesborges.conferencetracker.adapters.TopicSuggestionsAdapter;
import net.moisesborges.conferencetracker.data.SuggestionRepository;
import net.moisesborges.conferencetracker.model.TopicSuggestion;
import net.moisesborges.conferencetracker.services.LoginService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuggestionsFragment extends Fragment implements SuggestionsView {

    public static final String CONFERENCE_ID_ARG = "net.moisesborges.conferencetracker.suggestions.SuggestionsFragment.conferenceId";

    @BindView(R.id.add_topic_suggestion_button)
    ImageButton mAddSuggestionButton;

    @BindView(R.id.topic_suggestions_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.suggestion_edit_text)
    EditText mSuggestionEditText;

    private TopicSuggestionsAdapter mAdapter = new TopicSuggestionsAdapter(new ArrayList<TopicSuggestion>(0));

    private SuggestionsPresenter mPresenter;
    private long conferenceId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_suggestions, container, false);
        ButterKnife.bind(this,view);

        if (getArguments() != null) {
            conferenceId = getArguments().getLong(CONFERENCE_ID_ARG, SuggestionsPresenter.ALL_CONFERENCES_VALUE);
        } else {
            conferenceId = SuggestionsPresenter.ALL_CONFERENCES_VALUE;
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        initPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadSuggestions(conferenceId);
    }

    @Override
    public void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    private void initPresenter() {
        mPresenter = new SuggestionsPresenter(SuggestionRepository.getInstance(),
                new LoginService(getContext()));
        mPresenter.bindView(this);
        mPresenter.showAvailableFeatures();
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }


    @OnClick(R.id.add_topic_suggestion_button)
    void onAddTopicSuggestionClick() {
        String suggestion = mSuggestionEditText.getText().toString();
        mPresenter.addTopicSuggestion(conferenceId,suggestion);
    }

    @Override
    public void showTopicSuggestions(List<TopicSuggestion> topicSuggestions) {
        mAdapter.replaceData(topicSuggestions);
    }

    @Override
    public void showNoSuggestionsMessage() {

    }

    @Override
    public void showTopicSuggestion(TopicSuggestion topicSuggestion) {
        mAdapter.addSuggestion(topicSuggestion);
    }

    @Override
    public void clearTopicSuggestionField() {
        mSuggestionEditText.setText("");
    }

    @Override
    public void allowAddSuggestions(boolean allow) {
        mSuggestionEditText.setVisibility(allow ? View.VISIBLE : View.GONE);
        mAddSuggestionButton.setVisibility(allow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSuggestionsProgressIndicator(boolean loading) {
        Log.d("Activity", "loading " + loading);
    }

    public static Fragment newInstance(long conferenceId) {
        SuggestionsFragment fragment = new SuggestionsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(CONFERENCE_ID_ARG, conferenceId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance() {
        return new SuggestionsFragment();
    }
}
