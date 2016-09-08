package net.moisesborges.conferencetracker.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import net.moisesborges.conferencetracker.R;
import net.moisesborges.conferencetracker.dashboard.DashboardActivity;
import net.moisesborges.conferencetracker.services.LoginService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_text)
    EditText mEmailEditText;

    @BindView(R.id.password_edit_text)
    EditText mPasswordEditText;

    private LoginService mLoginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginService = new LoginService(this);
    }

    @OnClick(R.id.login_admin_button)
    void onAdminClick() {
        mLoginService.logAsAdmin(null, null);
        openConferencesActivity();
    }

    @OnClick(R.id.login_doctor_button)
    void onDoctorClick() {
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        mLoginService.logAsDoctor(email,password);
        openConferencesActivity();
    }

    private void openConferencesActivity() {
        DashboardActivity.start(this);
        finish();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
