package nl.hu.frontenddevelopment.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URI;

import nl.hu.frontenddevelopment.Fragment.ActorOverviewFragment;
import nl.hu.frontenddevelopment.Fragment.EditPersonFragment;
import nl.hu.frontenddevelopment.Fragment.ProjectNewFragment;
import nl.hu.frontenddevelopment.Fragment.ProjectOverviewFragment;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.Utils.CircleTransform;

public class ProjectActivity extends BaseActivity implements View.OnClickListener {
    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /* DEFAULT LAYOUT */
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private Toolbar toolbar;
    private String[] activityTitles;
    public static int navItemIndex = 0;
    private static final String TAG_PROJECTS = "projects";
    private static final String TAG_PROJECT_ADD = "add project";
    private static final String TAG_EDITACCOUNT = "edit account";
    private static final String TAG_SIGNOUT = "sign out";

    public static String CURRENT_TAG = TAG_PROJECTS;
    /* DEFAULT LAYOUT */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_overview);
        setOverviewFragment();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed out
                    startLoginChooserActivity();
                }
            }
        };
        initLayout();
    }

    public void setDetailProject(String projectId){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ActorOverviewFragment.newInstance(projectId)).addToBackStack(null).commit();
        }else {
            startActivity(new Intent(this, ActorActivity.class).putExtra("project_id", projectId));
        }
    }

    public void setOverviewFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ProjectOverviewFragment.newInstance()).addToBackStack(null).commit();
    }

    public void setEditProject(String id, String title, String description){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ProjectNewFragment.newInstance(id,title,description)).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ProjectNewFragment.newInstance(id,title,description)).addToBackStack(null).commit();
        }
    }

    public void setNewProject(){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ProjectNewFragment.newInstance()).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ProjectNewFragment.newInstance()).addToBackStack(null).commit();
        }
    }

    public void editProfile(){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, EditPersonFragment.newInstance()).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, EditPersonFragment.newInstance()).addToBackStack(null).commit();
        }
    }



    /***
     * START
     * DEFAULT LAYOUT
     */
    private void initLayout() {
        initToolbar();
        initDrawer();

        loadNavHeader();
        setUpNavigationView();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        setMenuCheckedItem(navItemIndex);
    }

    private void loadNavHeader() {
        Glide.with(this).load(getString(R.string.drawer_header_nav_bg)).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imgNavHeaderBg);
        String imgProfileUrl = getString(R.string.drawer_header_profile_bg);
        if(mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getPhotoUrl() != null && mAuth.getCurrentUser().getPhotoUrl().toString() != null && !mAuth.getCurrentUser().getPhotoUrl().toString().equals("")) {
            imgProfileUrl = mAuth.getCurrentUser().getPhotoUrl().toString();
        }
        Glide.with(this).load(imgProfileUrl).crossFade().thumbnail(0.5f).bitmapTransform(new CircleTransform(this)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgProfile);
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
                    case R.id.nav_account_edit:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_EDITACCOUNT;
                        break;
                    case R.id.nav_account_signout:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SIGNOUT;
                        break;
                    default:
                        navItemIndex = 0;
                }
                setMenuCheckedItem(menuItem.getItemId());

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

    private void setMenuCheckedItem(int itemId) {
        for (int i = 0; i < 3; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        navigationView.setCheckedItem(itemId);
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void loadHomeFragment() {
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        homeActivity();

        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void homeActivity() {
        switch (navItemIndex) {
            case 0:
                startActivity(new Intent(this, ProjectActivity.class));
                break;
            case 1:
                setNewProject();
                break;
            case 2:
                editProfile();
                break;
            case 3:
                signOut();
                break;
            default:
                startActivity(new Intent(this, ProjectActivity.class));
        }
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.nav_account_signout) {
            signOut();
        }
    }

    private void signOut() {
        mAuth.signOut();
        startActivity(new Intent(this, ProjectActivity.class));
    }

    protected void startLoginChooserActivity() {
        startActivity(new Intent(this, ChooserActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    /***
     * END
     * DEFAULT LAYOUT
     */
}