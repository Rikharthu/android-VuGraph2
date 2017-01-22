package com.example.uberv.vugraph2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.uberv.vugraph2.api.ApiBAAS;
import com.example.uberv.vugraph2.fragments.AugmentedRealityFragment;
import com.example.uberv.vugraph2.fragments.UserSettingsFragment;
import com.example.uberv.vugraph2.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;

public class MainActivity extends AppCompatActivity
        implements UserSettingsFragment.OnUserSettingsFragmentInteractionListener,
        AugmentedRealityFragment.OnARFragmentInteractionListener {

    public static final String LOGOUT_MESSAGE_KEY = "LOGOUT_MESSAGE_KEY";
    public static final String LOGOUT_MESSAGE_BUNDLE_KEY = "LOGOUT_MESSAGE_BUNDLE_KEY";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nvView)
    NavigationView nvDrawer;
    //@BindView(R.id.drawer_header_username_tv)
    //TextView drawerHeaderUsernameTv;

    private ArrayList<AugmentedExperience> augmentedExperiences;
    private FragmentManager fragmentManager;
    private VuGraphUser currentUser;
    private PhotoGettingSingleton photoGettingSingleton;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        photoGettingSingleton = PhotoGettingSingleton.getInstance(this);
        fragmentManager = getSupportFragmentManager();

        setSupportActionBar(toolbar);

        setupDrawerContent(nvDrawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(drawerToggle);

        currentUser = PreferencesUtils.getCurrentUser();
        populateDrawerHeader(currentUser);

        // FIXME replace with apigee baas (~~other github fork)
        augmentedExperiences = new ArrayList<>();
        AugmentedExperience experience = new AugmentedExperience();
        if(currentUser.getRole().equals("intern")) {
            experience.setHeading("Mazumtirdzniecības Uzņēmumi");
            experience.setSubheading("Kopējais apgrozījums faktiskajās cenās, milj. eiro");
            experience.setDescription("Mazumtirdzniecības uzņēmumu apgrozijumu grafika ilustrācija izmantojot AR tehnoloģijas.");
            experience.setImageName("vugraph_preview_xcdz0k");
            experience.setVuforiaRating(4);
            augmentedExperiences.add(experience);
        }

        // show initial state
        showARFragment();
    }

    private void showARFragment() {
        AugmentedRealityFragment arFragment = AugmentedRealityFragment.newInstance(augmentedExperiences);
        setTitle(AugmentedRealityFragment.TITLE);
        fragmentManager.beginTransaction().replace(R.id.container, arFragment).commit();
        drawer.closeDrawers();
    }

    private void showVRFragment(){
        // TODO implement
    }

    private void showSettingsFragment() {
        UserSettingsFragment userSettingsFragment = UserSettingsFragment.newInstance(currentUser);
        setTitle(UserSettingsFragment.TITLE);
        fragmentManager.beginTransaction().replace(R.id.container, userSettingsFragment).commit();
        drawer.closeDrawers();
    }

    private void populateDrawerHeader(VuGraphUser currentUser) {
        // get reference to navigation drawer's header
        View header = nvDrawer.getHeaderView(0);
        TextView drawerHeaderUsernameTv = (TextView) header.findViewById(R.id.drawer_header_username_tv);
        drawerHeaderUsernameTv.setText(currentUser.getUsername());
        ((TextView)header.findViewById(R.id.role_tv)).setText(currentUser.getRole());
        // set avatar logo
        NetworkImageView avatarNIV = (NetworkImageView) header.findViewById(R.id.avatar_iv);
        ImageLoader imageLoader = photoGettingSingleton.getImageLoader();
        String requestURL = photoGettingSingleton.getRoundedAvatarURL(currentUser.getAvatarUrl()); // FIXME
        avatarNIV.setImageUrl(requestURL, imageLoader);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.ar_fragment:
                                showARFragment();
                                break;
                            case R.id.nav_second_fragment:
                                showVRFragment();
                                break;
                            case R.id.nav_third_fragment:
                                Toast.makeText(MainActivity.this, "Not implemented yet ;)", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_logout:
                                // ask user to confirm logout
                                new AlertDialog.Builder(MainActivity.this)
                                        //.setIcon(R.drawable.logout)
                                        .setTitle(R.string.dialog_logout)
                                        .setMessage(R.string.dialog_confirm_logout)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                logout();
                                            }

                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                                break;
                            case R.id.nav_settings:
                                showSettingsFragment();
                                break;
                            default:
                        }
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.ar_fragment:
                fragmentClass = AugmentedRealityFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = AugmentedRealityFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = AugmentedRealityFragment.class;
                break;
            default:
                fragmentClass = AugmentedRealityFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawer.closeDrawers();
    }


    private void logout() {
        // 1 delete from preferences
        PreferencesUtils.remove(PreferencesUtils.CURRENT_USER);
        // 2 revoke tokens from ApiGee
        ApiBAAS.getInstance(MainActivity.this).getApigeeDataClient().logOutAppUserAsync(currentUser.getUsername(), null);
        // 3 open RegisterActivity
        Intent startRegisterActivityIntent = new Intent(this, RegisterActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(LOGOUT_MESSAGE_KEY, getString(R.string.logged_out));
        startRegisterActivityIntent.putExtra(LOGOUT_MESSAGE_BUNDLE_KEY, bundle);

        startRegisterActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startRegisterActivityIntent);
    }


    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startPreview() {
        boolean opened = openApp(this, "com.uberv.VuGraphUnity");
        if (!opened) {
            // player app was not found =>
            // alert the user to download player app
            new AlertDialog.Builder(MainActivity.this)
                    //.setIcon(R.drawable.logout)
                    .setTitle(R.string.dialog_player_needed)
                    .setMessage(R.string.dialog_player_no_player_desc)
                    .setPositiveButton("Download Player", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO open link to download player apk
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    /**
     * Open another app.
     *
     * @param context     current Context, like Activity, App, or Service
     * @param packageName the full package name of the app to open
     * @return true if likely successful, false if unsuccessful
     */
    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent intent = manager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
            //throw new PackageManager.NameNotFoundException();
        }
        // do not add this activity to recent apps
        intent.addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        // start it's LAUNCHER acitivity
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(intent);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "not implemented yet ;)", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListFragmentInteraction(AugmentedExperience experience) {
        startPreview();
    }
}
