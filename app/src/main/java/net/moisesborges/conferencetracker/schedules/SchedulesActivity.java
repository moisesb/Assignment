package net.moisesborges.conferencetracker.schedules;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.moisesborges.conferencetracker.R;

/**
 * Created by Moisés on 19/08/2016.
 */

public class SchedulesActivity extends AppCompatActivity {

    public static final String DOCTOR_ID_ARG = "net.moisesborges.conferencetracker.schedules.SchedulesActivity.doctorID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);
    }

    public static void start(Context context, long doctorId) {
        Intent intent = new Intent(context, SchedulesActivity.class);
        intent.putExtra(DOCTOR_ID_ARG, doctorId);
        context.startActivity(intent);
    }
}
