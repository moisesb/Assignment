package net.moisesborges.conferencetracker.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import net.moisesborges.conferencetracker.R;
import net.moisesborges.conferencetracker.conferences.ConferencesFragment;
import net.moisesborges.conferencetracker.invitations.InvitationsActivity;
import net.moisesborges.conferencetracker.login.LoginActivity;
import net.moisesborges.conferencetracker.menu.MenuPresenter;
import net.moisesborges.conferencetracker.menu.MenuView;
import net.moisesborges.conferencetracker.services.LoginService;
import net.moisesborges.conferencetracker.suggestions.SuggestionsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Moisés on 19/08/2016.
 */

public class DashboardActivity extends AppCompatActivity implements MenuView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private MenuPresenter mMenuPresenter;
    private boolean loggedAsAdmin;
    private boolean loggedAsDoctor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        initToolbar();
        initPresenter();
    }

    private void setupViewPager(PagerAdapter pagerAdapter) {
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onDestroy() {
        mMenuPresenter.unbindView();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if (loggedAsAdmin) {
            menuInflater.inflate(R.menu.conference_admin_menu, menu);
            return true;
        }

        if (loggedAsDoctor) {
            menuInflater.inflate(R.menu.doctor_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invitations:
                mMenuPresenter.openInvitations();
                break;
            case R.id.logout:
                mMenuPresenter.logout();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void initPresenter() {
        mMenuPresenter = new MenuPresenter(new LoginService(this));
        mMenuPresenter.bindView(this);
        mMenuPresenter.showAvailableFeatures();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void navigateToInvitations(long doctorId) {
        InvitationsActivity.start(this, doctorId);
    }

    @Override
    public void showDoctorMenu(long doctorId) {
        loggedAsDoctor = true;
        loggedAsAdmin = false;
        invalidateOptionsMenu();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPagerAdapter.addFragment(ConferencesFragment.newInstance(), R.string.upcoming_conferences_page_title);
        viewPagerAdapter.addFragment(ConferencesFragment.newInstance(doctorId), R.string.saved_conferences_page_title);
        setupViewPager(viewPagerAdapter);
    }

    @Override
    public void showAdminMenu() {
        loggedAsDoctor = false;
        loggedAsAdmin = true;
        invalidateOptionsMenu();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPagerAdapter.addFragment(ConferencesFragment.newInstance(), R.string.upcoming_conferences_page_title);
        viewPagerAdapter.addFragment(SuggestionsFragment.newInstance(), R.string.suggestions_page_title);
        setupViewPager(viewPagerAdapter);
    }

    @Override
    public void navigateToLogin() {
        LoginActivity.start(this);
        finish();
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<Integer> mFragmentTitleRes = new ArrayList<>();
        private final Context mContext;

        public ViewPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        public void addFragment(@NonNull Fragment fragment,
                                @StringRes int title) {
            mFragments.add(fragment);
            mFragmentTitleRes.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Integer stringRes = mFragmentTitleRes.get(position);
            return mContext.getResources().getString(stringRes);
        }
    }

}
