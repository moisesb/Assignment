package net.moisesborges.conferencetracker.conferencedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.moisesborges.conferencetracker.R;
import net.moisesborges.conferencetracker.data.CalendarInvitationRepository;
import net.moisesborges.conferencetracker.data.ConferenceRepository;
import net.moisesborges.conferencetracker.data.ScheduleRepository;
import net.moisesborges.conferencetracker.services.LoginService;
import net.moisesborges.conferencetracker.suggestions.SuggestionsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Moisés on 18/08/2016.
 */

public class ConferenceDetailsActivity extends AppCompatActivity implements ConferenceDetailsView {

    public static final String CONFERENCE_ID_ARG = "conferenceId";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.conference_title_text_text)
    TextView mTitleTextView;

    @BindView(R.id.conference_place_text_text)
    TextView mPlaceTextView;

    @BindView(R.id.conference_venue_text_text)
    TextView mVenueTextView;

    @BindView(R.id.suggestions_frame_layout)
    FrameLayout mFrameLayout;

    @BindView(R.id.send_invitation_button)
    FloatingActionButton mSendInvitationButton;

    @BindView(R.id.add_calendar_button)
    FloatingActionButton mAddToCalendarButton;

    private ConferenceDetailsPresenter mPresenter;
    private long conferenceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_details);
        ButterKnife.bind(this);

        conferenceId = getIntent().getLongExtra(CONFERENCE_ID_ARG, -1);

        initToolbar();
        initSuggestionsFragment();
        initPresenter();
    }

    private void initSuggestionsFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.suggestions_frame_layout, SuggestionsFragment.newInstance(conferenceId), "suggestions")
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadConferenceInfo(conferenceId);
    }

    @Override
    protected void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    private void initPresenter() {
        mPresenter = new ConferenceDetailsPresenter(ConferenceRepository.getInstance(),
                CalendarInvitationRepository.getInstance(),
                ScheduleRepository.getInstance(),
                new LoginService(this));
        mPresenter.bindView(this);
        mPresenter.showAvailableFeatures(conferenceId);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void start(Context context, long conferenceId) {
        Intent intent = new Intent(context,ConferenceDetailsActivity.class);
        intent.putExtra(CONFERENCE_ID_ARG, conferenceId);
        context.startActivity(intent);
    }


    @OnClick(R.id.send_invitation_button)
    void onSendInvitationClick() {
        mPresenter.sendCalendarInvitation(conferenceId);
    }

    @OnClick(R.id.add_calendar_button)
    void onAddToCalendaClick() {
        mPresenter.addToCalendar(conferenceId);
    }

    @Override
    public void allowAddToCalendar(boolean allowed) {
        mAddToCalendarButton.setVisibility(allowed ? View.VISIBLE : View.GONE);
    }

    @Override
    public void allowInviteDoctors(boolean allowed) {
        mSendInvitationButton.setVisibility(allowed ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setConferenceTitle(String title) {
        mTitleTextView.setText(title);
    }

    @Override
    public void setConferenceVenue(String venue) {
        mVenueTextView.setText(venue);
    }

    @Override
    public void setConferencePlace(String place) {
        mPlaceTextView.setText(place);
    }

    @Override
    public void invitationSentAlert() {
        Toast.makeText(this, R.string.invitation_sent_message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void scheduleMadeAlert() {
        Toast.makeText(this, R.string.added_to_calendar_message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showRemoveScheduleIcon() {
        mAddToCalendarButton.setImageResource(R.drawable.ic_event_busy_white_24dp);
    }

    @Override
    public void showAddScheduleIcon() {
        mAddToCalendarButton.setImageResource(R.drawable.ic_event_white_24dp);
    }

    @Override
    public void scheduleRemovedAlert() {
        Toast.makeText(this, R.string.removed_to_calendar_message, Toast.LENGTH_LONG)
                .show();
    }
}
