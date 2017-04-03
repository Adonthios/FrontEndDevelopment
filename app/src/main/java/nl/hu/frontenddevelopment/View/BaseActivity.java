package nl.hu.frontenddevelopment.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nl.hu.frontenddevelopment.Fragment.EditPersonFragment;
import nl.hu.frontenddevelopment.Fragment.ProjectNewFragment;
import nl.hu.frontenddevelopment.Fragment.ProjectOverviewFragment;
import nl.hu.frontenddevelopment.Model.Person;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.Utils.CircleTransform;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private static Boolean hasToAdd;

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

        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
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

        // Init the layout
        initLayout();
    }

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

    protected void checkUserExisted(Person person, String userID) {
        hasToAdd = true;
        mDatabase.child("persons").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hasToAdd = true;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Person person = snapshot.getValue(Person.class);

                    Log.d("PERSON","ID = " + person.getKey());
                    if(person.getKey().equals(userID)){
                        hasToAdd = false;
                        break;
                    }
                }
                if (hasToAdd) addToPersonDatabase(person,userID);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void addToPersonDatabase(Person person, String userID) {
        person.setKey(userID);
        //person.setProfilePhoto(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        mDatabase.child("persons").push().setValue(person);
    }

    protected void goToHomeActivity() {
        startActivity(new Intent(this, ProjectActivity.class));
    }



    /***
     * START
     * DEFAULT LAYOUT
     */
    protected void initLayout() {
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
                setProjectOverviewFragment();
                break;
            case 1:
                setNewProject();
                break;
            case 2:
                getCurrentPerson();
                break;
            case 3:
                signOut();
                break;
            default:
                setProjectOverviewFragment();
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
        hideProgressDialog();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    /***
     * END
     * DEFAULT LAYOUT
     */


    protected void setProjectOverviewFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ProjectOverviewFragment.newInstance()).addToBackStack(null).commit();
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

    public void editProfile(Person person){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();

        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, EditPersonFragment.newInstance(person.getKey(), person.getName(), person.getPhonenumber())).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, EditPersonFragment.newInstance(person.getKey(), person.getName(), person.getPhonenumber())).addToBackStack(null).commit();
        }
    }

    private void getCurrentPerson() {
        String currentUserKey = mAuth.getCurrentUser().getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("persons").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Person person = dataSnapshot.getValue(Person.class);
                if(person.getKey().equals(currentUserKey)){
                    editProfile(person);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}