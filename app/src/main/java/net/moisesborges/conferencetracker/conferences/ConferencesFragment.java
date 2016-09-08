package net.moisesborges.conferencetracker.conferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.moisesborges.conferencetracker.R;
import net.moisesborges.conferencetracker.adapters.ConferencesAdapter;
import net.moisesborges.conferencetracker.conferencedetails.ConferenceDetailsActivity;
import net.moisesborges.conferencetracker.data.ConferenceRepository;
import net.moisesborges.conferencetracker.model.Conference;
import net.moisesborges.conferencetracker.model.ConferenceViewModel;
import net.moisesborges.conferencetracker.editconference.EditConferenceActivity;
import net.moisesborges.conferencetracker.services.LoginService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Moisés on 19/08/2016.
 */

public class ConferencesFragment extends Fragment implements ConferencesView {

    private static final String DOCTOR_ID_ARG = "net.moisesborges.conferencetracker.conferences.ConferencesFragment.doctorId";

    FloatingActionButton mAddActionButton;
    @BindView(R.id.conferences_recycler_view)
    RecyclerView mRecyclerView;

    ConferencesAdapter mAdapter = new ConferencesAdapter(new ArrayList<ConferenceViewModel>(0), new ConferencesAdapter.OnConferenceClickCallback() {
        @Override
        public void onConferenceClick(Conference conference) {
            mConferencesPresenter.openConference(conference);
        }
    });

    private ConferencesPresenter mConferencesPresenter;

    private long doctorId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conferences, container, false);
        ButterKnife.bind(this,view);

        if (getArguments() != null) {
            doctorId = getArguments().getLong(DOCTOR_ID_ARG, ConferencesPresenter.NO_DOCTOR_ID_VALUE);
        }else {
            doctorId = ConferencesPresenter.NO_DOCTOR_ID_VALUE;
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAddActionButton = (FloatingActionButton) getActivity().findViewById(R.id.add_action_button);
        mAddActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConferencesPresenter.addConference();
            }
        });
        setupRecyclerView();
        initPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        mConferencesPresenter.loadConferences(doctorId);
    }

    private void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initPresenter() {
        LoginService loginService = new LoginService(getContext());
        mConferencesPresenter = new ConferencesPresenter(ConferenceRepository.getInstance(),
                loginService);
        mConferencesPresenter.bindView(this);
        mConferencesPresenter.showAvailableFeatures();
    }

    @Override
    public void showAddConferenceOption(boolean show) {
        mAddActionButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setProgressIndicator(boolean loading) {

    }

    @Override
    public void showConferences(List<ConferenceViewModel> conferences) {
        mAdapter.replaceData(conferences);
    }

    @Override
    public void showAddConferenceMessage() {

    }

    @Override
    public void showNoUpcomingConferencesMessage() {
    }

    @Override
    public void openConferenceDetails(long conferenceId) {
        ConferenceDetailsActivity.start(getContext(),conferenceId);
    }

    @Override
    public void openNewConference() {
        EditConferenceActivity.start(getContext());
    }

    public static Fragment newInstance() {
        return new ConferencesFragment();
    }

    public static Fragment newInstance(long doctorId) {
        ConferencesFragment fragment = new ConferencesFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(DOCTOR_ID_ARG,doctorId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
