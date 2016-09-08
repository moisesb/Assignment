package net.moisesborges.conferencetracker.invitations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import net.moisesborges.conferencetracker.R;
import net.moisesborges.conferencetracker.adapters.InvitationsAdapter;
import net.moisesborges.conferencetracker.data.CalendarInvitationRepository;
import net.moisesborges.conferencetracker.data.ScheduleRepository;
import net.moisesborges.conferencetracker.model.InvitationViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Moisés on 18/08/2016.
 */

public class InvitationsActivity extends AppCompatActivity implements InvitationsView {

    public static final String DOCTOR_ID_ARG = "doctorId";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.invitations_recycler_view)
    RecyclerView mRecyclerView;

    InvitationsAdapter mAdapter = new InvitationsAdapter(new ArrayList<InvitationViewModel>(),
            new InvitationsAdapter.OnInvitationClickCallback() {
                @Override
                public void onInvitationClick(InvitationViewModel invitation) {
                    mInvitationsPresenter.respondToInvitation(invitation,true,mDoctorId);
                }
            },
            new InvitationsAdapter.OnInvitationClickCallback() {
                @Override
                public void onInvitationClick(InvitationViewModel invitation) {
                    mInvitationsPresenter.respondToInvitation(invitation,false,mDoctorId);
                }
            });

    private long mDoctorId;

    private InvitationsPresenter mInvitationsPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);
        ButterKnife.bind(this);

        mDoctorId = getIntent().getLongExtra(DOCTOR_ID_ARG, -1);

        initToolbar();
        initRecyclerView();
        initPresenter();

    }


    @Override
    protected void onResume() {
        super.onResume();
        mInvitationsPresenter.loadInvitations(mDoctorId);
    }

    @Override
    protected void onDestroy() {
        mInvitationsPresenter.unbindView();
        super.onDestroy();
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initPresenter() {
        mInvitationsPresenter = new InvitationsPresenter(ScheduleRepository.getInstance(),
                CalendarInvitationRepository.getInstance());
        mInvitationsPresenter.bindView(this);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void start(Context context, long doctorId) {
        Intent intent = new Intent(context, InvitationsActivity.class);
        intent.putExtra(DOCTOR_ID_ARG, doctorId);
        context.startActivity(intent);
    }

    @Override
    public void updateInvitation(InvitationViewModel invitation) {
        mAdapter.updateInvitation(invitation);
    }

    @Override
    public void setProgressIndicator(boolean loading) {

    }

    @Override
    public void showNoInvitationsMessage() {

    }

    @Override
    public void showInvitations(List<InvitationViewModel> invitations) {
        mAdapter.replaceData(invitations);
    }
}
