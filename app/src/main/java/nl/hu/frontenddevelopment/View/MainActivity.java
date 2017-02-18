package nl.hu.frontenddevelopment.View;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nl.hu.frontenddevelopment.Controller.XmlClickable;
import nl.hu.frontenddevelopment.Fragment.ProjectNewFragment;
import nl.hu.frontenddevelopment.Fragment.MoviesFragment;
import nl.hu.frontenddevelopment.Fragment.NotificationsFragment;
import nl.hu.frontenddevelopment.Fragment.SettingsFragment;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.Utils.CircleTransform;

// TODO: Rename the Fragments to our way
// TODO: Make the HomeFragment the default one to show the projects
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";

    // TODO: Kunnen in en uit-loggen :P
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;

    // FloatingActionButtons
    private FloatingActionButton fab_project_new, fab_actor_new;

    // Normal buttons in fragments
    private Button project_new_button;

    private XmlClickable currentFragment;

    // TODO: Dit inladen van het Google+ account??
    // Set the URL's to load the navigation header background and profile image
    private static final String urlNavHeaderBg = "http://www.gettingsmart.com/wp-content/uploads/2016/02/meeting-project-management-feature-964x670.jpg";
    private static final String urlProfileImg = "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAADrAAAAJDcyNTEyOTkyLTkxZGYtNDMyNS1iZmYxLTM5ZWNiODcyNDI4Ng.jpg";

    // Set the index to identify current nav menu item
    public static int navItemIndex = 0;

    // Set the tags for the header bar title
    private static final String TAG_PROJECTS = "projects";
    private static final String TAG_PROJECT_ADD = "add project";
    private static final String TAG_SIGNOUT = "sign out";
    public static String CURRENT_TAG = TAG_PROJECTS;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // Flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentFragment = (XmlClickable) getHomeFragment();

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setAllTheButtons();

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // Load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        setAllTheButtonActions();

        // Load the navigation menu header data
        loadNavHeader();
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_PROJECTS;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("AT-App");
        txtWebsite.setText("Test@website.com");

        // Load the header background
        Glide.with(this).load(urlNavHeaderBg).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imgNavHeaderBg);

        // Load the profile image
        Glide.with(this).load(urlProfileImg).crossFade().thumbnail(0.5f).bitmapTransform(new CircleTransform(this)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgProfile);

        // Display the dot next to the Projects tab
    //    navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // Selecting appropriate nav menu item
        selectNavMenu();

        // Set toolbar title
        setToolbarTitle();

        // If user select the current navigation menu again, don't do anything
        // Just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // Show or hide the fab buttons
            toggleFabs();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // Show or hide the fab buttons
        toggleFabs();

        // Closing drawer on item click
        drawer.closeDrawers();

        // Refresh toolbar menu
        invalidateOptionsMenu();
    }

    // TODO: rename the fragments the way we want them to be
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // Project overview
                ProjectOverviewFragment projectOverviewFragment = new ProjectOverviewFragment();
                currentFragment = (XmlClickable) projectOverviewFragment;
                return projectOverviewFragment;
            case 1:
                // New project
                ProjectNewFragment projectNewFragment = new ProjectNewFragment();
                currentFragment = projectNewFragment;
                return projectNewFragment;
            case 2:
                // x
                MoviesFragment moviesFragment = new MoviesFragment();
                currentFragment = (XmlClickable) moviesFragment;
                return moviesFragment;
            case 3:
                // y
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                currentFragment = (XmlClickable) notificationsFragment;
                return notificationsFragment;
            case 4:
                // z
                SettingsFragment settingsFragment = new SettingsFragment();
                currentFragment = (XmlClickable) settingsFragment;
                return settingsFragment;
            default:
                return new ProjectOverviewFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        // Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                // Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    // Replacing the main content with ContentFragment;
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

                // Checking if the item is in checked state or not, if not make it in checked state
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
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        // Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        // Calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // Checking if user is on other navigation menu rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_PROJECTS;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // TODO: Log the user out
        // Noinspection SimplifiableIfStatement
        if (id == R.id.nav_account_signout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Define all the Buttons
    private void setAllTheButtons(){
        fab_project_new = (FloatingActionButton) findViewById(R.id.fab_project_new);
        fab_actor_new = (FloatingActionButton) findViewById(R.id.fab_actor_new);
    }

    // TODO: Actie toevoegen om een nieuw project te maken
    // Set all the actions to the buttons
    private void setAllTheButtonActions(){
        // New project action
        fab_project_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Snackbar.make(view, "New project page", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        // New actor action
        fab_actor_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Snackbar.make(view, "New actor page", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    public void myClickMethod(View v) {
        currentFragment.myClickMethod(v);
    }

    // Show or hide the fab at the home page
    private void toggleFabs() {
        if (navItemIndex == 0) {
            fab_project_new.show();
        } else {
            fab_project_new.hide();
        }

        if (navItemIndex == 1) {
            fab_actor_new.show();
        } else {
            fab_actor_new.hide();
        }
    }
}