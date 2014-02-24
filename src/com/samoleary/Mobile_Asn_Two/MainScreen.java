package com.samoleary.Mobile_Asn_Two;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Author: Sam O'Leary
 * Email: somhairle.olaoire@mycit.ie
 * Created: 15/11/13
 * Revision: 5
 * Revision History:
 *      1: 15/11/13
 *      2: 16/11/13
 *      3: 17/11/13
 *      4: 27/11/13
 *      5: 30/11/13
 *
 * Description:
 *      This is the main Activity and is the first Activity to be presented to the user when they start the app. From
 *      here the user can navigate to their profile, preferences, app info or they can start a game.
 */

public class MainScreen extends Activity {
    private Typeface chalkTypeFace;
    private Button play;
    private Button preferences;
    private Button profile;
    private ImageButton info;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        init();
    }

    /**
     * onCreateOptionsMenu allows menu items to be placed in the Action Bar that runs along the top of the screen. The
     * menu items and their properties, such as the id and icon, are described in the XML file 'main_action_menu'.
     * The MenuInflater takes this list and literally 'inflates' it into a menu to be displayed.
     *
     * @param menu
     *      The Menu object representing the Menu to be 'inflated', or populated.
     *
     * @return
     *      Returns either True or False if the Menu has been created successfully or not.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_action_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This initialize method sets up the Typeface, finds all the Buttons, sets the Typeface to them and sets up the
     * onClickListener() on each of them.
     */
    private void init() {
        chalkTypeFace = Typeface.createFromAsset(getAssets(), "fonts/kg.ttf");

        play = (Button) findViewById(R.id.main_play);
        play.setTypeface(chalkTypeFace);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoPlay();
            }
        });

        preferences = (Button) findViewById(R.id.main_options);
        preferences.setTypeface(chalkTypeFace);
        preferences.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoOptions();
            }
        });

        profile = (Button) findViewById(R.id.main_profile);
        profile.setTypeface(chalkTypeFace);
        profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoUserProfile();
            }
        });

        info = (ImageButton) findViewById(R.id.main_info);
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoInfo();
            }
        });
    }

    /**
     * onOptionsItemSelected is a method that identifies the Menu Item that is selected from the Menu(s) available and
     * and takes the appropriate action upon selection. In this case two Items are available to select from the Action
     * Bar.
     *
     * @param item
     *      The selected Item is passed into the method where its ID is identified and used in a Switch statement to
     *      determine the action to take.
     *
     * @return
     *      The method returns True if the Item ID is matched within in the Switch statement, otherwise the Menu Item is
     *      passed to the Superclass where it is dealt with and the value from that method is returned.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                gotoUserProfile();
                return true;
            case R.id.action_settings:
                gotoOptions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates an Intent to take the user to the Info Activity.
     */
    private void gotoInfo() {
        Intent launchInfo = new Intent(this, Info.class);
        startActivity(launchInfo);
    }

    /**
     * Creates an Intent to take the user to the Preferences Activity.
     */
    private void gotoOptions() {
        Intent launchOptions = new Intent(this, Preferences.class);
        startActivity(launchOptions);
    }

    /**
     * Creates an Intent to take the user to the Game Config Activity.
     */
    private void gotoPlay() {
        Intent launchPlay = new Intent(this, GameConfig.class);
        startActivity(launchPlay);
    }

    /**
     * Creates an Intent to take the user to the Profile Activity.
     */
    private void gotoUserProfile() {
        Intent launchUserProfile = new Intent(this, Profile.class);
        startActivity(launchUserProfile);
    }
}
