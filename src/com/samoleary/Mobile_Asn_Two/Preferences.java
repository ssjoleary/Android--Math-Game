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
import android.widget.TextView;

/**
 * Author: Sam O'Leary
 * Email: somhairle.olaoire@mycit.ie
 * Created: 15/11/13
 * Revision: 4
 * Revision History:
 *      1: 15/11/13
 *      2: 20/11/13
 *      3: 27/11/13
 *      4: 30/11/13
 *
 * Description:
 *      Preferences shows the user the settings, or preferences, for the app. If the app were to be expanded or updated
 *      this is where the user could modify the newly added settings. At the moment users can access the Preferences
 *      menu to reset their profile and delete their details and progress to date. From here users can navigate back to
 *      the main menu via the left-facing caret on the left hand side of the Action Bar at the top of the screen, or
 *      navigate to their profile via the Profile Icon on the right side of the Action Bar.
 */

public class Preferences extends Activity {
    private GameDB dba;                     // This is the SQLite Database object that allows us read/write access to the database.

    private Typeface chalkTypeFace;         // The Typeface object changes the font of the text in view to which it is applied to.

    private TextView title;
    private TextView resetProfile;
    private Button resetBtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        init();
    }

    /**
     * onCreateOptionsMenu allows menu items to be placed in the Action Bar that runs along the top of the screen. The
     * menu items and their properties, such as the id and icon, are described in the XML file 'pref_action_menu'.
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
        inflater.inflate(R.menu.pref_action_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This initialize method sets up the Views to be displayed to the user, as well as configuring a font for the Typeface
     * object to use and applying this Typeface to all the Views with text in them.
     * A 'Back' button is also enabled/created on the left hand side of the Action Bar. This 'Back' button is a left pointing
     * caret and takes the user back to the Main Screen.
     * The only other button displayed on this screen is a 'Reset' button. Once pressed all the users details and progress is
     * reset to zero.
     */
    private void init() {
        dba = new GameDB(this);
        dba.open();

        getActionBar().setDisplayHomeAsUpEnabled(true);                             // Allows the user to get back to the Main Screen via the enabled Back button in the Action Bar.

        chalkTypeFace = Typeface.createFromAsset(getAssets(), "fonts/kg.ttf");      // The file that contains the fonts is a .tff file and
                                                                                    // and is stored in the 'fonts' folder in the 'assets' folder.
        title = (TextView) findViewById(R.id.preferences_title);
        resetProfile = (TextView) findViewById(R.id.preferences_resetprofile);
        resetBtn = (Button) findViewById(R.id.preferences_resetbtn);

        title.setTypeface(chalkTypeFace);
        resetProfile.setTypeface(chalkTypeFace);
        resetBtn.setTypeface(chalkTypeFace);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dba.dropTable();
            }
        });
    }

    /**
     * onOptionsItemSelected is a method that identifies the Menu Item that is selected from the Menu(s) available and
     * and takes the appropriate action upon selection. In this case the user has only one Item to select from the Action
     * Bar and this Menu Item will take them to their Profile.
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates an Intent to take the user to the Profile Activity.
     */
    private void gotoUserProfile() {
        Intent launchUserProfile = new Intent(this, Profile.class);
        startActivity(launchUserProfile);
    }
}
