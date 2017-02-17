package nl.hu.frontenddevelopment.View;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nl.hu.frontenddevelopment.Controller.SignInActivity;
import nl.hu.frontenddevelopment.R;

/**
 * Created by Schultzie on 16-2-2017.
 */

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    protected Context mContext;

    public NavigationView getNavigationView(){
        return navigationView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;

        /*   navigationView = (NavigationView) findViewById(R.id.nav_view);
        setUpNav();*/
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        initToolbar();
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpNav() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(BaseActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
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

            drawerLayout.closeDrawers();
            Intent intent;
            switch (item.getItemId()) {
                case(R.id.drawer_projects) :
                    //Show the overview
                    break;

                case(R.id.drawer_project_add):
                    // Add menu
                    break;

                case (R.id.drawer_account_settings):
                        // Show account settings
                        break;

                case (R.id.drawer_account_signout):
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.drawer_projects) {
            //Show the overview
        } else if (id == R.id.drawer_project_add) {
            // Add menu
        } else if (id == R.id.drawer_account_settings){
            // Show account settings
        } else if(id == R.id.drawer_account_signout){
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
