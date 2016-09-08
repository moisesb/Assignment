package net.moisesborges.conferencetracker.editconference;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import net.moisesborges.conferencetracker.R;
import net.moisesborges.conferencetracker.data.ConferenceRepository;
import net.moisesborges.conferencetracker.services.LoginService;
import net.moisesborges.conferencetracker.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditConferenceActivity extends AppCompatActivity implements NewConferenceView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.conference_title_edit_text)
    EditText mTitleEditText;

    @BindView(R.id.conference_venue_edit_text)
    EditText mVenueEditText;

    @BindView(R.id.conference_place_edit_text)
    EditText mPlaceEditText;

    @BindView(R.id.conference_start_date_edit_text)
    EditText mStartDateEditText;

    @BindView(R.id.conference_days_edit_text)
    EditText mNumbberOfDaysEditText;

    @BindView(R.id.conference_speciality_spinner)
    Spinner mSpecilitySpinner;

    private ArrayAdapter<CharSequence> mAdapter;

    private int mYear;
    private int mMonthOfYear;
    private int mDayOfMonth;

    private String mSpeciality = "";

    private final DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonthOfYear = monthOfYear;
            mDayOfMonth = dayOfMonth;
            mStartDateEditText.setText(getDate(year, monthOfYear, dayOfMonth));
        }
    };

    private EditConferencePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_conference);
        ButterKnife.bind(this);

        initToolbar();
        initSpinner();
        initPresenter();
    }

    private void initSpinner() {
        mAdapter = ArrayAdapter.createFromResource(this,
                R.array.specialities_array, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpecilitySpinner.setAdapter(mAdapter);
        mSpecilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpeciality = mAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpeciality = "";
            }
        });
    }

    @Override
    protected void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_conference_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_conference_button:
                onAddConferenceClick();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @OnClick(R.id.conference_start_date_edit_text)
    void onDateClick() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, mOnDateSetListener, year, month, day);
        dialog.getDatePicker().setMinDate(DateUtils.getCurrentTimeInMillis() - 1000);
        dialog.show();
    }

    protected String getDate(int year, int monthOfYear, int dayOfMonth) {
        final Date date = DateUtils.getDate(year, monthOfYear, dayOfMonth);
        return DateUtils.dateToUiString(date);
    }

    private void initPresenter() {
        mPresenter = new EditConferencePresenter(ConferenceRepository.getInstance(),
                new LoginService(this));
        mPresenter.bindView(this);
    }


    public void onAddConferenceClick() {
        String title = mTitleEditText.getText().toString();
        String place = mPlaceEditText.getText().toString();
        String venue = mVenueEditText.getText().toString();
        String numOfDays = mNumbberOfDaysEditText.getText().toString();
        long startDate = DateUtils.getDate(mYear,mMonthOfYear,mDayOfMonth).getTime();

        mPresenter.addNewConference(title, venue, place, startDate, numOfDays, mSpeciality);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, EditConferenceActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void showInvalidNameMessage() {

    }

    @Override
    public void showInvalidPlaceMessage() {

    }

    @Override
    public void showInvalidVenueMessage() {

    }

    @Override
    public void showInvalidStartDateMessage() {

    }

    @Override
    public void showInvalidNumberOfDaysMessage() {

    }

    @Override
    public void showInvalidSpeciality() {

    }

    @Override
    public void close() {
        finish();
    }
}
