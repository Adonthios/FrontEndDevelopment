
package nl.hu.frontenddevelopment.View;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import nl.hu.frontenddevelopment.R;

public class BaseActivity extends AppCompatActivity {

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}
/*
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.Utils.CircleTransform;

*/
/**
 * Created by Schultzie on 16-2-2017.
 *//*


public class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;

    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtSubName;
    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    protected Context mContext;

    private Intent currentIntent;
    private Handler mHandler;
    public static int navItemIndex = 0;
    private String[] activityTitles;

    private static final String TAG_PROJECTS = "projects";
    private static final String TAG_PROJECT_ADD = "add project";
    private static final String TAG_SIGNOUT = "sign out";
    public static String CURRENT_TAG = TAG_PROJECTS;

    private static final String urlNavHeaderBg = "http://www.gettingsmart.com/wp-content/uploads/2016/02/meeting-project-management-feature-964x670.jpg";
    private static final String urlProfileImg = "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAADrAAAAJDcyNTEyOTkyLTkxZGYtNDMyNS1iZmYxLTM5ZWNiODcyNDI4Ng.jpg";

    public NavigationView getNavigationView(){
        return navigationView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;

*/
/*        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setUpNavigationView();*//*


       */
/* initActions();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_PROJECTS;
            loadHomeFragment();
        }*//*

    }

    private void initActions() {
        initToolbar();
        initVars();
        setUpNav();
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);

        */
/*drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);*//*


        initActions();

        setUpNavigationView();

        // Default data
        navItemIndex = 0;
        CURRENT_TAG = TAG_PROJECTS;

        // Load the menu
        selectNavMenu();
        setToolbarTitle();
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            toggleFabs();
            return;
        }

        */
/*Intent intent = getHomeActivity();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                startActivity(getHomeActivity());
            }
        };
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }*//*


        toggleFabs();
        drawer.closeDrawers();
        invalidateOptionsMenu();

      //  setUpNavigationView();

        */
/*initToolbar();

        Log.d("Kom ik hier?", "ja!");
        initVars();

        // Set the supportActionBar
        setSupportActionBar(toolbar);

        Log.d("Set up themm nav", "LOLOLOLOLOL!");
        setUpNav();
*//*

        // Load the page
     //   loadHomeFragment();
    }

    private void initVars() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtSubName = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        mHandler = new Handler();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(BaseActivity.this, drawerLayout, R.string.app_name, R.string.app_name);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        currentIntent = getHomeActivity();
    }

    private void initToolbar(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpNav() {
        Log.d("INFOOO","Lars is een stoere jonge man uit een boerendrop!");

        // Drawer nav header
        txtName.setText("AT-App");
        txtSubName.setText("Test@website.com");
        Glide.with(this).load(urlNavHeaderBg).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imgNavHeaderBg);
        Glide.with(this).load(urlProfileImg).crossFade().thumbnail(0.5f).bitmapTransform(new CircleTransform(this)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgProfile);

        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Log.d("ds", "setUpNav:" + navigationView );
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);
                loadHomeFragment();

                drawerLayout.closeDrawers();
                Intent intent;
                switch (item.getItemId()) {
                    case(R.id.nav_projects) :
                        //Show the overview

                        Log.d("BaseActivity","I'm here now!!");

                        break;

                    case(R.id.nav_project_add):
                        // Add menu
                        break;

                    case (R.id.nav_account_signout):
                        // Sign out

                        Log.d("BaseActivity", "Signing out");

                        mFirebaseAuth.signOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                        nextActivity(SignInActivity.class);
                }
                return false;
            }
        });
        drawerToggle.syncState();
    }

    private void nextActivity(Object activity) {
        startActivity(new Intent(this, activity.getClass()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.nav_projects) {
            //Show the overview
        } else if (id == R.id.nav_project_add) {
            // Add menu
        */
/*} else if (id == R.id.nav_account_settings){*//*

            // Show account settings
        } else if(id == R.id.nav_account_signout){
            // Sign out

            Log.d("BaseActivity", "Signing out");

            mFirebaseAuth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            nextActivity(SignInActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private Intent getHomeActivity() {
        switch (navItemIndex) {
            case 0:
                return new Intent(this, ProjectOverviewActivity.class);
            case 1:
                return new Intent(this, ProjectNewActivity.class);
            default:
                return new Intent(this, ProjectOverviewActivity.class);
        }
    }

    private void loadHomeFragment() {
        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            toggleFabs();
            return;
        }

        Intent intent = getHomeActivity();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                startActivity(getHomeActivity());
            }
        };
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        toggleFabs();
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private void toggleFabs() {
        // TODO: hide or show the FAB's
       */
/* if (navItemIndex == 0) {
            fab_button.show();
        } else {
            fab_button.hide();
        }*//*

    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_projects:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_PROJECTS;
                        break;
                    case R.id.nav_project_add:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PROJECT_ADD;
                        break;
                    case R.id.nav_account_signout:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_SIGNOUT;
                        break;
                    default:
                        navItemIndex = 0;
                }
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (navItemIndex != 0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_PROJECTS;
            loadHomeFragment();
            return;
        }
        super.onBackPressed();
    }
}
*/
